/*
 * $Id: MusicSchoolChoiceHomeImpl.java,v 1.6 2005/03/31 08:22:32 laddi Exp $
 * Created on 31.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
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
import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolChoiceHomeImpl
 * </p>
 *  Last modified: $Date: 2005/03/31 08:22:32 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public class MusicSchoolChoiceHomeImpl extends IDOFactory implements MusicSchoolChoiceHome {

	protected Class getEntityInterfaceClass() {
		return MusicSchoolChoice.class;
	}

	public MusicSchoolChoice create() throws javax.ejb.CreateException {
		return (MusicSchoolChoice) super.createIDO();
	}

	public MusicSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MusicSchoolChoice) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByChild(User child) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByChild(child);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByStatuses(School school, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(school, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByStatuses(School school, SchoolSeason season, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(school, season, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByStatuses(School school, SchoolYear year, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(school, year, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByStatuses(User child, School school, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(child, school, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public MusicSchoolChoice findAllByStatuses(User child, School school, SchoolSeason season, String[] statuses)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(child, school, season, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByStatuses(User child, School school, SchoolSeason season, SchoolYear year,
			SchoolStudyPath instrument, String[] statuses, Boolean showExtraApplications) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(child, school, season, year,
				instrument, statuses, showExtraApplications);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public MusicSchoolChoice findAllByChildAndChoiceNumberAndSeason(User child, int choiceNumber, SchoolSeason season)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByChildAndChoiceNumberAndSeason(child, choiceNumber,
				season);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public MusicSchoolChoice findAllByChildAndChoiceNumberAndSeason(User child, int choiceNumber, SchoolSeason season,
			boolean showExtraApplications) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByChildAndChoiceNumberAndSeason(child, choiceNumber,
				season, showExtraApplications);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByStatuses(User child, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(child, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByStatuses(User child, SchoolSeason season, String[] statuses, Boolean showExtraApplications)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(child, season, statuses,
				showExtraApplications);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByStatuses(User child, SchoolSeason season, SchoolYear year, String[] statuses,
			Boolean showExtraApplications) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MusicSchoolChoiceBMPBean) entity).ejbFindAllByStatuses(child, season, year, statuses,
				showExtraApplications);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNumberOfApplications(User child, String[] statuses) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetNumberOfApplications(child, statuses);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfApplications(User child, SchoolSeason season, String[] statuses) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetNumberOfApplications(child, season, statuses);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfApplications(User child, SchoolSeason season, String[] statuses, boolean extraApplication)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetNumberOfApplications(child, season, statuses,
				extraApplication);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfApplications(User child, SchoolYear year, String[] statuses) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetNumberOfApplications(child, year, statuses);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfApplications(School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument,
			String types, String[] statuses) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetNumberOfApplications(school, season, year,
				instrument, types, statuses);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfApplications(User child, School school, SchoolSeason season, SchoolYear year,
			SchoolStudyPath instrument, String types, String[] statuses, Boolean showExtraApplications) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetNumberOfApplications(child, school, season, year,
				instrument, types, statuses, showExtraApplications);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getMusicChoiceStatistics(String status, boolean firstChoiceOnly) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MusicSchoolChoiceBMPBean) entity).ejbHomeGetMusicChoiceStatistics(status, firstChoiceOnly);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
