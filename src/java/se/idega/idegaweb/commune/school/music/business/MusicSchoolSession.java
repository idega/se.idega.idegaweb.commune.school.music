package se.idega.idegaweb.commune.school.music.business;


public interface MusicSchoolSession extends com.idega.business.IBOSession
{
 public se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice getApplication() throws java.rmi.RemoteException;
 public java.lang.Object getApplicationPK() throws java.rmi.RemoteException;
 public com.idega.user.data.User getChild() throws java.rmi.RemoteException;
 public java.lang.Object getChildPK() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getDepartment() throws java.rmi.RemoteException;
 public java.lang.Object getDepartmentPK() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClass getGroup() throws java.rmi.RemoteException;
 public java.lang.Object getGroupPK() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolStudyPath getInstrument() throws java.rmi.RemoteException;
 public java.lang.Object getInstrumentPK() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameApplicationID() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameChildID() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameDepartmentID() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameGroupID() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameInstrumentID() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameProviderID() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameSeasonID() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getProvider()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.Object getProviderPK() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getSeason() throws java.rmi.RemoteException;
 public java.lang.Object getSeasonPK() throws java.rmi.RemoteException;
 public void setApplication(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setChild(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setDepartment(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setGroup(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setInstrument(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setProvider(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setSeason(java.lang.Object p0) throws java.rmi.RemoteException;
}
