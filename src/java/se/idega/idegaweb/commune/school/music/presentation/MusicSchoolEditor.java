/*
 * Created on 22.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.music.business.NoDepartmentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoLessonTypeFoundException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * @author laddi
 */
public class MusicSchoolEditor extends MusicSchoolBlock {
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_SAVE = 4;

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_SCHOOL = "prm_school_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_ZIPCODE = "prm_zipcode";
	private static final String PARAMETER_ZIPAREA = "prm_ziparea";
	private static final String PARAMETER_COMMUNE = "prm_commune";
	private static final String PARAMETER_DEPARTMENTS = "prm_departments";
	private static final String PARAMETER_LESSON_TYPES = "prm_lesson_types";
	
	private Object iSchoolPK;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW :
				showSchools();
				break;
			case ACTION_EDIT :
				showEditForm(iwc);
				break;
			case ACTION_SAVE :
				saveSchool(iwc);
				showSchools();
				break;
		}
	}
	
	private void showSchools() {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setColumns(6);
		table.setWidth(6, 12);
		form.add(table);
		int column = 1;
		int row = 1;
		
		table.add(getSmallHeader(localize("school.name", "Name")), column++, row);
		table.add(getSmallHeader(localize("school.address", "Address")), column++, row);
		table.add(getSmallHeader(localize("school.zip", "Zip")), column++, row);
		table.add(getSmallHeader(localize("school.area", "Area")), column++, row);
		table.add(getSmallHeader(localize("school.commune", "Commune")), column++, row);
		table.setCellpaddingLeft(1, row, 12);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		try {
			Collection schools = getSchoolBusiness().getSchoolHome().findAllByCategory(getSchoolBusiness().getCategoryMusicSchool());
			Iterator iter = schools.iterator();
			Link edit;
			Commune commune;
			while (iter.hasNext()) {
				column = 1;
				School school = (School) iter.next();
				commune = school.getCommune();
				
				edit = new Link(getEditIcon(localize("school.edit_school", "Edit school")));
				edit.addParameter(PARAMETER_SCHOOL, school.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
				
				table.add(getSmallText(school.getSchoolName()), column++, row);
				table.add(getSmallText(school.getSchoolAddress()), column++, row);
				table.add(getSmallText(school.getSchoolZipCode()), column++, row);
				table.add(getSmallText(school.getSchoolZipArea()), column++, row);
				if (commune != null) {
					table.add(getSmallText(commune.getCommuneName()), column++, row);
				}
				else {
					column++;
				}
				table.add(edit, column++, row);
				
				table.setCellpaddingLeft(1, row, 12);
				if (row % 2 == 0) {
					table.setRowStyleClass(row++, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row++, getLightRowClass());
				}
			}
			
			table.setHeight(row++, 12);
			table.mergeCells(1, row, table.getColumns(), row);
			SubmitButton newSchool = (SubmitButton) getButton(new SubmitButton(localize("school.new_school", "New school"), PARAMETER_ACTION, String.valueOf(ACTION_EDIT)));
			table.setCellpaddingLeft(1, row, 12);
			table.add(newSchool, 1, row);
		}
		catch (FinderException fe) {
			log(fe);
			//No schools in database...
		}
		catch (RemoteException re) {
			log(re);
		}
		
		add(form);
	}
	
	private void showEditForm(IWContext iwc) {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(2);
		table.setCellspacing(0);
		form.add(table);
		int row = 1;
		
		School school = null;
		Commune commune = null;
		if (iSchoolPK != null) {
			form.addParameter(PARAMETER_SCHOOL, iSchoolPK.toString());
			try {
				school = getSchoolBusiness().getSchool(new Integer(iSchoolPK.toString()));
				commune = school.getCommune();
			}
			catch (RemoteException re) {
				log(re);
			}
		}
		
		TextInput name = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
		if (school != null) {
			name.setContent(school.getSchoolName());
		}
		
		TextInput address = (TextInput) getStyledInterface(new TextInput(PARAMETER_ADDRESS));
		if (school != null) {
			address.setContent(school.getSchoolAddress());
		}
		
		TextInput zipCode = (TextInput) getStyledInterface(new TextInput(PARAMETER_ZIPCODE));
		if (school != null) {
			zipCode.setContent(school.getSchoolZipCode());
		}
		
		TextInput zipArea = (TextInput) getStyledInterface(new TextInput(PARAMETER_ZIPAREA));
		if (school != null) {
			zipArea.setContent(school.getSchoolZipArea());
		}
		
		Collection communes = null;
		try {
			communes = getCommuneBusiness(iwc).getCommunes();
		}
		catch (RemoteException re) {
			log(re);
		}
		SelectorUtility su = new SelectorUtility();
		DropdownMenu communeDrop = (DropdownMenu) su.getSelectorFromIDOEntities((DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COMMUNE)), communes, "getCommuneName");
		communeDrop.addMenuElementFirst("", localize("school.commune", "Commune"));
		if (commune != null) {
			communeDrop.setSelectedElement(commune.getPrimaryKey().toString());
		}
		
		Collection departments = null;
		try {
			departments = getDepartments();
		}
		catch (NoDepartmentFoundException ndfe) {
			departments = new ArrayList();
			log(ndfe);
		}
		
		Collection lessonTypes = null;
		try {
			lessonTypes = getLessonTypes();
		}
		catch (NoLessonTypeFoundException nltfe) {
			lessonTypes = new ArrayList();
			log(nltfe);
		}
		
		Collection schoolDepartments = new ArrayList();
		if (school != null) {
			try {
				schoolDepartments = school.findRelatedSchoolYears();
			}
			catch (IDORelationshipException ire) {
				schoolDepartments = new ArrayList();
			}
		}
		
		Collection schoolLessonTypes = new ArrayList();
		if (school != null) {
			try {
				schoolLessonTypes = school.findRelatedSchoolTypes();
			}
			catch (IDORelationshipException ire) {
				schoolLessonTypes = new ArrayList();
			}
		}
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.name", "Name")), 1, row);
		table.add(name, 2, row++);
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.address", "Address")), 1, row);
		table.add(address, 2, row++);
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.zip", "Zip")), 1, row);
		table.add(zipCode, 2, row++);
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.area", "Area")), 1, row);
		table.add(zipArea, 2, row++);
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.commune", "Commune")), 1, row);
		table.add(communeDrop, 2, row++);
		
		table.setHeight(row++, 12);
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.departments", "Departments")), 1, row);
		Iterator iter = departments.iterator();
		while (iter.hasNext()) {
			SchoolYear department = (SchoolYear) iter.next();
			CheckBox addDepartment = getCheckBox(PARAMETER_DEPARTMENTS, department.getPrimaryKey().toString());
			addDepartment.setChecked(schoolDepartments.contains(department));
			
			table.add(addDepartment, 2, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 2, row);
			table.add(getSmallText(localize(department.getLocalizedKey(), department.getName())), 2, row++);
		}
		if (departments.size() == 0) {
			row++;
		}
		
		table.setHeight(row++, 12);
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("school.departments", "Departments")), 1, row);
		iter = lessonTypes.iterator();
		while (iter.hasNext()) {
			SchoolType lessonType = (SchoolType) iter.next();
			CheckBox addDepartment = getCheckBox(PARAMETER_LESSON_TYPES, lessonType.getPrimaryKey().toString());
			addDepartment.setChecked(schoolLessonTypes.contains(lessonType));
			
			table.add(addDepartment, 2, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 2, row);
			table.add(getSmallText(localize(lessonType.getLocalizationKey(), lessonType.getName())), 2, row++);
		}
		if (lessonTypes.size() == 0) {
			row++;
		}
		
		table.setHeight(row++, 12);
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("school.save_school", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		table.setCellpaddingLeft(1, row, 12);
		table.add(save, 1, row);

		add(form);
	}
	
	private void saveSchool(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_NAME);
		String address = iwc.getParameter(PARAMETER_ADDRESS);
		String zip = iwc.getParameter(PARAMETER_ZIPCODE);
		String area = iwc.getParameter(PARAMETER_ZIPAREA);
		String commune = null;
		if (iwc.isParameterSet(PARAMETER_COMMUNE)) {
			iwc.getParameter(PARAMETER_COMMUNE);
		}
		int schoolID = -1;
		if (iSchoolPK != null) {
			schoolID = new Integer(iSchoolPK.toString()).intValue();
		}
		
		String[] departments = iwc.getParameterValues(PARAMETER_DEPARTMENTS);
		int[] departmentIDs = new int[departments.length];
		for (int i = 0; i < departments.length; i++) {
			departmentIDs[i] = new Integer(departments[i]).intValue();
		}
		
		String[] lessonTypes = iwc.getParameterValues(PARAMETER_LESSON_TYPES);
		int[] lessonTypeIDs = new int[lessonTypes.length];
		for (int i = 0; i < lessonTypes.length; i++) {
			lessonTypeIDs[i] = new Integer(lessonTypes[i]).intValue();
		}
		
		try {
			getSchoolBusiness().storeSchool(schoolID, name, null, address, zip, area, null, null, null, null, -1, lessonTypeIDs, departmentIDs, commune);
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_SCHOOL)) {
			iSchoolPK = iwc.getParameter(PARAMETER_SCHOOL);
		}

		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

  public CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) {
  		try {
  			return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
  		}
  		catch (IBOLookupException ile) {
  			throw new IBORuntimeException(ile);
  		}
  }
}