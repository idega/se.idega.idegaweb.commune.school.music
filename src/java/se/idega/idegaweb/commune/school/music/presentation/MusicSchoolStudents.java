/**
 * Created on 1.2.2003 This class does something very clever.
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.SchoolChoiceComparator;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolGroupWriter;
import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;
import se.idega.util.SchoolClassMemberComparatorForSweden;

import com.idega.block.school.business.SchoolClassMemberComparator;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MediaWritable;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class MusicSchoolStudents extends MusicSchoolBlock {

	private final String PARAMETER_ACTION = "sch_action";
	private final String PARAMETER_SORT = "sch_student_sort";

	private final int ACTION_MANAGE = 1;

	private int action = 0;
	private int sortStudentsBy = SchoolChoiceComparator.NAME_SORT;

	private SchoolClass _group;

	/**
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws RemoteException {
		if (iwc.isLoggedOn()) {
			parseAction(iwc);

			switch (action) {
				case ACTION_MANAGE :
					drawForm(iwc);
					break;
			}
		}
		else {
			add(super.getSmallHeader(localize("not_logged_on", "Not logged on")));
		}
	}

	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		else
			action = ACTION_MANAGE;

		if (iwc.isParameterSet(PARAMETER_SORT))
			sortStudentsBy = Integer.parseInt(iwc.getParameter(PARAMETER_SORT));
		else
			sortStudentsBy = SchoolChoiceComparator.NAME_SORT;
	}

	private void drawForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(MusicSchoolEventListener.class);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(action)));

		Table table = new Table(1, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		table.setHeight(3, 12);

		form.add(table);

		Table headerTable = new Table(1, 3);
		headerTable.setWidth(Table.HUNDRED_PERCENT);
		headerTable.setCellpaddingAndCellspacing(0);
		headerTable.setHeight(1, 2, 6);
		table.add(headerTable, 1, 1);

		headerTable.setCellpaddingLeft(1, 1, 12);
		headerTable.add(getNavigationTable(), 1, 1);
		headerTable.setCellpaddingLeft(1, 3, 12);
		headerTable.add(getSortTable(), 1, 3);
		headerTable.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);

		if (getSession().getGroup() != null) {
			_group = getSession().getGroup();
			table.setCellpaddingRight(1, 3, 6);
			table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(getXLSLink(), 1, 3);
			table.add(getStudentTable(iwc), 1, 5);
		}
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

	private Table getStudentTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(5);
		int row = 1;

		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(getSmallHeader(localize("personal_id", "Personal ID")), 2, row);
		table.add(getSmallHeader(localize("address", "Address")), 3, row);
		table.add(getSmallHeader(localize("zip_code", "Zip code")), 4, row);
		table.add(getSmallHeader(localize("home_phone", "Phone")), 5, row);
		table.setCellpaddingLeft(1, row, 12);
		table.setRowStyleClass(row++, getHeaderRowClass());

		User student;
		Address address;
		PostalCode postal;
		Phone phone;
		SchoolClassMember studentMember;
		int numberOfStudents = 0;
		boolean notStarted = false;
		boolean hasTerminationDate = false;
		boolean showNotStarted = false;
		boolean showHasTermination = false;

		IWTimestamp stamp = new IWTimestamp();
		IWTimestamp startDate;

		List students = null;
		if (!_group.getIsSubGroup()) {
			try {
				students = new ArrayList(getSchoolBusiness().getSchoolClassMemberHome().findBySchoolClassAndYearAndStudyPath(getSession().getGroup(), getSession().getDepartment(), getSession().getInstrument()));
			}
			catch (FinderException fe) {
				students = new ArrayList();
			}
		}
		else {
			students = new ArrayList(getSchoolBusiness().findSubGroupPlacements(_group));
		}

		if (!students.isEmpty()) {
			numberOfStudents = students.size();
			Map studentMap = getCareBusiness().getStudentList(students);
			Collections.sort(students, SchoolClassMemberComparatorForSweden.getComparatorSortBy(sortStudentsBy, iwc.getCurrentLocale(), getUserBusiness(), studentMap));
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				studentMember = (SchoolClassMember) iter.next();
				student = (User) studentMap.get(new Integer(studentMember.getClassMemberId()));
				address = getUserBusiness().getUsersMainAddress(student);
				if (address != null) {
					postal = address.getPostalCode();
				}
				else {
					postal = null;
				}
				try {
					phone = getUserBusiness().getUsersHomePhone(student);
				}
				catch (NoPhoneFoundException npfe) {
					phone = null;
				}
				notStarted = false;
				hasTerminationDate = false;

				if (studentMember.getRegisterDate() != null) {
					startDate = new IWTimestamp(studentMember.getRegisterDate());
					if (startDate.isLaterThan(stamp)) {
						notStarted = true;
					}
				}
				if (studentMember.getRemovedDate() != null) {
					hasTerminationDate = true;
				}

				Name studentName = new Name(student.getFirstName(), student.getMiddleName(), student.getLastName());
				String name = studentName.getName(iwc.getApplicationSettings().getDefaultLocale(), true);

				if (row % 2 == 0)
					table.setRowStyleClass(row, getDarkRowClass());
				else
					table.setRowStyleClass(row, getLightRowClass());

				if (notStarted) {
					showNotStarted = true;
					table.add(getSmallErrorText("+"), 1, row);
				}
				if (hasTerminationDate) {
					showHasTermination = true;
					table.add(getSmallErrorText("&Delta;"), 1, row);
				}
				if (notStarted || hasTerminationDate) {
					table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
				}

				table.setCellpaddingLeft(1, row, 12);
				table.add(getSmallText(name), 1, row);
				table.add(getSmallText(PersonalIDFormatter.format(student.getPersonalID(), iwc.getCurrentLocale())), 2, row);

				if (address != null && address.getStreetAddress() != null)
					table.add(getSmallText(address.getStreetAddress()), 3, row);
				if (postal != null)
					table.add(getSmallText(postal.getPostalAddress()), 4, row);
				if (phone != null)
					table.add(getSmallText(phone.getNumber()), 5, row);
				row++;
			}

			if (showNotStarted || showHasTermination) {
				table.setHeight(row++, 2);
				if (showNotStarted) {
					table.mergeCells(1, row, table.getColumns(), row);
					table.setCellpaddingLeft(1, row, 12);
					table.add(getSmallErrorText("+ "), 1, row);
					table.add(getSmallText(localize("school.placement_has_not_started", "Placment has not started yet")), 1, row++);
				}
				if (showHasTermination) {
					table.mergeCells(1, row, table.getColumns(), row);
					table.setCellpaddingLeft(1, row, 12);
					table.add(getSmallErrorText("&Delta; "), 1, row);
					table.add(getSmallText(localize("school.placement_has_termination_date", "Placment has termination date")), 1, row++);
				}
			}
		}

		if (numberOfStudents > 0) {
			table.setHeight(row++, 6);
			table.mergeCells(1, row, table.getColumns(), row);
			table.setCellpaddingLeft(1, row, 12);
			table.add(getSmallHeader(localize("school.number_of_students", "Number of students") + ": " + String.valueOf(numberOfStudents)), 1, row++);
		}

		return table;
	}

	protected Table getSortTable() {
		Table table = new Table(2, 3);
		table.setCellpadding(0);
		table.setCellspacing(0);

		table.add(getSmallHeader(localize("school.sort_by", "Sort by") + ":" + Text.NON_BREAKING_SPACE), 1, 3);

		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SORT));
		menu.addMenuElement(SchoolChoiceComparator.NAME_SORT, localize("school.sort_by", "- Sort by -"));
		menu.addMenuElement(SchoolChoiceComparator.NAME_SORT, localize("school.sort_name", "Name"));
		menu.addMenuElement(SchoolChoiceComparator.PERSONAL_ID_SORT, localize("school.sort_personal_id", "Personal ID"));
		menu.addMenuElement(SchoolChoiceComparator.ADDRESS_SORT, localize("school.sort_address", "Address"));
		menu.addMenuElement(SchoolChoiceComparator.GENDER_SORT, localize("school.sort_gender", "Gender"));
		menu.setSelectedElement(sortStudentsBy);
		menu.setToSubmit();
		table.add(menu, 2, 3);

		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);

		return table;
	}
}