package wcg.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;
import wcg.shared.events.GameEndEvent;
import wcg.shared.events.GameEvent;
import wcg.shared.events.RoundConclusionEvent;
import wcg.shared.events.RoundUpdateEvent;
import wcg.shared.events.SendCardsEvent;

public abstract class GamePlay extends SubPanel {

	protected Widget gamePlay;

	/**
	 * The main Widgets which compose a GamePlay panel
	 */
	private final DockPanel gamePlayPanel = new DockPanel();
	private Widget centerPanel = new VerticalPanel();
	private Widget southPanel = new VerticalPanel();

	// To process all events
	private String gameId;

	// To process RoundUpdateEvent, RoundConclusionEvent, GameEndEvent
	private Map<String, List<Card>> onTable;
	private int roundsCompleted;

	// To process RoundConclusionEvent, GameEndEvent
	private Map<String, Integer> points;

	// To process GameEndEvent
	private String winner;

	// To process RoundUpdateEvent
	private String hasTurn;
	private String mode;

	// To process SendCardsEvent
	private final List<Card> cardsOnHand = new ArrayList<>();

	// To schedule the processEvents routine
	private static final int TIMER_DELAY = 500;

	// To control whether the task in Scheduler should continue repeating or not
	private boolean repeatScheduledTask = true;

	// To be used only in WAR. Since there are no turns in this game, it is
	// necessary to prevent playing multiple cards during the same turn.
	protected boolean hasPlayedThisTurn = false;

	/**
	 * Creates the structure for a GamePlay tab, and uses the Scheduler to prompt
	 * the Server for existing events to process
	 * 
	 * @param gameId - of game
	 */
	protected GamePlay(String gameId) {
		super(username, password);
		this.gameId = gameId;
		gamePlay = onGamePlayInitialize();
		applyStylizingSettings();

		processEvents();

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

			@Override
			public boolean execute() {
				if (repeatScheduledTask)
					processEvents();
				return repeatScheduledTask;
			}
		}, TIMER_DELAY);

	}

	/**
	 * Creates a DockPanel and adds to it the centerPanel and the southPanel, which
	 * initially are empty
	 * 
	 * @return gamePlayPanel
	 */
	private Widget onGamePlayInitialize() {

		gamePlayPanel.add(centerPanel, DockPanel.CENTER);
		gamePlayPanel.add(southPanel, DockPanel.SOUTH);

		return gamePlayPanel;
	}

	/**
	 * @return the gamePlay Widget
	 */
	protected Widget getGamePlay() {
		return gamePlay;
	}

	/**
	 * @return the gameId
	 */
	protected String getGameId() {
		return gameId;
	}

	/**
	 * @return the onTable
	 */
	protected Map<String, List<Card>> getOnTable() {
		return onTable;
	}

	/**
	 * @return the hasTurn
	 */
	protected String getHasTurn() {
		return hasTurn;
	}

	/**
	 * @return the mode
	 */
	protected String getMode() {
		return mode;
	}

	/**
	 * @return the cardsOnHand
	 */
	protected List<Card> getCardsOnHand() {
		return cardsOnHand;
	}

	/**
	 * Requests a list of Recent Events from the server, and processes them
	 */
	private void processEvents() {
		cardGameService.getRecentEvents(gameId, username, password, new AsyncCallback<List<GameEvent>>() {

			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(gameId + ": Failed to get recent events. " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameEvent> recentEvents) {
				for (GameEvent event : recentEvents) {
					if (event instanceof SendCardsEvent) {
						cardsOnHand.addAll(((SendCardsEvent) event).getCards());

						systemMessages.setHTML(gameId + ": Cards have been given.");

						redoSouthPanel();
					}
					if (event instanceof RoundUpdateEvent) {
						onTable = ((RoundUpdateEvent) event).getCardsOnTable();
						hasTurn = ((RoundUpdateEvent) event).getNickWithTurn();
						roundsCompleted = ((RoundUpdateEvent) event).getRoundsCompleted();
						mode = ((RoundUpdateEvent) event).getMode();

						String messageToSet;
						String gameSection = gameId + ": ";
						String turnSection = "Cards have been played.";

						if (hasTurn != null)
							if (username.equals(hasTurn))
								turnSection = "It is now your turn";
							else
								turnSection = "It is now " + hasTurn + "'s turn.";

						messageToSet = gameSection + turnSection;

						systemMessages.setHTML(messageToSet);

						redoCenterPanel();
					}
					if (event instanceof RoundConclusionEvent) {
						onTable = ((RoundConclusionEvent) event).getCardsOnTable();
						roundsCompleted = ((RoundConclusionEvent) event).getRoundsCompleted();
						points = ((RoundConclusionEvent) event).getPoints();

						hasPlayedThisTurn = false;

						String messageToSet;
						String roundsSection = gameId + ": Round #" + roundsCompleted + " is complete. ";
						String turnSection = "";

						if (hasTurn != null)
							if (username.equals(hasTurn))
								turnSection = "It is now your turn.";
							else
								turnSection = "It is now " + hasTurn + "'s turn.";

						messageToSet = roundsSection + turnSection;

						systemMessages.setHTML(messageToSet);

						redoCenterPanel();
					}
					if (event instanceof GameEndEvent) {
						onTable = ((GameEndEvent) event).getCardsOnTable();
						roundsCompleted = ((GameEndEvent) event).getRoundsCompleted();
						winner = ((GameEndEvent) event).getWinner();
						points = ((GameEndEvent) event).getPoints();

						repeatScheduledTask = false;

						systemMessages
								.setHTML(gameId + ": " + winner + " has won with " + points.get(winner) + " points.");

						tabPanel.remove(gameId);

						tabPanel.selectTab(SELECT_GAME_TAB);
					}
				}
			}
		});
	}

	/**
	 * Removes the southPanel from the DockPanel, draws the Cards On Hand once more,
	 * and adds this new southPanel again to the DockPanel
	 */
	private void redoSouthPanel() {
		gamePlayPanel.remove(southPanel);
		southPanel = drawCardsOnHand();
		gamePlayPanel.add(southPanel, DockPanel.SOUTH);
	}

	/**
	 * Removes the centerPanel from the DockPanel, draws the Cards On Table once
	 * more, and adds this new centerPanel again to the DockPanel
	 */
	private void redoCenterPanel() {
		gamePlayPanel.remove(centerPanel);
		centerPanel = drawCardsOnTable();
		gamePlayPanel.add(centerPanel, DockPanel.CENTER);
	}

	/**
	 * Draw the cards currently on Player's hand
	 */
	protected abstract Widget drawCardsOnHand();

	/**
	 * Draw the cards currently on the Table
	 */
	protected abstract Widget drawCardsOnTable();

	/**
	 * Applies the diverse StyleNames and other layout settings to the elements
	 */
	private void applyStylizingSettings() {
		gamePlay.setStyleName("wcg-Panel");
		gamePlay.addStyleName("wcg-GameArea");

		gamePlayPanel.setSpacing(2);
		gamePlayPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
	}

}
