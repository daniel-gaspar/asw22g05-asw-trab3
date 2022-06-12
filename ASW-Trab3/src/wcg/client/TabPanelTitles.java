/**
 * 
 */
package wcg.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extension of TabPanel to be able to make adjustments by using the tab's
 * title/text
 * 
 * Inspired by
 * https://stackoverflow.com/questions/24936400/how-to-get-tab-text-in-gwt
 * 
 * Assumes that each Tab's Title is unique
 */
public class TabPanelTitles extends TabPanel {

	private List<String> tabIds = new ArrayList<>();

	public TabPanelTitles() {
		super();
	}

	@Override
	public void add(Widget w, String tabId) {
		super.add(w, tabId);
		tabIds.add(tabId);
	}

	/**
	 * Adds a Widget w to the TabPanel, while specifying the Text tabText as the
	 * displayed on the Tab and the identifier with tabId
	 * 
	 * @param w       - Widget to add
	 * @param tabText - Text to display on Tab
	 * @param tabId   - Identifier of the Tab
	 */
	public void add(Widget w, String tabText, String tabId) {
		super.add(w, tabText);
		tabIds.add(tabId);
	}

	/**
	 * Adds a Widget panel to the TabPanel, while specifying the Widget tab as the
	 * display on the Tab and the identifier with tabId
	 * 
	 * @param panel - Widget to add
	 * @param tab   - Widget to display on Tab
	 * @param tabId - Identifier of the Tab
	 */
	public void add(Widget panel, Widget tab, String tabId) {
		super.add(panel, tab);
		tabIds.add(tabId);
	}

	@Override
	public boolean remove(int index) {
		boolean removed = super.remove(index);
		if (removed) {
			tabIds.remove(index);
			return removed;
		}
		return removed;
	}

	/**
	 * Removes a Tab with the specified tabId
	 * 
	 * @param tabId - Identifier of the Tab
	 * @return Whether the Tab was successfully removed or not
	 */
	public boolean remove(String tabId) {
		int tabIndex = getTabIndex(tabId);
		boolean removed = remove(tabIndex);
		return removed;
	}

	@Override
	public void clear() {
		super.clear();
		tabIds.clear();
	}

	/**
	 * Returns the Identifier of the Tab of a given index
	 * 
	 * @param index - Index of the Tab
	 * @return Identifier of the Tab
	 */
	public String getTabId(int index) {
		return tabIds.get(index);
	}

	/**
	 * Returns the index of the Tab with a given Identifier
	 * 
	 * @param tabId - Identifier of the Tab
	 * @return Index of the Tab
	 */
	public int getTabIndex(String tabId) {
		for (int i = 0; i < tabIds.size(); i++) {
			if (tabId.equals(tabIds.get(i)))
				return i;
		}
		return -1;
	}

	/**
	 * Selects a Tab while using it's Identifier
	 * 
	 * @param tabId - Identifier of the Tab
	 */
	public void selectTab(String tabId) {
		int tabIndex = getTabIndex(tabId);
		super.selectTab(tabIndex);
	}
}