package se.idega.idegaweb.commune.school.music.data;


public interface MusicSchoolChoiceHome extends com.idega.data.IDOHome
{
 public MusicSchoolChoice create() throws javax.ejb.CreateException;
 public MusicSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findAllByChild(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public MusicSchoolChoice findAllByChildAndChoiceNumberAndSeason(com.idega.user.data.User p0,int p1,com.idega.block.school.data.SchoolSeason p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.block.school.data.School p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolYear p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.School p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public MusicSchoolChoice findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolSeason p2,java.lang.String[] p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolSeason p2,com.idega.block.school.data.SchoolYear p3,com.idega.block.school.data.SchoolStudyPath p4,java.lang.String[] p5)throws javax.ejb.FinderException;
 public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,java.lang.String[] p3)throws javax.ejb.FinderException;
 public int getNumberOfApplications(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1,java.lang.String[] p2)throws com.idega.data.IDOException;
 public int getNumberOfApplications(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3,java.lang.String p4,java.lang.String[] p5)throws com.idega.data.IDOException;
 public int getNumberOfApplications(com.idega.user.data.User p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getNumberOfApplications(com.idega.user.data.User p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolSeason p2,com.idega.block.school.data.SchoolYear p3,com.idega.block.school.data.SchoolStudyPath p4,java.lang.String p5,java.lang.String[] p6)throws com.idega.data.IDOException;
 public int getNumberOfApplications(com.idega.user.data.User p0,com.idega.block.school.data.SchoolYear p1,java.lang.String[] p2)throws com.idega.data.IDOException;

}