package se.idega.idegaweb.commune.school.music.business;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceHome;

import com.idega.block.school.data.SchoolSeason;
import com.idega.user.data.User;


public interface MusicSchoolBusiness extends com.idega.block.process.business.CaseBusiness
{
 public MusicSchoolChoiceHome getMusicSchoolChoiceHome() throws java.rmi.RemoteException;
 public boolean addStudentsToGroup(java.lang.String[] p0,com.idega.block.school.data.SchoolClass p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3,com.idega.user.data.User p4) throws java.rmi.RemoteException;
 public void deleteInstrument(Object instrumentPK) throws java.rmi.RemoteException;
 public java.util.Collection findAllDepartments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllInstruments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllMusicSchools()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllSelectableDepartments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllTypes()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoicesByChildAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoicesInSchool(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findPendingChoicesInSchool(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findDepartmentsInSchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findGroupsInSchool(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findInstrumentsInSchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice findMusicSchoolChoice(java.lang.Object p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public java.util.Map getInstrumentSchoolMap(java.util.Locale p0) throws java.rmi.RemoteException;
 public java.util.Map getInstrumentSchoolMap(java.util.Collection p0,java.util.Locale p1) throws java.rmi.RemoteException;
 public java.lang.String getLocalizedCaseDescription(com.idega.block.process.data.Case p0,java.util.Locale p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness() throws java.rmi.RemoteException;
 public java.util.Map getStudentList(java.util.Collection students) throws java.rmi.RemoteException;
 public boolean hasGrantedApplication(User student, SchoolSeason season) throws java.rmi.RemoteException;
 public boolean isPlacedInSchool(com.idega.user.data.User p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolSeason p2,com.idega.block.school.data.SchoolStudyPath p3) throws java.rmi.RemoteException;
 public boolean rejectApplication(Object applicationPK, com.idega.user.data.User performer) throws java.rmi.RemoteException;
 public boolean removeChoiceFromGroup(java.lang.Object p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean saveChoices(com.idega.user.data.User p0,com.idega.user.data.User p1,java.lang.Object[] p2,java.lang.Object p3,java.lang.Object p4,java.lang.Object p5,java.lang.Object[] p6,java.lang.String p7,java.lang.String p8,java.lang.Object p9,java.lang.Object p10,java.lang.String p11,java.lang.String p12,int p13)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public void saveInstrument(Object instrumentPK, String code, String description, String localizedKey) throws FinderException, CreateException, java.rmi.RemoteException;
 public void saveDepartment(Object instrumentPK, String name, String description, String localizedKey, int order, boolean isSelectable) throws FinderException, CreateException, java.rmi.RemoteException;
 public void saveLessonType(Object lessonTypePK, String name, String description, String localizedKey, int order, boolean isSelectable) throws CreateException, java.rmi.RemoteException;
 public void reactivateApplication(Object applicationPK, User performer) throws java.rmi.RemoteException;
 public int getMusicSchoolStatistics(boolean showFirstChoiceOnly) throws java.rmi.RemoteException;
}
