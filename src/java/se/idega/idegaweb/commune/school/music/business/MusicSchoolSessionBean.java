/*
 * Created on 3.5.2004
 */
package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class MusicSchoolSessionBean extends IBOSessionBean implements MusicSchoolSession {

	protected User iUser;
	protected Object iUserID;
	
	protected User iChild;
	protected Object iChildPK;
	
	protected School iProvider;
	protected Object iProviderPK;
	
	protected SchoolStudyPath iInstrument;
	protected Object iInstrumentPK;
	
	protected SchoolYear iDepartment;
	protected Object iDepartmentPK;
	
	protected SchoolSeason iSeason;
	protected Object iSeasonPK;
	
	protected SchoolClass iGroup;
	protected Object iGroupPK;
	
	protected static final String PARAMETER_CHILD_ID = "ms_child_id";
	protected static final String PARAMETER_PROVIDER_ID = "ms_provider_id";
	protected static final String PARAMETER_INSTRUMENT_ID = "ms_instrument_id";
	protected static final String PARAMETER_DEPARTMENT_ID = "ms_department_id";
	protected static final String PARAMETER_SEASON_ID = "ms_season_id";
	protected static final String PARAMETER_GROUP_ID = "ms_group_id";
	
	public User getChild() {
		if (iChild == null && getChildPK() != null) {
			try {
				iChild = getUserBusiness().getUserHome().findByPrimaryKey(Integer.valueOf(getChildPK().toString()));
			}
			catch (FinderException fe) {
				log(fe);
				iChild = null;
			}
			catch (RemoteException re) {
				log(re);
				iChild = null;
			}
		}
		return iChild;
	}
	
	public Object getChildPK() {
		return iChildPK;
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public School getProvider() throws RemoteException {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			Object userID = user.getPrimaryKey();
			
			if (iUserID != null && iUserID.equals(userID)) {
				if (iProvider != null) {
					return iProvider;
				}
				else {
					return getSchoolIDFromUser(user);
				}
			}
			else {
				iUserID = userID;
				setSeason(null);
				setDepartment(null);
				setGroup(null);
				setInstrument(null);
				return getSchoolIDFromUser(user);
			}
		}
		else {
			return null;	
		}
	}
	
	private School getSchoolIDFromUser(User user) throws RemoteException {
		iProviderPK = null;
		if (user != null) {
			try {
				School school = getUserBusiness().getFirstManagingMusicSchoolForUser(user);
				if (school != null) {
					iProvider = school;
					iProviderPK = school.getPrimaryKey();
				}
			}
			catch (FinderException fe) {
				setProvider(null);
			}
		}
		return iProvider;
	}

	public Object getProviderPK() {
		return iProviderPK;
	}
	
	public SchoolStudyPath getInstrument() {
		if (iInstrument == null && iInstrumentPK != null) {
			try {
				iInstrument = getSchoolBusiness().getSchoolStudyPathHome().findByPrimaryKey(new Integer(iInstrumentPK.toString()));
			}
			catch (FinderException fe) {
				iInstrument = null;
			}
			catch (RemoteException re) {
				iInstrument = null;
			}
		}
		return iInstrument;
	}
	
	public Object getInstrumentPK() {
		return iInstrumentPK;
	}
	
	public SchoolYear getDepartment() {
		if (iDepartment == null && iDepartmentPK != null) {
			try {
				iDepartment = getSchoolBusiness().getSchoolYear(new Integer(iInstrumentPK.toString()));
			}
			catch (RemoteException re) {
				iDepartment = null;
			}
		}
		return iDepartment;
	}
	
	public Object getDepartmentPK() {
		return iDepartmentPK;
	}
	
	public SchoolSeason getSeason() {
		if (iSeason == null && iSeasonPK != null) {
			try {
				iSeason = getSchoolBusiness().getSchoolSeason(new Integer(iInstrumentPK.toString()));
			}
			catch (RemoteException re) {
				iSeason = null;
			}
		}
		return iSeason;
	}
	
	public Object getSeasonPK() {
		return iSeasonPK;
	}
	
	public SchoolClass getGroup() {
		if (iGroup == null && iGroupPK != null) {
			try {
				iGroup = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(iGroupPK.toString()));
			}
			catch (FinderException fe) {
				iGroup = null;
			}
			catch (RemoteException re) {
				iGroup = null;
			}
		}
		return iGroup;
	}
	
	public Object getGroupPK() {
		return iGroupPK;
	}
	
	public String getParameterNameChildID() {
		return PARAMETER_CHILD_ID;
	}
	
	public String getParameterNameProviderID() {
		return PARAMETER_PROVIDER_ID;
	}
	
	public String getParameterNameDepartmentID() {
		return PARAMETER_DEPARTMENT_ID;
	}
	
	public String getParameterNameInstrumentID() {
		return PARAMETER_INSTRUMENT_ID;
	}
	
	public String getParameterNameSeasonID() {
		return PARAMETER_SEASON_ID;
	}
	
	public String getParameterNameGroupID() {
		return PARAMETER_GROUP_ID;
	}
	
	//Setters
	public void setChild(Object childPK) {
		iChildPK = childPK;
		iChild = null;
	}
	
	public void setProvider(Object providerPK) {
		iProviderPK = providerPK;
		iProvider = null;
	}
	
	public void setInstrument(Object instrumentPK) {
		iInstrumentPK = instrumentPK;
		iInstrument = null;
	}
	
	public void setDepartment(Object departmentPK) {
		iDepartmentPK = departmentPK;
		iDepartment = null;
	}
	
	public void setSeason(Object seasonPK) {
		iSeasonPK = seasonPK;
		iSeason = null;
	}
	
	public void setGroup(Object groupPK) {
		iGroupPK = groupPK;
		iGroup = null;
	}
	
	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}