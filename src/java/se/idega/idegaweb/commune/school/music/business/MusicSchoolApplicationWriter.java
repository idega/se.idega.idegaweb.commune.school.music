package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class MusicSchoolApplicationWriter extends MusicSchoolGroupWriter {

	public MusicSchoolApplicationWriter() {
		// empty
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			iwrb = iwc.getIWMainApplication().getBundle(MusicSchoolBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			schoolName = getSession(iwc).getProvider().getSchoolName();
			
			Collection choices = getCollection(iwc);
			buffer = writeXLS(choices);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Collection getCollection(IWContext iwc) {
		try {
			return getBusiness(iwc).findChoicesInSchool(getSession(iwc).getProvider(), getSession(iwc).getSeason(), getSession(iwc).getDepartment(), getSession(iwc).getInstrument());
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected User getUser(IDOEntity entity) {
		return ((MusicSchoolChoice) entity).getChild();
	}
	
	protected SchoolYear getDepartment(IDOEntity entity) {
		return ((MusicSchoolChoice) entity).getSchoolYear();
	}
	
	protected Collection getInstruments(IDOEntity entity) {
		try {
			return ((MusicSchoolChoice) entity).getStudyPaths();
		}
		catch (IDORelationshipException ire) {
			return new ArrayList();
		}
	}
}