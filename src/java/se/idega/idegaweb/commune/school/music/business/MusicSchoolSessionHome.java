/*
 * $Id: MusicSchoolSessionHome.java,v 1.2 2005/03/20 12:47:09 laddi Exp $
 * Created on 20.3.2005
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
 * TODO laddi Describe Type MusicSchoolSessionHome
 * </p>
 *  Last modified: $Date: 2005/03/20 12:47:09 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MusicSchoolSessionHome extends IBOHome {

	public MusicSchoolSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
