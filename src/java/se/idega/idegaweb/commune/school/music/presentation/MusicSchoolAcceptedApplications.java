/*
 * $Id: MusicSchoolAcceptedApplications.java,v 1.9 2005/03/30 14:00:47 laddi Exp $
 * Created on 18.3.2005
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
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolGroupWriter;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;
import se.idega.util.SchoolClassMemberComparatorForSweden;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolStudyPath;
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
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;


public class MusicSchoolAcceptedApplications extends MusicSchoolBlock {

	private static final String PARAMETER_ACTION ="prm_action";
	
	private static final String PARAMETER_STUDENT ="prm_student";
	private static final String PARAMETER_DEPARTMENT ="prm_department";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_FORM = 2;
	private static final int ACTION_TRANSFER = 3;

	public void init(IWContext iwc) throws Exception {
		if (getSession().getProvider() != null) {
			switch (parseAction(iwc)) {
				case ACTION_VIEW:
					getGroupsTable(iwc);
					break;

				case ACTION_FORM:
					getTransferTable(iwc);
					break;

				case ACTION_TRANSFER:
					transferStudents(iwc);
					getGroupsTable(iwc);
					break;
			}
		}
		else {
			add(getErrorText(localize("no_school_found", "No school found for user")));
		}
	}

	private void getGroupsTable(IWContext iwc) throws RemoteException {
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
		
		add(form);

		if (getSession().getSeason() != null) {
			table.setCellpaddingRight(1, row, 6);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(getXLSLink(), 1, row++);
			table.setHeight(row++, 3);

			Table groupTable = new Table();
			groupTable.setColumns(9);
			groupTable.setWidth(Table.HUNDRED_PERCENT);
			groupTable.setCellpadding(0);
			groupTable.setCellspacing(0);
			table.add(groupTable, 1, row++);
			int iColumn = 1;
			int iRow = 1;
			
			boolean hasNextSeason = getBusiness().hasNextSeason(getSession().getSeason());
			
			groupTable.add(getSmallHeader(localize("nr", "Nr.")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("name", "Name")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("date_of_birth", "Date of birth")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("age", "Age")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("postal_code", "Postal code")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("instruments.plural_or_singular", "Instrument/s")), iColumn++, iRow);
			groupTable.add(getSmallHeader(localize("department", "Department")), iColumn++, iRow);
			groupTable.setCellpaddingLeft(1, iRow, 12);
			groupTable.setRowStyleClass(iRow++, getHeaderRow2Class());
			
			try {
				List students = new ArrayList(getSchoolBusiness().getSchoolClassMemberHome().findBySchoolAndSeasonAndYearAndStudyPath(getSession().getProvider(), getSession().getSeason(), getSession().getDepartment(), getSession().getInstrument()));
				Map studentMap = getCareBusiness().getStudentList(students);
				Collections.sort(students, SchoolClassMemberComparatorForSweden.getComparatorSortBy(SchoolClassMemberComparatorForSweden.NAME_SORT, iwc.getCurrentLocale(), getUserBusiness(), studentMap));
				SchoolClassMember student;
				SchoolYear department;
				Collection instruments;
				User user;
				Address address;
				PostalCode code;
				CheckBox box;
				Link userLink;
				
				int count = 1;
				Iterator iter = students.iterator();
				while (iter.hasNext()) {
					iColumn = 1;
					student = (SchoolClassMember) iter.next();
					user = (User) studentMap.get(new Integer(student.getClassMemberId()));
					address = getUserBusiness().getUsersMainAddress(user);
					if (address != null) {
						code = address.getPostalCode();
					}
					else {
						code = null;
					}
					Age age = new Age(user.getDateOfBirth());
					IWTimestamp dateOfBirth = new IWTimestamp(user.getDateOfBirth());
					try {
						instruments = student.getStudyPaths();
					}
					catch (IDORelationshipException ire) {
						log(ire);
						instruments = null;
					}
					department = student.getSchoolYear();

					userLink = getSmallLink(user.getName());
					userLink.setEventListener(MusicSchoolEventListener.class);
					userLink.addParameter(getSession().getParameterNameChildID(), user.getPrimaryKey().toString());
					userLink.addParameter(getSession().getParameterNameStudentID(), student.getPrimaryKey().toString());
					if (getResponsePage() != null) {
						userLink.setPage(getResponsePage());
					}

					groupTable.add(getSmallText(String.valueOf(count)), iColumn++, iRow);
					if (getResponsePage() != null) {
						groupTable.add(userLink, iColumn++, iRow);
					}
					else {
						groupTable.add(getSmallText(user.getName()), iColumn++, iRow);
					}
					groupTable.add(getSmallText(dateOfBirth.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
					groupTable.add(getSmallText(String.valueOf(age.getYears())), iColumn++, iRow);
					if (address != null) {
						if (code != null) {
							groupTable.add(getSmallText(code.getPostalCode()), iColumn++, iRow);
						}
						else {
							groupTable.add(getSmallText("-"), iColumn++, iRow);
						}
					}
					else {
						groupTable.add(getSmallText("-"), iColumn++, iRow);
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
					groupTable.add(instrumentText, iColumn++, iRow);
					if (department != null) {
						groupTable.add(getSmallText(localize(department.getLocalizedKey(), department.getSchoolYearName())), iColumn, iRow);
					}
					iColumn++;

					box = getCheckBox(PARAMETER_STUDENT, student.getPrimaryKey().toString());
					if (!hasNextSeason) {
						box.setDisabled(true);
					}
					groupTable.setWidth(iColumn, iRow, 12);
					if (!student.getNeedsSpecialAttention()) {
						groupTable.add(box, iColumn, iRow);
					}

					groupTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						groupTable.setRowStyleClass(iRow++, getLightRowClass());
					}
					else {
						groupTable.setRowStyleClass(iRow++, getDarkRowClass());
					}
					
					count++;
				}
				
				table.setHeight(row++, 12);
				table.setCellpaddingRight(1, row, 12);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

				if (hasNextSeason) {
					SubmitButton acceptNextYear = (SubmitButton) getButton(new SubmitButton(localize("accept_for_next_year", "Accept for next year"), PARAMETER_ACTION, String.valueOf(ACTION_FORM)));
					table.add(acceptNextYear, 1, row);
				}
				table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
				table.add(getHelpButton("help_music_school_accepted_applications"), 1, row);
			}
			catch (FinderException fe) {
				table.add(getErrorText(localize("no_students_found", "No students found...")), 1, row);
			}
		}
		else {
			table.add(getErrorText(localize("must_select_season", "You must select a season...")), 1, row);
		}
	}
	
	private void getTransferTable(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(MusicSchoolEventListener.class);
		
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		Collection departments = null;
		try {
			departments = getBusiness().findDepartmentsInSchool(getSession().getProvider());
		}
		catch (FinderException ndfe) {
			add(getErrorText(localize("no_departments_found", "No departments found...")));
			return;
		}
		
		table.setCellpaddingLeft(1, row, 12);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADING_CELL));
		table.add(getHeader(localize("students", "Students")), 1, row++);
		
		Table departmentTable = new Table();
		departmentTable.setCellpadding(getCellpadding());
		departmentTable.setCellspacing(getCellspacing());
		departmentTable.setColumns(2);
		table.setCellpaddingLeft(1, row, 12);
		table.add(departmentTable, 1, row++);
		int iRow = 1;

		String[] students = iwc.getParameterValues(PARAMETER_STUDENT);
		for (int i = 0; i < students.length; i++) {
			try {
				SchoolClassMember member = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(students[i]);
				User user = member.getStudent();

				DropdownMenu departmentDrop = getDropdown(PARAMETER_DEPARTMENT, new Integer(member.getSchoolYearId()));
				Iterator iter = departments.iterator();
				while (iter.hasNext()) {
					SchoolYear year = (SchoolYear) iter.next();
					departmentDrop.addMenuElement(year.getPrimaryKey().toString(), localize(year.getSchoolYearName(), year.getSchoolYearName()));
				}

				departmentTable.add(getText(user.getName()), 1, iRow);
				departmentTable.add(new HiddenInput(PARAMETER_STUDENT, member.getPrimaryKey().toString()), 1, iRow);
				departmentTable.add(departmentDrop, 2, iRow++);
			}
			catch (FinderException fe) {
				log(fe);
			}
		}
		
		table.setHeight(row++, 12);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("transfer", "Transfer"), PARAMETER_ACTION, String.valueOf(ACTION_TRANSFER)));
		submit.setSubmitConfirm(localize("confirm_transfer", "Are you sure you want to transfer the students to next year?"));

		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(submit, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_music_school_edit_application"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		form.setToDisableOnSubmit(submit, true);

		add(form);
	}
	
	private Link getXLSLink() {
		Window window = new Window(localize("Group", "School group"), getIWApplicationContext().getIWMainApplication().getMediaServletURI());
		window.setResizable(true);
		window.setMenubar(true);
		window.setHeight(400);
		window.setWidth(500);
		
		Image image = getBundle().getImage("shared/xls.gif");
		image.setToolTip(localize("excel_list", "Get list in Excel format"));

		Link link = new Link(image);
		link.setWindow(window);
		link.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(MusicSchoolGroupWriter.class));
		return link;
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
	
	private void transferStudents(IWContext iwc) {
		String[] students = iwc.getParameterValues(PARAMETER_STUDENT);
		String[] departments = iwc.getParameterValues(PARAMETER_DEPARTMENT);
		try {
			getBusiness().transferToNextSchoolSeason(students, departments, getSession().getProvider(), getSession().getSeason(), iwc.getCurrentUser());
			if (getParentPage() != null) {
				getParentPage().setAlertOnLoad(localize("selected_students_transferred", "The selected students have been transferred to the next season."));
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			log(fe);
		}
	}
}