/*
 * $Id: MusicSchoolSessionHomeImpl.java,v 1.2 2005/03/20 12:47:09 laddi Exp $
 * Created on 20.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolSessionHomeImpl
 * </p>
 *  Last modified: $Date: 2005/03/20 12:47:09 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MusicSchoolSessionHomeImpl extends IBOHomeImpl implements MusicSchoolSessionHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolSession.class;
	}

	public MusicSchoolSession create() throws javax.ejb.CreateException {
		return (MusicSchoolSession) super.createIBO();
	}
}
