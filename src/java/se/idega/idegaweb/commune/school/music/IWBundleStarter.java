package se.idega.idegaweb.commune.school.music;

import se.idega.idegaweb.commune.school.music.business.MusicConstants;
import se.idega.idegaweb.commune.school.music.business.MusicSchoolBusiness;
import com.idega.block.process.business.CaseCodeManager;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public void start(IWBundle starterBundle) {
		CaseCodeManager caseCodeManager = CaseCodeManager.getInstance();
		caseCodeManager.addCaseBusinessForCode( MusicConstants.MUSIC_SCHOOL_CASE_CODE_KEY, MusicSchoolBusiness.class);
	}

	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
	
}
