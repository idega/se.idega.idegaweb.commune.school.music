/*
 * $Id: MusicSchoolStudentImportFileHandlerHomeImpl.java,v 1.1 2005/03/29 13:59:17 laddi Exp $
 * Created on 29.3.2005
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
 * TODO laddi Describe Type MusicSchoolStudentImportFileHandlerHomeImpl
 * </p>
 *  Last modified: $Date: 2005/03/29 13:59:17 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MusicSchoolStudentImportFileHandlerHomeImpl extends IBOHomeImpl implements
		MusicSchoolStudentImportFileHandlerHome {

	protected Class getBeanInterfaceClass() {
		return MusicSchoolStudentImportFileHandler.class;
	}

	public MusicSchoolStudentImportFileHandler create() throws javax.ejb.CreateException {
		return (MusicSchoolStudentImportFileHandler) super.createIBO();
	}
}
