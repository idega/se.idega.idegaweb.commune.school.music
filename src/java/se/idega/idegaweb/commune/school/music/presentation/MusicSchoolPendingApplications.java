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
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
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
import com.idega.util.Age;
import com.idega.util.IWTimestamp;


/**
 * @author laddi
 */
public class MusicSchoolPendingApplications extends MusicSchoolBlock {

	private static final String PARAMETER_REACTIVATE ="prm_reactivate";
	private static final String PARAMETER_APPLICATION ="prm_application";
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().getProvider() != null) {
			parseAction(iwc);
			
			Form form = new Form();
			form.setEventListener(MusicSchoolEventListener.class);
			
			Table table = new Table();
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setCellpadding(0);
			table.setCellspacing(0);
			form.add(table);
			int row = 1;
			
			table.setCellpaddingLeft(1, row, 12);
			table.add(getNavigationTable(), 1, row++);
			table.setHeight(row++, 12);
			table.add(getChoicesTable(iwc), 1, row);
			table.setHeight(row++, 12);

			SubmitButton reactivate = (SubmitButton) getButton(new SubmitButton(localize("reactivate", "Reactivate"), PARAMETER_REACTIVATE, "true"));
			
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(reactivate, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			table.add(getHelpButton("Help_music_school_pending"), 1, row);
			table.setCellpaddingRight(1, row, 12);
			add(form);
		}
		else {
			add(getErrorText(localize("no_school_found", "No school found for user")));
		}
	}
	
	private Table getChoicesTable(IWContext iwc) throws RemoteException {
		Table choicesTable = new Table();
		choicesTable.setColumns(9);
		choicesTable.setWidth(Table.HUNDRED_PERCENT);
		choicesTable.setCellpadding(0);
		choicesTable.setCellspacing(0);
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
				Collection choices = getBusiness().findPendingChoicesInSchool(getSession().getProvider(), getSession().getSeason(), getSession().getDepartment(), getSession().getInstrument());
				MusicSchoolChoice choice;
				User user;
				Address address;
				PostalCode code;
				Collection instruments;
				SchoolYear department;
				IWTimestamp choiceDate;
				Link userLink;
				CheckBox box;
				int count = 1;
				
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					iColumn = 1;
					choice = (MusicSchoolChoice) iter.next();
					user = choice.getChild();
					address = getUserBusiness().getUsersMainAddress(user);
					if (address != null) {
						code = address.getPostalCode();
					}
					else {
						code = null;
					}
					try {
						instruments = choice.getStudyPaths();
					}
					catch (IDORelationshipException ire) {
						log(ire);
						instruments = null;
					}
					department = choice.getSchoolYear();
					IWTimestamp dateOfBirth = new IWTimestamp(user.getDateOfBirth());
					Age age = new Age(user.getDateOfBirth());
					box = getCheckBox(PARAMETER_APPLICATION, choice.getPrimaryKey().toString());
					if (choice.getChoiceDate() != null) {
						choiceDate = new IWTimestamp(choice.getChoiceDate());
					}
					else {
						choiceDate = null;
					}

					choicesTable.setWidth(iColumn, iRow, 12);
					choicesTable.add(box, iColumn, iRow);
					
					userLink = getSmallLink(user.getName());
					userLink.setEventListener(MusicSchoolEventListener.class);
					userLink.addParameter(getSession().getParameterNameChildID(), user.getPrimaryKey().toString());
					userLink.addParameter(getSession().getParameterNameApplicationID(), choice.getPrimaryKey().toString());
					if (getResponsePage() != null) {
						userLink.setPage(getResponsePage());
					}
					
					choicesTable.add(getSmallText(String.valueOf(count)), iColumn++, iRow);
					if (choiceDate != null) {
						choicesTable.add(getSmallText(choiceDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
					}
					else {
						choicesTable.add(getSmallText("-"), iColumn++, iRow);
					}
					if (getResponsePage() != null) {
						choicesTable.add(userLink, iColumn++, iRow);
					}
					else {
						choicesTable.add(getSmallText(user.getName()), iColumn++, iRow);
					}
					choicesTable.add(getSmallText(dateOfBirth.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), iColumn++, iRow);
					choicesTable.add(getSmallText(String.valueOf(age.getYears())), iColumn++, iRow);
					if (address != null) {
						if (code != null) {
							choicesTable.add(getSmallText(code.getPostalAddress()), iColumn++, iRow);
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
					choicesTable.add(box, iColumn, iRow);
					
					choicesTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						choicesTable.setRowStyleClass(iRow++, getLightRowClass());
					}
					else {
						choicesTable.setRowStyleClass(iRow++, getDarkRowClass());
					}
					count++;
				}
			}
			catch (FinderException fe) {
				choicesTable.setHeight(iRow++, 6);
				choicesTable.mergeCells(1, iRow, choicesTable.getColumns(), iRow);
				choicesTable.add(getErrorText(localize("no_choices_found", "No choices found...")), 1, iRow);
			}
		}
		return choicesTable;
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_REACTIVATE)) {
			try {
				String[] applications = iwc.getParameterValues(PARAMETER_APPLICATION);
				getBusiness().reactivateApplications(applications, iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				log(re);
			}
		}
	}
}