/*
 * $Id: MusicSchoolBusinessHome.java,v 1.8 2005/03/31 08:22:32 laddi Exp $
 * Created on 31.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolBusinessHome
 * </p>
 *  Last modified: $Date: 2005/03/31 08:22:32 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
 */
public interface MusicSchoolBusinessHome extends IBOHome {

	public MusicSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
