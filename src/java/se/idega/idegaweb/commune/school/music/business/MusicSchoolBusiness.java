/*
 * $Id: MusicSchoolBusiness.java,v 1.13 2005/03/19 16:37:28 laddi Exp $
 * Created on 19.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceHome;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolBusiness
 * </p>
 *  Last modified: $Date: 2005/03/19 16:37:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.13 $
 */
public interface MusicSchoolBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getMusicSchoolChoiceHome
	 */
	public MusicSchoolChoiceHome getMusicSchoolChoiceHome() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getMessageBusiness
	 */
	public MessageBusiness getMessageBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findMusicSchoolChoice
	 */
	public MusicSchoolChoice findMusicSchoolChoice(Object primaryKey) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findChoicesByChildAndSeason
	 */
	public Collection findChoicesByChildAndSeason(User child, SchoolSeason season, boolean showExtraApplications)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findAllMusicSchools
	 */
	public Collection findAllMusicSchools() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findMusicSchool
	 */
	public School findMusicSchool(Object schoolPK) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findAllInstruments
	 */
	public Collection findAllInstruments() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findAllDepartments
	 */
	public Collection findAllDepartments() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findAllSelectableDepartments
	 */
	public Collection findAllSelectableDepartments() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findAllTypes
	 */
	public Collection findAllTypes() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findInstrumentsInSchool
	 */
	public Collection findInstrumentsInSchool(School school) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findDepartmentsInSchool
	 */
	public Collection findDepartmentsInSchool(School school) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findGroupsInSchool
	 */
	public Collection findGroupsInSchool(School school, SchoolSeason season, SchoolYear department,
			SchoolStudyPath instrument) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findChoicesInSchool
	 */
	public Collection findChoicesInSchool(School school, SchoolSeason season, SchoolYear department,
			SchoolStudyPath instrument) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findPendingChoicesInSchool
	 */
	public Collection findPendingChoicesInSchool(School school, SchoolSeason season, SchoolYear department,
			SchoolStudyPath instrument) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getInstrumentSchoolMap
	 */
	public Map getInstrumentSchoolMap(Locale locale) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getInstrumentSchoolMap
	 */
	public Map getInstrumentSchoolMap(Collection instruments, Locale locale) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#saveChoices
	 */
	public boolean saveChoices(User user, User child, Collection schools, Object seasonPK, Object departmentPK,
			Object lessonTypePK, Collection instruments, String otherInstrument, String teacherRequest, String message,
			Object currentYear, Object currentInstrument, String previousStudy, String elementarySchool, int paymentMethod,
			boolean extraApplications) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#updateChoice
	 */
	public MusicSchoolChoice updateChoice(Object choicePK, Object departmentPK, Object lessonTypePK,
			Collection instrumentsPKs, String teacherRequest, String message, String otherInstrument, String previousStudy,
			String elementarySchool) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(School school, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#addStudentsToGroup
	 */
	public boolean addStudentsToGroup(String[] choiceIDs, SchoolClass group, SchoolYear department,
			SchoolStudyPath instrument, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#removeChoiceFromGroup
	 */
	public boolean removeChoiceFromGroup(Object studentPK, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#rejectApplications
	 */
	public void rejectApplications(Object[] applicationPKs, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#rejectApplication
	 */
	public boolean rejectApplication(Object applicationPK, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#reactivateApplications
	 */
	public void reactivateApplications(Object[] applicationPKs, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#reactivateApplication
	 */
	public void reactivateApplication(Object applicationPK, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#isPlacedInSchool
	 */
	public boolean isPlacedInSchool(User student, School school, SchoolSeason season, SchoolStudyPath instrument)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#hasGrantedApplication
	 */
	public boolean hasGrantedApplication(User student, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#hasNextSeason
	 */
	public boolean hasNextSeason(SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getMusicSchoolStatistics
	 */
	public int getMusicSchoolStatistics(boolean showFirstChoiceOnly) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#deleteInstrument
	 */
	public void deleteInstrument(Object instrumentPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#saveInstrument
	 */
	public void saveInstrument(Object instrumentPK, String code, String description, String localizedKey)
			throws FinderException, CreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#saveDepartment
	 */
	public void saveDepartment(Object departmentPK, String name, String description, String localizedKey, int order,
			boolean isSelectable) throws FinderException, CreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#saveLessonType
	 */
	public void saveLessonType(Object lessonTypePK, String name, String description, String localizedKey, int order,
			boolean isSelectable) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#transferToNextSchoolSeason
	 */
	public void transferToNextSchoolSeason(Object[] studentPKs, School school, SchoolSeason currentSeason, User performer)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getLocalizedCaseDescription
	 */
	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws java.rmi.RemoteException;
}
