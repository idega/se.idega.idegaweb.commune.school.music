package se.idega.idegaweb.commune.school.music.data;


public class MusicSchoolChoiceHomeImpl extends com.idega.data.IDOFactory implements MusicSchoolChoiceHome
{
 protected Class getEntityInterfaceClass(){
  return MusicSchoolChoice.class;
 }


 public MusicSchoolChoice create() throws javax.ejb.CreateException{
  return (MusicSchoolChoice) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByChild(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public MusicSchoolChoice findAllByChildAndChoiceNumberAndSeason(com.idega.user.data.User p0,int p1,com.idega.block.school.data.SchoolSeason p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByChildAndChoiceNumberAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByStatuses(com.idega.block.school.data.School p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByStatuses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByStatuses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolYear p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolSeason p2,com.idega.block.school.data.SchoolYear p3,com.idega.block.school.data.SchoolStudyPath p4,java.lang.String[] p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByStatuses(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,java.lang.String[] p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MusicSchoolChoiceBMPBean)entity).ejbFindAllByStatuses(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public MusicSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MusicSchoolChoice) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfApplications(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((MusicSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(com.idega.user.data.User p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((MusicSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(com.idega.user.data.User p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolSeason p2,com.idega.block.school.data.SchoolYear p3,com.idega.block.school.data.SchoolStudyPath p4,java.lang.String[] p5)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((MusicSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(com.idega.user.data.User p0,com.idega.block.school.data.SchoolYear p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((MusicSchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}