/*
 * $Id: InstrumentComparator.java,v 1.1 2005/03/19 16:37:28 laddi Exp $
 * Created on 15.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.music.business;

import java.text.Collator;
import java.util.Comparator;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.idegaweb.IWResourceBundle;


public class InstrumentComparator implements Comparator {

	private IWResourceBundle iwrb;
	
	public InstrumentComparator(IWResourceBundle iwrb) {
		this.iwrb = iwrb;
	}
	
	public int compare(Object arg0, Object arg1) {
		Collator collator = Collator.getInstance(iwrb.getLocale());
		SchoolStudyPath obj1 = (SchoolStudyPath) arg0;
		SchoolStudyPath obj2 = (SchoolStudyPath) arg1;
		
		return collator.compare(iwrb.getLocalizedString(obj1.getLocalizedKey(), obj1.getDescription()), iwrb.getLocalizedString(obj2.getLocalizedKey(), obj2.getDescription()));
	}
}