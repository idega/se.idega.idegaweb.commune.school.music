/*
 * $Id: MusicSchoolAcceptedApplications.java,v 1.1 2005/03/19 16:37:29 laddi Exp $
 * Created on 18.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


public class MusicSchoolAcceptedApplications extends MusicSchoolBlock {

	private static final String PARAMETER_ACTION ="prm_action";
	private static final String PARAMETER_STUDENT ="prm_student";
	
	private static final int ACTION_ACCEPT = 1;

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
			
			parseAction(iwc);
			getGroupsTable(iwc, table, row);
	
			add(form);
		}
		else {
			add(getErrorText(localize("no_school_found", "No school found for user")));
		}
	}

	private void getGroupsTable(IWContext iwc, Table table, int row) throws RemoteException {
		if (getSession().getSeason() != null) {
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
				Collection students = getSchoolBusiness().getSchoolClassMemberHome().findBySchoolAndSeasonAndYearAndStudyPath(getSession().getProvider(), getSession().getSeason(), getSession().getDepartment(), getSession().getInstrument());
				SchoolClassMember student;
				User user;
				Address address;
				PostalCode code;
				CheckBox box;
				
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

					box = getCheckBox(PARAMETER_STUDENT, student.getPrimaryKey().toString());
					groupTable.setWidth(iColumn, iRow, 12);
					groupTable.add(box, iColumn, iRow);

					groupTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						groupTable.setRowStyleClass(iRow++, getLightRowClass());
					}
					else {
						groupTable.setRowStyleClass(iRow++, getDarkRowClass());
					}
				}
				
				table.setHeight(row++, 12);
				table.setCellpaddingRight(1, row, 12);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

				if (getBusiness().hasNextSeason(getSession().getSeason())) {
					SubmitButton acceptNextYear = (SubmitButton) getButton(new SubmitButton(localize("accept_for_next_year", "Accept for next year"), PARAMETER_ACTION, String.valueOf(ACTION_ACCEPT)));
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
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			String[] students = iwc.getParameterValues(PARAMETER_STUDENT);
			try {
				getBusiness().transferToNextSchoolSeason(students, getSession().getProvider(), getSession().getSeason(), iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
			catch (FinderException fe) {
				log(fe);
			}
		}
	}
}