/*
 * Created on 3.5.2004
 */
package se.idega.idegaweb.commune.school.music.business;

import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoice;
import se.idega.idegaweb.commune.school.music.data.MusicSchoolChoiceHome;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class MusicSchoolBusinessBean extends CaseBusinessBean implements MusicSchoolBusiness {
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school.music";
	private final static String CASE_CODE_KEY = "MUSICCH";

	private MusicSchoolChoiceHome getMusicSchoolChoiceHome() {
		try {
			return (MusicSchoolChoiceHome) IDOLookup.getHome(MusicSchoolChoice.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	private SchoolCommuneBusiness getSchoolCommuneBusiness() {
		try {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolCommuneBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public MessageBusiness getMessageBusiness() {
		try {
			return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public MusicSchoolChoice findMusicSchoolChoice(Object primaryKey) throws FinderException {
		return getMusicSchoolChoiceHome().findByPrimaryKey(primaryKey);
	}
	
	public Collection findChoicesByChildAndSeason(User child, SchoolSeason season) throws FinderException {
		String[] statuses = { getCaseStatusPreliminary().getStatus(), getCaseStatusInactive().getStatus() };
		return getMusicSchoolChoiceHome().findAllByStatuses(child, season, statuses);
	}
	
	public Collection findAllMusicSchools() throws FinderException {
		try {
			return getSchoolBusiness().getSchoolHome().findAllByCategory(getSchoolBusiness().getCategoryMusicSchool());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findAllInstruments() throws FinderException {
		try {
			Collection instruments = getSchoolBusiness().getSchoolStudyPathHome().findBySchoolCategory(getSchoolBusiness().getCategoryMusicSchool());
			if (instruments.isEmpty()) {
				throw new FinderException("No instruments installed.");
			}
			return instruments;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findAllDepartments() throws FinderException {
		try {
			Collection departments = getSchoolBusiness().getSchoolYearHome().findAllSchoolYearBySchoolCategory(getSchoolBusiness().getCategoryMusicSchool());
			if (departments.isEmpty()) {
				throw new FinderException("No departments installed.");
			}
			return departments;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findAllSelectableDepartments() throws FinderException {
		try {
			Collection departments = getSchoolBusiness().getSchoolYearHome().findAllSchoolYearsBySchoolCategory(getSchoolBusiness().getCategoryMusicSchool(), true);
			if (departments.isEmpty()) {
				throw new FinderException("No departments installed.");
			}
			return departments;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findAllTypes() throws FinderException {
		try {
			Collection types = getSchoolBusiness().getSchoolTypeHome().findAllByCategory(getSchoolBusiness().getCategoryMusicSchool().getCategory());
			if (types.isEmpty()) {
				throw new FinderException("No types installed.");
			}
			return types;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findInstrumentsInSchool(School school) throws FinderException {
		try {
			return getSchoolBusiness().getSchoolStudyPathHome().findBySchoolAndSchoolCategory(school, getSchoolBusiness().getCategoryMusicSchool());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findDepartmentsInSchool(School school) throws FinderException {
		try {
			return getSchoolBusiness().getSchoolYearHome().findAllBySchoolAndSchoolCategory(school, getSchoolBusiness().getCategoryMusicSchool(), true);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findGroupsInSchool(School school, SchoolSeason season, SchoolYear department, SchoolStudyPath instrument) throws FinderException {
		try {
			return getSchoolBusiness().getSchoolClassHome().findBySchoolAndSeasonAndYearAndStudyPath(school, season, department, instrument, false);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection findChoicesInSchool(School school, SchoolSeason season, SchoolYear department, SchoolStudyPath instrument) throws FinderException {
		String[] statuses = { getCaseStatusPreliminary().getStatus(), getCaseStatusPlaced().getStatus() };
		return getMusicSchoolChoiceHome().findAllByStatuses(null, school, season, department, instrument, statuses);
	}
	
	public Collection findPendingChoicesInSchool(School school, SchoolSeason season, SchoolYear department, SchoolStudyPath instrument) throws FinderException {
		String[] statuses = { getCaseStatusPending().getStatus() };
		return getMusicSchoolChoiceHome().findAllByStatuses(null, school, season, department, instrument, statuses);
	}
	
	public Map getInstrumentSchoolMap(Locale locale) {
		try {
			Collection instruments = findAllInstruments();
			return getInstrumentSchoolMap(instruments, locale);
		}
		catch (FinderException fe) {
			log(fe);
			return new HashMap();
		}
	}
	
	public Map getInstrumentSchoolMap(Collection instruments, Locale locale) {
		Map map = new HashMap();

		Iterator iter = instruments.iterator();
		while (iter.hasNext()) {
			SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
			try {
				List schools = new ArrayList(instrument.getSchools());
				Collections.sort(schools, new SchoolComparator(locale));
				map.put(instrument.getPrimaryKey().toString(), schools);
			}
			catch (IDORelationshipException ire) {
				log(ire);
			}
		}
		
		return map;
	}

	public boolean saveChoices(User user, User child, Object[] schools, Object seasonPK, Object departmentPK, Object lessonTypePK, Object[] instruments, String teacherRequest, String message, Object currentYear, Object currentInstrument, String previousStudy, String elementarySchool, int paymentMethod) throws IDOCreateException {
		int caseCount = 3;
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			Collection instrumentsCollection = new ArrayList();
			for (int i = 0; i < instruments.length; i++) {
				instrumentsCollection.add(getSchoolBusiness().getSchoolStudyPathHome().findByPrimaryKey(new Integer(instruments[i].toString())));
			}
			
			CaseStatus first = super.getCaseStatusPreliminary();
			CaseStatus other = super.getCaseStatusInactive();
			CaseStatus status = null;
			MusicSchoolChoice choice = null;
			int choiceNumber = 1;
			
			IWTimestamp timeNow = new IWTimestamp();
			for (int i = 0; i < caseCount; i++) {
				if (schools[i] != null && Integer.parseInt(schools[i].toString()) > 0) {
					if (i == 0) {
						status = first;
					}
					else {
						status = other;
					}
					
					choice = saveChoice(user, child, schools[i], seasonPK, departmentPK, lessonTypePK, instrumentsCollection, teacherRequest, message, currentYear, currentInstrument, previousStudy, elementarySchool, paymentMethod, status, choice, choiceNumber++, timeNow);
				}
			}
			trans.commit();

			return true;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	private MusicSchoolChoice saveChoice(User user, User child, Object schoolPK, Object seasonPK, Object departmentPK, Object lessonTypePK, Collection instruments, String teacherRequest, String message, Object currentYear, Object currentInstrument, String previousStudy, String elementarySchool, int paymentMethod, CaseStatus status, Case parentCase, int choiceNumber, IWTimestamp stamp) throws IDOCreateException {
		SchoolSeason season = null;
		try {
			season = getSchoolBusiness().getSchoolSeason(new Integer(seasonPK.toString()));
		}
		catch (RemoteException fe) {
			season = null;
		}
		MusicSchoolChoice choice = null;
		stamp.addSeconds(1 - choiceNumber);

		if (season != null) {
			try {
				choice = getMusicSchoolChoiceHome().findAllByChildAndChoiceNumberAndSeason(child, choiceNumber, season);
				try {
					choice.removeStudyPaths();
				}
				catch (IDORemoveRelationshipException irre) {
					throw new IDOCreateException(irre);
				}
			}
			catch (FinderException fex) {
				choice = null;
			}
		}
		if (choice == null) {
			try {
				choice = getMusicSchoolChoiceHome().create();
			}
			catch (CreateException ce) {
				throw new IDOCreateException(ce);
			}
		}
		choice.setOwner(user);
		choice.setChild(child);
		choice.setSchool(schoolPK);
		choice.setSchoolSeason(season);
		choice.setSchoolType(lessonTypePK);
		choice.setSchoolYear(departmentPK);
		choice.setChoiceNumber(choiceNumber);
		choice.setTeacherRequest(teacherRequest);
		choice.setElementarySchool(elementarySchool);
		choice.setChoiceDate(stamp.getTimestamp());
		choice.setPaymentMethod(paymentMethod);
		choice.setPlacementDate(season.getSchoolSeasonStart());
		choice.setPreviousStudies(previousStudy);
		choice.setPreviousStudyPath(currentInstrument);
		choice.setPreviousYear(currentYear);
		choice.setMessage(message);
		
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(status);
		choice.setParentCase(parentCase);
		
		try {
			choice.store();
			Iterator iter = instruments.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath instrument = (SchoolStudyPath) iter.next();
				try {
					choice.addStudyPath(instrument);
				}
				catch (IDOAddRelationshipException iare) {
					throw new IDOCreateException(iare);
				}
			}
		}
		catch (IDOStoreException idos) {
			idos.printStackTrace();
			throw new IDOCreateException(idos);
		}

		if (status.equals(getCaseStatusPreliminary())) {
			String subject = getLocalizedString("music_school.choice_received_subject", "Music school choice received");
			String body = getLocalizedString("music_school.choice_received_body", "{1} has received the application for a music school placing for {0}, {2}.  The application will be handled as soon as possible.");
			sendMessageToParents(choice, subject, body);
		}

		return choice;
	}
	
	public boolean addStudentsToGroup(String[] choiceIDs, SchoolClass group, SchoolYear department, SchoolStudyPath instrument, User performer) {
		IWTimestamp stamp = new IWTimestamp();
		for (int i = 0; i < choiceIDs.length; i++) {
			try {
				MusicSchoolChoice choice = findMusicSchoolChoice(choiceIDs[i]);
				changeCaseStatus(choice, getCaseStatusPlaced().getStatus(), performer);
				
				try {
					SchoolClassMember student = getSchoolBusiness().getSchoolClassMemberHome().create();
					student.setStudent(choice.getChild());
					student.setSchoolType(choice.getSchoolType());
					student.setSchoolClass(group);
					student.setSchoolYear(department);
					student.setStudyPath(instrument);
					student.setNotes(choice.getMessage());
					student.setRegisterDate(stamp.getTimestamp());
					student.store();
				}
				catch (CreateException ce) {
					log(ce);
					return false;
				}
			}
			catch (FinderException fe) {
				log(fe);
				return false;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return true;
	}
	
	public boolean removeChoiceFromGroup(Object studentPK, User performer) {
		try {
			SchoolClassMember student = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(new Integer(studentPK.toString()));
			SchoolClass group = student.getSchoolClass();
			User user = student.getStudent();
			School school = group.getSchool();
			SchoolSeason season = group.getSchoolSeason();
			
			if (!isPlacedInSchool(user, school, season, null)) {
				String[] statuses = { getCaseStatusPlaced().getStatus() };
				MusicSchoolChoice choice = getMusicSchoolChoiceHome().findAllByStatuses(user, school, season, statuses);
				changeCaseStatus(choice, getCaseStatusPreliminary().getStatus(), performer);
			}
			
			try {
				student.remove();
			}
			catch (RemoveException re) {
				log(re);
				return false;
			}
			
			return true;
		}
		catch (FinderException fe) {
			log(fe);
			return false;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean rejectApplication(Object applicationPK, User performer) {
		try {
			MusicSchoolChoice application = findMusicSchoolChoice(applicationPK);
			Iterator iter = application.getChildren();
			boolean hasChildrenApplications = false;
			while (iter.hasNext()) {
				Case theCase = (Case) iter.next();
				if (theCase.getCaseCode().equals(application.getCaseCode())) {
					String subject = getLocalizedString("music_school.choice_received_subject", "Music school choice received");
					String body = getLocalizedString("music_school.choice_received_body", "{1} has received the application for a music school placing for {0}, {2}.  The application will be handled as soon as possible.");
					sendMessageToParents((MusicSchoolChoice)theCase, subject, body);
					changeCaseStatus(theCase, getCaseStatusPreliminary().getStatus(), performer);
					hasChildrenApplications = true;
				}
			}
			if (!hasChildrenApplications && application.getChoiceNumber() == 1) {
				String subject = getLocalizedString("music_school.choice_pending_subject", "Music school choice added to waiting list");
				String body = getLocalizedString("music_school.choice_pending_body", "{1} has added the application for a music school placing for {0}, {2}, to our waiting list.  If a placement becomes available you may get an offer for placement.");
				sendMessageToParents(application, subject, body);
				changeCaseStatus(application, getCaseStatusPending().getStatus(), performer);
			}
			else {
				String subject = getLocalizedString("music_school.choice_rejected_subject", "Music school choice rejected");
				String body = getLocalizedString("music_school.choice_rejected_body", "{1} has rejected the application for a music school placing for {0}, {2}.");
				sendMessageToParents(application, subject, body);
				changeCaseStatus(application, getCaseStatusDenied().getStatus(), performer);
			}
			return true;
		}
		catch (FinderException fe) {
			log(fe);
			return false;
		}
	}
	
	public void reactivateApplication(Object applicationPK, User performer) {
		try {
			MusicSchoolChoice application = findMusicSchoolChoice(applicationPK);
			String subject = getLocalizedString("music_school.choice_reactivated_subject", "Music school choice reactivated");
			String body = getLocalizedString("music_school.choice_reactivated_body", "{1} has reactivated the application for a music school placing for {0}, {2}.");
			sendMessageToParents(application, subject, body);
			changeCaseStatus(application, getCaseStatusPreliminary().getStatus(), performer);
		}
		catch (FinderException fe) {
			log(fe);
		}
	}
	
	public boolean isPlacedInSchool(User student, School school, SchoolSeason season, SchoolStudyPath instrument) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().countByUserAndSchoolAndSeasonAndStudyPath(student, school, season, instrument) > 0;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (IDOException ie) {
			return false;
		}
	}
	
	public boolean hasGrantedApplication(User student, SchoolSeason season) {
		try {
			String[] statuses = { getCaseStatusPlaced().getStatus() };
			return getMusicSchoolChoiceHome().getNumberOfApplications(student, season, statuses) > 0;
		}
		catch (IDOException ie) {
			return false;
		}
	}
	
	private void sendMessageToParents(MusicSchoolChoice choice, String subject, String body) {
		sendMessageToParents(choice, subject, body, body, false);
	}

	private void sendMessageToParents(MusicSchoolChoice application, String subject, String body, String letterBody, boolean alwaysSendLetter) {
		try {
			User child = application.getChild();
			Object[] arguments = {child.getName(), application.getSchool().getSchoolName(), PersonalIDFormatter.format(child.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };

			User appParent = application.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
				Message message = getMessageBusiness().createUserMessage(application, appParent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
				message.setParentCase(application);
				message.store();
			}

			try {
				Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (!getUserBusiness().haveSameAddress(parent, appParent)) {
						getMessageBusiness().createUserMessage(application, parent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
					}
				}
			}
			catch (NoCustodianFound ncf) {
				getMessageBusiness().createUserMessage(application, child, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public Map getStudentList(Collection students) {
		try {
			return getSchoolCommuneBusiness().getStudentList(students);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public void deleteInstrument(Object instrumentPK) {
		try {
			SchoolStudyPath instrument = getSchoolBusiness().getSchoolStudyPathHome().findByPrimaryKey(new Integer(instrumentPK.toString()));
			instrument.remove();
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoveException re) {
			log(re);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public void saveInstrument(Object instrumentPK, String code, String description, String localizedKey) throws FinderException, CreateException {
		try {
			SchoolStudyPath instrument;
			if (instrumentPK != null) {
				instrument = getSchoolBusiness().getSchoolStudyPathHome().findByPrimaryKey(new Integer(instrumentPK.toString()));
			}
			else {
				instrument = getSchoolBusiness().getSchoolStudyPathHome().create();
			}
			
			if (instrument != null) {
				instrument.setCode(code);
				instrument.setDescription(description);
				instrument.setLocalizedKey(localizedKey);
				instrument.setIsValid(true);
				instrument.setSchoolCategory(getSchoolBusiness().getCategoryMusicSchool().getPrimaryKey());
				instrument.store();
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void saveDepartment(Object departmentPK, String name, String description, String localizedKey, int order, boolean isSelectable) throws FinderException, CreateException {
		try {
			SchoolYear department;
			if (departmentPK != null) {
				department = getSchoolBusiness().getSchoolYear(new Integer(departmentPK.toString()));
			}
			else {
				department = getSchoolBusiness().getSchoolYearHome().create();
			}
			
			if (department != null) {
				department.setSchoolYearName(name);
				department.setSchoolYearInfo(description);
				department.setLocalizedKey(localizedKey);
				department.setIsSelectable(isSelectable);
				department.setSchoolYearAge(order);
				department.setSchoolCategory(getSchoolBusiness().getCategoryMusicSchool().getPrimaryKey());
				department.store();
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void saveLessonType(Object lessonTypePK, String name, String description, String localizedKey, int order, boolean isSelectable) throws CreateException {
		try {
			SchoolType lessonType;
			if (lessonTypePK != null) {
				lessonType = getSchoolBusiness().getSchoolType(new Integer(lessonTypePK.toString()));
			}
			else {
				lessonType = getSchoolBusiness().getSchoolTypeHome().create();
			}
			
			if (lessonType != null) {
				lessonType.setSchoolTypeName(name);
				lessonType.setSchoolTypeInfo(description);
				lessonType.setLocalizationKey(localizedKey);
				lessonType.setSelectable(isSelectable);
				lessonType.setOrder(order);
				lessonType.setCategory(getSchoolBusiness().getCategoryMusicSchool());
				lessonType.store();
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		MusicSchoolChoice choice = getMusicSchoolChoiceInstance(theCase);
		Object[] arguments = {choice.getChild().getFirstName(), String.valueOf(choice.getChoiceNumber()), choice.getSchool().getName()};

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	protected MusicSchoolChoice getMusicSchoolChoiceInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (CASE_CODE_KEY.equals(caseCode)) {
				int caseID = ((Integer) theCase.getPrimaryKey()).intValue();
				return findMusicSchoolChoice(new Integer(caseID));
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a schoolchoice");
	}
}