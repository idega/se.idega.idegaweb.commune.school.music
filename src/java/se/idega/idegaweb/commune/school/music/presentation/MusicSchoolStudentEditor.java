/*
 * $Id: MusicSchoolStudentEditor.java,v 1.5 2005/03/31 09:42:14 laddi Exp $
 * Created on 20.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.music.business.InstrumentComparator;
import se.idega.idegaweb.commune.school.music.business.NoDepartmentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoInstrumentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoLessonTypeFoundException;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.User;


public class MusicSchoolStudentEditor extends MusicSchoolBlock {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_STUDENT = "prm_student";
	private static final String PARAMETER_DEPARTMENT = "prm_department";
	private static final String PARAMETER_INSTRUMENTS = "prm_instruments";
	private static final String PARAMETER_LESSON_TYPE = "prm_lesson_type";

	private static final int ACTION_SAVE = 1;

	public void init(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			save(iwc);
		}
		else {
			add(getEditForm(iwc));
		}
	}

	private Form getEditForm(IWContext iwc) throws FinderException, RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		
		SchoolClassMember student = getSession().getStudent();
		if (student == null) {
			form.add(getErrorText(localize("no_student_found", "No student found...")));
			return form; 
		}
		
		User user = student.getStudent();
		
		form.addParameter(PARAMETER_STUDENT, student.getPrimaryKey().toString());
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
		table.add(getHeader(localize("application.applicant", "Applicant")), 1, row++);
		table.add(getPersonInfoTable(iwc, user), 1, row++);
		
		table.setHeight(row++, 18);
		
		Table editTable = new Table(2, 6);
		editTable.setCellpadding(0);
		editTable.setCellspacing(0);
		table.add(editTable, 1, row++);
		int editRow = 1;
		
		List instruments = null;
		try {
			instruments = new ArrayList(getInstruments());
		}
		catch (NoInstrumentFoundException nife) {
			throw new FinderException("No instruments found...");
		}
		Collections.sort(instruments, new InstrumentComparator(getResourceBundle()));
		
		Collection departments = null;
		try {
			departments = getDepartments();
		}
		catch (NoDepartmentFoundException ndfe) {
			throw new FinderException("No departments found...");
		}
		
		Collection lessonTypes = null;
		try {
			lessonTypes = getLessonTypes();
		}
		catch (NoLessonTypeFoundException ndfe) {
			throw new FinderException("No lesson types found...");
		}
		
		Collection chosenInstruments = null;
		try {
			chosenInstruments = new ArrayList(student.getStudyPaths());
		}
		catch (IDORelationshipException ire) {
			log(ire);
		}

		SelectorUtility util = new SelectorUtility();
		DropdownMenu instrumentsDrop1 = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_INSTRUMENTS + "_1"), instruments, "getLocalizedKey", getResourceBundle()));
		instrumentsDrop1.addMenuElementFirst("", localize("select_instrument", "Select instrument"));
		DropdownMenu instrumentsDrop2 = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_INSTRUMENTS + "_2"), instruments, "getLocalizedKey", getResourceBundle()));
		instrumentsDrop2.addMenuElementFirst("", localize("select_instrument", "Select instrument"));
		DropdownMenu instrumentsDrop3 = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_INSTRUMENTS + "_3"), instruments, "getLocalizedKey", getResourceBundle()));
		instrumentsDrop3.addMenuElementFirst("", localize("select_instrument", "Select instrument"));
		if (chosenInstruments != null) {
			int index = 1;
			Iterator iter = chosenInstruments.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				if (index == 1) {
					instrumentsDrop1.setSelectedElement(instrument.getPrimaryKey().toString());
				}
				else if (index == 2) {
					instrumentsDrop2.setSelectedElement(instrument.getPrimaryKey().toString());
				}
				else if (index == 3) {
					instrumentsDrop3.setSelectedElement(instrument.getPrimaryKey().toString());
				}
				index++;
			}
		}
		
		DropdownMenu departmentDrop = getDropdown(PARAMETER_DEPARTMENT, new Integer(student.getSchoolYearId()));
		departmentDrop.addMenuElementFirst("", localize("select_department", "Select department"));
		Iterator iter = departments.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			if (year.isSelectable()) {
				departmentDrop.addMenuElement(year.getPrimaryKey().toString(), localize(year.getSchoolYearName(), year.getSchoolYearName()));
			}
		}

		DropdownMenu lessonTypeDrop = getDropdown(PARAMETER_LESSON_TYPE, new Integer(student.getSchoolTypeId()));
		lessonTypeDrop.addMenuElementFirst("", localize("select_lesson_type", "Select lesson type"));
		iter = lessonTypes.iterator();
		while (iter.hasNext()) {
			SchoolType type = (SchoolType) iter.next();
			lessonTypeDrop.addMenuElement(type.getPrimaryKey().toString(), localize(type.getLocalizationKey(), type.getSchoolTypeName()));
		}
		
		editTable.setStyleClass(1, editRow, getStyleName(STYLENAME_TEXT_CELL));
		editTable.add(getText(localize("instrument_1", "Instrument 1")), 1, editRow);
		editTable.setStyleClass(2, editRow, getStyleName(STYLENAME_INPUT_CELL));
		editTable.add(instrumentsDrop1, 2, editRow++);

		editTable.setStyleClass(1, editRow, getStyleName(STYLENAME_TEXT_CELL));
		editTable.add(getText(localize("instrument_2", "Instrument 2")), 1, editRow);
		editTable.setStyleClass(2, editRow, getStyleName(STYLENAME_INPUT_CELL));
		editTable.add(instrumentsDrop2, 2, editRow++);

		editTable.setStyleClass(1, editRow, getStyleName(STYLENAME_TEXT_CELL));
		editTable.add(getText(localize("instrument_3", "Instrument 3")), 1, editRow);
		editTable.setStyleClass(2, editRow, getStyleName(STYLENAME_INPUT_CELL));
		editTable.add(instrumentsDrop3, 2, editRow++);

		editTable.setHeight(editRow++, 12);

		editTable.setStyleClass(1, editRow, getStyleName(STYLENAME_TEXT_CELL));
		editTable.add(getText(localize("department", "Department")), 1, editRow);
		editTable.setStyleClass(2, editRow, getStyleName(STYLENAME_INPUT_CELL));
		editTable.add(departmentDrop, 2, editRow++);

		editTable.setStyleClass(1, editRow, getStyleName(STYLENAME_TEXT_CELL));
		editTable.add(getText(localize("lesson_type", "Lesson type")), 1, editRow);
		editTable.setStyleClass(2, editRow, getStyleName(STYLENAME_INPUT_CELL));
		editTable.add(lessonTypeDrop, 2, editRow++);

		table.setHeight(row++, 18);
		
		BackButton previous = (BackButton) getButton(new BackButton(localize("previous", "Previous")));
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("change", "Change")));

		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(submit, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_music_school_edit_placement"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		submit.setOnSubmitFunction("checkApplication", getSubmitConfirmScript());
		form.setToDisableOnSubmit(submit, true);

		return form;
	}
	
	private void save(IWContext iwc) throws FinderException, RemoteException {
		SchoolClassMember student = getSession().getStudent();
		
		Collection instrumentPKs = new ArrayList();
		for (int i = 0; i < 3; i++) {
			if (iwc.isParameterSet(PARAMETER_INSTRUMENTS + "_" + (i+1))) {
				instrumentPKs.add(iwc.getParameter(PARAMETER_INSTRUMENTS + "_" + (i+1)));
			}
		}
		String lessonType = iwc.getParameter(PARAMETER_LESSON_TYPE);
		String department = iwc.getParameter(PARAMETER_DEPARTMENT);
		
		try {
			getBusiness().updateStudent(student, department, lessonType, instrumentPKs);
			if (getParentPage() != null) {
				getParentPage().setAlertOnLoad(localize("selected_student_updated", "The selected student has been updated."));
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		
		if (getResponsePage() != null) {
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		}
		else {
			add(getEditForm(iwc));
		}
	}

	private String getSubmitConfirmScript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("function checkApplication() {").append("\n\t");
		buffer.append("\n\t var dropDepartment = ").append("findObj('").append(PARAMETER_DEPARTMENT).append("');");
		buffer.append("\n\t var dropLessonTypes = ").append("findObj('").append(PARAMETER_LESSON_TYPE).append("');");
		buffer.append("\n\t var dropInstrumentOne = ").append("findObj('").append(PARAMETER_INSTRUMENTS + "_1").append("');");
		buffer.append("\n\t var dropInstrumentTwo = ").append("findObj('").append(PARAMETER_INSTRUMENTS + "_2").append("');");
		buffer.append("\n\t var dropInstrumentThree = ").append("findObj('").append(PARAMETER_INSTRUMENTS + "_3").append("');");

		buffer.append("\n\t var department = -1;");
		buffer.append("\n\t var lessonType = -1;");
		buffer.append("\n\t var instrumentOne = 0;");
		buffer.append("\n\t var instrumentTwo = 0;");
		buffer.append("\n\t var instrumentThree = 0;");

		buffer.append("\n\t if (dropDepartment.selectedIndex > 0) {\n\t\t department = dropDepartment.options[dropDepartment.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropLessonTypes.selectedIndex > 0) {\n\t\t lessonType = dropLessonTypes.options[dropLessonTypes.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropInstrumentOne.selectedIndex > 0) {\n\t\t instrumentOne = dropInstrumentOne.options[dropInstrumentOne.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropInstrumentTwo.selectedIndex > 0) {\n\t\t instrumentTwo = dropInstrumentTwo.options[dropInstrumentTwo.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropInstrumentThree.selectedIndex > 0) {\n\t\t instrumentThree = dropInstrumentThree.options[dropInstrumentThree.selectedIndex].value;\n\t }");

		String message = localize("must_fill_out_department", "Please fill out department.");
		buffer.append("\n\t if(department < 0){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		
		message = localize("must_fill_out_lesson_type", "Please fill out lesson type.");
		buffer.append("\n\t if(lessonType < 0){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		
		buffer.append("\n\t if(instrumentOne > 0 && (instrumentOne == instrumentTwo || instrumentOne == instrumentThree)){");
		message = localize("instrument_must_not_be_the_same", "Please do not choose the same instrument more than once.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if(instrumentTwo > 0 && (instrumentTwo == instrumentOne || instrumentTwo == instrumentThree)){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if(instrumentThree > 0 && (instrumentThree == instrumentOne || instrumentThree == instrumentTwo )){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		message = localize("must_fill_out_instrument", "Please select at least one instrument.");
		buffer.append("\n\t if(instrumentOne == 0 && instrumentTwo == 0 && instrumentThree == 0){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");

		buffer.append("\n\t document.body.style.cursor = 'wait'");
		buffer.append("\n\t return true;");
		buffer.append("\n}\n");

		return buffer.toString();
	}
}