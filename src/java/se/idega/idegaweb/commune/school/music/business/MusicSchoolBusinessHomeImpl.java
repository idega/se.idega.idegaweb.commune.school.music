/*
 * $Id: MusicSchoolBusinessHomeImpl.java,v 1.2 2004/10/21 10:57:27 thomas Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/10/21 10:57:27 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class MusicSchoolBusinessHomeImpl extends IBOHomeImpl implements MusicSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolBusiness.class;
	}

	public MusicSchoolBusiness create() throws javax.ejb.CreateException {
		return (MusicSchoolBusiness) super.createIBO();
	}
}
