/*
 * $Id: MusicSchoolBusinessHomeImpl.java,v 1.8 2005/03/31 08:22:32 laddi Exp $
 * Created on 31.3.2005
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
 *  Last modified: $Date: 2005/03/31 08:22:32 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
 */
public class MusicSchoolBusinessHomeImpl extends IBOHomeImpl implements MusicSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolBusiness.class;
	}

	public MusicSchoolBusiness create() throws javax.ejb.CreateException {
		return (MusicSchoolBusiness) super.createIBO();
	}
}
