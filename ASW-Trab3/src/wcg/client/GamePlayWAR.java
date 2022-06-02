package wcg.client;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class GamePlayWAR extends GamePlay {

	private Widget gamePlayWAR;

	public GamePlayWAR(TabPanel tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(tabPanel, username, password, cardGameService);
		this.gamePlayWAR = onGamePlayWARInitialize();
	}

	public Widget getGamePlayWAR() {
		return gamePlayWAR;
	}

	private Widget onGamePlayWARInitialize() {
		// TODO - create Widget for game play WAR
		return null;
	}
}
