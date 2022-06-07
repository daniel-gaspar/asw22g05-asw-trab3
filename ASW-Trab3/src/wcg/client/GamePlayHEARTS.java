package wcg.client;

import com.google.gwt.user.client.ui.Widget;

public class GamePlayHEARTS extends GamePlay {

	private Widget gamePlayHEARTS;

	public GamePlayHEARTS(String gameId) {
		super(gameId);
		this.gamePlayHEARTS = onGamePlayHEARTSInitialize();
	}

	public Widget getGamePlayHEARTS() {
		return gamePlayHEARTS;
	}

	private Widget onGamePlayHEARTSInitialize() {
		// TODO - Create widget for game play HEARTS
		return null;
	}

	@Override
	protected void drawCardsOnHand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawCardsOnTable() {
		// TODO Auto-generated method stub
		
	}
}
