package se.idega.idegaweb.commune.school.music.business;

import com.idega.block.school.data.SchoolSeason;


public interface MusicSchoolReport extends com.idega.business.IBOSession
{
 public static final String PROPERTY_SINGING_STUDY_PATH_ID = "singing_study_path_id";
 public static final String PROPERTY_TYPE_IDS = "type_ids";
 
 public com.idega.block.datareport.util.ReportableCollection getChoicesReport(SchoolSeason season) throws java.rmi.RemoteException;
}
