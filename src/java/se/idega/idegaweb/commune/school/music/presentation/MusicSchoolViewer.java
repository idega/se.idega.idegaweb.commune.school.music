/*
 * Created on 14.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;


/**
 * @author laddi
 */
public class MusicSchoolViewer extends MusicSchoolBlock {
	
	private String linkStyleName;
	private String textStyleName;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		try {
			List schools = new ArrayList(getBusiness().findAllMusicSchools());
			Collections.sort(schools, new SchoolComparator(iwc.getCurrentLocale()));
			
			Lists list = new Lists();
			list.setCompact(true);
			if (textStyleName != null) {
				list.setClass(textStyleName);
			}
			
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				if (getResponsePage() != null) {
					Link link = new Link(school.getSchoolName());
					link.setPage(getResponsePage());
					if (linkStyleName != null) {
						link.setStyleClass(linkStyleName);
					}
					list.add(link);
				}
				else {
					Text text = new Text(school.getSchoolName());
					list.add(text);
				}
			}
			add(list);
		}
		catch (FinderException fe) {
			add(getErrorText(localize("no_music_schools_found", "No music school were found...")));
		}
	}
	
	/**
	 * @param linkStyleName The linkStyleName to set.
	 */
	public void setLinkStyleName(String linkStyleName) {
		this.linkStyleName = linkStyleName;
	}
	
	/**
	 * @param textStyleName The textStyleName to set.
	 */
	public void setTextStyleName(String textStyleName) {
		this.textStyleName = textStyleName;
	}
}