/*
 * Created on 2004-maj-13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.music.business.NoInstrumentFoundException;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/**
 * Title: MusicSchoolInstrumentConnector
 * Description: A class to connect music instruments to schools of type music school
 * @author <a href="mailto:malin.anulf@agurait.com">Malin Anulf</a>
 * @version 1.0
 * 
 */
public class MusicSchoolInstrumentConnector extends MusicSchoolBlock{

	private static final int ACTION_FORM = 1;
	private static final int ACTION_VERIFY = 2;
	private static final int ACTION_SAVE = 3;
	
	private Form form;
	
	private static final String PARAMETER_INSTRUMENTS = "prm_instruments";
	private static final String PARAMETER_PROVIDER = "prm_provider";
	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_PROVIDER_CHANGED = "prm_provider_changed"; 
	
	private static final String STYLENAME_HEADER_CELL = "HeaderCell";
	private static final String STYLENAME_HEADING_CELL = "HeadingCell";
	private static final String STYLENAME_TEXT_CELL = "TextCell";
	private static final String STYLENAME_INPUT_CELL = "InputCell";
	private static final String STYLENAME_INFORMATION_CELL = "InformationCell";
	
	//localized keys and strings
	private static final String INSTRUMENT_KEY = "ms_instrument";
	private static final String INSTRUMENT_DEFAULT = "Intstrument";
	private static final String SCHOOL_KEY = "ms_school";
	private static final String SCHOOL_DEFAULT = "School";
	private static final String INTSTRUMENT_CONNECTOR_INFO_KEY = "ms_instrument_connector_info";
	private static final String INTSTRUMENT_CONNECTOR_INFO_DEFAULT = "Select instruments to connect to the selected school";
	
	
	public static final String FORM_NAME = "music_school_instrument_connector_form";
	
	public void init(IWContext iwc) throws Exception {
		form = new Form();
		form.setName(FORM_NAME);
		form.setEventListener(MusicSchoolEventListener.class);
		
		
		switch (parseAction(iwc)) {
		case ACTION_FORM:
			form.add(getTable(iwc));	
			break;
		
		case ACTION_VERIFY:			
			break;

		case ACTION_SAVE:
			saveConnections(iwc);
			form.add(getTable(iwc));
			break;
		}
		
		add(form);
	
	}
	
