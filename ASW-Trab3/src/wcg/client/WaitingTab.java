/**"
 * 
 */
package wcg.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.GameInfo;

/**
 *
 */
public class WaitingTab extends SubPanel {

	private static final boolean FLAG_AUTOBOTS_ROLLOUT = false;

	private Widget waitingTab = new HorizontalPanel();

	private final HTML waitingGameLabel = new HTML("Waiting for the game to start");
	private final HorizontalPanel tabWidget = new HorizontalPanel();
	private final HTML tabWidgetText = new HTML();
	private final Button tabWidgetClose = new Button("x", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			closeGame();
		}
	});

	private String gameId;
	private boolean owner_flag;

	private boolean repeat = true;

	private static final int TIMER_DELAY = 10 * 1000; // 10 seconds
	private static final int GAME_START_TIMEOUT = 60 * 1000; // 1 minute

	public WaitingTab(String gameId, boolean owner) {
		super(username, password);
		this.gameId = gameId;
		this.owner_flag = owner;

		waitingTabInitialization();

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

			@Override
			public boolean execute() {
				if (repeat)
					verifyStartGame();
				if (FLAG_AUTOBOTS_ROLLOUT && owner_flag && repeat)
					verifyAddBots();
				return repeat;
			}

		}, TIMER_DELAY);
	}

	private void waitingTabInitialization() {
		// Starts by applying StyleNames and other layout settings to the elements
		applyStylizingSettings();

		// Adds the waitingGameLabel to the Waiting Tab Panel
		((HorizontalPanel) waitingTab).add(waitingGameLabel);
	}

	public Widget getWaitingTab() {
		return waitingTab;
	}

	/**
	 * <p>
	 * Prompts the server for a list of Available Game Infos, to verify whether the
	 * current Game is ready to start or not
	 * </p>
	 * <p>
	 * If it's ready to start, stops the Scheduler, removes the current Tab and
	 * replaces it with a new, GamePlayWAR or GamePlayHEARTS
	 */
	private void verifyStartGame() {
		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(gameId + ": Failed to verify available game infos while waiting to start. "
						+ caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					String currentGameName = gameInfo.getGameName();
					int currentPlayersCount = gameInfo.getPlayersCount();
					String currentGameId = gameInfo.getGameId();

					if (currentGameId.equals(gameId)) {
						if (currentPlayersCount == AuxMethods.numberOfPlayers(currentGameName)) {
							repeat = false;
							tabPanel.remove(gameId);

							tabWidget.add(tabWidgetText);
							tabWidget.add(tabWidgetClose);

							if ("WAR".equals(currentGameName)) {
								tabPanel.add(new GamePlayWAR(gameId).getGamePlay(), tabWidget, gameId);
							}
							if ("HEARTS".equals(currentGameName)) {
								tabPanel.add(new GamePlayHEARTS(gameId).getGamePlay(), tabWidget, gameId);
							}
							tabPanel.selectTab(gameId);
						}
					}
				}
			}
		});
	}

	/**
	 * <p>
	 * Prompts the server for a list of Available Game Infos, to verify whether the
	 * current Game is ready to start or not
	 * </p>
	 * <p>
	 * If the Game isn't ready to start, and longer than GAME_START_TIMEOUT has
	 * passed, Bots are added to the Game
	 * </p>
	 */
	private void verifyAddBots() {
		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages
						.setHTML(gameId + ": Failed to get available game infos while checking whether to add bots. "
								+ caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					String currentGameId = gameInfo.getGameId();

					if (currentGameId.equals(gameId)) {
						Date currentLastAccessDate = gameInfo.getLastAccessDate();
						Date nowDate = new Date();

						if ((nowDate.getTime() - currentLastAccessDate.getTime()) > GAME_START_TIMEOUT) {

							String currentGameName = gameInfo.getGameName();
							int currentPlayersCount = gameInfo.getPlayersCount();
							for (int i = currentPlayersCount; i < AuxMethods.numberOfPlayers(currentGameName); i++)
								addBot(gameId);

						}
					}
				}
			}
		});
	}

	/**
	 * Applies the diverse StyleNames and other layout settings to the elements
	 */
	private void applyStylizingSettings() {
		waitingTab.setStyleName("wcg-Panel");

		waitingGameLabel.setStyleName("wcg-Text");

		tabWidget.addStyleName("wcg-TabWidget");

		tabWidgetText.setHTML("Play: " + gameId);
		tabWidgetText.addStyleName("wcg-TabWidgetText");

		tabWidgetClose.addStyleName("wcg-TabWidgetClose");
	}

	/**
	 * Selects the 'Select Game' Tab and closes the current Game Tab
	 */
	private void closeGame() {
		tabPanel.selectTab(SELECT_GAME_TAB);
		tabPanel.remove(gameId);
	}
}
