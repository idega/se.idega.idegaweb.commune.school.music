/*
 * $Id: MusicSchoolPendingApplicationWriter.java,v 1.1 2005/03/21 09:00:46 laddi Exp $
 * Created on 21.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;


public class MusicSchoolPendingApplicationWriter extends MusicSchoolApplicationWriter {

	protected Collection getCollection(IWContext iwc) {
		try {
			return getBusiness(iwc).findPendingChoicesInSchool(getSession(iwc).getProvider(), getSession(iwc).getSeason(), getSession(iwc).getDepartment(), getSession(iwc).getInstrument());
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
}