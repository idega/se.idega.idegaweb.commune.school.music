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
import se.idega.idegaweb.commune.presentation.CommuneBlock;
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
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author laddi
 */
public abstract class MusicSchoolBlock extends CommuneBlock {

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school.music";
	
	private final static int PAYMENT_METHOD_VISA = 1;
	private final static int PAYMENT_METHOD_EUROCARD = 2;
	private final static int PAYMENT_METHOD_GIRO = 3;
	private final static int PAYMENT_METHOD_CASH = 4;

	private SchoolBusiness sBusiness;
	private CommuneUserBusiness uBusiness;
	private MusicSchoolBusiness business;
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
			return getBusiness().findAllDepartments();
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
			return getBusiness().findAllSelectableDepartments();
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
			departments.setToSubmit(true);
			
			departments.addMenuElementFirst("", localize("department", "- Department -"));
			try {
				List coll = new ArrayList(getBusiness().findDepartmentsInSchool(getSession().getProvider()));
				Collections.sort(coll, new SchoolYearComparator());
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
			instruments.setToSubmit(true);
			
			instruments.addMenuElementFirst("", localize("instrument", "- Instrument -"));
			try {
				Collection coll = getBusiness().findInstrumentsInSchool(getSession().getProvider());
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
					instruments.addMenuElement(instrument.getPrimaryKey().toString(), localize(instrument.getCode(), instrument.getDescription()));
				}
			}
			catch (FinderException fe) {
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
			seasons.setToSubmit(true);
			
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
	}

	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CommuneUserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac, CommuneUserBusiness.class);
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

	private MusicSchoolSession getMusicSchoolSession(IWUserContext iwuc) {
		try {
			return (MusicSchoolSession) IBOLookup.getSessionInstance(iwuc, MusicSchoolSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}