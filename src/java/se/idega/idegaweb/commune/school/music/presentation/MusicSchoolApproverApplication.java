/*
 * Created on 16.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;

import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


/**
 * @author laddi
 */
public class MusicSchoolApproverApplication extends MusicSchoolBlock {
	
	private static final String PARAMETER_REJECT = "prm_reject";

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().getApplication() != null && !parseAction(iwc)) {
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setColumns(2);
			int row = 1;
			
			MusicSchoolChoice application = getSession().getApplication();
			User user = application.getChild();
			Address address = getUserBusiness().getUsersMainAddress(user);
			PostalCode postal = null;
			if (address != null) {
				postal = address.getPostalCode();
			}
			Phone phone = null;
			try {
				phone = getUserBusiness().getUsersHomePhone(user);
			}
			catch (NoPhoneFoundException npfe) {
				phone = null;
			}
			Phone mobile = null;
			try {
				mobile = getUserBusiness().getUsersMobilePhone(user);
			}
			catch (NoPhoneFoundException npfe) {
				mobile = null;
			}
			Email email = null;
			try {
				email = getUserBusiness().getUsersMainEmail(user);
			}
			catch (NoEmailFoundException nefe) {
				email = null;
			}
			
			table.mergeCells(1, row, 2, row);
			table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
			table.add(getHeader(localize("application.applicant", "Applicant")), 1, row++);
			
			table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
			table.add(getSmallHeader(localize("name", "Name")), 1, row);
			table.add(getText(user.getName()), 2, row++);
			
			table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
			table.add(getSmallHeader(localize("personal_id", "Personal ID")), 1, row);
			table.add(getText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 2, row++);
			
			if (address != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("address", "Address")), 1, row);
				table.add(getText(address.getStreetAddress()), 2, row++);
			}
			
			if (postal != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("zip_code", "Postal code")), 1, row);
				table.add(getText(postal.getPostalAddress()), 2, row++);
			}
			
			if (phone != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("home_phone", "Home phone")), 1, row);
				table.add(getText(phone.getNumber()), 2, row++);
			}
			
			if (mobile != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("mobile_phone", "Mobile phone")), 1, row);
				table.add(getText(mobile.getNumber()), 2, row++);
			}
			
			if (email != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("email", "E-mail")), 1, row);
				table.add(getText(email.getEmailAddress()), 2, row++);
			}
			
			table.setHeight(row++, 18);
			
			//Application choices...
			SchoolYear department = application.getSchoolYear();
			SchoolType lessonType = application.getSchoolType();
			Collection instruments = application.getStudyPaths();
			SchoolYear currentYear = application.getPreviousYear();
			SchoolStudyPath currentInstrument = application.getPreviousStudyPath();
			
			table.mergeCells(1, row, 2, row);
			table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
			table.add(getHeader(localize("application.application_for", "Application for")), 1, row++);
			
			table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
			table.add(getSmallHeader(localize("department", "Department")), 1, row);
			table.add(getText(localize(department.getSchoolYearName(), department.getSchoolYearName())), 2, row++);
			
			table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
			table.add(getSmallHeader(localize("instruments", "Instruments")), 1, row);
			Iterator iter = instruments.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				table.setStyleClass(2, row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getText(localize(instrument.getLocalizedKey(), instrument.getDescription())), 2, row++);
			}
			
			table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
			table.add(getSmallHeader(localize("lesson_type", "Lesson type")), 1, row);
			table.add(getText(localize(lessonType.getLocalizationKey(), lessonType.getSchoolTypeName())), 2, row++);
			
			if (application.getTeacherRequest() != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("teacher_request", "Teacher request")), 1, row);
				table.add(getText(application.getTeacherRequest()), 2, row++);
			}
			
			if (application.getMessage() != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				table.add(getSmallHeader(localize("message", "Message")), 1, row);
				table.add(getText(application.getMessage()), 2, row++);
			}
			
			table.setHeight(row++, 18);
			
			//Application choices...
			if (currentYear != null && currentInstrument != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("current_degree", "Current degree")), 1, row);
				table.add(getText(localize(currentYear.getSchoolYearName(), currentYear.getSchoolYearName()) + " - " + localize(currentInstrument.getCode(), currentInstrument.getDescription())), 2, row++);
			}
			
			if (application.getPreviousStudies() != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				table.add(getSmallHeader(localize("previous_studies", "Previous studies")), 1, row);
				table.add(getText(application.getPreviousStudies()), 2, row++);
			}

			if (application.getElementarySchool() != null) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
				table.add(getSmallHeader(localize("elementary_school", "Elementary school")), 1, row);
				table.add(getText(application.getElementarySchool()), 2, row++);
			}
			
			table.setHeight(row++, 18);
			table.mergeCells(1, row, 2, row);
			table.setRowStyleClass(row, getStyleName(STYLENAME_TEXT_CELL));
			
			if (getResponsePage() != null) {
				GenericButton button = getButton(new GenericButton("back", localize("back", "Back")));
				button.setPageToOpen(getResponsePage());
				table.add(button, 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			}
			
			if (application.getCaseStatus().equals(getBusiness().getCaseStatusPreliminary())) {
				GenericButton button = getButton(new GenericButton("reject", localize("reject", "Reject")));
				button.setPageToOpen(iwc.getCurrentIBPageID());
				button.addParameterToPage(PARAMETER_REJECT, application.getPrimaryKey().toString());
				table.add(button, 1, row);
			}
			
			add(table);
		}
		else {
			add(getErrorText(localize("no_application_found", "No application found...")));
		}
	}
	
	private boolean parseAction(IWContext iwc) {
		boolean success = false;
		if (iwc.isParameterSet(PARAMETER_REJECT)) {
			try {
				success = getBusiness().rejectApplication(getSession().getApplicationPK(), iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				success = false;
			}
			
			if (success) {
				if (getResponsePage() != null) {
					iwc.forwardToIBPage(getParentPage(), getResponsePage());
				}
				add(getHeader(localize("application_rejected", "Application rejected..")));
			}
			else {
				add(getErrorText(localize("application_rejection_failed", "Application rejection failed...")));
			}
		}
		return success;
	}
}