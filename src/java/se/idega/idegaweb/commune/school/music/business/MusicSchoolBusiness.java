package se.idega.idegaweb.commune.school.music.business;


public interface MusicSchoolBusiness extends com.idega.block.process.business.CaseBusiness
{
 public java.util.Collection findAllDepartments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllInstruments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllSelectableDepartments()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllTypes()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoicesByChildAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoicesInSchool(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findDepartmentsInSchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findGroupsInSchool(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findInstrumentsInSchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice findMusicSchoolChoice(java.lang.Object p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public java.util.Map getInstrumentSchoolMap(java.util.Locale p0) throws java.rmi.RemoteException;
 public java.util.Map getInstrumentSchoolMap(java.util.Collection p0,java.util.Locale p1) throws java.rmi.RemoteException;
 public java.lang.String getLocalizedCaseDescription(com.idega.block.process.data.Case p0,java.util.Locale p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness() throws java.rmi.RemoteException;
 public boolean saveChoices(com.idega.user.data.User p0,com.idega.user.data.User p1,java.lang.Object[] p2,java.lang.Object p3,java.lang.Object p4,java.lang.Object p5,java.lang.Object[] p6,java.lang.String p7,java.lang.String p8,java.lang.Object p9,java.lang.Object p10,java.lang.String p11,java.lang.String p12,int p13)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
}
