package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOAddRelationshipException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author <a href="mailto:laddi@idega.is">Laddi</a>
 */
public class MusicSchoolStudentImportFileHandlerBean extends IBOServiceBean implements ImportFileHandler , MusicSchoolStudentImportFileHandler{

	private UserTransaction transaction;
	private MusicSchoolBusiness schoolBiz;
	private SchoolHome sHome;
	private SchoolType fullStudy;
	private SchoolType halfStudy;
	private SchoolSeason schoolSeason;
	private ImportFile file;
	private ArrayList failedRecords = new ArrayList();

	public MusicSchoolStudentImportFileHandlerBean() {
	}

	public boolean handleRecords() throws RemoteException {
		transaction = this.getSessionContext().getUserTransaction();
		try {
			schoolBiz = (MusicSchoolBusiness) this.getServiceInstance(MusicSchoolBusiness.class);
			sHome = schoolBiz.getSchoolBusiness().getSchoolHome();
			fullStudy = getFullTimeStudySchoolType(schoolBiz.getSchoolBusiness());
			halfStudy = getHalfTimeStudySchoolType(schoolBiz.getSchoolBusiness());
			SchoolSeasonHome schoolSeasonHome = schoolBiz.getSchoolBusiness().getSchoolSeasonHome();
			schoolSeason = schoolSeasonHome.findSeasonByDate(new IWTimestamp().getDate());
			transaction.begin();
			String item;
			int count = 1;
			while (!(item = (String) file.getNextRecord()).equals("")) {
				if (!processRecord(item)) {
					failedRecords.add(item);
				}
				else {
					System.out.println("Processed record number: " + (count++));
				}
			}
			transaction.commit();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * The record is: nr,musicSchoolName,studentSSN,studentTelNr,preSchool,
	 * instrument,singing,level,levelNr, instrument2,level2Nr,level2,
	 * instument3,level3Nr,level3
	 */
	private boolean processRecord(String record) throws RemoteException {
		// data elements from file
		int index = 1;
		String studentSSN = file.getValueAtIndexFromRecordString(index++, record);
		String studentName = file.getValueAtIndexFromRecordString(index++, record);
		index++;
		index++;
//		String address = file.getValueAtIndexFromRecordString(index++, record);
//		String postalCode = file.getValueAtIndexFromRecordString(index++, record);
		String musicSchoolName = file.getValueAtIndexFromRecordString(index++, record);
		String level = file.getValueAtIndexFromRecordString(index++, record);
		String instrument = file.getValueAtIndexFromRecordString(index++, record);
		boolean success = storeInfo(studentSSN, studentName, musicSchoolName, level, instrument);
		return success;
	}

	protected boolean storeInfo(String studentSSN, String studentName, String musicSchoolName, String level,
			String instrument) throws RemoteException {
		School musicSchool = null;
		SchoolClass mainClass = null;
		try {
			musicSchool = sHome.findBySchoolName(musicSchoolName);
		}
		catch (FinderException e) {
			try {
				musicSchool = sHome.create();
				musicSchool.setSchoolName(musicSchoolName);
				musicSchool.store();
				musicSchool.addSchoolType(fullStudy);
				musicSchool.addSchoolType(halfStudy);
			}
			catch (CreateException ce) {
				musicSchool = null;
			}
			catch (IDOAddRelationshipException idoe) {
			}
		}
		mainClass = schoolBiz.getDefaultGroup(musicSchool, schoolSeason);
		processStudent(studentSSN, studentName, schoolSeason, mainClass, level, instrument);
		return true;
	}

	/**
	 * Processes the music school student info. Gets a user by ssn from ic_user
	 * table, creates a new student (SchoolClassMember), stores the students phone
	 * number in the ic_user table, connects SchoolClass to the student (the
	 * schoolSeason is accessable through the schoolClass).
	 * 
	 * @throws RemoteException
	 */
	private void processStudent(String studentSSN, String studentName, SchoolSeason schoolSeason, SchoolClass mainClass,
			String level, String instrumentCode) throws RemoteException {
		UserBusiness userBiz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
		User studentUser = null;
		SchoolStudyPath instrument = null;
		try {
			instrument = schoolBiz.getSchoolBusiness().getSchoolStudyPathHome().findByCode(instrumentCode);
		}
		catch (FinderException fEx) {
			fEx.printStackTrace();
			return;
		}
		SchoolYear schoolYear = null;
		try {
			schoolYear = schoolBiz.getSchoolBusiness().getSchoolYearHome().findByYearName(
					schoolBiz.getSchoolBusiness().getCategoryMusicSchool(), level);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return;
		}
		try {
			studentUser = userBiz.getUser(studentSSN);
		}
		catch (FinderException ce) {
			try {
				studentUser = userBiz.createUserByPersonalIDIfDoesNotExist(studentName, studentSSN, null, null);
			}
			catch (CreateException crEx) {
				studentUser = null;
			}
		}
		if (studentUser != null) {
			schoolBiz.addStudentToGroup(studentUser, mainClass, schoolYear, fullStudy, instrument, null, null,
					new IWTimestamp(schoolSeason.getSchoolSeasonStart()), null);
		}
	}

	private SchoolType getHalfTimeStudySchoolType(SchoolBusiness schoolBiz) {
		String schoolTypeKey = "sch_type.music_school_half_time_study";
		return getSchoolType(schoolBiz, schoolTypeKey, "MusicSchHalf");
	}

	private SchoolType getFullTimeStudySchoolType(SchoolBusiness schoolBiz) {
		String schoolTypeKey = "sch_type.music_school_full_time_study";
		return getSchoolType(schoolBiz, schoolTypeKey, "MusicSchFull");
	}

	/**
	 * If school type with schoolTypeKey does not exist it is created
	 * 
	 * @param schoolBiz
	 * @param schoolTypeKey
	 * @param SchoolTypeName
	 * @return the school type -
	 */
	private SchoolType getSchoolType(SchoolBusiness schoolBiz, String schoolTypeKey, String SchoolTypeName) {
		SchoolType type = null;
		try {
			SchoolTypeHome stHome = schoolBiz.getSchoolTypeHome();
			try {
				type = stHome.findByTypeKey(schoolTypeKey);
			}
			catch (FinderException fe) {
				try {
					type = stHome.create();
					type.setLocalizationKey(schoolTypeKey);
					type.setSchoolTypeName(SchoolTypeName);
					type.setCategory(schoolBiz.getCategoryMusicSchool());
					type.store();
				}
				catch (Exception e) {
					throw new IBORuntimeException(e);
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		return type;
	}

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return failedRecords;
	}
}
