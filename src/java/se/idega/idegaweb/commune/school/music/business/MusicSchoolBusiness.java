/*
 * $Id: MusicSchoolBusiness.java,v 1.12 2004/10/21 10:57:27 thomas Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
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
 * 
 *  Last modified: $Date: 2004/10/21 10:57:27 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.12 $
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
	public Collection findChoicesByChildAndSeason(User child, SchoolSeason season) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#findAllMusicSchools
	 */
	public Collection findAllMusicSchools() throws FinderException, java.rmi.RemoteException;

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
	public boolean saveChoices(User user, User child, Object[] schools, Object seasonPK, Object departmentPK,
			Object lessonTypePK, Object[] instruments, String teacherRequest, String message, Object currentYear,
			Object currentInstrument, String previousStudy, String elementarySchool, int paymentMethod)
			throws IDOCreateException, java.rmi.RemoteException;

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
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#rejectApplication
	 */
	public boolean rejectApplication(Object applicationPK, User performer) throws java.rmi.RemoteException;

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
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolBusinessBean#getLocalizedCaseDescription
	 */
	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws java.rmi.RemoteException;
}
