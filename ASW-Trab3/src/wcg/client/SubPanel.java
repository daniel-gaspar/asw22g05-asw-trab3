package wcg.client;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * Extends MainPanel by adding the fields username and password
 *
 */
public abstract class SubPanel extends MainPanel {

	protected static String username;
	protected static String password;

	public SubPanel(TabPanel tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(tabPanel, cardGameService);
		SubPanel.username = username;
		SubPanel.password = password;
	}

}
