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
import se.idega.idegaweb.commune.school.music.business.MusicConstants;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressBMPBean;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class MusicSchoolChoiceBMPBean extends AbstractCaseBMPBean implements MusicSchoolChoice {

	public static final String ENTITY_NAME = "comm_music_choice";

	private final static String SCHOOL_SEASON = "school_season_id";
	private final static String SCHOOL_TYPE = "school_type_id";
	private final static String SCHOOL_YEAR = "school_year_id";
	private final static String OTHER_INSTRUMENT = "other_instrument";
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
	
	private final static String EXTRA_APPLICATION = "extra_application";

	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return MusicConstants.MUSIC_SCHOOL_CASE_CODE_KEY;
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

		addAttribute(OTHER_INSTRUMENT, "Other instrument", true, true, String.class);
		addAttribute(PREFERRED_PLACEMENT_DATE, "Preferred placement date", true, true, Date.class);
		addAttribute(CHOICE_DATE, "Choice date", Timestamp.class);
		addAttribute(MESSAGE, "Message", String.class, 4000);
		addAttribute(CHOICE_ORDER, "Choice order", Integer.class);
		addAttribute(TEACHER_REQUEST, "Request for a teacher", String.class);
		addAttribute(ELEMENTARY_SCHOOL, "Elementary school", String.class);

		addAttribute(PREVIOUS_STUDIES, "Previous studies", String.class, 4000);
		addManyToOneRelationship(CURRENT_YEAR, SchoolYear.class);
		addManyToOneRelationship(CURRENT_STUDY_PATH, SchoolStudyPath.class);

		addAttribute(PAYMENT_METHOD, "Payment method", Integer.class);
		
		addAttribute(EXTRA_APPLICATION, "Extra application", Boolean.class);

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
	
	public String getOtherInstrument() {
		return getStringColumnValue(OTHER_INSTRUMENT);
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

	public boolean isExtraApplication() {
		return getBooleanColumnValue(EXTRA_APPLICATION, false);
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
	
	public void setOtherInstrument(String otherInstrument) {
		setColumn(OTHER_INSTRUMENT, otherInstrument);
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
	
	public void setAsExtraApplication(boolean extraApplication) {
		setColumn(EXTRA_APPLICATION, extraApplication);
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
		return ejbFindAllByStatuses(null, school, null, null, null, statuses, null);
	}

	public Collection ejbFindAllByStatuses(School school, SchoolSeason season, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(null, school, season, null, null, statuses, null);
	}
	
	public Collection ejbFindAllByStatuses(School school, SchoolYear year, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(null, school, null, year, null, statuses, null);
	}

	public Collection ejbFindAllByStatuses(User child, School school, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(child, school, null, null, null, statuses, null);
	}

	public Integer ejbFindAllByStatuses(User child, School school, SchoolSeason season, String[] statuses) throws FinderException {
		SelectQuery query = getDefaultQuery(child, school, season, null, null, null, statuses, null);
		
		return (Integer) idoFindOnePKBySQL(query.toString());
	}

	public Collection ejbFindAllByStatuses(User child, School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String[] statuses, Boolean showExtraApplications) throws FinderException {
		SelectQuery query = getDefaultQuery(child, school, season, year, instrument, null, statuses, showExtraApplications);
		
		return idoFindPKsBySQL(query.toString());
	}

	public Integer ejbFindAllByChildAndChoiceNumberAndSeason(User child, int choiceNumber, SchoolSeason season) throws FinderException {
		return ejbFindAllByChildAndChoiceNumberAndSeason(child, choiceNumber, season, false);
	}
	
	public Integer ejbFindAllByChildAndChoiceNumberAndSeason(User child, int choiceNumber, SchoolSeason season, boolean showExtraApplications) throws FinderException {
		Table choice = new Table(this);
		
		SelectQuery query = new SelectQuery(choice);
		query.addColumn(new Column(choice, this.getIDColumnName()));
		query.addCriteria(new MatchCriteria(choice, CHILD, MatchCriteria.EQUALS, child));
		query.addCriteria(new MatchCriteria(choice, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(choice, CHOICE_ORDER, MatchCriteria.EQUALS, choiceNumber));
		if (showExtraApplications) {
			query.addCriteria(new MatchCriteria(choice, EXTRA_APPLICATION, MatchCriteria.EQUALS, true));
		}
		else {
			MatchCriteria isNull = new MatchCriteria(choice.getColumn(EXTRA_APPLICATION), false);
			MatchCriteria isFalse = new MatchCriteria(choice, EXTRA_APPLICATION, MatchCriteria.EQUALS, false);
			query.addCriteria(new OR(isNull, isFalse));
		}
		
		return (Integer) idoFindOnePKBySQL(query.toString());
	}

	public Collection ejbFindAllByStatuses(User child, String[] statuses) throws FinderException {
		return ejbFindAllByStatuses(child, null, null, null, null, statuses, null);
	}
	
	public Collection ejbFindAllByStatuses(User child, SchoolSeason season, String[] statuses, Boolean showExtraApplications) throws FinderException {
		return ejbFindAllByStatuses(child, season, null, statuses, showExtraApplications);
	}
	
	public Collection ejbFindAllByStatuses(User child, SchoolSeason season, SchoolYear year, String[] statuses, Boolean showExtraApplications) throws FinderException {
		return ejbFindAllByStatuses(child, null, season, year, null, statuses, showExtraApplications);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, null, null, null, null, statuses, null);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, SchoolSeason season, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, season, null, null, null, statuses, null);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, SchoolSeason season, String[] statuses, boolean extraApplication) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, season, null, null, null, statuses, new Boolean(extraApplication));
	}
	
	public int ejbHomeGetNumberOfApplications(User child, SchoolYear year, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(child, null, null, year, null, null, statuses, null);
	}
	
	public int ejbHomeGetNumberOfApplications(School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String types, String[] statuses) throws IDOException {
		return ejbHomeGetNumberOfApplications(null, school, season, year, instrument, types, statuses,  null);
	}
	
	public int ejbHomeGetNumberOfApplications(User child, School school, SchoolSeason season, SchoolYear year, SchoolStudyPath instrument, String types, String[] statuses, Boolean showExtraApplications) throws IDOException {
		try {
			SelectQuery query = getDefaultQuery(child, school, season, year, instrument, types, statuses, showExtraApplications);
			query.removeAllColumns();
			query.removeAllOrder();
			query.addColumn(new CountColumn(query.getBaseTable(), this.getIDColumnName()));
			
			return idoGetNumberOfRecords(query.toString());
		}
		catch (FinderException fe) {
			throw new IDOException(fe.getMessage());
		}
	}
	
	public int ejbHomeGetMusicChoiceStatistics(String status, SchoolSeason season, boolean firstChoiceOnly) throws IDOException {
		Table choice = new Table(this, "c");
		Table process = new Table(Case.class, "p");
		
		SelectQuery query = new SelectQuery(choice);
		query.addColumn(new CountColumn(choice, this.getIDColumnName()));
		try {
			query.addJoin(choice, process);
		}
		catch (IDORelationshipException ile) {
			throw new IDOException(ile.getMessage());
		}
		query.addCriteria(new MatchCriteria(process, "CASE_STATUS", MatchCriteria.NOTEQUALS, status));
		if (firstChoiceOnly) {
			query.addCriteria(new MatchCriteria(new Column(process, "PARENT_CASE_ID"), false));
		}
		if (season != null) {
			query.addCriteria(new MatchCriteria(choice, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		}

		return idoGetNumberOfRecords(query.toString());
	}
	
	private SelectQuery getDefaultQuery(User child, School school, SchoolSeason season, SchoolYear department, SchoolStudyPath instrument, String types, String[] statuses, Boolean showExtraApplications) throws FinderException {
		Table choice = new Table(this, "c");
		Table process = new Table(Case.class, "p");
		Table instruments = new Table(SchoolStudyPath.class, "sp");
		
		SelectQuery query = new SelectQuery(choice);
		query.addColumn(new Column(choice, this.getIDColumnName()));
		try {
			query.addJoin(choice, process);
		}
		catch (IDORelationshipException ile) {
			throw new FinderException(ile.getMessage());
		}
		if (instrument != null) {
			try {
				query.addManyToManyJoin(choice, instruments, "csp");
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
		if (types != null) {
			query.addCriteria(new InCriteria(choice, SCHOOL_TYPE, types));
		}
		if (showExtraApplications != null) {
			if (showExtraApplications.booleanValue()) {
				query.addCriteria(new MatchCriteria(choice, EXTRA_APPLICATION, MatchCriteria.EQUALS, true));
			}
			else {
				MatchCriteria isNull = new MatchCriteria(choice.getColumn(EXTRA_APPLICATION), false);
				MatchCriteria isFalse = new MatchCriteria(choice, EXTRA_APPLICATION, MatchCriteria.EQUALS, false);
				query.addCriteria(new OR(isNull, isFalse));
			}
		}
		
		query.addOrder(choice, CHOICE_DATE, true);
		
		return query;
	}

	public int ejbHomeGetApplicationCount(School school, SchoolSeason season, SchoolYear department, SchoolStudyPath instrument, String types, String[] statuses, int choiceNumber, Commune commune) throws IDOException {
		Table choice = new Table(this, "c");
		Table process = new Table(Case.class, "p");
		Table instruments = new Table(SchoolStudyPath.class, "sp");
		Table user = new Table(User.class, "u");
		Table address = new Table(Address.class, "a");
		Table postal = new Table(PostalCode.class, "pc");
		
		SelectQuery query = new SelectQuery(choice);
		query.addColumn(new CountColumn(choice, this.getIDColumnName()));
		try {
			query.addJoin(choice, process);
		}
		catch (IDORelationshipException ile) {
			throw new IDOException(ile.getMessage());
		}
		if (instrument != null) {
			try {
				query.addManyToManyJoin(choice, instruments, "csp");
			}
			catch (IDORelationshipException ile) {
				throw new IDOException(ile.getMessage());
			}
		}
		
		query.addCriteria(new InCriteria(process, "case_status", statuses));
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
				throw new IDOException(icpke.getMessage());
			}
		}
		if (types != null) {
			query.addCriteria(new InCriteria(choice, SCHOOL_TYPE, types));
		}
		if (choiceNumber > 0) {
			query.addCriteria(new MatchCriteria(choice, CHOICE_ORDER, MatchCriteria.EQUALS, choiceNumber));
		}
		if (commune != null) {
			try {
				query.addJoin(choice, user);
			}
			catch (IDORelationshipException ile) {
				throw new IDOException("Tables " + choice.getName() + " and " + user.getName() + " don't have a relation.");
			}
			try {
				query.addManyToManyJoin(user, address, "ua");
			}
			catch (IDORelationshipException ile) {
				throw new IDOException("Tables " + user.getName() + " and " + address.getName() + " don't have a relation.");
			}
			try {
				query.addJoin(address, postal);
			}
			catch (IDORelationshipException ile) {
				throw new IDOException("Tables " + address.getName() + " and " + postal.getName() + " don't have a relation.");
			}
			query.addCriteria(new MatchCriteria(address, AddressBMPBean.getColumnNameAddressTypeId(), MatchCriteria.EQUALS, 1));
			query.addCriteria(new MatchCriteria(postal, "ic_commune_id", MatchCriteria.EQUALS, commune));
		}
		
		Table student = new Table(SchoolClassMember.class);
		Table group = new Table(SchoolClass.class);
		
		SelectQuery inQuery = new SelectQuery(student);
		inQuery.addColumn(student, "ic_user_id");
		inQuery.addJoin(student, group);
		inQuery.addCriteria(new MatchCriteria(group, "school_id", MatchCriteria.EQUALS, school));
		
		query.addCriteria(new InCriteria(choice, CHILD, inQuery, true));
		
		return idoGetNumberOfRecords(query);
	}
}