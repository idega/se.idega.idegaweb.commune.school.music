/*
 * Created on 22.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;

import se.idega.idegaweb.commune.school.music.business.NoLessonTypeFoundException;

import com.idega.block.school.data.SchoolType;
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
public class MusicSchoolLessonTypeEditor extends MusicSchoolBlock {
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_LESSON_TYPE = "prm_lesson_type_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_DESCRIPTION = "prm_description";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	private static final String PARAMETER_ORDER = "prm_order";
	private static final String PARAMETER_IS_SELECTABLE = "prm_selectable";
	
	private Object iLessonTypePK;
	private boolean editLessonType = false;
	private boolean newLessonType = false;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW :
				showLessonTypes();
				break;
			case ACTION_EDIT :
				editLessonType = true;
				showLessonTypes();
				break;
			case ACTION_NEW :
				newLessonType = true;
				showLessonTypes();
				break;
			case ACTION_SAVE :
				saveLessonType(iwc);
				showLessonTypes();
				break;
		}
	}
	
	private void showLessonTypes() {
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
		
		table.add(getSmallHeader(localize("lesson_type.lesson_type", "Lesson type")), column++, row);
		table.add(getSmallHeader(localize("lesson_type.description", "Description")), column++, row);
		table.add(getSmallHeader(localize("lesson_type.localized_key", "Localized key")), column++, row);
		table.add(getSmallHeader(localize("lesson_type.order", "Order")), column++, row);
		table.add(getSmallHeader(localize("lesson_type.selectable", "Selectable")), column++, row);
		table.setCellpaddingLeft(1, row, 12);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		try {
			Collection instruments = getLessonTypes();
			Iterator iter = instruments.iterator();
			Link edit;
			while (iter.hasNext()) {
				column = 1;
				SchoolType lessonType = (SchoolType) iter.next();
				
				if (editLessonType && lessonType.getPrimaryKey().toString().equals(iLessonTypePK.toString())) {
					TextInput name = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME, lessonType.getSchoolTypeName()));
					TextInput description = (TextInput) getStyledInterface(new TextInput(PARAMETER_DESCRIPTION, lessonType.getSchoolTypeInfo()));
					TextInput localizedKey = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOCALIZED_KEY, lessonType.getLocalizationKey()));
					TextInput order = (TextInput) getStyledInterface(new TextInput(PARAMETER_ORDER, String.valueOf(lessonType.getOrder())));
					order.setAsIntegers(localize("lesson_type.only_integers_allowed", "Order can only be integers"));
					order.setSize(2);
					CheckBox selectable = getCheckBox(PARAMETER_IS_SELECTABLE, Boolean.TRUE.toString());
					selectable.setChecked(lessonType.isSelectable());
					HiddenInput pk = new HiddenInput(PARAMETER_LESSON_TYPE, lessonType.getPrimaryKey().toString());
					
					table.add(name, column++, row);
					table.add(description, column++, row);
					table.add(localizedKey, column++, row);
					table.add(selectable, column++, row);
					table.add(pk, column++, row);
				}
				else {
					edit = new Link(getEditIcon(localize("lesson_type.edit_lesson_type", "Edit lesson type")));
					edit.addParameter(PARAMETER_LESSON_TYPE, lessonType.getPrimaryKey().toString());
					edit.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
					
					table.add(getSmallText(lessonType.getSchoolTypeName()), column++, row);
					table.add(getSmallText(lessonType.getSchoolTypeInfo()), column++, row);
					table.add(getSmallText(lessonType.getLocalizationKey()), column++, row);
					table.add(getSmallText(String.valueOf(lessonType.getOrder())), column++, row);
					table.add(getSmallText(localize("lesson_type." + String.valueOf(lessonType.isSelectable()), String.valueOf(lessonType.isSelectable()))), column++, row);
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
			
			if (newLessonType) {
				column = 1;
				TextInput name = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
				TextInput description = (TextInput) getStyledInterface(new TextInput(PARAMETER_DESCRIPTION));
				TextInput localizedKey = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOCALIZED_KEY));
				TextInput order = (TextInput) getStyledInterface(new TextInput(PARAMETER_ORDER));
				order.setAsIntegers(localize("lesson_type.only_integers_allowed", "Order can only be integers"));
				order.setSize(2);
				CheckBox selectable = getCheckBox(PARAMETER_IS_SELECTABLE, Boolean.TRUE.toString());
				
				table.add(name, column++, row);
				table.add(description, column++, row);
				table.add(localizedKey, column++, row);
				table.add(order, column++, row);
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
			if (newLessonType || editLessonType) {
				SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("lesson_type.save_lesson_type", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
				table.setCellpaddingLeft(1, row, 12);
				table.add(save, 1, row);
			}
			else {
				SubmitButton newLessonType = (SubmitButton) getButton(new SubmitButton(localize("lesson_type.new_lesson_type", "New"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
				table.setCellpaddingLeft(1, row, 12);
				table.add(newLessonType, 1, row);
			}
		}
		catch (NoLessonTypeFoundException nltfe) {
			//No lesson types in database...
		}
		
		add(form);
	}
	
	private void saveLessonType(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_NAME);
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		String localizedKey = iwc.getParameter(PARAMETER_LOCALIZED_KEY);
		int order = -1;
		if (iwc.isParameterSet(PARAMETER_ORDER)) {
			order = Integer.parseInt(iwc.getParameter(PARAMETER_ORDER));
		}
		boolean isSelectable = iwc.isParameterSet(PARAMETER_IS_SELECTABLE);
		
		try {
			getBusiness().saveLessonType(iLessonTypePK, name, description, localizedKey, order, isSelectable);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (CreateException ce) {
			log(ce);
			add(getErrorText(localize("lesson_type.could_not_create_lesson_type", "The lesson type could not be created...")));
			add(new Break(2));
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_LESSON_TYPE)) {
			iLessonTypePK = iwc.getParameter(PARAMETER_LESSON_TYPE);
		}

		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
}