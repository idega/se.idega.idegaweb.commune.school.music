package se.idega.idegaweb.commune.school.music.business;


public class MusicSchoolSessionHomeImpl extends com.idega.business.IBOHomeImpl implements MusicSchoolSessionHome
{
 protected Class getBeanInterfaceClass(){
  return MusicSchoolSession.class;
 }


 public MusicSchoolSession create() throws javax.ejb.CreateException{
  return (MusicSchoolSession) super.createIBO();
 }



}