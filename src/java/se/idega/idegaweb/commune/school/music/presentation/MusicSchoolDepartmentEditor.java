/*
 * Created on 22.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.music.business.NoDepartmentFoundException;

import com.idega.block.school.data.SchoolYear;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class MusicSchoolDepartmentEditor extends MusicSchoolBlock {
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_DEPARTMENT = "prm_department_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DESCRIPTION = "prm_description";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	private static final String PARAMETER_ORDER = "prm_order";
	private static final String PARAMETER_IS_SELECTABLE = "prm_selectable";
	
	private Object iDepartmentPK;
	private boolean editDepartment = false;
	private boolean newDepartment = false;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW :
				showDepartments();
				break;
			case ACTION_EDIT :
				editDepartment = true;
				showDepartments();
				break;
			case ACTION_NEW :
				newDepartment = true;
				showDepartments();
				break;
			case ACTION_SAVE :
				saveDepartment(iwc);
				showDepartments();
				break;
		}
	}
	
	private void showDepartments() {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setColumns(6);
		table.setWidth(6, 12);
		form.add(table);
		int column = 1;
		int row = 1;
		
		table.add(getSmallHeader(localize("department.department", "Department")), column++, row);
		table.add(getSmallHeader(localize("department.description", "Description")), column++, row);
		table.add(getSmallHeader(localize("department.localized_key", "Localized key")), column++, row);
		table.add(getSmallHeader(localize("department.order", "Order")), column++, row);
		table.add(getSmallHeader(localize("department.selectable", "Selectable")), column++, row);
		table.setCellpaddingLeft(1, row, 12);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		try {
			Collection instruments = getDepartments();
			Iterator iter = instruments.iterator();
			Link edit;
			while (iter.hasNext()) {
				column = 1;
				SchoolYear department = (SchoolYear) iter.next();
				
				if (editDepartment && department.getPrimaryKey().toString().equals(iDepartmentPK.toString())) {
					TextInput code = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME, department.getSchoolYearName()));
					TextInput description = (TextInput) getStyledInterface(new TextInput(PARAMETER_DESCRIPTION, department.getSchoolYearInfo()));
					TextInput localizedKey = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOCALIZED_KEY, department.getLocalizedKey()));
					TextInput order = (TextInput) getStyledInterface(new TextInput(PARAMETER_ORDER, String.valueOf(department.getSchoolYearAge())));
					order.setAsIntegers(localize("department.only_integers_allowed", "Order can only be integers"));
					order.setSize(2);
					CheckBox selectable = getCheckBox(PARAMETER_IS_SELECTABLE, Boolean.TRUE.toString());
					selectable.setChecked(department.isSelectable());
					HiddenInput pk = new HiddenInput(PARAMETER_DEPARTMENT, department.getPrimaryKey().toString());
					
					table.add(code, column++, row);
					table.add(description, column++, row);
					table.add(localizedKey, column++, row);
					table.add(selectable, column++, row);
					table.add(pk, column++, row);
				}
				else {
					edit = new Link(getEditIcon(localize("department.edit_department", "Edit department")));
					edit.addParameter(PARAMETER_DEPARTMENT, department.getPrimaryKey().toString());
					edit.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
					
					table.add(getSmallText(department.getSchoolYearName()), column++, row);
					table.add(getSmallText(department.getSchoolYearInfo()), column++, row);
					table.add(getSmallText(department.getLocalizedKey()), column++, row);
					table.add(getSmallText(String.valueOf(department.getSchoolYearAge())), column++, row);
					table.add(getSmallText(localize("department." + String.valueOf(department.isSelectable()), String.valueOf(department.isSelectable()))), column++, row);
					table.add(edit, column++, row);
				}
				
				table.setCellpaddingLeft(1, row, 12);
				if (row % 2 == 0) {
					table.setRowStyleClass(row++, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row++, getLightRowClass());
				}
			}
			
			if (newDepartment) {
				column = 1;
				TextInput code = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
				TextInput description = (TextInput) getStyledInterface(new TextInput(PARAMETER_DESCRIPTION));
				TextInput localizedKey = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOCALIZED_KEY));
				CheckBox selectable = getCheckBox(PARAMETER_IS_SELECTABLE, Boolean.TRUE.toString());
				
				table.add(code, column++, row);
				table.add(description, column++, row);
				table.add(localizedKey, column++, row);
				table.add(selectable, column++, row);
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
			if (newDepartment || editDepartment) {
				SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("department.save_department", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
				table.setCellpaddingLeft(1, row, 12);
				table.add(save, 1, row);
			}
			else {
				SubmitButton newDepartment = (SubmitButton) getButton(new SubmitButton(localize("department.new_department", "New"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
				table.setCellpaddingLeft(1, row, 12);
				table.add(newDepartment, 1, row);
			}
		}
		catch (NoDepartmentFoundException nife) {
			//No departments in database...
		}
		
		add(form);
	}
	
	private void saveDepartment(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_NAME);
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		String localizedKey = iwc.getParameter(PARAMETER_LOCALIZED_KEY);
		int order = -1;
		if (iwc.isParameterSet(PARAMETER_ORDER)) {
			order = Integer.parseInt(iwc.getParameter(PARAMETER_ORDER));
		}
		boolean isSelectable = iwc.isParameterSet(PARAMETER_IS_SELECTABLE);
		
		try {
			getBusiness().saveDepartment(iDepartmentPK, name, description, localizedKey, order, isSelectable);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			log(fe);
			add(getErrorText(localize("department.could_not_find_department", "The department was not found...")));
			add(new Break(2));
		}
		catch (CreateException ce) {
			log(ce);
			add(getErrorText(localize("department.could_not_create_department", "The department could not be created...")));
			add(new Break(2));
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_DEPARTMENT)) {
			iDepartmentPK = iwc.getParameter(PARAMETER_DEPARTMENT);
		}

		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
}