package se.idega.idegaweb.commune.school.music.business;


public class MusicSchoolBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements MusicSchoolBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return MusicSchoolBusiness.class;
 }


 public MusicSchoolBusiness create() throws javax.ejb.CreateException{
  return (MusicSchoolBusiness) super.createIBO();
 }



}