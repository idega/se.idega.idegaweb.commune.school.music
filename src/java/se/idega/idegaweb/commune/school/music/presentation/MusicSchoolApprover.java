/*
 * Created on 9.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;


/**
 * @author laddi
 */
public class MusicSchoolApprover extends MusicSchoolBlock {

	private static final String PARAMETER_ACTION ="prm_action";
	private static final String PARAMETER_REMOVE ="prm_remove";
	private static final String PARAMETER_ADD ="prm_add";
	
	private static final int ACTION_FORM = 1;
	private static final int ACTION_SAVE = 2;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().getProvider() != null) {
			Form form = new Form();
			form.setEventListener(MusicSchoolEventListener.class);
			form.setMethod("get");
			
			Table table = new Table();
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setCellpadding(0);
			table.setCellspacing(0);
			form.add(table);
			int row = 1;
			
			table.setCellpaddingLeft(1, row, 12);
			table.add(getNavigationTable(), 1, row++);
			table.setHeight(row++, 12);
			
			switch (parseAction(iwc)) {
				case ACTION_FORM:
					getChoicesTable(iwc, table, row);
					break;
	
				case ACTION_SAVE:
					getGroupsTable(iwc, table, row);
					break;
			}
	
			add(form);
		}
		else {
			add(getErrorText(localize("no_school_found", "No school found for user")));
		}
	}
	
	private void getChoicesTable(IWContext iwc, Table table, int row) throws RemoteException {
		Table choicesTable = new Table();
		if (getSession().getGroupPK() != null) {
			choicesTable.setColumns(9);
		}
		else {
			choicesTable.setColumns(8);
		}
		choicesTable.setWidth(Table.HUNDRED_PERCENT);
		choicesTable.setCellpadding(0);
		choicesTable.setCellspacing(0);
		table.add(choicesTable, 1, row++);
		int iColumn = 1;
		int iRow = 1;
		
		choicesTable.add(getSmallHeader(localize("number", "Nr.")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("name", "Name")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("personal_id", "Personal ID")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("address", "Address")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("postal_code", "Postal code")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("choice_date", "Choice date")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("department", "Department")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("instruments.plural_or_singular", "Instrument/s")), iColumn++, iRow);
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
					applicationCount++;
					
					choice = (MusicSchoolChoice) iter.next();
					user = choice.getChild();
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
					isPlaced = choice.getCaseStatus().equals(getBusiness().getCaseStatusPlaced());
					if (isPlaced) {
						hasInstrumentPlacement = getBusiness().isPlacedInSchool(user, getSession().getProvider(), getSession().getSeason(), getSession().getInstrument());
					}
					else {
						hasInstrumentPlacement = false;
					}
					if (previousSeason != null) {
						olderStudent = getBusiness().isPlacedInSchool(user, getSession().getProvider(), previousSeason, null);
					}
					else {
						olderStudent = false;
					}
					
					if (olderStudent) {
						showOlderStudentMessage = true;
						choicesTable.add(getSmallErrorText("* "), iColumn, iRow);
					}
					
					userLink = getSmallLink(user.getName());
					userLink.setEventListener(MusicSchoolEventListener.class);
					userLink.addParameter(getSession().getParameterNameChildID(), user.getPrimaryKey().toString());
					userLink.addParameter(getSession().getParameterNameApplicationID(), choice.getPrimaryKey().toString());
					if (getResponsePage() != null) {
						userLink.setPage(getResponsePage());
					}
					
					choicesTable.add(getSmallText(String.valueOf(applicationCount)), iColumn++, iRow);
					choicesTable.add(userLink, iColumn++, iRow);
					choicesTable.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), iColumn++, iRow);
					if (address != null) {
						choicesTable.add(getSmallText(address.getStreetAddress()), iColumn++, iRow);
						if (code != null) {
							choicesTable.add(getSmallText(code.getPostalAddress()), iColumn++, iRow);
						}
						else {
							choicesTable.add(getSmallText("-"), iColumn++, iRow);
						}
					}
					else {
						choicesTable.add(getSmallText("-"), iColumn++, iRow);
						choicesTable.add(getSmallText("-"), iColumn++, iRow);
					}
					
					if (choiceDate != null) {
						choicesTable.add(getSmallText(choiceDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
					}
					else {
						choicesTable.add(getSmallText("-"), iColumn++, iRow);
					}
					
					choicesTable.add(getSmallText(localize(department.getLocalizedKey(), department.getSchoolYearName())), iColumn++, iRow);
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
					
					if (getSession().getGroupPK() != null) {
						box = getCheckBox(PARAMETER_ADD, choice.getPrimaryKey().toString());
						box.setDisabled(hasInstrumentPlacement);
						choicesTable.setWidth(iColumn, iRow, 12);
						choicesTable.add(box, iColumn, iRow);
					}
					
					choicesTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						choicesTable.setRowStyleClass(iRow, getLightRowClass());
					}
					else {
						choicesTable.setRowStyleClass(iRow, getDarkRowClass());
					}
					
					if (hasInstrumentPlacement) {
						choicesTable.setRowColor(iRow, PLACED_FOR_INSTRUMENT_COLOR);
					}
					else if (isPlaced) {
						choicesTable.setRowColor(iRow, PLACED_COLOR);
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
				table.setCellpaddingLeft(1, row, 12);
				table.add(getLegendTable(), 1, row++);
				
				if (getSession().getGroup() != null) {
					table.setHeight(row++, 12);
					
					SubmitButton showGroup = (SubmitButton) getButton(new SubmitButton(localize("show_group", "Show group"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
					SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
					
					table.add(showGroup, 1, row);
					table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
					table.add(submit, 1, row);
					table.setCellpaddingLeft(1, row, 12);
				}
			}
			catch (FinderException fe) {
				table.setHeight(row++, 6);
				table.add(getErrorText(localize("no_choices_found", "No choices found...")), 1, row);
				return;
			}
		}
	}
	
	private void getGroupsTable(IWContext iwc, Table table, int row) throws RemoteException {
		if (getSession().getGroup() != null && getSession().getDepartment() != null && getSession().getInstrument() != null) {
			Table groupTable = new Table();
			groupTable.setColumns(5);
			groupTable.setWidth(Table.HUNDRED_PERCENT);
			groupTable.setCellpadding(0);
			groupTable.setCellspacing(0);
			table.add(groupTable, 1, row++);
			int iColumn = 1;
			int iRow = 1;
			
			groupTable.add(getSmallHeader(localize("name", "Name")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("personal_id", "Personal ID")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("address", "Address")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("postal_code", "Postal code")), iColumn++, iRow);
			groupTable.setCellpaddingLeft(1, iRow, 12);
			groupTable.setRowStyleClass(iRow++, getHeaderRow2Class());
			
			try {
				Collection students = getSchoolBusiness().getSchoolClassMemberHome().findBySchoolClassAndYearAndStudyPath(getSession().getGroup(), getSession().getDepartment(), getSession().getInstrument());
				SchoolClassMember student;
				User user;
				Address address;
				PostalCode code;
				Link remove;
				
				Iterator iter = students.iterator();
				while (iter.hasNext()) {
					iColumn = 1;
					student = (SchoolClassMember) iter.next();
					user = student.getStudent();
					address = getUserBusiness().getUsersMainAddress(user);
					if (address != null) {
						code = address.getPostalCode();
					}
					else {
						code = null;
					}
					remove = new Link(getDeleteIcon(localize("remove_from_group", "Remove student from group")));
					remove.addParameter(PARAMETER_REMOVE, student.getPrimaryKey().toString());
					remove.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));

					groupTable.add(getSmallText(user.getName()), iColumn++, iRow);
					groupTable.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), iColumn++, iRow);
					if (address != null) {
						groupTable.add(getSmallText(address.getStreetAddress()), iColumn++, iRow);
						if (code != null) {
							groupTable.add(getSmallText(code.getPostalAddress()), iColumn++, iRow);
						}
						else {
							groupTable.add(getSmallText("-"), iColumn++, iRow);
						}
					}
					else {
						groupTable.add(getSmallText("-"), iColumn++, iRow);
						groupTable.add(getSmallText("-"), iColumn++, iRow);
					}
					groupTable.add(remove, iColumn++, iRow);

					groupTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						groupTable.setRowStyleClass(iRow++, getLightRowClass());
					}
					else {
						groupTable.setRowStyleClass(iRow++, getDarkRowClass());
					}
				}
				
				table.setHeight(row++, 12);
				
				table.setCellpaddingLeft(1, row, 12);
				SubmitButton showChoices = (SubmitButton) getButton(new SubmitButton(localize("show_choices", "Show choices"), PARAMETER_ACTION, String.valueOf(ACTION_FORM)));
				table.add(showChoices, 1, row);
			}
			catch (FinderException fe) {
				table.add(getErrorText(localize("no_students_found", "No students found...")), 1, row);
			}
		}
		else {
			table.add(getErrorText(localize("must_select", "You must select year, department, season and group...")), 1, row);
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ADD)) {
			String[] students = iwc.getParameterValues(PARAMETER_ADD);
			try {
				getBusiness().addStudentsToGroup(students, getSession().getGroup(), getSession().getDepartment(), getSession().getInstrument(), iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_REMOVE)) {
			String student = iwc.getParameter(PARAMETER_REMOVE);
			try {
				getBusiness().removeChoiceFromGroup(student, iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_FORM;
		}
	}
}