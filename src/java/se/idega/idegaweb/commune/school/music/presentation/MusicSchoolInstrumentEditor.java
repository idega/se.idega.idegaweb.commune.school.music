/*
 * Created on 22.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.music.business.NoInstrumentFoundException;

import com.idega.block.school.data.SchoolStudyPath;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class MusicSchoolInstrumentEditor extends MusicSchoolBlock {
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_DELETE = 3;
	private static final int ACTION_NEW = 4;
	private static final int ACTION_SAVE = 5;

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_INSTRUMENT = "prm_instrument_id";
	private static final String PARAMETER_CODE = "prm_code";
	private static final String PARAMETER_DESCRIPTION = "prm_description";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	
	private Object iInstrumentPK;
	private boolean editInstrument = false;
	private boolean newInstrument = false;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW :
				showInstruments();
				break;
			case ACTION_EDIT :
				editInstrument = true;
				showInstruments();
				break;
			case ACTION_NEW :
				newInstrument = true;
				showInstruments();
				break;
			case ACTION_DELETE :
				deleteInstrument();
				showInstruments();
				break;
			case ACTION_SAVE :
				saveInstrument(iwc);
				showInstruments();
				break;
		}
	}
	
	private void showInstruments() {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setColumns(5);
		form.add(table);
		int column = 1;
		int row = 1;
		
		table.add(getSmallHeader(localize("instrument.code", "Code")), column++, row);
		table.add(getSmallHeader(localize("instrument.description", "Description")), column++, row);
		table.add(getSmallHeader(localize("instrument.localized_key", "Localized key")), column++, row);
		table.setCellpaddingLeft(1, row, 12);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		try {
			Collection instruments = getInstruments();
			Iterator iter = instruments.iterator();
			Link edit;
			Link delete;
			while (iter.hasNext()) {
				column = 1;
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				
				if (editInstrument && instrument.getPrimaryKey().equals(iInstrumentPK)) {
					TextInput code = (TextInput) getStyledInterface(new TextInput(PARAMETER_CODE, instrument.getCode()));
					TextInput description = (TextInput) getStyledInterface(new TextInput(PARAMETER_DESCRIPTION, instrument.getDescription()));
					TextInput localizedKey = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOCALIZED_KEY, instrument.getLocalizedKey()));
					
					table.add(code, column++, row);
					table.add(description, column++, row);
					table.add(localizedKey, column++, row);
				}
				else {
					edit = new Link(getEditIcon(localize("instrument.edit_instrument", "Edit instrument")));
					edit.addParameter(PARAMETER_INSTRUMENT, instrument.getPrimaryKey().toString());
					edit.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
					
					delete = new Link(getDeleteIcon(localize("instrument.delete_instrument", "Delete instrument")));
					delete.addParameter(PARAMETER_INSTRUMENT, instrument.getPrimaryKey().toString());
					delete.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_DELETE));
					
					table.add(getSmallText(instrument.getCode()), column++, row);
					table.add(getSmallText(instrument.getDescription()), column++, row);
					table.add(getSmallText(instrument.getLocalizedKey()), column++, row);
					table.add(edit, column++, row);
					table.add(delete, column++, row);
				}
				
				table.setCellpaddingLeft(1, row, 12);
				if (row % 2 == 0) {
					table.setRowStyleClass(row++, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row++, getLightRowClass());
				}
			}
			
			if (newInstrument) {
				column = 1;
				TextInput code = (TextInput) getStyledInterface(new TextInput(PARAMETER_CODE));
				TextInput description = (TextInput) getStyledInterface(new TextInput(PARAMETER_DESCRIPTION));
				TextInput localizedKey = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOCALIZED_KEY));
				
				table.add(code, column++, row);
				table.add(description, column++, row);
				table.add(localizedKey, column++, row);
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
			if (newInstrument || editInstrument) {
				SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("instrument.save_instrument", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
				table.setCellpaddingLeft(1, row, 12);
				table.add(save, 1, row);
			}
			else {
				SubmitButton newInstrument = (SubmitButton) getButton(new SubmitButton(localize("instrument.new_instrument", "New"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
				table.setCellpaddingLeft(1, row, 12);
				table.add(newInstrument, 1, row);
			}
		}
		catch (NoInstrumentFoundException nife) {
			//No instruments in database...
		}
		
		add(form);
	}
	
	private void deleteInstrument() {
		try {
			getBusiness().deleteInstrument(iInstrumentPK);
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	private void saveInstrument(IWContext iwc) {
		String code = iwc.getParameter(PARAMETER_CODE);
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		String localizedKey = iwc.getParameter(PARAMETER_LOCALIZED_KEY);
		
		try {
			getBusiness().saveInstrument(iInstrumentPK, code, description, localizedKey);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			log(fe);
			add(getErrorText(localize("instrument.could_not_find_instrument", "The instrument was not found...")));
			add(new Break(2));
		}
		catch (CreateException ce) {
			log(ce);
			add(getErrorText(localize("instrument.could_not_create_instrument", "The instrument could not be created...")));
			add(new Break(2));
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_INSTRUMENT)) {
			iInstrumentPK = iwc.getParameter(PARAMETER_ACTION);
		}

		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
}