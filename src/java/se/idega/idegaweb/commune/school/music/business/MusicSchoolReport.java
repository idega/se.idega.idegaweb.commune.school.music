/*
 * $Id: MusicSchoolReport.java,v 1.2 2005/04/06 09:29:06 laddi Exp $
 * Created on 6.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOSession;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolReport
 * </p>
 *  Last modified: $Date: 2005/04/06 09:29:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MusicSchoolReport extends IBOSession {

	public static final String PROPERTY_SINGING_STUDY_PATH_ID = "singing_study_path_id";
	public static final String PROPERTY_TYPE_IDS = "type_ids";

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolReportBean#getPendingChoicesReport
	 */
	public ReportableCollection getPendingChoicesReport(SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolReportBean#getPendingCommuneChoicesReport
	 */
	public ReportableCollection getPendingCommuneChoicesReport(SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolReportBean#getChoicesReport
	 */
	public ReportableCollection getChoicesReport(SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolReportBean#getCommuneChoicesReport
	 */
	public ReportableCollection getCommuneChoicesReport(SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolReportBean#getPlacingsReport
	 */
	public ReportableCollection getPlacingsReport(SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolReportBean#getCommunePlacingsReport
	 */
	public ReportableCollection getCommunePlacingsReport(SchoolSeason season) throws java.rmi.RemoteException;
}
