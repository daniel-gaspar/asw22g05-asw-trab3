package wcg.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;
import wcg.shared.events.GameEndEvent;
import wcg.shared.events.GameEvent;
import wcg.shared.events.RoundConclusionEvent;
import wcg.shared.events.RoundUpdateEvent;
import wcg.shared.events.SendCardsEvent;

public abstract class GamePlay extends SubPanel {

	//To process all events
	private String gameId;
	
	//To process RoundUpdateEvent, RoundConclusionEvent, GameEndEvent
	private Map<String, List<Card>> onTable;
	private int roundsCompleted;
	
	//To process RoundConclusionEvent, GameEndEvent
	private Map<String,Integer> points;
	
	//To process GameEndEvent
	private String winner;
	
	//To process RoundUpdateEvent
	private String hasTurn;
	private String mode;
	
	//To process SendCardsEvent
	private List<Card> cardsOnHand = new ArrayList<>();
	
	//To schedule the processEvents routine
	private static final int TIMER_DELAY = 5 * 1000; // 5 seconds
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		public void run() {
			processEvents();
		}
	};
	
	protected GamePlay(String gameId) {
		super(username, password);
		this.gameId = gameId;
		timer.scheduleAtFixedRate(task, 0, TIMER_DELAY);
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
	 * @return the timer
	 */
	protected Timer getTimer() {
		return timer;
	}

	/**
	 * @return the task
	 */
	protected TimerTask getTask() {
		return task;
	}

	/**
	 * Requests a list of Recent Events from the server, and processes them
	 */
	private void processEvents()  {
		cardGameService.getRecentEvents(username, password, new AsyncCallback<List<GameEvent>>() {

			@Override
			public void onFailure(Throwable caught) {
				messages.setHTML(caught.getMessage());	
			}

			@Override
			public void onSuccess(List<GameEvent> recentEvents) {
				for(GameEvent event : recentEvents) {
					if(event instanceof SendCardsEvent) {
						cardsOnHand.addAll(((SendCardsEvent) event).getCards());
						
						messages.setHTML("Cards have been given.");
						
						drawCardsOnHand();
					}
					if(event instanceof RoundUpdateEvent) {
						onTable = ((RoundUpdateEvent) event).getCardsOnTable();
						hasTurn = ((RoundUpdateEvent) event).getNickWithTurn();
						roundsCompleted = ((RoundUpdateEvent) event).getRoundsCompleted();
						mode = ((RoundUpdateEvent) event).getMode();
						
						messages.setHTML("It is now " + hasTurn + "'s turn.");
						
						drawCardsOnTable();
					}
					if(event instanceof RoundConclusionEvent) {
						onTable = ((RoundConclusionEvent) event).getCardsOnTable();
						roundsCompleted = ((RoundConclusionEvent) event).getRoundsCompleted();
						points = ((RoundConclusionEvent) event).getPoints();
						
						messages.setHTML("Round #" + roundsCompleted + " is complete. It is now " + hasTurn + "'s turn.");
						
						drawCardsOnTable();
					}
					if(event instanceof GameEndEvent) {
						onTable = ((GameEndEvent) event).getCardsOnTable();
						roundsCompleted = ((GameEndEvent) event).getRoundsCompleted();
						winner = ((GameEndEvent) event).getWinner();
						points = ((GameEndEvent) event).getPoints();
						
						messages.setHTML(winner + " has won the game with " + points.get(winner)  + " points.");
						
						drawCardsOnTable();
					}
				}
			}
		});
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
