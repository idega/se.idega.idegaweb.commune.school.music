package se.idega.idegaweb.commune.school.music.data;


public interface MusicSchoolChoice extends com.idega.block.process.data.Case
{
 public void addStudyPath(com.idega.block.school.data.SchoolStudyPath p0)throws com.idega.data.IDOAddRelationshipException;
 public void addStudyPaths(java.lang.Object[] p0)throws com.idega.data.IDOAddRelationshipException;
 public void addStudyPaths(java.util.Collection p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public com.idega.user.data.User getChild();
 public java.lang.Object getChildPK();
 public java.sql.Timestamp getChoiceDate();
 public int getChoiceNumber();
 public java.lang.String getElementarySchool();
 public java.lang.String getMessage();
 public int getPaymentMethod();
 public java.sql.Date getPlacementDate();
 public java.lang.String getPreviousStudies();
 public com.idega.block.school.data.SchoolStudyPath getPreviousStudyPath();
 public java.lang.Object getPreviousStudyPathPK();
 public com.idega.block.school.data.SchoolYear getPreviousYear();
 public java.lang.Object getPreviousYearPK();
 public com.idega.block.school.data.School getSchool();
 public java.lang.Object getSchoolPK();
 public com.idega.block.school.data.SchoolSeason getSchoolSeason();
 public java.lang.Object getSchoolSeasonPK();
 public com.idega.block.school.data.SchoolType getSchoolType();
 public java.lang.Object getSchoolTypePK();
 public com.idega.block.school.data.SchoolYear getSchoolYear();
 public java.lang.Object getSchoolYearPK();
 public java.util.Collection getStudyPaths()throws com.idega.data.IDORelationshipException;
 public java.lang.String getTeacherRequest();
 public void removeStudyPath(com.idega.block.school.data.SchoolStudyPath p0)throws com.idega.data.IDORemoveRelationshipException;
 public void removeStudyPaths()throws com.idega.data.IDORemoveRelationshipException;
 public void setChild(com.idega.user.data.User p0);
 public void setChild(java.lang.Object p0);
 public void setChoiceDate(java.sql.Timestamp p0);
 public void setChoiceNumber(int p0);
 public void setElementarySchool(java.lang.String p0);
 public void setMessage(java.lang.String p0);
 public void setPaymentMethod(int p0);
 public void setPlacementDate(java.sql.Date p0);
 public void setPreviousStudies(java.lang.String p0);
 public void setPreviousStudyPath(com.idega.block.school.data.SchoolStudyPath p0);
 public void setPreviousStudyPath(java.lang.Object p0);
 public void setPreviousYear(com.idega.block.school.data.SchoolYear p0);
 public void setPreviousYear(java.lang.Object p0);
 public void setSchool(com.idega.block.school.data.School p0);
 public void setSchool(java.lang.Object p0);
 public void setSchoolSeason(java.lang.Object p0);
 public void setSchoolSeason(com.idega.block.school.data.SchoolSeason p0);
 public void setSchoolType(java.lang.Object p0);
 public void setSchoolType(com.idega.block.school.data.SchoolType p0);
 public void setSchoolYear(com.idega.block.school.data.SchoolYear p0);
 public void setSchoolYear(java.lang.Object p0);
 public void setTeacherRequest(java.lang.String p0);
}
