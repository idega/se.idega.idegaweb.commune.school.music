package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.business.SchoolUserBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.school.presentation.SchoolUserEditor;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.User;

public class MusicSchoolUserEditor extends SchoolUserEditor {

	IWBundle communeBundle;
	private GroupBusiness groupBusiness = null;
	
	private static String PROPERTY_MUSIC_SCHOOL_ADMIN_GROUP_ID = "music_school_administrators_group_id";

	public MusicSchoolUserEditor() {
		super();
	}

	public MusicSchoolUserEditor(IWContext iwc) throws RemoteException {
		super(iwc);
	}
	
	public String getBundleIdentifier() {
		return MusicSchoolBlock.IW_BUNDLE_IDENTIFIER;
	}

	protected void postSaveUpdate(School school, User user, int userType) throws RemoteException {
		postSaveNew(school, user, userType);
	}
	
	protected void postSaveNew(School school, User user, int userType) throws RemoteException {
		int groupId = getMusicSchoolAdminGroupId();
		
		if (userType != SchoolUserBusinessBean.USER_TYPE_TEACHER && groupId > 0) {
			groupBusiness.addUser(groupId, user);
		}
	}
	
	private int getMusicSchoolAdminGroupId() {
		String groupId = communeBundle.getProperty(PROPERTY_MUSIC_SCHOOL_ADMIN_GROUP_ID);
		if (groupId != null) {
			return Integer.parseInt(groupId);
		}
		logError("MusicSchoolAdminGroup not found (parameter="+PROPERTY_MUSIC_SCHOOL_ADMIN_GROUP_ID+", in commune="+communeBundle.getBundleIdentifier());
		return -1;
	}

	public void main(IWContext iwc) throws RemoteException {
		communeBundle = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		groupBusiness = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		super.main(iwc);
	}

}
