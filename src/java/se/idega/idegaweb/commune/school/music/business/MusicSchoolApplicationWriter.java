package se.idega.idegaweb.commune.school.music.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
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
public class MusicSchoolApplicationWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private CommuneUserBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;
	
	private String schoolName;

	public MusicSchoolApplicationWriter() {
		// empty
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			userBusiness = getUserBusiness(iwc);
			iwrb = iwc.getIWMainApplication().getBundle(MusicSchoolBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			schoolName = getSession(iwc).getProvider().getSchoolName();
			
			Collection choices = getCollection(iwc);
			
			buffer = writeXLS(choices);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Collection getCollection(IWContext iwc) {
		try {
			return getBusiness(iwc).findChoicesInSchool(getSession(iwc).getProvider(), getSession(iwc).getSeason(), getSession(iwc).getDepartment(), getSession(iwc).getInstrument());
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
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
	
	public MemoryFileBuffer writeXLS(Collection students) throws RemoteException {
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
			int cellColumn = 0;
			HSSFRow row = sheet.createRow(cellRow++);
			HSSFCell cell = row.createCell((short)0);
			cell.setCellValue(schoolName);
			cell.setCellStyle(style);
			cell = row.createCell((short)1);
			
			row = sheet.createRow(cellRow++);
			
	    row = sheet.createRow(cellRow++);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("name","Name"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("personal_id","Personal ID"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("address","Address"));
	    cell.setCellStyle(style);
			cell = row.createCell((short) cellColumn++);
			cell.setCellValue(iwrb.getLocalizedString("zip_code","Postal code"));
			cell.setCellStyle(style);
			cell = row.createCell((short) cellColumn++);
			cell.setCellValue(iwrb.getLocalizedString("zip_area","Area"));
			cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("home_phone","Home phone"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("work_phone","Work phone"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("mobile_phone","Mobile phone"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("email","E-mail"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("custodian_email","Custodian e-mail"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("instruments","Instruments"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short) cellColumn++);
	    cell.setCellValue(iwrb.getLocalizedString("department","Department"));
	    cell.setCellStyle(style);

			MusicSchoolChoice choice;
			User student;
			Address address;
			PostalCode postalCode = null;
			Phone homePhone;
			Phone workPhone;
			Phone mobilePhone;
			Email email;
			Email custodianEmail;
			SchoolYear department;
			Collection instruments;
			
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				cellColumn = 0;
				row = sheet.createRow(cellRow++);
				choice = (MusicSchoolChoice) iter.next();
				student = choice.getChild();
				address = userBusiness.getUsersMainAddress(student);
				if (address != null)
					postalCode = address.getPostalCode();
				homePhone = userBusiness.getChildHomePhone(student);
				try {
					workPhone = userBusiness.getUsersWorkPhone(student);
				}
				catch (NoPhoneFoundException npfe) {
					workPhone = null;
				}
				try {
					mobilePhone = userBusiness.getUsersMobilePhone(student);
				}
				catch (NoPhoneFoundException npfe) {
					mobilePhone = null;
				}
				try {
					email = userBusiness.getUsersMainEmail(student);
				}
				catch (NoEmailFoundException nefe) {
					email = null;
				}

				User custodian = userBusiness.getCustodianForChild(student);
				if (custodian != null) {
					try {
						custodianEmail = userBusiness.getUsersMainEmail(custodian);
					}
					catch (NoEmailFoundException nefe) {
						custodianEmail = null;
					}
				}
				else {
					custodianEmail = null;
				}

				try {
					instruments = choice.getStudyPaths();
				}
				catch (IDORelationshipException ire) {
					instruments = new ArrayList();
				}
				department = choice.getSchoolYear();
				
				row.createCell((short)cellColumn++).setCellValue(student.getName());
		    row.createCell((short)cellColumn++).setCellValue(PersonalIDFormatter.format(student.getPersonalID(), locale));
		    if (address != null) {
			    row.createCell((short)cellColumn++).setCellValue(address.getStreetAddress());
			    if (postalCode != null) {
						row.createCell((short)cellColumn++).setCellValue(postalCode.getPostalCode());
						row.createCell((short)cellColumn++).setCellValue(postalCode.getName());
			    }
			    else {
						cellColumn += 2;
			    }
		    }
		    else {
					cellColumn += 3;
		    }
			  if (homePhone != null) {
			    row.createCell((short)cellColumn).setCellValue(homePhone.getNumber());
			  }
			  cellColumn++;
			  if (workPhone != null) {
			    row.createCell((short)cellColumn).setCellValue(workPhone.getNumber());
			  }
			  cellColumn++;
			  if (mobilePhone != null) {
			    row.createCell((short)cellColumn).setCellValue(mobilePhone.getNumber());
			  }
			  cellColumn++;
			  if (email != null) {
			    row.createCell((short)cellColumn).setCellValue(email.getEmailAddress());
			  }
			  cellColumn++;
			  if (custodianEmail != null) {
			    row.createCell((short)cellColumn).setCellValue(custodianEmail.getEmailAddress());
			  }
			  cellColumn++;

				Iterator iterator = instruments.iterator();
				StringBuffer instrumentText = new StringBuffer();
				while (iterator.hasNext()) {
					SchoolStudyPath instrument = (SchoolStudyPath) iterator.next();
					instrumentText.append(iwrb.getLocalizedString(instrument.getLocalizedKey(), instrument.getDescription()));
					if (iterator.hasNext()) {
						instrumentText.append(", ");
					}
				}
		    row.createCell((short)cellColumn++).setCellValue(instrumentText.toString());
		    row.createCell((short)cellColumn++).setCellValue(iwrb.getLocalizedString(department.getLocalizedKey(), department.getSchoolYearName()));
			}
			try {
				wb.write(mos);
			}
			catch (IOException ie) {
				ie.printStackTrace();
			}
		}
		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
	
	protected MusicSchoolSession getSession(IWUserContext iwc) throws RemoteException {
		return (MusicSchoolSession) IBOLookup.getSessionInstance(iwc, MusicSchoolSession.class);	
	}

	protected MusicSchoolBusiness getBusiness(IWUserContext iwc) throws RemoteException {
		return (MusicSchoolBusiness) IBOLookup.getSessionInstance(iwc, MusicSchoolBusiness.class);	
	}

	private CommuneUserBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
}