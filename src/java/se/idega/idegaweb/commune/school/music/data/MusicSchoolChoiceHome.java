/*
 * $Id: MusicSchoolChoiceHome.java,v 1.4 2004/09/26 10:14:17 laddi Exp $
 * Created on 26.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/26 10:14:17 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface MusicSchoolChoiceHome extends IDOHome {

	public MusicSchoolChoice create() throws javax.ejb.CreateException;

	public MusicSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByChild
	 */
	public Collection findAllByChild(User child) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(School school, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(School school, SchoolSeason season, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(School school, SchoolYear year, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(User child, School school, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public MusicSchoolChoice findAllByStatuses(User child, School school, SchoolSeason season, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(User child, School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByChildAndChoiceNumberAndSeason
	 */
	public MusicSchoolChoice findAllByChildAndChoiceNumberAndSeason(User child, int choiceNumber, SchoolSeason season) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(User child, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(User child, SchoolSeason season, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbFindAllByStatuses
	 */
	public Collection findAllByStatuses(User child, SchoolSeason season, SchoolYear year, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
	 */
	public int getNumberOfApplications(User child, String[] statuses) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
	 */
	public int getNumberOfApplications(User child, SchoolSeason season, String[] statuses) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
	 */
	public int getNumberOfApplications(User child, SchoolYear year, String[] statuses) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
	 */
	public int getNumberOfApplications(School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String types, String[] statuses) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
	 */
	public int getNumberOfApplications(User child, School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String types, String[] statuses) throws IDOException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#ejbHomeGetMusicChoiceStatistics
	 */
	public int getMusicChoiceStatistics(String status, boolean firstChoiceOnly) throws IDOException;

}
