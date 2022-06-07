package wcg.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

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
	
	public GamePlay(String gameId) {
		super(username, password);
		this.gameId = gameId;
		
	}
	
	private void processEvents()  {
		cardGameService.getRecentEvents(username, password, new AsyncCallback<List<GameEvent>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<GameEvent> recentEvents) {
				// TODO Auto-generated method stub
				for(GameEvent event : recentEvents) {
					if(event instanceof SendCardsEvent) {
						cardsOnHand.addAll(((SendCardsEvent) event).getCards());
						
						drawCardsOnHand();
					}
					if(event instanceof RoundUpdateEvent) {
						onTable = ((RoundUpdateEvent) event).getCardsOnTable();
						hasTurn = ((RoundUpdateEvent) event).getNickWithTurn();
						roundsCompleted = ((RoundUpdateEvent) event).getRoundsCompleted();
						mode = ((RoundUpdateEvent) event).getMode();
						
						drawCardsOnTable();
					}
					if(event instanceof RoundConclusionEvent) {
						onTable = ((RoundConclusionEvent) event).getCardsOnTable();
						roundsCompleted = ((RoundConclusionEvent) event).getRoundsCompleted();
						points = ((RoundConclusionEvent) event).getPoints();
						
						drawCardsOnTable();
					}
					if(event instanceof GameEndEvent) {
						onTable = ((GameEndEvent) event).getCardsOnTable();
						roundsCompleted = ((GameEndEvent) event).getRoundsCompleted();
						winner = ((GameEndEvent) event).getWinner();
						points = ((GameEndEvent) event).getPoints();
						
						drawCardsOnTable();
					}
				}
			}
		});
	}
	
	protected abstract void drawCardsOnHand();
	
	protected abstract void drawCardsOnTable();
}
