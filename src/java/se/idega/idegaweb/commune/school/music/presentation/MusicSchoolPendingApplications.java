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
import com.idega.presentation.ui.Form;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


/**
 * @author laddi
 */
public class MusicSchoolPendingApplications extends MusicSchoolBlock {

	private static final String PARAMETER_REACTIVATE ="prm_reactivate";
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().getProvider() != null) {
			parseAction(iwc);
			
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
			table.add(getChoicesTable(iwc), 1, row);
	
			add(form);
		}
		else {
			add(getErrorText(localize("no_school_found", "No school found for user")));
		}
	}
	
	private Table getChoicesTable(IWContext iwc) throws RemoteException {
		Table choicesTable = new Table();
		choicesTable.setColumns(7);
		choicesTable.setWidth(Table.HUNDRED_PERCENT);
		choicesTable.setCellpadding(0);
		choicesTable.setCellspacing(0);
		int iColumn = 1;
		int iRow = 1;
		
		choicesTable.add(getSmallHeader(localize("name", "Name")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("personal_id", "Personal ID")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("address", "Address")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("postal_code", "Postal code")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("department", "Department")), iColumn++, iRow);
		choicesTable.add(getSmallHeader(localize("instruments.plural_or_singular", "Instrument/s")), iColumn++, iRow);
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
				Link userLink;
				Link reactivate;
				
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
					reactivate = new Link(getEditIcon(localize("reactivate", "Reactivate application")));
					reactivate.addParameter(PARAMETER_REACTIVATE, choice.getPrimaryKey().toString());
					
					userLink = getSmallLink(user.getName());
					userLink.setEventListener(MusicSchoolEventListener.class);
					userLink.addParameter(getSession().getParameterNameChildID(), user.getPrimaryKey().toString());
					userLink.addParameter(getSession().getParameterNameApplicationID(), choice.getPrimaryKey().toString());
					if (getResponsePage() != null) {
						userLink.setPage(getResponsePage());
					}
					
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
					choicesTable.add(reactivate, iColumn, iRow);
					
					choicesTable.setCellpaddingLeft(1, iRow, 12);
					if (iRow % 2 == 0) {
						choicesTable.setRowStyleClass(iRow++, getLightRowClass());
					}
					else {
						choicesTable.setRowStyleClass(iRow++, getDarkRowClass());
					}
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
				getBusiness().reactivateApplication(iwc.getParameter(PARAMETER_REACTIVATE), iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				log(re);
			}
		}
	}
}