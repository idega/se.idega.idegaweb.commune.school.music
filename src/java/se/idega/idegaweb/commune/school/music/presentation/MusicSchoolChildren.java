/*
 * Created on 6.5.2004
 */
package se.idega.idegaweb.commune.school.music.presentation;

import is.idega.idegaweb.member.business.NoChildrenFound;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.music.event.MusicSchoolEventListener;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class MusicSchoolChildren extends MusicSchoolBlock {

	private final static String ERROR_NO_RESPONSE_PAGE = "no_response_page";
	private final static String SELECT_CHILD = "select_applicant";

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getResponsePage() != null) {
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			
			int row = 1;
			
			Collection children = null;
			try {
				children = getUserBusiness().getMemberFamilyLogic().getChildrenFor(iwc.getCurrentUser());
			}
			catch (NoChildrenFound e) {
				children = new ArrayList();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			children.add(iwc.getCurrentUser());
	
			table.add(getSmallHeader(localize(SELECT_CHILD,"Select the appropriate applicant") + ":"),1,row++);
			table.setHeight(row++,12);
						
			Iterator it = children.iterator();
			while (it.hasNext()) {
				User child = (User) it.next();
				
				Link link = getLink(child.getName());
				link.addParameter(getSession().getParameterNameChildID(), child.getPrimaryKey().toString());
				link.setEventListener(MusicSchoolEventListener.class);
				link.setPage(getResponsePage());

				table.add(link, 1, row++);
				table.setHeight(row++,2);
			}
			
			add(table);
		}
		else {
			add(getErrorText(localize(ERROR_NO_RESPONSE_PAGE, "The response page has not been set.")));
		}
	}
}