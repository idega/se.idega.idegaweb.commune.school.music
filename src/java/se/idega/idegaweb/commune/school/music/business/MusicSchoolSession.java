/*
 * $Id: MusicSchoolSession.java,v 1.4 2005/03/30 14:00:47 laddi Exp $
 * Created on 30.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOSession;
import com.idega.user.data.User;


/**
 * <p>
 * TODO laddi Describe Type MusicSchoolSession
 * </p>
 *  Last modified: $Date: 2005/03/30 14:00:47 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface MusicSchoolSession extends IBOSession {

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#reset
	 */
	public void reset() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setCurrentBlock
	 */
	public void setCurrentBlock(MusicSchoolBlock block) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#isCurrentBlock
	 */
	public boolean isCurrentBlock(MusicSchoolBlock block) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getChild
	 */
	public User getChild() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getChildPK
	 */
	public Object getChildPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getStudent
	 */
	public SchoolClassMember getStudent() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getStudentPK
	 */
	public Object getStudentPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getProvider
	 */
	public School getProvider() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getProviderPK
	 */
	public Object getProviderPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getInstrument
	 */
	public SchoolStudyPath getInstrument() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getInstrumentPK
	 */
	public Object getInstrumentPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getDepartment
	 */
	public SchoolYear getDepartment() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getDepartmentPK
	 */
	public Object getDepartmentPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getSeason
	 */
	public SchoolSeason getSeason() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getSeasonPK
	 */
	public Object getSeasonPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getGroup
	 */
	public SchoolClass getGroup() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getGroupPK
	 */
	public Object getGroupPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getApplication
	 */
	public MusicSchoolChoice getApplication() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getApplicationPK
	 */
	public Object getApplicationPK() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameChildID
	 */
	public String getParameterNameChildID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameProviderID
	 */
	public String getParameterNameProviderID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameStudentID
	 */
	public String getParameterNameStudentID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameDepartmentID
	 */
	public String getParameterNameDepartmentID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameInstrumentID
	 */
	public String getParameterNameInstrumentID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameSeasonID
	 */
	public String getParameterNameSeasonID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameGroupID
	 */
	public String getParameterNameGroupID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#getParameterNameApplicationID
	 */
	public String getParameterNameApplicationID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setChild
	 */
	public void setChild(Object childPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setStudent
	 */
	public void setStudent(Object studentPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setProvider
	 */
	public void setProvider(Object providerPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setInstrument
	 */
	public void setInstrument(Object instrumentPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setDepartment
	 */
	public void setDepartment(Object departmentPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setSeason
	 */
	public void setSeason(Object seasonPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setGroup
	 */
	public void setGroup(Object groupPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.business.MusicSchoolSessionBean#setApplication
	 */
	public void setApplication(Object applicationPK) throws java.rmi.RemoteException;
}
