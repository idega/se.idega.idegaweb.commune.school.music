/*
 * Created on 9.5.2004
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
import se.idega.idegaweb.commune.school.music.business.MusicSchoolApplicationWriter;
import se.idega.idegaweb.commune.school.music.business.NoDepartmentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoInstrumentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoLessonTypeFoundException;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MediaWritable;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;


/**
 * @author laddi
 */
public class MusicSchoolApprover extends MusicSchoolBlock {

	private static final String PARAMETER_VIEW = "prm_view";
	private static final String PARAMETER_EDIT = "prm_edit";
	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_APPLICATION = "prm_application";
	private static final String PARAMETER_DEPARTMENT = "prm_department";
	private static final String PARAMETER_INSTRUMENTS = "prm_instruments";
	private static final String PARAMETER_LESSON_TYPE = "prm_lesson_type";

	private static final String PARAMETER_TEACHER_REQUEST = "prm_teacher_request";
	private static final String PARAMETER_OTHER_INSTRUMENT = "prm_other_instrument";
	private static final String PARAMETER_MESSAGE = "prm_message";
	private static final String PARAMETER_PREVIOUS_STUDIES = "prm_previoues_studies";
	private static final String PARAMETER_ELEMENTARY_SCHOOL = "prm_elementary_school";

	private static final int ACTION_ACCEPT = 1;
	private static final int ACTION_REJECT = 2;
	private static final int ACTION_SAVE = 3;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().getProvider() != null) {
			if (iwc.isParameterSet(PARAMETER_EDIT)) {
				add(getEditForm(iwc));
			}
			else if (iwc.isParameterSet(PARAMETER_VIEW)) {
				add(getViewForm(iwc));
			}
			else {
				Form form = new Form();
				form.setEventListener(MusicSchoolEventListener.class);
				
				Table table = new Table();
				table.setWidth(Table.HUNDRED_PERCENT);
				table.setCellpadding(0);
				table.setCellspacing(0);
				form.add(table);
				int row = 1;
				
				table.setCellpaddingLeft(1, row, 12);
				table.add(getNavigationTable(this), 1, row++);
				table.setHeight(row++, 12);
				
				table.setCellpaddingRight(1, row, 6);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.add(getXLSLink(), 1, row++);
				table.setHeight(row++, 3);

				parseAction(iwc);
				getChoicesTable(iwc, table, row);
		
				add(form);
			}
		}
		else {
			add(getErrorText(localize("no_school_found", "No school found for user")));
		}
	}
	
	private Link getXLSLink() {
		Window window = new Window(localize("applications", "Applications"), getIWApplicationContext().getIWMainApplication().getMediaServletURI());
		window.setResizable(true);
		window.setMenubar(true);
		window.setHeight(400);
		window.setWidth(500);
		
		Image image = getBundle().getImage("shared/xls.gif");
		image.setToolTip(localize("excel_list", "Get list in Excel format"));

		Link link = new Link(image);
		link.setWindow(window);
		link.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(MusicSchoolApplicationWriter.class));
		return link;
	}

	private void getChoicesTable(IWContext iwc, Table table, int row) throws RemoteException {
		Table choicesTable = new Table();
		choicesTable.setColumns(9);
		choicesTable.setWidth(Table.HUNDRED_PERCENT);
		choicesTable.setCellpadding(0);
		choicesTable.setCellspacing(0);
		table.add(choicesTable, 1, row++);
		int iColumn = 1;
		int iRow = 1;
		
		choicesTable.add(getSmallHeader(localize("number", "Nr.")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("choice_date", "Choice date")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("name", "Name")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("date_of_birth", "Date of birth")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("age", "Age")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("postal_code", "Postal code")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("instruments.plural_or_singular", "Instrument/s")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("department", "Department")), iColumn++, iRow);
		choicesTable.setCellpaddingLeft(1, iRow, 12);
		choicesTable.setRowStyleClass(iRow++, getHeaderRow2Class());
		
		if (getSession().getSeason() != null) {
			try {
				Collection choices = getBusiness().findChoicesInSchool(getSession().getProvider(), getSession().getSeason(), getSession().getDepartment(), getSession().getInstrument());
				MusicSchoolChoice choice;
				User user;
				Address address;
				PostalCode code;
				Collection instruments;
				SchoolYear department;
				Link userLink;
				IWTimestamp choiceDate;
				CheckBox box;
				boolean isPlaced = true;
				boolean hasInstrumentPlacement = false;
				boolean olderStudent = false;
				boolean showOlderStudentMessage = false;
				SchoolSeason previousSeason = getSchoolBusiness().findPreviousSchoolSeason(((Integer) getSession().getSeason().getPrimaryKey()).intValue());
				int applicationCount = 0;
				
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					iColumn = 1;
					
					choice = (MusicSchoolChoice) iter.next();
					user = choice.getChild();
					IWTimestamp dateOfBirth = new IWTimestamp(user.getDateOfBirth());
					Age age = new Age(user.getDateOfBirth());
					isPlaced = choice.getCaseStatus().equals(getBusiness().getCaseStatusPlaced());
					if (isPlaced && getSession().getInstrument() != null) {
						hasInstrumentPlacement = getBusiness().isPlacedInSchool(user, getSession().getProvider(), getSession().getSeason(), getSession().getInstrument());
					}
					else {
						hasInstrumentPlacement = false;
					}
					
					if (hasInstrumentPlacement || (getSession().getInstrument() == null && isPlaced)) {
						continue;
					}
					applicationCount++;

					address = getUserBusiness().getUsersMainAddress(user);
					if (address != null) {
						code = address.getPostalCode();
					}
					else {
						code = null;
					}
					if (choice.getChoiceDate() != null) {
						choiceDate = new IWTimestamp(choice.getChoiceDate());
					}
					else {
						choiceDate = null;
					}
					try {
						instruments = choice.getStudyPaths();
					}
					catch (IDORelationshipException ire) {
						log(ire);
						instruments = null;
					}
					department = choice.getSchoolYear();
					
					if (previousSeason != null) {
						olderStudent = getBusiness().isPlacedInSchool(user, getSession().getProvider(), previousSeason, null);
					}
					else {
						olderStudent = false;
					}
					
					choicesTable.add(getSmallText(String.valueOf(applicationCount)), iColumn++, iRow);

					if (choiceDate != null) {
						choicesTable.add(getSmallText(choiceDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
					}
					else {
						choicesTable.add(getSmallText("-"), iColumn++, iRow);
					}
					
					if (olderStudent) {
						showOlderStudentMessage = true;
						choicesTable.add(getSmallErrorText("* "), iColumn, iRow);
					}
					
					userLink = getSmallLink(user.getName());
					userLink.setEventListener(MusicSchoolEventListener.class);
					userLink.addParameter(getSession().getParameterNameChildID(), user.getPrimaryKey().toString());
					userLink.addParameter(getSession().getParameterNameApplicationID(), choice.getPrimaryKey().toString());
					userLink.addParameter(PARAMETER_VIEW, "true");
					
					choicesTable.add(userLink, iColumn++, iRow);
					choicesTable.add(getSmallText(dateOfBirth.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
					choicesTable.add(getSmallText(String.valueOf(age.getYears())), iColumn++, iRow);
					if (address != null) {
						if (code != null) {
							choicesTable.add(getSmallText(code.getPostalCode()), iColumn++, iRow);
						}
						else {
							choicesTable.add(getSmallText("-"), iColumn++, iRow);
						}
					}
					else {
						choicesTable.add(getSmallText("-"), iColumn++, iRow);
					}
					
					Iterator iterator = instruments.iterator();
					Text instrumentText = null;
					while (iterator.hasNext()) {
						SchoolStudyPath instrument = (SchoolStudyPath) iterator.next();
						if (instrumentText == null) {
							instrumentText = getSmallText(localize(instrument.getLocalizedKey(), instrument.getDescription()));
						}
						else {
							instrumentText.addToText(localize(instrument.getLocalizedKey(), instrument.getDescription()));
						}
						
						if (iterator.hasNext()) {
							instrumentText.addToText(", ");
						}
					}
					choicesTable.add(instrumentText, iColumn++, iRow);
					choicesTable.add(getSmallText(localize(department.getLocalizedKey(), department.getSchoolYearName())), iColumn++, iRow);
					
					box = getCheckBox(PARAMETER_APPLICATION, choice.getPrimaryKey().toString());
					choicesTable.setWidth(iColumn, iRow, 12);
					choicesTable.add(box, iColumn, iRow);
					
					choicesTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						choicesTable.setRowStyleClass(iRow, getLightRowClass());
					}
					else {
						choicesTable.setRowStyleClass(iRow, getDarkRowClass());
					}
					
					iRow++;
				}
				choicesTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);
				
				if (showOlderStudentMessage) {
					table.setHeight(row++, 6);
					table.setCellpaddingLeft(1, row, 12);
					table.add(getSmallErrorText("* "), 1, row);
					table.add(getSmallText(localize("student_previously_placed", "Student is previously placed at school")), 1, row++);
				}
				
				table.setHeight(row++, 12);
				
				SubmitButton reject = (SubmitButton) getButton(new SubmitButton(localize("reject", "Reject"), PARAMETER_ACTION, String.valueOf(ACTION_REJECT)));
				reject.setSubmitConfirm(localize("confirm_reject", "Are you sure you want to reject the selected applications?"));
				SubmitButton accept = (SubmitButton) getButton(new SubmitButton(localize("accept_applications", "Accept applications"), PARAMETER_ACTION, String.valueOf(ACTION_ACCEPT)));
				accept.setSubmitConfirm(localize("confirm_accep_applications", "Are you sure you want to accept the selected applications?"));
				
				table.add(accept, 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
				table.add(reject, 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
				table.add(getHelpButton("help_music_school_approve_applications"), 1, row);
				table.setCellpaddingRight(1, row, 12);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			}
			catch (FinderException fe) {
				table.setHeight(row++, 6);
				table.add(getErrorText(localize("no_choices_found", "No choices found...")), 1, row);
				return;
			}
		}
	}
	
	private Form getViewForm(IWContext iwc) throws RemoteException {
		Form form = new Form();

		MusicSchoolChoice choice = getSession().getApplication();
		if (choice == null) {
			form.add(getErrorText(localize("no_application_found", "No application found...")));
			return form; 
		}
		SchoolYear department = choice.getSchoolYear();
		SchoolType lessonType = choice.getSchoolType();
		String otherInstrument = choice.getOtherInstrument();
		String teacherRequest = choice.getTeacherRequest();
		String message = choice.getMessage();
		String previousStudies = choice.getPreviousStudies();
		String elementarySchool = choice.getElementarySchool();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getPersonInfoTable(iwc, choice.getChild()), 1, row++);
		
		table.setHeight(row++, 18);
		
		Table viewTable = new Table();
		viewTable.setCellpadding(2);
		viewTable.setCellspacing(2);
		viewTable.setColumns(2);
		table.setCellpaddingLeft(1, row, 12);
		table.add(viewTable, 1, row++);
		int viewRow = 1;
		
		Collection chosenInstruments = null;
		try {
			chosenInstruments = new ArrayList(choice.getStudyPaths());
		}
		catch (IDORelationshipException ire) {
			log(ire);
		}

		if (chosenInstruments != null) {
			int index = 1;
			Iterator iter = chosenInstruments.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				if (index == 1) {
					viewTable.add(getSmallHeader(localize("first_instrument", "First instrument")), 1, viewRow);
				}
				else if (index == 2) {
					viewTable.add(getSmallHeader(localize("second_instrument", "Second instrument")), 1, viewRow);
				}
				else if (index == 3) {
					viewTable.add(getSmallHeader(localize("third_instrument", "Third instrument")), 1, viewRow);
				}
				viewTable.add(getText(localize(instrument.getLocalizedKey(), instrument.getDescription())), 2, viewRow++);
				index++;

				viewTable.setHeight(viewRow++, 3);
			}
		}
		
		if (otherInstrument != null) {
			viewTable.add(getSmallHeader(localize("other_instrument", "Other instrument")), 1, viewRow);
			viewTable.add(getText(otherInstrument), 2, viewRow++);
		}
		
		viewTable.setHeight(viewRow++, 12);
		
		if (department != null) {
			viewTable.add(getSmallHeader(localize("department", "Department")), 1, viewRow);
			viewTable.add(getText(localize(department.getLocalizedKey(), department.getSchoolYearName())), 2, viewRow++);
			viewTable.setHeight(viewRow++, 3);
		}
		
		if (lessonType != null) {
			viewTable.add(getSmallHeader(localize("lesson_type", "Lesson type")), 1, viewRow);
			viewTable.add(getText(localize(lessonType.getLocalizationKey(), lessonType.getSchoolTypeName())), 2, viewRow++);
			viewTable.setHeight(viewRow++, 3);
		}
		
		if (teacherRequest != null) {
			viewTable.add(getSmallHeader(localize("teacher_request", "Teacher request")), 1, viewRow);
			viewTable.add(getText(teacherRequest), 2, viewRow++);
		}
		
		table.setHeight(row++, 18);
		
		if (elementarySchool != null) {
			table.add(getSmallHeader(localize("elementary_school", "Elementary school")), 1, row++);
			table.add(getText(elementarySchool), 1, row++);
			table.setHeight(row++, 6);
			
		}
		
		if (previousStudies != null) {
			table.add(getSmallHeader(localize("previous_studies", "Previous studies")), 1, row++);
			table.add(getText(previousStudies), 1, row++);
			table.setHeight(row++, 6);
			
		}
		
		if (message != null) {
			table.add(getSmallHeader(localize("message", "Message")), 1, row++);
			table.add(getText(message), 1, row++);
		}
		
		table.setHeight(row++, 18);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_ACTION, String.valueOf(-1)));
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("change", "Change"), PARAMETER_EDIT, Boolean.TRUE.toString()));

		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(submit, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_music_school_view_application"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		return form;
	}
	
	private Form getEditForm(IWContext iwc) throws FinderException, RemoteException {
		Form form = new Form();

		MusicSchoolChoice choice = getSession().getApplication();
		if (choice == null) {
			form.add(getErrorText(localize("no_application_found", "No application found...")));
			return form; 
		}
		
		User user = choice.getChild();
		Age age = new Age(user.getDateOfBirth());
		
		form.addParameter(PARAMETER_APPLICATION, choice.getPrimaryKey().toString());
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getPersonInfoTable(iwc, choice.getChild()), 1, row++);
		
		table.setHeight(row++, 18);
		
		Table editTable = new Table();
		editTable.setCellpadding(0);
		editTable.setCellspacing(0);
		editTable.setColumns(2);
		table.setCellpaddingLeft(1, row, 12);
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
			chosenInstruments = new ArrayList(choice.getStudyPaths());
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
		
		DropdownMenu departmentDrop = getDropdown(PARAMETER_DEPARTMENT, choice.getSchoolYearPK());
		departmentDrop.addMenuElementFirst("-1", localize("select_department", "Select department"));
		Iterator iter = departments.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			if (year.isSelectable()) {
				departmentDrop.addMenuElement(year.getPrimaryKey().toString(), localize(year.getSchoolYearName(), year.getSchoolYearName()));
			}
		}

		DropdownMenu lessonTypeDrop = getDropdown(PARAMETER_LESSON_TYPE, choice.getSchoolTypePK());
		lessonTypeDrop.addMenuElementFirst("-1", localize("select_lesson_type", "Select lesson type"));
		iter = lessonTypes.iterator();
		while (iter.hasNext()) {
			SchoolType type = (SchoolType) iter.next();
			lessonTypeDrop.addMenuElement(type.getPrimaryKey().toString(), localize(type.getLocalizationKey(), type.getSchoolTypeName()));
		}
		
		TextInput teacherRequest = getTextInput(PARAMETER_TEACHER_REQUEST, choice.getTeacherRequest());
		TextInput otherInstrument = getTextInput(PARAMETER_OTHER_INSTRUMENT, choice.getOtherInstrument());

		editTable.add(getText(localize("instrument_1", "Instrument 1")), 1, editRow);
		editTable.add(instrumentsDrop1, 2, editRow++);
		editTable.setHeight(editRow++, 3);

		editTable.add(getText(localize("instrument_2", "Instrument 2")), 1, editRow);
		editTable.add(instrumentsDrop2, 2, editRow++);
		editTable.setHeight(editRow++, 3);

		editTable.add(getText(localize("instrument_3", "Instrument 3")), 1, editRow);
		editTable.add(instrumentsDrop3, 2, editRow++);
		editTable.setHeight(editRow++, 3);

		editTable.add(getText(localize("other_instrument", "Other instrument")), 1, editRow);
		editTable.add(otherInstrument, 2, editRow++);
		
		editTable.setHeight(editRow++, 12);

		editTable.add(getText(localize("department", "Department")), 1, editRow);
		editTable.add(departmentDrop, 2, editRow++);
		editTable.setHeight(editRow++, 3);

		editTable.add(getText(localize("lesson_type", "Lesson type")), 1, editRow);
		editTable.add(lessonTypeDrop, 2, editRow++);
		editTable.setHeight(editRow++, 3);

		editTable.add(getText(localize("teacher_request", "Teacher request")), 1, editRow);
		editTable.add(teacherRequest, 2, editRow++);
		
		table.setHeight(row++, 18);
		
		if (age.getYears() < 16) {
			table.setCellpaddingLeft(1, row, 12);
			table.add(getText(localize("elementary_school", "Elementary school")), 1, row++);
			table.setCellpaddingLeft(1, row, 12);
			TextInput elementarySchool = getTextInput(PARAMETER_ELEMENTARY_SCHOOL, choice.getElementarySchool());
			elementarySchool.setDisabled(true);
			table.add(elementarySchool, 1, row++);
			table.setHeight(row++, 6);
		}
		
		TextArea previousStudies = getTextArea(PARAMETER_PREVIOUS_STUDIES, choice.getPreviousStudies());
		previousStudies.setHeight("50");
		previousStudies.setDisabled(true);
		table.setCellpaddingLeft(1, row, 12);
		table.add(getText(localize("previous_studies", "Previous studies")), 1, row++);
		table.setCellpaddingLeft(1, row, 12);
		table.add(previousStudies, 1, row++);
		
		table.setHeight(row++, 12);
		
		TextArea message = getTextArea(PARAMETER_MESSAGE, choice.getMessage());
		message.setHeight("50");
		message.setDisabled(true);
		table.setCellpaddingLeft(1, row, 12);
		table.add(getText(localize("message", "message")), 1, row++);
		table.setCellpaddingLeft(1, row, 12);
		table.add(message, 1, row++);
		
		table.setHeight(row++, 18);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_VIEW, Boolean.TRUE.toString()));
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("change", "Change"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));

		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(submit, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_music_school_edit_application"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		submit.setOnSubmitFunction("checkApplication", getSubmitConfirmScript());
		form.setToDisableOnSubmit(submit, true);

		return form;
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
	
	private void parseAction(IWContext iwc) {
		int action = -1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			action = -1;
		}

		String[] students = iwc.getParameterValues(PARAMETER_APPLICATION);
		switch (action) {
			case ACTION_ACCEPT:
				try {
					getBusiness().addStudentsToGroup(students, getBusiness().getDefaultGroup(getSession().getProvider(), getSession().getSeason()), getSession().getDepartment(), getSession().getInstrument(), iwc.getCurrentUser());
					if (getParentPage() != null) {
						getParentPage().setAlertOnLoad(localize("selected_applications_accepted", "The selected applications have been accepted."));
					}
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
				break;

			case ACTION_REJECT:
				try {
					getBusiness().rejectApplications(students, iwc.getCurrentUser());
					if (getParentPage() != null) {
						getParentPage().setAlertOnLoad(localize("selected_applications_rejected", "The selected applications have been rejected."));
					}
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
				break;

			case ACTION_SAVE:
				Collection instrumentPKs = new ArrayList();
				for (int i = 0; i < 3; i++) {
					if (iwc.isParameterSet(PARAMETER_INSTRUMENTS + "_" + (i+1))) {
						instrumentPKs.add(iwc.getParameter(PARAMETER_INSTRUMENTS + "_" + (i+1)));
					}
				}
				String application = iwc.getParameter(PARAMETER_APPLICATION);
				String lessonType = iwc.getParameter(PARAMETER_LESSON_TYPE);
				String department = iwc.getParameter(PARAMETER_DEPARTMENT);
				String otherInstrument = iwc.getParameter(PARAMETER_OTHER_INSTRUMENT);
				String teacherRequest = iwc.getParameter(PARAMETER_TEACHER_REQUEST);
				
				try {
					getBusiness().updateChoice(application, department, lessonType, instrumentPKs, teacherRequest, otherInstrument);
					if (getParentPage() != null) {
						getParentPage().setAlertOnLoad(localize("selected_application_updated", "The selected application has been updated."));
					}
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
				break;
		}
	}
}