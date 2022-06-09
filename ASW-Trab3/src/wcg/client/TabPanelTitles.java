/**
 * 
 */
package wcg.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extension of TabPanel to be able to make adjustments by using the tab's title/text
 * 
 * Inspired by https://stackoverflow.com/questions/24936400/how-to-get-tab-text-in-gwt
 * 
 * Assumes that each Tab's Title is unique
 */
public class TabPanelTitles extends TabPanel {
	
	private List<String> tabTitles = new ArrayList<>();
	
	public TabPanelTitles() {
		super();
	}
	
	@Override
	public void add(Widget w, String tabTitle) {
		super.add(w, tabTitle);
		tabTitles.add(tabTitle);
	}
	
	public void add(Widget w, String tabText, String tabTitle) {
		super.add(w, tabText);
		tabTitles.add(tabTitle);
	}
	
	@Override
	public boolean remove(int index) {
		boolean removed = super.remove(index);
		if(removed) {
			tabTitles.remove(index);
			return removed;
		}
		return removed;
	}
	
	public boolean remove(String tabTitle) {
		int tabIndex = getTabIndex(tabTitle);
		boolean removed = remove(tabIndex);
		return removed;
	}
	
	@Override
	public void clear() {
		super.clear();
		tabTitles.clear();
	}
	
	public String getTabTitle(int index) {
		return tabTitles.get(index);
	}
	
	public int getTabIndex(String tabTitle) {
		for(int i = 0; i < tabTitles.size() ; i++) {
			if(tabTitle.equals(tabTitles.get(i)))
				return i;
		}
		return -1;
	}
	
	public void selectTab(String tabTitle) {
		int tabIndex = getTabIndex(tabTitle);
		super.selectTab(tabIndex);
	}
}