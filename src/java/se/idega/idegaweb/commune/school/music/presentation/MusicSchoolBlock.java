/*
 * Created on 3.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.music.business.InstrumentComparator;
import se.idega.idegaweb.commune.school.music.business.MusicConstants;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolBusiness;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolSession;
import se.idega.idegaweb.commune.school.music.business.NoDepartmentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoInstrumentFoundException;
import se.idega.idegaweb.commune.school.music.business.NoLessonTypeFoundException;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public abstract class MusicSchoolBlock extends CommuneBlock {
	
	protected static String PLACED_COLOR;
	protected static String PLACED_FOR_INSTRUMENT_COLOR;

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school.music";
	
	protected static final String STYLENAME_HEADER_CELL = "HeaderCell";
	protected static final String STYLENAME_HEADING_CELL = "HeadingCell";
	protected static final String STYLENAME_TEXT_CELL = "TextCell";
	protected static final String STYLENAME_INPUT_CELL = "InputCell";
	protected static final String STYLENAME_INFORMATION_CELL = "InformationCell";
	
	private final static int PAYMENT_METHOD_VISA = 1;
	private final static int PAYMENT_METHOD_EUROCARD = 2;
	private final static int PAYMENT_METHOD_GIRO = 3;
	private final static int PAYMENT_METHOD_CASH = 4;

	private SchoolBusiness sBusiness;
	private CommuneUserBusiness uBusiness;
	private MusicSchoolBusiness business;
	private CareBusiness careBusiness;
	private MusicSchoolSession session;

	public void main(IWContext iwc) throws Exception {
		initialize(iwc);
		init(iwc);
	}

	public abstract void init(IWContext iwc) throws Exception;

	public SchoolBusiness getSchoolBusiness() {
		return sBusiness;
	}

	public CommuneUserBusiness getUserBusiness() {
		return uBusiness;
	}

	public MusicSchoolBusiness getBusiness() {
		return business;
	}
	
	public CareBusiness getCareBusiness() {
		return careBusiness;
	}

	public MusicSchoolSession getSession() {
		return session;
	}

	public Collection getInstruments() throws NoInstrumentFoundException {
		try {
			return getBusiness().findAllInstruments();
		}
		catch (FinderException fe) {
			throw new NoInstrumentFoundException(fe);
		}
		catch (RemoteException re) {
			throw new NoInstrumentFoundException(re);
		}
	}
	
	public Collection getDepartments() throws NoDepartmentFoundException {
		try {
			List coll = new ArrayList(getBusiness().findAllDepartments());
			Collections.sort(coll, new SchoolYearComparator());
			return coll;
		}
		catch (FinderException fe) {
			throw new NoDepartmentFoundException(fe);
		}
		catch (RemoteException re) {
			throw new NoDepartmentFoundException(re);
		}
	}
	
	public Collection getSelectableDepartments() throws NoDepartmentFoundException {
		try {
			List coll = new ArrayList(getBusiness().findAllSelectableDepartments());
			Collections.sort(coll, new SchoolYearComparator());
			return coll;
		}
		catch (FinderException fe) {
			throw new NoDepartmentFoundException(fe);
		}
		catch (RemoteException re) {
			throw new NoDepartmentFoundException(re);
		}
	}
	
	public Collection getLessonTypes() throws NoLessonTypeFoundException {
		try {
			return getBusiness().findAllTypes();
		}
		catch (FinderException fe) {
			throw new NoLessonTypeFoundException(fe);
		}
		catch (RemoteException re) {
			throw new NoLessonTypeFoundException(re);
		}
	}
	
	public String getPaymentMethodString(int paymentMethod) {
		switch (paymentMethod) {
			case PAYMENT_METHOD_VISA:
				return localize("payment_method.VISA", "Visa");
			case PAYMENT_METHOD_EUROCARD:
				return localize("payment_method.EUROCARD", "Eurocard/Mastercard");
			case PAYMENT_METHOD_GIRO:
				return localize("payment_method.GIRO", "Giro");
			case PAYMENT_METHOD_CASH:
				return localize("payment_method.CASH", "Cash");
		}
		return null;
	}
	
	public DropdownMenu getPaymentMethods(String parameterName, int selectedMethod) {
		DropdownMenu methods = (DropdownMenu) getStyledInterface(new DropdownMenu(parameterName));
		methods.addMenuElement(PAYMENT_METHOD_VISA, getPaymentMethodString(PAYMENT_METHOD_VISA));
		methods.addMenuElement(PAYMENT_METHOD_EUROCARD, getPaymentMethodString(PAYMENT_METHOD_EUROCARD));
		methods.addMenuElement(PAYMENT_METHOD_GIRO, getPaymentMethodString(PAYMENT_METHOD_GIRO));
		methods.addMenuElement(PAYMENT_METHOD_CASH, getPaymentMethodString(PAYMENT_METHOD_CASH));
		if (selectedMethod != -1) {
			methods.setSelectedElement(selectedMethod);
		}
		
		return methods;
	}
	
	public DropdownMenu getDepartmentsDropdown() {
		try {
			DropdownMenu departments = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterNameDepartmentID()));
			
			departments.addMenuElementFirst("", localize("department", "- Department -"));
			try {
				List coll = new ArrayList(getBusiness().findDepartmentsInSchool(getSession().getProvider()));
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					SchoolYear department = (SchoolYear) iter.next();
					departments.addMenuElement(department.getPrimaryKey().toString(), localize(department.getLocalizedKey(), department.getSchoolYearName()));
				}
			}
			catch (FinderException fe) {
				log(fe);
			}
			
			if (getSession().getDepartmentPK() != null) {
				departments.setSelectedElement(getSession().getDepartmentPK().toString());
			}
			
			return departments;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public DropdownMenu getInstrumentsDropdown() {
		try {
			DropdownMenu instruments = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterNameInstrumentID()));
			
			instruments.addMenuElementFirst("", localize("instrument", "- Instrument -"));
			try {
				List coll = new ArrayList(getInstruments());
				Collections.sort(coll, new InstrumentComparator(getResourceBundle()));
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
					instruments.addMenuElement(instrument.getPrimaryKey().toString(), localize(instrument.getLocalizedKey(), instrument.getDescription()));
				}
			}
			catch (NoInstrumentFoundException fe) {
				log(fe);
			}

			if (getSession().getInstrumentPK() != null) {
				instruments.setSelectedElement(getSession().getInstrumentPK().toString());
			}
			
			return instruments;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public DropdownMenu getSeasonsDropdown() {
		try {
			DropdownMenu seasons = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterNameSeasonID()));
			
			seasons.addMenuElementFirst("", localize("season", "- Season -"));
			try {
				Collection coll = getSchoolBusiness().getSchoolSeasonHome().findAllSchoolSeasons();
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					SchoolSeason season = (SchoolSeason) iter.next();
					seasons.addMenuElement(season.getPrimaryKey().toString(), localize(season.getSchoolSeasonName(), season.getSchoolSeasonName()));
				}
			}
			catch (FinderException fe) {
				log(fe);
			}

			if (getSession().getSeasonPK() != null) {
				seasons.setSelectedElement(getSession().getSeasonPK().toString());
			}
			
			return seasons;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	protected Table getNavigationTable() {
		Table table = new Table(4, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		int column = 1;
		
		DropdownMenu seasons = getSeasonsDropdown();
		table.add(seasons, column, 1);
		table.setCellpaddingRight(column++, 1, 3);
		
		DropdownMenu departments = getDepartmentsDropdown();
		table.add(departments, column, 1);
		table.setCellpaddingRight(column, 1, 3);
		table.setCellpaddingLeft(column++, 1, 3);
		
		DropdownMenu instruments = getInstrumentsDropdown();
		table.add(instruments, column, 1);
		table.setCellpaddingRight(column, 1, 3);
		table.setCellpaddingLeft(column++, 1, 3);
		
		SubmitButton fetch = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("get", "Get")));
		table.add(fetch, column, 1);
		table.setCellpaddingLeft(column++, 1, 3);
		
		return table;
	}
	
	protected Table getLegendTable() {
		Table table = new Table();
		table.setHeight(1, 12);
		table.setWidth(1, "12");
		table.setWidth(3, "12");
		table.setWidth(4, "12");
		
		setColorToCell(table, 1, 1, PLACED_COLOR);
		setColorToCell(table, 4, 1, PLACED_FOR_INSTRUMENT_COLOR);
		
		table.add(getSmallHeader(localize("music_choice.placed_application","Application placed for other instrument/s")), 2, 1);
		table.add(getSmallHeader(localize("music_choice.placed_application_for_instrument","Application placed for instrument")), 5, 1);
		
		return table;
	}
	
	protected Table getPersonInfoTable(IWContext iwc, User user) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(0);
		table.setColumns(5);
		table.setWidth(3, 12);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		
		Age age = new Age(user.getDateOfBirth());
		Address address = getUserBusiness().getUsersMainAddress(user);
		PostalCode postal = null;
		if (address != null) {
			postal = address.getPostalCode();
		}
		Phone phone = null;
		try {
			phone = getUserBusiness().getUsersHomePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			phone = null;
		}
		Phone mobile = null;
		try {
			mobile = getUserBusiness().getUsersMobilePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			mobile = null;
		}
		Email email = null;
		try {
			email = getUserBusiness().getUsersMainEmail(user);
		}
		catch (NoEmailFoundException nefe) {
			email = null;
		}
		
		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(getText(user.getName()), 2, row);
		
		table.add(getSmallHeader(localize("personal_id", "Personal ID")), 4, row);
		table.add(getText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 5, row++);
		
		table.add(getSmallHeader(localize("address", "Address")), 1, row);
		table.add(getSmallHeader(localize("zip_code", "Postal code")), 4, row);
		if (address != null) {
			table.add(getText(address.getStreetAddress()), 2, row);
		}
		if (postal != null) {
			table.add(getText(postal.getPostalAddress()), 5, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("home_phone", "Home phone")), 1, row);
		table.add(getSmallHeader(localize("mobile_phone", "Mobile phone")), 4, row);
		if (phone != null && phone.getNumber() != null) {
			table.add(getText(phone.getNumber()), 2, row);
		}
		if (mobile != null && mobile.getNumber() != null) {
			table.add(getText(mobile.getNumber()), 5, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("email", "E-mail")), 1, row);
		if (email != null && email.getEmailAddress() != null) {
			table.add(getText(email.getEmailAddress()), 2, row);
		}
		row++;
		
		table.setHeight(row, 6);
		table.mergeCells(1, row, table.getColumns(), row);
		table.setBottomCellBorder(1, row++, 1, "#D7D7D7", "solid");
		table.setHeight(row++, 6);
		
		
		if (age.getYears() < 18) {
			User custodian = getUserBusiness().getCustodianForChild(user);
			if (custodian != null) {
				phone = null;
				try {
					phone = getUserBusiness().getUsersHomePhone(custodian);
				}
				catch (NoPhoneFoundException npfe) {
					phone = null;
				}
				mobile = null;
				try {
					mobile = getUserBusiness().getUsersMobilePhone(custodian);
				}
				catch (NoPhoneFoundException npfe) {
					mobile = null;
				}
				 email = null;
				try {
					email = getUserBusiness().getUsersMainEmail(custodian);
				}
				catch (NoEmailFoundException nefe) {
					email = null;
				}

				table.add(getSmallHeader(localize("custodian", "Custodian")), 1, row);
				table.add(getText(custodian.getName()), 2, row);

				table.add(getSmallHeader(localize("personal_id", "Personal ID")), 4, row);
				table.add(getText(PersonalIDFormatter.format(custodian.getPersonalID(), iwc.getCurrentLocale())), 5, row++);
				
				table.add(getSmallHeader(localize("home_phone", "Home phone")), 1, row);
				table.add(getSmallHeader(localize("mobile_phone", "Mobile phone")), 4, row);
				if (phone != null) {
					table.add(getText(phone.getNumber()), 2, row);
				}
				if (mobile != null) {
					table.add(getText(mobile.getNumber()), 5, row);
				}
				row++;
				
				table.add(getSmallHeader(localize("email", "E-mail")), 1, row);
				if (email != null) {
					table.add(getText(email.getEmailAddress()), 2, row);
				}
				row++;
				
				table.setHeight(row, 6);
				table.mergeCells(1, row, table.getColumns(), row);
				table.setBottomCellBorder(1, row++, 1, "#D7D7D7", "solid");
			}
		}
		
		return table;
	}
	
	protected void setColorToCell(Table table, int column, int row, String color) {
		table.setColor(column, row, color);
		table.setCellBorder(column, row, 1, "#000000", "solid");
	}
	
	protected DropdownMenu getDropdown(String parameterName, Object selectedElement) {
		DropdownMenu drop = (DropdownMenu) getStyledInterface(new DropdownMenu(parameterName));
		drop.setWidth("100%");
		if (selectedElement != null) {
			drop.setSelectedElement(selectedElement.toString());
		}
		
		return drop;
	}
	
	protected TextInput getTextInput(String parameterName, Object content) {
		return getTextInput(parameterName, content, false);
	}
	
	protected TextInput getTextInput(String parameterName, Object content, boolean disabled) {
		TextInput input = (TextInput) getStyledInterface(new TextInput(parameterName));
		input.setWidth("100%");
		if (content != null) {
			input.setContent(content.toString());
		}
		input.setDisabled(disabled);
		
		return input;
	}
	
	protected TextArea getTextArea(String parameterName, Object content) {
		TextArea area = (TextArea) getStyledInterface(new TextArea(parameterName));
		area.setWidth("100%");
		if (content != null) {
			area.setContent(content.toString());
		}
		
		return area;
	}
	
	protected Help getHelpButton(String key) {
		Help help = new Help();
		help.setHelpTextBundle(MusicConstants.HELP_BUNDLE_IDENTFIER);
		help.setHelpTextKey(key);
		help.setImage(getBundle().getImage("help.gif"));
		return help;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private void initialize(IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));
		setBundle(getBundle(iwc));
		business = getMusicSchoolBusiness(iwc);
		session = getMusicSchoolSession(iwc);
		sBusiness = getSchoolBusiness(iwc);
		uBusiness = getUserBusiness(iwc);
		careBusiness = getCareBusiness(iwc);
		
		PLACED_COLOR = getBundle().getProperty("placed_color", "#FFE0E0");
		PLACED_FOR_INSTRUMENT_COLOR = getBundle().getProperty("placed_for_instrument_color", "#E0FFE0");
	}

	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected CommuneUserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return super.getUserBusiness(iwac);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private MusicSchoolBusiness getMusicSchoolBusiness(IWApplicationContext iwac) {
		try {
			return (MusicSchoolBusiness) IBOLookup.getServiceInstance(iwac, MusicSchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	private CareBusiness getCareBusiness(IWApplicationContext iwac) {
		try {
			return (CareBusiness) IBOLookup.getServiceInstance(iwac, CareBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}


	private MusicSchoolSession getMusicSchoolSession(IWUserContext iwuc) {
		try {
			return (MusicSchoolSession) IBOLookup.getSessionInstance(iwuc, MusicSchoolSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	

}