package se.idega.idegaweb.commune.school.music.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock;
import se.idega.util.SchoolClassMemberComparatorForSweden;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class MusicSchoolGroupWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private CareBusiness careBusiness;
	private CommuneUserBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;
	
	private String schoolName;
	private String groupName;

	public MusicSchoolGroupWriter() {
		// empty
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			careBusiness = getCareBusiness(iwc);
			userBusiness = getUserBusiness(iwc);
			iwrb = iwc.getIWMainApplication().getBundle(MusicSchoolBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			schoolName = getSession(iwc).getProvider().getSchoolName();
			groupName = getSession(iwc).getGroup().getSchoolClassName();
			
			List students = new ArrayList(getSchoolBusiness(iwc).getSchoolClassMemberHome().findBySchoolClassAndYearAndStudyPath(getSession(iwc).getGroup(), getSession(iwc).getDepartment(), getSession(iwc).getInstrument()));
			Map studentMap = careBusiness.getStudentList(students);
			Collections.sort(students, SchoolClassMemberComparatorForSweden.getComparatorSortByName(iwc.getCurrentLocale(), userBusiness, studentMap));
			
			buffer = writeXLS(students);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return "application/pdf";
	}
	
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");
	}
	
	public MemoryFileBuffer writeXLS(Collection students) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		if (!students.isEmpty()) {
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet(schoolName);
	    sheet.setColumnWidth((short)0, (short) (30 * 256));
	    sheet.setColumnWidth((short)1, (short) (14 * 256));
	    sheet.setColumnWidth((short)2, (short) (30 * 256));
	    sheet.setColumnWidth((short)3, (short) (14 * 256));
			sheet.setColumnWidth((short)4, (short) (14 * 256));
	    HSSFFont font = wb.createFont();
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    font.setFontHeightInPoints((short)12);
	    HSSFCellStyle style = wb.createCellStyle();
	    style.setFont(font);

			int cellRow = 0;
			HSSFRow row = sheet.createRow(cellRow++);
			HSSFCell cell = row.createCell((short)0);
			cell.setCellValue(schoolName);
			cell.setCellStyle(style);
			cell = row.createCell((short)1);
			
			if (groupName != null) {
				row = sheet.createRow(cellRow++);
				cell = row.createCell((short)0);
				cell.setCellValue(groupName);
				cell.setCellStyle(style);
			}
			
			row = sheet.createRow(cellRow++);
			
	    row = sheet.createRow(cellRow++);
	    cell = row.createCell((short)0);
	    cell.setCellValue(iwrb.getLocalizedString("name","Name"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)1);
	    cell.setCellValue(iwrb.getLocalizedString("personal_id","Personal ID"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)2);
	    cell.setCellValue(iwrb.getLocalizedString("address","Address"));
	    cell.setCellStyle(style);
			cell = row.createCell((short)3);
			cell.setCellValue(iwrb.getLocalizedString("zip_code","Postal code"));
			cell.setCellStyle(style);
	    cell = row.createCell((short)4);
	    cell.setCellValue(iwrb.getLocalizedString("home_phone","Phone"));
	    cell.setCellStyle(style);

			User student;
			Address address;
			PostalCode postalCode = null;
			Phone phone;
			SchoolClassMember studentMember;
			
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				row = sheet.createRow(cellRow++);
				studentMember = (SchoolClassMember) iter.next();
				student = studentMember.getStudent();
				address = userBusiness.getUsersMainAddress(student);
				if (address != null)
					postalCode = address.getPostalCode();
				phone = userBusiness.getChildHomePhone(student);

		    row.createCell((short)0).setCellValue(student.getName());
		    row.createCell((short)1).setCellValue(PersonalIDFormatter.format(student.getPersonalID(), locale));
		    if (address != null) {
			    row.createCell((short)2).setCellValue(address.getStreetAddress());
			    if (postalCode != null)
						row.createCell((short)3).setCellValue(postalCode.getPostalAddress());
		    }
			  if (phone != null)
			    row.createCell((short)4).setCellValue(phone.getNumber());
			}
			wb.write(mos);
		}
		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
	
	protected MusicSchoolSession getSession(IWUserContext iwc) throws RemoteException {
		return (MusicSchoolSession) IBOLookup.getSessionInstance(iwc, MusicSchoolSession.class);	
	}

	protected CareBusiness getCareBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CareBusiness) IBOLookup.getServiceInstance(iwc, CareBusiness.class);	
	}

	protected CommuneUserBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}

	protected SchoolBusiness getSchoolBusiness(IWApplicationContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);	
	}
}