/*
 * Created on 3.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.music.business.NoDepartmentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoInstrumentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoLessonTypeFoundException;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;

import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public class MusicSchoolApplication extends MusicSchoolBlock {
	
	private static final int ACTION_FORM = 1;
	private static final int ACTION_VERIFY = 2;
	private static final int ACTION_SAVE = 3;
	
	private static final String PARAMETER_ACTION = "prm_action";

	private static final String PARAMETER_SCHOOLS = "prm_school";
	private static final String PARAMETER_SEASON = "prm_season";
	private static final String PARAMETER_DEPARTMENT = "prm_department";
	private static final String PARAMETER_INSTRUMENTS = "prm_instruments";
	private static final String PARAMETER_LESSON_TYPE = "prm_lesson_type";

	private static final String PARAMETER_TEACHER_REQUEST = "prm_teacher_request";
	private static final String PARAMETER_MESSAGE = "prm_message";
	private static final String PARAMETER_PREVIOUS_STUDIES = "prm_previoues_studies";
	private static final String PARAMETER_ELEMENTARY_SCHOOL = "prm_elementary_school";
	private static final String PARAMETER_CURRENT_YEAR = "prm_current_year";
	private static final String PARAMETER_CURRENT_INSTRUMENT = "prm_current_instrument";
	private static final String PARAMETER_PAYMENT_METHOD = "prm_payment_method";

	private static final String PARAMETER_HOME_PHONE = "prm_home_phone";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile_phone";
	private static final String PARAMETER_EMAIL = "prm_email";

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().getChild() != null) {
			switch (parseAction(iwc)) {
				case ACTION_FORM:
					showForm(iwc);
					break;

				case ACTION_VERIFY:
					
					break;

				case ACTION_SAVE:
					saveChoices(iwc);
					break;
			}
		}
		else {
			add(getErrorText(localize("application.no_user_found", "No user found...")));
		}
	}
	
	private void showForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(MusicSchoolEventListener.class);
		
		Table table = new Table();
		table.setColumns(2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		SchoolSeason season = null;
		try {
			season = getSchoolCommuneBusiness(iwc).getCurrentSchoolSeason();
		}
		catch (FinderException fe) {
			log(fe);
			add(getErrorText(localize("no_season_found", "No season found...")));
			return;
		}
		
		if (getBusiness().hasGrantedApplication(getSession().getChild(), season)) {
			add(getErrorText(localize("student_already_has_placement", "Student already has a granted placement for the school season")));
			add(new Break(2));
			add(new UserHomeLink());
			return;
		}
		
		Collection instruments = null;
		try {
			instruments = getInstruments();
		}
		catch (NoInstrumentFoundException nife) {
			log(nife);
			add(getErrorText(localize("no_instruments_found", "No instruments found...")));
			return;
		}
		
		Collection departments = null;
		try {
			departments = getDepartments();
		}
		catch (NoDepartmentFoundException ndfe) {
			log(ndfe);
			add(getErrorText(localize("no_departments_found", "No departments found...")));
			return;
		}
		
		Collection lessonTypes = null;
		try {
			lessonTypes = getLessonTypes();
		}
		catch (NoLessonTypeFoundException ndfe) {
			log(ndfe);
			add(getErrorText(localize("no_departments_found", "No departments found...")));
			return;
		}
		
		Collection chosenInstruments = null;
		Object chosenSchool1 = null;
		Object chosenSchool2 = null;
		Object chosenSchool3 = null;
		Object chosenDepartment = null;
		Object chosenLessonType = null;
		String chosenTeacher = null;
		String chosenPreviousStudy = null;
		Object chosenCurrentYear = null;
		Object chosenCurrentInstrument = null;
		String chosenMessage = null;
		String chosenElementarySchool = null;
		//int chosenPaymentMethod = -1;
		
		Collection choices = null;
		try {
			choices = getBusiness().findChoicesByChildAndSeason(getSession().getChild(), season);
			Iterator iter = choices.iterator();
			boolean initialValuesSet = false;
			int choiceNumber = 1;
			while (iter.hasNext()) {
				MusicSchoolChoice choice = (MusicSchoolChoice) iter.next();
				choiceNumber = choice.getChoiceNumber();
				if (!initialValuesSet) {
					try {
						chosenInstruments = choice.getStudyPaths();
					}
					catch (IDORelationshipException ire) {
						log(ire);
						break;
					}
					chosenDepartment = choice.getSchoolYearPK();
					chosenLessonType = choice.getSchoolTypePK();
					chosenTeacher = choice.getTeacherRequest();
					chosenPreviousStudy = choice.getPreviousStudies();
					chosenCurrentYear = choice.getPreviousYearPK();
					chosenCurrentInstrument = choice.getPreviousStudyPathPK();
					chosenMessage = choice.getMessage();
					chosenElementarySchool = choice.getElementarySchool();
					//chosenPaymentMethod = choice.getPaymentMethod();
					initialValuesSet = true;
				}
				if (choiceNumber == 1) {
					chosenSchool1 = choice.getSchoolPK();
					getParentPage().setOnLoad("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_1'), '"+chosenSchool1+"');");
				}
				else if (choiceNumber == 2) {
					chosenSchool2 = choice.getSchoolPK();
					getParentPage().setOnLoad("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_2'), '"+chosenSchool2+"');");
				}
				else if (choiceNumber == 3) {
					chosenSchool3 = choice.getSchoolPK();
					getParentPage().setOnLoad("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_3'), '"+chosenSchool3+"');");
				}
				choiceNumber++;
			}
		}
		catch (FinderException fe) {
			//Nothing found...
		}
		if (choices != null) {
			if (chosenSchool2 == null) {
				getParentPage().setOnLoad("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_2'));");
			}
			if (chosenSchool3 == null) {
				getParentPage().setOnLoad("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_3'));");
			}
		}
		form.add(getJavascript(instruments, iwc.getLocale()));
		
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADER_CELL));
		table.add(getBigHeader(localize("application.application_for_music_school", "Application for music school")), 1, row++);
		
		//Showing applicant information
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
		table.add(getHeader(localize("application.applicant", "Applicant")), 1, row++);
		
		User child = getSession().getChild();
		Address address = getUserBusiness().getUsersMainAddress(child);
		Phone phone = null;
		try {
			phone = getUserBusiness().getUsersHomePhone(child);
		}
		catch (NoPhoneFoundException npfe) {
			phone = null;
		}
		Phone mobile = null;
		try {
			mobile = getUserBusiness().getUsersMobilePhone(child);
		}
		catch (NoPhoneFoundException npfe) {
			mobile = null;
		}
		Email email = null;
		try {
			email = getUserBusiness().getUsersMainEmail(child);
		}
		catch (NoEmailFoundException nefe) {
			email = null;
		}
		
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("name", "Name")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("personal_id", "Personal ID")), 2, row++);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(getTextInput("name", child.getName(), true), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(getTextInput("personalID", PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale()), true), 2, row++);
		
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("address", "Address")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("zip_code", "Zip code")), 2, row++);
		TextInput addr = getTextInput("address", null, true);
		TextInput zip = getTextInput("zipCode", null, true);
		if (address != null) {
			addr.setContent(address.getStreetAddress());
			PostalCode code = address.getPostalCode();
			if (code != null) {
				zip.setContent(code.getPostalAddress());
			}
		}
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(addr, 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(zip, 2, row++);

		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("home_phone", "Home phone")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("mobile_phone", "Mobile phone")), 2, row++);
		TextInput homePhone = getTextInput(PARAMETER_HOME_PHONE, null, false);
		TextInput mobilePhone = getTextInput(PARAMETER_MOBILE_PHONE, null, false);
		if (phone != null) {
			homePhone.setContent(phone.getNumber());
		}
		if (mobile != null) {
			mobilePhone.setContent(mobile.getNumber());
		}
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(homePhone, 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(mobilePhone, 2, row++);

		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("email", "E-mail")), 1, row++);
		TextInput mail = getTextInput(PARAMETER_EMAIL, null, false);
		if (email != null) {
			mail.setContent(email.getEmailAddress());
		}
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(mail, 1, row++);

		//Done showing applicant information
		
		table.setHeight(row++, 18);
		
		//Showing application selection
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
		table.add(getHeader(localize("application.selection", "Selection")), 1, row++);
		
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("instruments", "Instruments")), 1, row++);
		table.mergeCells(1, row, 2, row);
		
		Table instrumentTable = new Table();
		instrumentTable.setWidth(Table.HUNDRED_PERCENT);
		instrumentTable.setStyleAttribute("border:1px solid #000000;");
		instrumentTable.setColor("#FFFFFF");
		instrumentTable.setCellpadding(2);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(instrumentTable, 1, row);
		table.setVerticalAlignment(1, row++, Table.VERTICAL_ALIGN_TOP);
		int iRow = 1;
		int numberOfInstruments = instruments.size();
		int lastRow = numberOfInstruments % 4 == 0 ? numberOfInstruments / 4 : (numberOfInstruments / 4) + 1;
		int iColumn = 1;
		
		if (instruments != null) {
			Iterator iter = instruments.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				if (iColumn > 4) {
					iRow++;
					iColumn = 1;
				}
				
				CheckBox box = getCheckBox(PARAMETER_INSTRUMENTS, instrument.getPrimaryKey().toString());
				box.setOnClick("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_1'));");
				box.setOnClick("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_2'));");
				box.setOnClick("filter(findObj('" + PARAMETER_INSTRUMENTS + "'), findObj('" + PARAMETER_SCHOOLS + "_3'));");
				if (chosenInstruments != null) {
					box.setChecked(chosenInstruments.contains(instrument));
				}
				if (iRow == 1) {
					instrumentTable.setCellpaddingTop(iColumn, iRow, 4);
				}
				else if (iRow == lastRow) {
					instrumentTable.setCellpaddingBottom(iColumn, iRow, 4);
				}
				if (iColumn == 1) {
					instrumentTable.setCellpaddingLeft(iColumn, iRow, 4);
				}
				else if (iColumn == 4) {
					instrumentTable.setCellpaddingRight(iColumn, iRow, 4);
				}
				instrumentTable.setWidth(iColumn, iRow, "25%");
				instrumentTable.add(box, iColumn, iRow);
				instrumentTable.add(getSmallText(Text.NON_BREAKING_SPACE), iColumn, iRow);
				instrumentTable.add(getSmallText(localize(instrument.getLocalizedKey(), instrument.getDescription())), iColumn++, iRow);
			}
		}
		
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("first_school", "First choice")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("department", "Department")), 2, row++);
		
		DropdownMenu school1 = getDropdown(PARAMETER_SCHOOLS+"_1", chosenSchool1);
		school1.addMenuElement("-1", localize("select_school", "Select school"));
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(school1, 1, row);

		DropdownMenu departmentDrop = getDropdown(PARAMETER_DEPARTMENT, chosenDepartment);
		departmentDrop.addMenuElementFirst("-1", localize("select_department", "Select department"));
		Iterator iter = departments.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			if (year.isSelectable()) {
				departmentDrop.addMenuElement(year.getPrimaryKey().toString(), localize(year.getSchoolYearName(), year.getSchoolYearName()));
			}
		}
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(departmentDrop, 2, row++);

		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("second_school", "Second choice")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("lesson_type", "Lesson type")), 2, row++);

		DropdownMenu school2 = getDropdown(PARAMETER_SCHOOLS+"_2", chosenSchool2);
		school2.addMenuElement("-1", localize("select_school", "Select school"));
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(school2, 1, row);

		DropdownMenu lessonTypeDrop = getDropdown(PARAMETER_LESSON_TYPE, chosenLessonType);
		lessonTypeDrop.addMenuElementFirst("-1", localize("select_lesson_type", "Select lesson type"));
		iter = lessonTypes.iterator();
		while (iter.hasNext()) {
			SchoolType type = (SchoolType) iter.next();
			lessonTypeDrop.addMenuElement(type.getPrimaryKey().toString(), localize(type.getLocalizationKey(), type.getSchoolTypeName()));
		}
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(lessonTypeDrop, 2, row++);

		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("third_school", "Third school")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("teacher_request", "Teacher request")), 2, row++);

		DropdownMenu school3 = getDropdown(PARAMETER_SCHOOLS+"_3", chosenSchool3);
		school3.addMenuElement("-1", localize("select_school", "Select school"));
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(school3, 1, row);
		
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(getTextInput(PARAMETER_TEACHER_REQUEST, chosenTeacher), 2, row++);
		
		TextArea message = getTextArea(PARAMETER_MESSAGE, chosenMessage);
		message.setHeight("50");
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("message", "message")), 1, row++);
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(message, 1, row++);
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INFORMATION_CELL));
		table.add(localize("application.message_information", "(for example, if applicant has been in a choir)"), 1, row++);
		
		
		//Done with application selection
		
		table.setHeight(row++, 18);
		
		//Showing current situation selection
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("previous_studies", "Previous studies")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("elementary_school", "Elementary school")), 2, row++);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(getTextInput(PARAMETER_PREVIOUS_STUDIES, chosenPreviousStudy), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(getTextInput(PARAMETER_ELEMENTARY_SCHOOL, chosenElementarySchool), 2, row++);
		table.setHeight(row++, 3);
		
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
		table.add(getHeader(localize("application.current_status", "Current status")), 1, row++);
		
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("current_year", "Current year")), 1, row);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("current_instrument", "Current instrument")), 2, row++);

		DropdownMenu currentYear = getDropdown(PARAMETER_CURRENT_YEAR, chosenCurrentYear);
		currentYear.addMenuElementFirst("", localize("select_current_year", "Select year"));
		iter = departments.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			currentYear.addMenuElement(year.getPrimaryKey().toString(), localize(year.getLocalizedKey(), year.getSchoolYearName()));
		}
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(currentYear, 1, row);

		DropdownMenu currentInstrument = getDropdown(PARAMETER_CURRENT_INSTRUMENT, chosenCurrentInstrument);
		currentInstrument.addMenuElementFirst("", localize("select_current_instrument", "Select instrument"));
		iter = instruments.iterator();
		while (iter.hasNext()) {
			SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
			currentInstrument.addMenuElement(instrument.getPrimaryKey().toString(), localize(instrument.getCode(), instrument.getDescription()));
		}
		table.setStyleClass(2, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(currentInstrument, 2, row++);

		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INFORMATION_CELL));
		table.add(localize("application.current_information", "(example, 4th grade in singing)"), 1, row++);
		
		//Done with current situation

		table.setHeight(row++, 18);
		
		//Showing payment method selection
		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
		table.add(getHeader(localize("application.information", "Information")), 1, row++);

		/*table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("payment_method_header", "Payment method for music schools")), 1, row++);*/

		table.mergeCells(1, row, 2, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_INFORMATION_CELL));
		table.add(getSmallText(TextSoap.formatTabsAndReturnsToHtml(localize("payment_method_information", "Information about payment..."))), 1, row++);
		
		/*table.setHeight(row++, 12);
		
		table.setStyleClass(1, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(getText(localize("payment_method", "How do you want to pay?")), 1, row++);
		DropdownMenu paymentMethods = getPaymentMethods(PARAMETER_PAYMENT_METHOD, chosenPaymentMethod);
		paymentMethods.addMenuElementFirst("-1", localize("select_paymentType", "Select payment method"));
		table.setStyleClass(1, row, getStyleName(STYLENAME_INPUT_CELL));
		table.add(paymentMethods, 1, row++);*/

		//Done with payment method situation

		table.setHeight(row++, 18);
		
		//Showing submit buttons
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("next", "Next &gt;&gt;"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		table.add(submit, 2, row);
		submit.setOnSubmitFunction("checkApplication", getSubmitConfirmScript());
		form.setToDisableOnSubmit(submit, true);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
		table.add(new HiddenInput(PARAMETER_SEASON, season.getPrimaryKey().toString()));
		
		add(form);
	}
	
	private Script getJavascript(Collection instruments, Locale locale) {
		Script script = new Script();
		
		StringBuffer filterFunction = new StringBuffer();
		filterFunction.append("function filter(inputs, iDropdowns, selectedValue) {").append("\n\t");
		filterFunction.append("var selected = new Array();").append("\n\t");
		filterFunction.append("if (inputs.length > 1) {").append("\n\t\t");
		filterFunction.append("for (var a = 0; a < inputs.length; a++) {").append("\n\t\t\t");
		filterFunction.append("if (inputs[a].checked == true) {").append("\n\t\t\t\t");
		filterFunction.append("selected[selected.length] = inputs[a].value;").append("\n\t\t\t");
		filterFunction.append("}").append("\n\t\t").append("}").append("\n\t").append("}").append("\n\t");
		filterFunction.append("else {").append("\n\t\t");
		filterFunction.append("if (inputs.checked == true) {").append("\n\t\t\t");
		filterFunction.append("selected[selected.length] = inputs.value;").append("\n\t\t");
		filterFunction.append("}").append("\n\t").append("}").append("\n\t");
		filterFunction.append("filterDropdown(iDropdowns, selected, selectedValue);").append("\n").append("}\n");
		script.addFunction("filter", filterFunction.toString());
		
		Map schoolMap = null;
		try {
			schoolMap = getBusiness().getInstrumentSchoolMap(instruments, locale);
		}
		catch (RemoteException re) {
			log(re);
		}
		
		if (schoolMap != null) {
			StringBuffer s = new StringBuffer();
			s.append("function getDropdownValues() {").append("\n\t");
			s.append("var dropdownValues = new Array();").append("\n\t");
			
			int column = 0;
			if (schoolMap != null) {
				Iterator iter = schoolMap.keySet().iterator();
				while (iter.hasNext()) {
					column = 0;
					String key = (String) iter.next();
					Collection map = (Collection) schoolMap.get(key);
					s.append("\n\t").append("dropdownValues[\""+key+"\"] = new Array();").append("\n\t");
					s.append("dropdownValues[\""+key+"\"]["+column+++"] = new Option('"+localize("select_school", "Select school")+"','-1');").append("\n\t");
					
					Iterator iterator = map.iterator();
					while (iterator.hasNext()) {
						School element = (School) iterator.next();
						String secondKey = element.getPrimaryKey().toString();
						String value = element.getSchoolName();
						s.append("dropdownValues[\""+key+"\"]["+column+++"] = new Option('"+value+"','"+secondKey+"');").append("\n\t");
					}
				}
			}
			s.append("return dropdownValues;").append("\n}\n");
			script.addFunction("getDropdownValues", s.toString());
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("function filterDropdown(dropdown, selected, selectedValue) {").append("\n\t");
		sb.append("var values = getDropdownValues();").append("\n\t");
		sb.append("var iOptions;").append("\n\n\t");
		sb.append("if (selected.length > 0) {").append("\n\t\t");
		sb.append("iOptions = values[selected[0]];").append("\n\t\t");
		sb.append("for (var a = 1; a < selected.length; a++) {").append("\n\t\t\t");
		sb.append("var nextCheck = values[selected[a]];").append("\n\t\t\t");
		sb.append("var tempArray = new Array();").append("\n\n\t\t\t");
		sb.append("for (var b = 0; b < nextCheck.length; b++) {").append("\n\t\t\t\t");
		sb.append("for (var c = 0; c < iOptions.length; c++) {").append("\n\t\t\t\t\t");
		sb.append("if (nextCheck[b].value == iOptions[c].value) {").append("\n\t\t\t\t\t\t");
		sb.append("tempArray[tempArray.length] = iOptions[c];").append("\n\t\t\t\t\t");
		sb.append("}").append("\n\t\t\t\t").append("}").append("\n\t\t\t").append("}").append("\n\t\t\t");
		sb.append("iOptions = tempArray;").append("\n\t\t").append("}").append("\n\n\t\t");
		sb.append("var selectedOption = dropdown.options[dropdown.selectedIndex].value;").append("\n\t\t");
		sb.append("dropdown.options.length = 0;").append("\n\n\t\t");
		sb.append("for (var a = 0; a < iOptions.length; a++) {").append("\n\t\t\t");
		sb.append("var index = dropdown.options.length;").append("\n\t\t\t");
		sb.append("dropdown.options[index] = iOptions[a];").append("\n\t\t\t");
		sb.append("if (dropdown.options[index].value == selectedValue) {").append("\n\t\t\t\t");
		sb.append("dropdown.options[index].selected = true;").append("\n\t\t\t").append("}").append("\n\t\t\t");
		sb.append("if (dropdown.options[index].value == selectedOption) {").append("\n\t\t\t\t");
		sb.append("dropdown.options[index].selected = true;").append("\n\t\t\t").append("}");
		sb.append("\n\t\t").append("}").append("\n\t").append("}").append("\n\t");
		sb.append("else {").append("\n\t\t");
		sb.append("dropdown.options.length = 0;").append("\n\t\t");
		sb.append("dropdown.options[0] = new Option('"+localize("select_school", "Select school")+"', '-1');").append("\n\t").append("}").append("\n").append("}\n");
		script.addFunction("filterDropdown", sb.toString());
		
		return script;
	}
	
	private String getSubmitConfirmScript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("function checkApplication() {").append("\n\t");
		buffer.append("\n\t var dropOne = ").append("findObj('").append(PARAMETER_SCHOOLS + "_1").append("');");
		buffer.append("\n\t var dropTwo = ").append("findObj('").append(PARAMETER_SCHOOLS + "_2").append("');");
		buffer.append("\n\t var dropThree = ").append("findObj('").append(PARAMETER_SCHOOLS + "_3").append("');");
		buffer.append("\n\t var dropDepartment = ").append("findObj('").append(PARAMETER_DEPARTMENT).append("');");
		buffer.append("\n\t var dropLessonTypes = ").append("findObj('").append(PARAMETER_LESSON_TYPE).append("');");

		buffer.append("\n\t var one = 0;");
		buffer.append("\n\t var two = 0;");
		buffer.append("\n\t var three = 0;");
		buffer.append("\n\t var department = -1;");
		buffer.append("\n\t var lessonType = -1;");
		buffer.append("\n\t var length = 0;");

		buffer.append("\n\n\t if (dropOne.selectedIndex > 0) {\n\t\t one = dropOne.options[dropOne.selectedIndex].value;\n\t\t if (one > 0) length++;\n\t }");
		buffer.append("\n\t if (dropTwo.selectedIndex > 0) {\n\t\t two = dropTwo.options[dropTwo.selectedIndex].value;\n\t\t if (two > 0) length++;\n\t }");
		buffer.append("\n\t if (dropThree.selectedIndex > 0) {\n\t\t three = dropThree.options[dropThree.selectedIndex].value;\n\t\t if (three > 0) length++;\n\t }");
		buffer.append("\n\t if (dropDepartment.selectedIndex > 0) {\n\t\t department = dropDepartment.options[dropDepartment.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropLessonTypes.selectedIndex > 0) {\n\t\t lessonType = dropLessonTypes.options[dropLessonTypes.selectedIndex].value;\n\t }");

		buffer.append("\n\t if(length > 0){");
		buffer.append("\n\t\t if(one > 0 && (one == two || one == three)){");
		String message = localize("must_not_be_the_same", "Please do not choose the same provider more than once.");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if(two > 0 && (two == one || two == three)){");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");
		buffer.append("\n\t\t if(three > 0 && (three == one || three == two )){");
		buffer.append("\n\t\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t\t return false;");
		buffer.append("\n\t\t }");

		buffer.append("\n\t }");
		buffer.append("\n\t else {");
		message = localize("must_fill_out_one", "Please fill out the first choice.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		message = localize("less_than_three_chosen", "You have chosen less than three choices.  An offer can not be guaranteed within three months.");

		message = localize("must_fill_out_department", "Please fill out department.");
		buffer.append("\n\t if(department < 0){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		
		message = localize("must_fill_out_lesson_type", "Please fill out lesson type.");
		buffer.append("\n\t if(lessonType < 0){");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		
		buffer.append("\n\t document.body.style.cursor = 'wait'");
		buffer.append("\n\t return true;");
		buffer.append("\n}\n");

		return buffer.toString();
	}
	
	private TextInput getTextInput(String parameterName, Object content) {
		return getTextInput(parameterName, content, false);
	}
	
	private TextInput getTextInput(String parameterName, Object content, boolean disabled) {
		TextInput input = (TextInput) getStyledInterface(new TextInput(parameterName));
		input.setWidth("100%");
		if (content != null) {
			input.setContent(content.toString());
		}
		input.setDisabled(disabled);
		
		return input;
	}
	
	private TextArea getTextArea(String parameterName, Object content) {
		TextArea area = (TextArea) getStyledInterface(new TextArea(parameterName));
		area.setWidth("100%");
		if (content != null) {
			area.setContent(content.toString());
		}
		
		return area;
	}
	
	private DropdownMenu getDropdown(String parameterName, Object selectedElement) {
		DropdownMenu drop = (DropdownMenu) getStyledInterface(new DropdownMenu(parameterName));
		drop.setWidth("100%");
		if (selectedElement != null) {
			drop.setSelectedElement(selectedElement.toString());
		}
		
		return drop;
	}
	
	private void saveChoices(IWContext iwc) {
		String[] schools = new String[3];
		for (int i = 0; i < schools.length; i++) {
			schools[i] = iwc.getParameter(PARAMETER_SCHOOLS + "_" + (i+1));
		}
		String[] instruments = iwc.getParameterValues(PARAMETER_INSTRUMENTS);
		
		String season = iwc.getParameter(PARAMETER_SEASON);
		String department = iwc.getParameter(PARAMETER_DEPARTMENT);
		String lessonType = iwc.getParameter(PARAMETER_LESSON_TYPE);
		String teacherRequest = iwc.getParameter(PARAMETER_TEACHER_REQUEST);
		String message = iwc.getParameter(PARAMETER_MESSAGE);
		String previousStudies = iwc.getParameter(PARAMETER_PREVIOUS_STUDIES);
		String elementarySchool = iwc.getParameter(PARAMETER_ELEMENTARY_SCHOOL);
		String homePhone = iwc.getParameter(PARAMETER_HOME_PHONE);
		String mobilePhone = iwc.getParameter(PARAMETER_MOBILE_PHONE);
		String email = iwc.getParameter(PARAMETER_EMAIL);
		
		String currentYear = null;
		if (iwc.isParameterSet(PARAMETER_CURRENT_YEAR)) {
			currentYear = iwc.getParameter(PARAMETER_CURRENT_YEAR);
		}
		String currentInstrument = null;
		if (iwc.isParameterSet(PARAMETER_CURRENT_INSTRUMENT)) {
			currentInstrument = iwc.getParameter(PARAMETER_CURRENT_INSTRUMENT);
		}
		int paymentMethod = -1;
		if (iwc.isParameterSet(PARAMETER_PAYMENT_METHOD)) {
			paymentMethod = Integer.parseInt(iwc.getParameter(PARAMETER_PAYMENT_METHOD));
		}
		
		try {
			boolean success = getBusiness().saveChoices(iwc.getCurrentUser(), getSession().getChild(), schools, season, department, lessonType, instruments, teacherRequest, message, currentYear, currentInstrument, previousStudies, elementarySchool, paymentMethod);
			
			if (homePhone != null) {
				getUserBusiness().updateUserHomePhone(getSession().getChild(), homePhone);
			}
			if (mobilePhone != null) {
				getUserBusiness().updateUserMobilePhone(getSession().getChild(), mobilePhone);
			}
			if (email != null) {
				try {
					getUserBusiness().updateUserMail(getSession().getChild(), email);
				}
				catch (CreateException ce) {
					log(ce);
				}
			}
			
			if (success) {
				if (getResponsePage() != null) {
					iwc.forwardToIBPage(getParentPage(), getResponsePage());
				}
				else {
					Table table = new Table();
					table.add(getErrorText(localize("submit_success", "Submit success...")), 1, 1);
					table.add(new UserHomeLink(), 1, 2);
				}
			}
			else {
				Table table = new Table();
				table.add(getErrorText(localize("submit_failed", "Submit failed...")), 1, 1);
				table.add(new UserHomeLink(), 1, 2);
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (IDOCreateException ice) {
			add(new ExceptionWrapper(ice));
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_FORM;
		}
	}

	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) {
		try {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(STYLENAME_HEADER_CELL, "");
		map.put(STYLENAME_HEADING_CELL, "");
		map.put(STYLENAME_TEXT_CELL, "");
		map.put(STYLENAME_INPUT_CELL, "");
		map.put(STYLENAME_INFORMATION_CELL, "");
		
		return map;
	}
}