/*
 * Created on 2.5.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.music.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class MusicSchoolChoiceBMPBean extends AbstractCaseBMPBean implements MusicSchoolChoice {

	public static final String ENTITY_NAME = "comm_music_choice";
	public static final String CASECODE = "MUSICCH";

	private final static String SCHOOL_SEASON = "school_season_id";
	private final static String SCHOOL_TYPE = "school_type_id";
	private final static String SCHOOL_YEAR = "school_year_id";
	private final static String SCHOOL = "school_id";
	private final static String CHILD = "child_id";
	private final static String PREFERRED_PLACEMENT_DATE = "placement_date";

	private final static String CHOICE_DATE = "school_choice_date";
	private final static String MESSAGE = "message_body";
	private final static String CHOICE_ORDER = "choice_order";
	private final static String TEACHER_REQUEST = "teacher_request";
	private final static String ELEMENTARY_SCHOOL = "elementary_school";
	private final static String PAYMENT_METHOD = "payment_method";

	private final static String PREVIOUS_STUDIES = "previous_studies";
	private final static String CURRENT_YEAR = "current_year";
	private final static String CURRENT_STUDY_PATH = "current_study_path";

	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CASECODE;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return "Music school application";
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addGeneralCaseRelation();

		addManyToOneRelationship(CHILD, User.class);
		addManyToOneRelationship(SCHOOL, School.class);
		addManyToOneRelationship(SCHOOL_TYPE, SchoolType.class);
		addManyToOneRelationship(SCHOOL_SEASON, SchoolSeason.class);
		addManyToOneRelationship(SCHOOL_YEAR, SchoolYear.class);

		addAttribute(PREFERRED_PLACEMENT_DATE, "Preferred placement date", true, true, Date.class);
		addAttribute(CHOICE_DATE, "Choice date", Timestamp.class);
		addAttribute(MESSAGE, "Message", String.class, 4000);
		addAttribute(CHOICE_ORDER, "Choice order", Integer.class);
		addAttribute(TEACHER_REQUEST, "Request for a teacher", String.class);
		addAttribute(ELEMENTARY_SCHOOL, "Elementary school", String.class);

		addAttribute(PREVIOUS_STUDIES, "Previous studies", String.class);
		addManyToOneRelationship(CURRENT_YEAR, SchoolYear.class);
		addManyToOneRelationship(CURRENT_STUDY_PATH, SchoolStudyPath.class);

		addAttribute(PAYMENT_METHOD, "Payment method", Integer.class);

		addManyToManyRelationShip(SchoolStudyPath.class, "comm_music_choice_study_path");
	}
	
	
	//Getters
	public User getChild() {
		return (User) getColumnValue(CHILD);
	}
	
	public Object getChildPK() {
		return getIntegerColumnValue(CHILD);
	}
	
	public School getSchool() {
		return (School) getColumnValue(SCHOOL);
	}
	
	public Object getSchoolPK() {
		return getIntegerColumnValue(SCHOOL);
	}
	
	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(SCHOOL_TYPE);
	}
	
	public Object getSchoolTypePK() {
		return getIntegerColumnValue(SCHOOL_TYPE);
	}
	
	public SchoolSeason getSchoolSeason() {
		return (SchoolSeason) getColumnValue(SCHOOL_SEASON);
	}
	
	public Object getSchoolSeasonPK() {
		return getIntegerColumnValue(SCHOOL_SEASON);
	}
	
	public SchoolYear getSchoolYear() {
		return (SchoolYear) getColumnValue(SCHOOL_YEAR);
	}
	
	public Object getSchoolYearPK() {
		return getIntegerColumnValue(SCHOOL_YEAR);
	}
	
	public Date getPlacementDate() {
		return getDateColumnValue(PREFERRED_PLACEMENT_DATE);
	}
	
	public Timestamp getChoiceDate() {
		return getTimestampColumnValue(CHOICE_DATE);
	}
	
	public int getChoiceNumber() {
		return getIntColumnValue(CHOICE_ORDER);
	}
	
	public String getMessage() {
		return getStringColumnValue(MESSAGE);
	}
	
	public String getTeacherRequest() {
		return getStringColumnValue(TEACHER_REQUEST);
	}

	public String getElementarySchool() {
		return getStringColumnValue(ELEMENTARY_SCHOOL);
	}

	public String getPreviousStudies() {
		return getStringColumnValue(PREVIOUS_STUDIES);
	}
	
	public SchoolYear getPreviousYear() {
		return (SchoolYear) getColumnValue(CURRENT_YEAR);
	}
	
	public Object getPreviousYearPK() {
		return getIntegerColumnValue(CURRENT_YEAR);
	}
	
	public SchoolStudyPath getPreviousStudyPath() {
		return (SchoolStudyPath) getColumnValue(CURRENT_STUDY_PATH);
	}
	
	public Object getPreviousStudyPathPK() {
		return getIntegerColumnValue(CURRENT_STUDY_PATH);
	}
	
	public int getPaymentMethod() {
		return getIntColumnValue(PAYMENT_METHOD);
	}
	
	public Collection getStudyPaths() throws IDORelationshipException {
		return idoGetRelatedEntities(SchoolStudyPath.class);
	}
	
	
	//Setters
	public void setChild(User child) {
		setColumn(CHILD, child);
	}
	
	public void setChild(Object childID) {
		setColumn(CHILD, childID);
	}
	
	public void setSchool(School school) {
		setColumn(SCHOOL, school);
	}
	
	public void setSchool(Object schoolID) {
		setColumn(SCHOOL, schoolID);
	}
	
	public void setSchoolSeason(SchoolSeason schoolSeason) {
		setColumn(SCHOOL_SEASON, schoolSeason);
	}
	
	public void setSchoolSeason(Object schoolSeasonID) {
		setColumn(SCHOOL_SEASON, schoolSeasonID);
	}
	
	public void setSchoolType(SchoolType schoolType) {
		setColumn(SCHOOL_TYPE, schoolType);
	}
	
	public void setSchoolType(Object schoolTypeID) {
		setColumn(SCHOOL_TYPE, schoolTypeID);
	}
	
	public void setSchoolYear(SchoolYear schoolYear) {
		setColumn(SCHOOL_YEAR, schoolYear);
	}
	
	public void setSchoolYear(Object schoolYearID) {
		setColumn(SCHOOL_YEAR, schoolYearID);
	}
	
	public void setPlacementDate(Date placementDate) {
		setColumn(PREFERRED_PLACEMENT_DATE, placementDate);
	}
	
	public void setChoiceDate(Timestamp choiceDate) {
		setColumn(CHOICE_DATE, choiceDate);
	}
	
	public void setChoiceNumber(int choiceNumber) {
		setColumn(CHOICE_ORDER, choiceNumber);
	}
	
	public void setMessage(String message) {
		setColumn(MESSAGE, message);
	}
	
	public void setTeacherRequest(String teacherRequest) {
		setColumn(TEACHER_REQUEST, teacherRequest);
	}

	public void setElementarySchool(String elementarySchool) {
		setColumn(ELEMENTARY_SCHOOL, elementarySchool);
	}

	public void setPreviousStudies(String previousStudies) {
		setColumn(PREVIOUS_STUDIES, previousStudies);
	}
	
	public void setPreviousYear(SchoolYear previousYear) {
		setColumn(CURRENT_YEAR, previousYear);
	}
	
	public void setPreviousYear(Object previousYearID) {
		setColumn(CURRENT_YEAR, previousYearID);
	}
	
	public void setPreviousStudyPath(SchoolStudyPath studyPath) {
		setColumn(CURRENT_STUDY_PATH, studyPath);
	}
	
	public void setPreviousStudyPath(Object studyPathID) {
		setColumn(CURRENT_STUDY_PATH, studyPathID);
	}
	
	public void setPaymentMethod(int paymentMethod) {
		setColumn(PAYMENT_METHOD, paymentMethod);
	}
	
	public void addStudyPaths(Object[] studyPathIDs) throws IDOAddRelationshipException {
		for (int i = 0; i < studyPathIDs.length; i++) {
			Object object = studyPathIDs[i];
			idoAddTo(SchoolStudyPath.class, object);
		}
	}
	
	public void addStudyPaths(Collection studyPaths) throws IDOAddRelationshipException {
		Iterator iter = studyPaths.iterator();
		while (iter.hasNext()) {
			IDOEntity element = (IDOEntity) iter.next();
			idoAddTo(element);
		}
	}
	
	public void addStudyPath(SchoolStudyPath studyPath) throws IDOAddRelationshipException {
		idoAddTo(studyPath);
	}
	
	public void removeStudyPaths() throws IDORemoveRelationshipException {
		idoRemoveFrom(SchoolStudyPath.class);
	}
	
	public void removeStudyPath(SchoolStudyPath studyPath) throws IDORemoveRelationshipException {
		idoRemoveFrom(studyPath);
	}
	
	
	//Find methods
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, this.getIDColumnName()));
		
		return idoFindPKsBySQL(query.toString());
	}
	
	public Collection ejbFindAllByChild(User child) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, this.getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, CHILD, MatchCriteria.EQUALS, child));
		
		return idoFindPKsBySQL(query.toString());
	}
	
	public Collection ejbFindAllByStatuses(School school, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(null, school, null, null, null, statuses);
	}

	public Collection ejbFindAllByStatuses(School school, SchoolSeason season, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(null, school, season, null, null, statuses);
	}
	
	public Collection ejbFindAllByStatuses(School school, SchoolYear year, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(null, school, null, year, null, statuses);
	}

	public Collection ejbFindAllByStatuses(User child, School school, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(child, school, null, null, null, statuses);
	}

	public Integer ejbFindAllByStatuses(User child, School school, SchoolSeason season, String[] statuses) throws FinderException {
		SelectQuery query = getDefaultQuery(child, school, season, null, null, statuses);
		
		return (Integer) idoFindOnePKBySQL(query.toString());
	}

	public Collection ejbFindAllByStatuses(User child, School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String[] statuses) throws FinderException {
		SelectQuery query = getDefaultQuery(child, school, season, year, instrument, statuses);
		
		return idoFindPKsBySQL(query.toString());
	}
	
	public Integer ejbFindAllByChildAndChoiceNumberAndSeason(User child, int choiceNumber, SchoolSeason season) throws FinderException {
		Table choice = new Table(this);
		
		SelectQuery query = new SelectQuery(choice);
		query.addColumn(new Column(choice, this.getIDColumnName()));
		query.addCriteria(new MatchCriteria(choice, CHILD, MatchCriteria.EQUALS, child));
		query.addCriteria(new MatchCriteria(choice, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(choice, CHOICE_ORDER, MatchCriteria.EQUALS, choiceNumber));
		
		return (Integer) idoFindOnePKBySQL(query.toString());
	}

	public Collection ejbFindAllByStatuses(User child, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(child, null, null, null, null, statuses);
	}
	
	public Collection ejbFindAllByStatuses(User child, SchoolSeason season, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(child, season, null, statuses);
	}
	
	public Collection ejbFindAllByStatuses(User child, SchoolSeason season, SchoolYear year, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(child, null, season, year, null, statuses);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, null, null, null, statuses);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, SchoolSeason season, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, season, null, null, statuses);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, SchoolYear year, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, null, year, null, statuses);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String[] statuses) throws IDOException {
		try {
			SelectQuery query = getDefaultQuery(child, school, season, year, instrument, statuses);
			query.setAsCountQuery(true);
			
			return idoGetNumberOfRecords(query.toString());
		}
		catch (FinderException fe) {
			throw new IDOException(fe.getMessage());
		}
	}
	
	private SelectQuery getDefaultQuery(User child, School school, SchoolSeason season, SchoolYear department, SchoolStudyPath instrument, String[] statuses) throws FinderException {
		Table choice = new Table(this);
		Table process = new Table(Case.class);
		Table instruments = new Table(SchoolStudyPath.class);
		
		SelectQuery query = new SelectQuery(choice);
		query.addColumn(new Column(choice, this.getIDColumnName()));
		try {
			query.addManyToManyJoin(choice, process);
		}
		catch (IDORelationshipException ile) {
			throw new FinderException(ile.getMessage());
		}
		if (instrument != null) {
			try {
				query.addJoin(choice, instruments);
			}
			catch (IDORelationshipException ile) {
				throw new FinderException(ile.getMessage());
			}
		}
		
		query.addCriteria(new InCriteria(process, "case_status", statuses));
		if (child != null) {
			query.addCriteria(new MatchCriteria(choice, CHILD, MatchCriteria.EQUALS, child));
		}
		if (school != null) {
			query.addCriteria(new MatchCriteria(choice, SCHOOL, MatchCriteria.EQUALS, school));
		}
		if (season != null) {
			query.addCriteria(new MatchCriteria(choice, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		}
		if (department != null) {
			query.addCriteria(new MatchCriteria(choice, SCHOOL_YEAR, MatchCriteria.EQUALS, department));
		}
		if (instrument != null) {
			try {
				query.addCriteria(new MatchCriteria(instruments, instruments.getPrimaryKeyColumnName(), MatchCriteria.EQUALS, instrument));
			}
			catch (IDOCompositePrimaryKeyException icpke) {
				throw new FinderException(icpke.getMessage());
			}
		}
		
		return query;
	}
}