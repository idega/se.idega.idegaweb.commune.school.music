/*
 * $Id: MusicSchoolStudentImportFileHandlerHome.java,v 1.1 2005/03/29 13:59:17 laddi Exp $
 * Created on 29.3.2005
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
 * TODO laddi Describe Type MusicSchoolStudentImportFileHandlerHome
 * </p>
 *  Last modified: $Date: 2005/03/29 13:59:17 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface MusicSchoolStudentImportFileHandlerHome extends IBOHome {

	public MusicSchoolStudentImportFileHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
