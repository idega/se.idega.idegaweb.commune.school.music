/*
 * $Id: MusicSchoolChoice.java,v 1.4 2004/09/26 10:14:17 laddi Exp $
 * Created on 26.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/26 10:14:17 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface MusicSchoolChoice extends Case {

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getChild
	 */
	public User getChild();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getChildPK
	 */
	public Object getChildPK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolPK
	 */
	public Object getSchoolPK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolTypePK
	 */
	public Object getSchoolTypePK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolSeason
	 */
	public SchoolSeason getSchoolSeason();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolSeasonPK
	 */
	public Object getSchoolSeasonPK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolYear
	 */
	public SchoolYear getSchoolYear();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getSchoolYearPK
	 */
	public Object getSchoolYearPK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPlacementDate
	 */
	public Date getPlacementDate();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getChoiceDate
	 */
	public Timestamp getChoiceDate();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getChoiceNumber
	 */
	public int getChoiceNumber();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getMessage
	 */
	public String getMessage();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getTeacherRequest
	 */
	public String getTeacherRequest();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getElementarySchool
	 */
	public String getElementarySchool();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPreviousStudies
	 */
	public String getPreviousStudies();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPreviousYear
	 */
	public SchoolYear getPreviousYear();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPreviousYearPK
	 */
	public Object getPreviousYearPK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPreviousStudyPath
	 */
	public SchoolStudyPath getPreviousStudyPath();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPreviousStudyPathPK
	 */
	public Object getPreviousStudyPathPK();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getPaymentMethod
	 */
	public int getPaymentMethod();

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#getStudyPaths
	 */
	public Collection getStudyPaths() throws IDORelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setChild
	 */
	public void setChild(User child);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setChild
	 */
	public void setChild(Object childID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchool
	 */
	public void setSchool(Object schoolID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchoolSeason
	 */
	public void setSchoolSeason(SchoolSeason schoolSeason);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchoolSeason
	 */
	public void setSchoolSeason(Object schoolSeasonID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType schoolType);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchoolType
	 */
	public void setSchoolType(Object schoolTypeID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchoolYear
	 */
	public void setSchoolYear(SchoolYear schoolYear);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setSchoolYear
	 */
	public void setSchoolYear(Object schoolYearID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPlacementDate
	 */
	public void setPlacementDate(Date placementDate);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setChoiceDate
	 */
	public void setChoiceDate(Timestamp choiceDate);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setChoiceNumber
	 */
	public void setChoiceNumber(int choiceNumber);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setMessage
	 */
	public void setMessage(String message);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setTeacherRequest
	 */
	public void setTeacherRequest(String teacherRequest);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setElementarySchool
	 */
	public void setElementarySchool(String elementarySchool);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPreviousStudies
	 */
	public void setPreviousStudies(String previousStudies);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPreviousYear
	 */
	public void setPreviousYear(SchoolYear previousYear);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPreviousYear
	 */
	public void setPreviousYear(Object previousYearID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPreviousStudyPath
	 */
	public void setPreviousStudyPath(SchoolStudyPath studyPath);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPreviousStudyPath
	 */
	public void setPreviousStudyPath(Object studyPathID);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#setPaymentMethod
	 */
	public void setPaymentMethod(int paymentMethod);

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#addStudyPaths
	 */
	public void addStudyPaths(Object[] studyPathIDs) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#addStudyPaths
	 */
	public void addStudyPaths(Collection studyPaths) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#addStudyPath
	 */
	public void addStudyPath(SchoolStudyPath studyPath) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#removeStudyPaths
	 */
	public void removeStudyPaths() throws IDORemoveRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceBMPBean#removeStudyPath
	 */
	public void removeStudyPath(SchoolStudyPath studyPath) throws IDORemoveRelationshipException;

}
