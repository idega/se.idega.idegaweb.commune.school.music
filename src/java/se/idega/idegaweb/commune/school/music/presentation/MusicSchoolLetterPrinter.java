/*
 * Created on May 21, 2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.presentation.PrintMessageViewer;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolSession;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class MusicSchoolLetterPrinter extends PrintMessageViewer{
	
	private MusicSchoolSession getMusicSchoolSession(IWContext iwc)
	throws RemoteException {
	return (MusicSchoolSession) IBOLookup.getSessionInstance(
		iwc,
		MusicSchoolSession.class);
}

	
	protected Collection getLetters(IWContext iwc)  throws FinderException {
		try {
			Integer musicSchoolID =  (Integer) getMusicSchoolSession(iwc).getProviderPK();
			Collection unprintedLetters =
				getPrintedLetter().findAllLettersBySchool(musicSchoolID.intValue(),getSearchSsn(),getSearchMsgId(),getUFrom(),getUTo());
			return unprintedLetters;
		}
		catch (RemoteException e) {
			throw new FinderException(e.getMessage());
		}
		
	}

	
}
