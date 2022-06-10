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
	private String gameID;

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

	private boolean repeat = true;

	/**
	 * Creates the structure for a GamePlay tab, and uses the Scheduler to prompt
	 * the Server for existing events to process
	 * 
	 * @param gameId - of game
	 */
	protected GamePlay(String gameId) {
		super(username, password);
		this.gameID = gameId;
		gamePlay = onGamePlayInitialize();
		processEvents();

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

			@Override
			public boolean execute() {
				if (repeat)
					processEvents();
				return repeat;
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
		gamePlayPanel.setStyleName("cw-DockPanel");
		gamePlayPanel.setSpacing(0);
		gamePlayPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

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
		return gameID;
	}

	/**
	 * @return the onTable
	 */
	protected Map<String, List<Card>> getOnTable() {
		return onTable;
	}

	/**
	 * @return the roundsCompleted
	 */
	protected int getRoundsCompleted() {
		return roundsCompleted;
	}

	/**
	 * @return the points
	 */
	protected Map<String, Integer> getPoints() {
		return points;
	}

	/**
	 * @return the winner
	 */
	protected String getWinner() {
		return winner;
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
		cardGameService.getRecentEvents(gameID, username, password, new AsyncCallback<List<GameEvent>>() {

			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameEvent> recentEvents) {
				for (GameEvent event : recentEvents) {
					if (event instanceof SendCardsEvent) {
						cardsOnHand.addAll(((SendCardsEvent) event).getCards());

						systemMessages.setHTML("Cards have been given.");

						redoSouthPanel();
					}
					if (event instanceof RoundUpdateEvent) {
						onTable = ((RoundUpdateEvent) event).getCardsOnTable();
						hasTurn = ((RoundUpdateEvent) event).getNickWithTurn();
						roundsCompleted = ((RoundUpdateEvent) event).getRoundsCompleted();
						mode = ((RoundUpdateEvent) event).getMode();

						systemMessages.setHTML("It is now " + hasTurn + "'s turn.");

						redoCenterPanel();
					}
					if (event instanceof RoundConclusionEvent) {
						onTable = ((RoundConclusionEvent) event).getCardsOnTable();
						roundsCompleted = ((RoundConclusionEvent) event).getRoundsCompleted();
						points = ((RoundConclusionEvent) event).getPoints();

						systemMessages.setHTML(
								"Round #" + roundsCompleted + " is complete. It is now " + hasTurn + "'s turn.");

						redoCenterPanel();
					}
					if (event instanceof GameEndEvent) {
						onTable = ((GameEndEvent) event).getCardsOnTable();
						roundsCompleted = ((GameEndEvent) event).getRoundsCompleted();
						winner = ((GameEndEvent) event).getWinner();
						points = ((GameEndEvent) event).getPoints();

						repeat = false;

						systemMessages
								.setHTML(winner + " has won " + gameID + " with " + points.get(winner) + " points.");

						tabPanel.remove(gameID);

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

}
