/*
 * $Id: MusicSchoolBusinessHomeImpl.java,v 1.6 2005/03/29 06:26:37 laddi Exp $
 * Created on 28.3.2005
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
 *  Last modified: $Date: 2005/03/29 06:26:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public class MusicSchoolBusinessHomeImpl extends IBOHomeImpl implements MusicSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolBusiness.class;
	}

	public MusicSchoolBusiness create() throws javax.ejb.CreateException {
		return (MusicSchoolBusiness) super.createIBO();
	}
}
