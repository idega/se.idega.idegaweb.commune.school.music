package se.idega.idegaweb.commune.school.music.business;


public class MusicSchoolReportHomeImpl extends com.idega.business.IBOHomeImpl implements MusicSchoolReportHome
{
 protected Class getBeanInterfaceClass(){
  return MusicSchoolReport.class;
 }


 public MusicSchoolReport create() throws javax.ejb.CreateException{
  return (MusicSchoolReport) super.createIBO();
 }



}