package wcg.client;

import com.google.gwt.user.client.ui.Widget;

public class GamePlayWAR extends GamePlay {

	private Widget gamePlayWAR;

	public GamePlayWAR(String gameId) {
		super(gameId);
		this.gamePlayWAR = onGamePlayWARInitialize();
	}

	public Widget getGamePlayWAR() {
		return gamePlayWAR;
	}

	private Widget onGamePlayWARInitialize() {
		// TODO - create Widget for game play WAR
		return null;
	}

	@Override
	protected Widget drawCardsOnHand() {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	protected Widget drawCardsOnTable() {
		// TODO Auto-generated method stub
		return null;
		
	}
}
