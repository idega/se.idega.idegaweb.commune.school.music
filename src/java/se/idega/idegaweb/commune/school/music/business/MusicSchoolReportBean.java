/*
 * Created on 24.6.2004
 */
package se.idega.idegaweb.commune.school.music.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


/**
 * @author laddi
 */
public class MusicSchoolReportBean extends IBOSessionBean implements MusicSchoolReport {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	private final static String FIELD_SCHOOL = "school";
	
	private final static String PREFIX = "music_school_report.";

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;

	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	public ReportableCollection getChoicesReport(SchoolSeason season) {
		try {
			initializeBundlesIfNeeded();
			Locale currentLocale = this.getUserContext().getCurrentLocale();
			Object singingStudyPathID = _iwb.getProperty(MusicSchoolReport.PROPERTY_SINGING_STUDY_PATH_ID, "");
			SchoolStudyPath singing = getSchoolBusiness().getSchoolStudyPathHome().findByPrimaryKey(new Integer(singingStudyPathID.toString()));
			String typeIDs = _iwb.getProperty(MusicSchoolReport.PROPERTY_TYPE_IDS, "");
			Map map = new HashMap();
			
			Collection schools = null;
			try {
				schools = getMusicSchoolBusiness().findAllMusicSchools();
			}
			catch (FinderException fe) {
				schools = new ArrayList();
			}
			catch (RemoteException re) {
				log(re);
			}
			
			Collection departments = null;
			try {
				departments = getMusicSchoolBusiness().findAllSelectableDepartments();
			}
			catch (FinderException fe) {
				departments = new ArrayList();
			}
			catch (RemoteException re) {
				log(re);
			}
			
			ReportableCollection reportCollection = new ReportableCollection();
			
			ReportableField name = new ReportableField(FIELD_SCHOOL, String.class);
			name.setLocalizedName(getLocalizedString(FIELD_SCHOOL, "School"), currentLocale);
			reportCollection.addField(name);
			
			Iterator iter = departments.iterator();
			while (iter.hasNext()) {
				SchoolYear department = (SchoolYear) iter.next();
				
				ReportableField allDepartments = new ReportableField(department.getSchoolYearName() + "_all", String.class);
				allDepartments.setLocalizedName(getLocalizedString(department.getSchoolYearName() + "_all", department.getSchoolYearName() + " all"), currentLocale);
				reportCollection.addField(allDepartments);
				map.put(department.getSchoolYearName() + "_all", allDepartments);
				
				ReportableField singingDepartment = new ReportableField(department.getSchoolYearName() + "_singing", String.class);
				singingDepartment.setLocalizedName(getLocalizedString(department.getSchoolYearName() + "_singing", department.getSchoolYearName() + " singing"), currentLocale);
				reportCollection.addField(singingDepartment);
				map.put(department.getSchoolYearName() + "_singing", singingDepartment);
				
				ReportableField otherDepartments = new ReportableField(department.getSchoolYearName() + "_other", String.class);
				otherDepartments.setLocalizedName(getLocalizedString(department.getSchoolYearName() + "_other", department.getSchoolYearName() + " other"), currentLocale);
				reportCollection.addField(otherDepartments);
				map.put(department.getSchoolYearName() + "_other", otherDepartments);
				
			}
			
			String[] statuses = { getMusicSchoolBusiness().getCaseStatusPlaced().getStatus() }; 
			iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				
				ReportableData data = new ReportableData();
				data.addData(name, school.getSchoolName());
				
				int singingNR = 0;
				int totalNR = 0;
				int otherNR = 0;
				Iterator iterator = departments.iterator();
				while (iterator.hasNext()) {
					SchoolYear department = (SchoolYear) iterator.next();
					try {
						singingNR = getMusicSchoolBusiness().getMusicSchoolChoiceHome().getNumberOfApplications(school, season, department, singing, typeIDs, statuses);
						totalNR = getMusicSchoolBusiness().getMusicSchoolChoiceHome().getNumberOfApplications(school, season, department, null, typeIDs, statuses);
						otherNR = totalNR - singingNR;
					}
					catch (IDOException ie) {
						log(ie);
					}
					data.addData((ReportableField) map.get(department.getSchoolYearName() + "_other"), new Integer(otherNR));
					data.addData((ReportableField) map.get(department.getSchoolYearName() + "_singing"), new Integer(singingNR));
					data.addData((ReportableField) map.get(department.getSchoolYearName() + "_all"), new Integer(totalNR));
				}
				reportCollection.add(data);
			}
			
			return reportCollection;
		}
		catch (FinderException fe) {
			throw new IBORuntimeException(fe);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private String getLocalizedString(String key, String defaultValue) {
		return _iwrb.getLocalizedString(PREFIX + key, defaultValue);
	}
	
	private MusicSchoolBusiness getMusicSchoolBusiness() {
		try {
			return (MusicSchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), MusicSchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
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
}