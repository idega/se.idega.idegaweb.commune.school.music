/*
 * $Id: MusicSchoolBusinessHomeImpl.java,v 1.7 2005/03/30 14:00:47 laddi Exp $
 * Created on 30.3.2005
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
 * TODO laddi Describe Type MusicSchoolBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2005/03/30 14:00:47 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class MusicSchoolBusinessHomeImpl extends IBOHomeImpl implements MusicSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolBusiness.class;
	}

	public MusicSchoolBusiness create() throws javax.ejb.CreateException {
		return (MusicSchoolBusiness) super.createIBO();
	}
}
