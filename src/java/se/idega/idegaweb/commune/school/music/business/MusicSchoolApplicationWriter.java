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
			careBusiness = getCareBusiness(iwc);
			userBusiness = getUserBusiness(iwc);
			iwrb = iwc.getIWMainApplication().getBundle(MusicSchoolBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			schoolName = getSession(iwc).getProvider().getSchoolName();
			
			Collection choices = getCollection(iwc);
			buffer = writeXLS(choices, iwc);
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
			fe.printStackTrace(System.err);
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
	
	protected boolean showEntry(IWContext iwc, IDOEntity entity, User student) {
		try {
			MusicSchoolChoice choice = (MusicSchoolChoice) entity;
			boolean isPlaced = choice.getCaseStatus().equals(getBusiness(iwc).getCaseStatusPlaced());
			boolean hasInstrumentPlacement = false;
			if (isPlaced && getSession(iwc).getInstrument() != null) {
				hasInstrumentPlacement = getBusiness(iwc).isPlacedInSchool(student, getSession(iwc).getProvider(), getSession(iwc).getSeason(), getSession(iwc).getInstrument());
			}
			else {
				hasInstrumentPlacement = false;
			}
			
			if (hasInstrumentPlacement || (getSession(iwc).getInstrument() == null && isPlaced)) {
				return false;
			}
			return true;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}