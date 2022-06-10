/**"
 * 
 */
package wcg.client;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.GameInfo;

/**
 *
 */
public class WaitingTab extends SubPanel {

	private Widget waitingTab = new HTML("Waiting for Game to Start");

	private String gameID;
	private boolean owner_flag;

	private boolean repeat = true;

	private static final int TIMER_DELAY = 10 * 1000; // 10 seconds
	private static final int GAME_START_TIMEOUT = 60 * 1000; // 1 minute

	public WaitingTab(String gameID, boolean owner) {
		super(username, password);
		this.gameID = gameID;
		this.owner_flag = owner;

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

			@Override
			public boolean execute() {
				Logger logger = Logger.getLogger("nameOfLogger");
				logger.log(Level.SEVERE, "executing waitingTabScheduler");
				if (repeat)
					verifyStartGame();
				if (FLAG_AUTOBOTS_ROLLOUT && owner_flag && repeat)
					verifyAddBots();
				return repeat;
			}

		}, TIMER_DELAY);
	}

	public Widget getWaitingTab() {
		return waitingTab;
	}

	private void verifyStartGame() {
		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					String currentGameName = gameInfo.getGameName();
					int currentPlayersCount = gameInfo.getPlayersCount();
					String currentGameID = gameInfo.getGameId();

					if (currentGameID.equals(gameID)) {
						if (currentPlayersCount == AuxMethods.numberOfPlayers(currentGameName)) {
							repeat = false;
							tabPanel.remove(gameID);
							if ("WAR".equals(currentGameName)) {
								tabPanel.add(new GamePlayWAR(gameID).getGamePlay(), "Play: " + gameID, gameID);
							}
							if ("HEARTS".equals(currentGameName)) {
								tabPanel.add(new GamePlayHEARTS(gameID).getGamePlay(), "Play: " + gameID, gameID);
							}
							tabPanel.selectTab(gameID);
						}
					}
				}
			}
		});
	}

	private void verifyAddBots() {
		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					String currentGameID = gameInfo.getGameId();

					if (currentGameID.equals(gameID)) {
						Date currentLastAccessDate = gameInfo.getLastAccessDate();
						Date nowDate = new Date();

						if ((nowDate.getTime() - currentLastAccessDate.getTime()) > GAME_START_TIMEOUT) {

							String currentGameName = gameInfo.getGameName();
							int currentPlayersCount = gameInfo.getPlayersCount();
							for (int i = currentPlayersCount; i < AuxMethods.numberOfPlayers(currentGameName); i++)
								addBot(gameID);

						}
					}
				}
			}
		});
	}

	/**
	 * Prompts the server to add a bot to a Game with gameID
	 * 
	 * @param gameID - of game
	 */
	private void addBot(String gameID) {
		cardGameService.addBotPlayer(gameID, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				systemMessages.setHTML("Bot added");
			}
		});
	}
}