	private Table getTable(IWContext iwc) throws RemoteException {
		
		
		Table connectionTable = new Table();
		
		connectionTable.setStyleAttribute("border:1px solid #000000;");
		connectionTable.setColor("#FFFFFF");
		connectionTable.setCellpadding(3);
		int iRow = 1;
		int iColumn = 1;
		
		connectionTable.add(getHeader(localize(SCHOOL_KEY, SCHOOL_DEFAULT)), 1, iRow);
		connectionTable.mergeCells(1, iRow, 4, iRow);
		
		
		
		connectionTable.mergeCells(1,iRow, 4, iRow++);
		connectionTable.mergeCells(1,iRow, 4, iRow);
		connectionTable.add(getProvidersDropdown(iwc), 1, iRow++);
		connectionTable.add(new HiddenInput(PARAMETER_PROVIDER_CHANGED, "-1"), 1, iRow++);
		connectionTable.add(getHeader(localize(INSTRUMENT_KEY, INSTRUMENT_DEFAULT)), 1, iRow);
		connectionTable.mergeCells(1, iRow++, 4, iRow++);
		connectionTable.add(getSmallText(localize(INTSTRUMENT_CONNECTOR_INFO_KEY, INTSTRUMENT_CONNECTOR_INFO_DEFAULT)), 1, iRow);
		connectionTable.mergeCells(1, iRow++, 4, iRow++);
		
		Collection instruments = getInstrumentColl();
		String school = "";
		School selectedSchool = null;
		if (iwc.isParameterSet(PARAMETER_PROVIDER_CHANGED)) {
			school = iwc.getParameter(PARAMETER_PROVIDER);
			try {
				selectedSchool = getSchoolBusiness().getSchoolHome().findByPrimaryKey(school);
				
			}
			catch (FinderException fe) {
				log(fe);
			}
			
		}
		
		if (instruments != null) {
			Iterator iter = instruments.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				if (iColumn > 4) {
					iRow++;
					iColumn = 1;
				}
				
				CheckBox box = getCheckBox(PARAMETER_INSTRUMENTS + instrument.getPrimaryKey().toString(), instrument.getPrimaryKey().toString());
				
				if (selectedSchool != null) {
					try {
						box.setChecked(selectedSchool.getStudyPaths().contains(instrument));
					}
					catch (IDORelationshipException re) {
						log (re);
					}
				}
				
				connectionTable.setStyleClass(iColumn, iRow, getStyleName(STYLENAME_INPUT_CELL));
				connectionTable.add(box, iColumn, iRow);
				connectionTable.add(getSmallText(Text.NON_BREAKING_SPACE), iColumn, iRow);
				connectionTable.add(getSmallText(localize(instrument.getCode(), instrument.getDescription())), iColumn++, iRow);
			}
			
		}
				
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		++iRow;
		connectionTable.setHeight(iRow, 40);
		connectionTable.setVerticalAlignment(2, iRow, Table.VERTICAL_ALIGN_BOTTOM);
		connectionTable.setAlignment(2, iRow, Table.HORIZONTAL_ALIGN_RIGHT);
		connectionTable.add(submit, 4, iRow++);
		return connectionTable;
		
	}
	
	/* Removes the instruments and calls the method to add the all instruments
	 * 
	 * */
	
	private void saveConnections(IWContext iwc) {
		
		String school = "";
		School selectedSchool = null;
		if (iwc.isParameterSet(PARAMETER_PROVIDER_CHANGED)) {
			school = iwc.getParameter(PARAMETER_PROVIDER);
			try {
				selectedSchool = getSchoolBusiness().getSchoolHome().findByPrimaryKey(school);
				
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (RemoteException re) {
				log(re);
			}
			//remove study path so that selected ones can be added
			try {
			selectedSchool.removeAllStudyPaths();
			}
			catch (IDORemoveRelationshipException removeRelEx){
				log(removeRelEx);
			}
					
			saveSchoolInstruments(iwc, selectedSchool);
					
		}
		
	}
	
	
	/*
	 * Save instruments that were selected
	 * 
	 */
	
	
	private void saveSchoolInstruments(IWContext iwc, School selectedSchool) {
		Iterator iter = getInstrumentColl().iterator();
		while(iter.hasNext()) {
			SchoolStudyPath ssPath = (SchoolStudyPath) iter.next();
			String sspId = ssPath.getPrimaryKey().toString();			
			if (iwc.isParameterSet(PARAMETER_INSTRUMENTS + sspId)) {		
				try {
					selectedSchool.addStudyPath(ssPath);
				}
				catch (IDOAddRelationshipException aRe) {
					log(aRe);
				}
			}
		}
	}
	
	
	/*
	 * Returns all school study paths / instruments.
	 */
		
	private Collection getInstrumentColl() {
		Collection instruments = null;
		try {
			instruments = getInstruments();
		}
		catch (NoInstrumentFoundException nife) {
			log(nife);
			add(getErrorText(localize("no_instruments_found", "No instruments found...")));
			return instruments;
		}
		return instruments;
	}

	/*
	 * Returns providers in a dropdown.
	 */
	
	private DropdownMenu getProvidersDropdown(IWContext iwc) {
		// Get dropdown for providers
		DropdownMenu providers = (DropdownMenu) getStyledInterface(
				new DropdownMenu(PARAMETER_PROVIDER));
		providers.setValueOnChange(PARAMETER_PROVIDER_CHANGED, "1");
		providers.setToSubmit(true);
		providers.addMenuElement("-1", localize("school", "- School -"));
		try {
			Collection schools = getSchoolBusiness().getSchoolHome().findAllByCategory(getSchoolBusiness().getCategoryMusicSchool());
				// Fill dropdown with schools
				for (Iterator iter = schools.iterator(); iter.hasNext();) {
					School tmpSchool = (School) iter.next();
					int schoolID = ((Integer) tmpSchool.getPrimaryKey()).intValue();
					providers.addMenuElement(schoolID, tmpSchool.getSchoolName());
				}
				 if (iwc.isParameterSet(PARAMETER_PROVIDER)) {
					providers.setSelectedElement(iwc.getParameter(PARAMETER_PROVIDER));					
				}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return providers;
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_FORM;
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
