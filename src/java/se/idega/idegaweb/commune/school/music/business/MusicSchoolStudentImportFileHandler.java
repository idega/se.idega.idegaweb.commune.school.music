/*
 * $Id: MusicSchoolStudentImportFileHandler.java,v 1.1 2005/03/29 13:59:17 laddi Exp $
 * Created on 29.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOService;
import com.idega.user.data.Group;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolStudentImportFileHandler
 * </p>
 *  Last modified: $Date: 2005/03/29 13:59:17 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface MusicSchoolStudentImportFileHandler extends IBOService, ImportFileHandler {

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolStudentImportFileHandlerBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolStudentImportFileHandlerBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolStudentImportFileHandlerBean#setRootGroup
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolStudentImportFileHandlerBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException;
}
