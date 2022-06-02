package wcg.client;

import com.google.gwt.user.client.ui.TabPanel;

public class GamePlay extends SubPanel {

	public GamePlay(TabPanel tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(tabPanel, username, password, cardGameService);
	}
}
