package wcg.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import wcg.shared.GameInfo;
import wcg.shared.cards.Card;
import wcg.shared.events.GameEvent;

public interface CardGameServiceAsync {

	public void addBotPlayer(String gameId, AsyncCallback<Void> callback);

	public void addPlayer(String gameId, String nick, String password, AsyncCallback<Void> callback);

	public void createGame(String name, AsyncCallback<String> callback);

	public void getAvailableGameInfos(AsyncCallback<List<GameInfo>> callback);

	public void getGameNames(AsyncCallback<List<String>> callback);

	public void getRecentEvents(String gameID, String nick, String password, AsyncCallback<List<GameEvent>> callback);

	public void playCards(String gameId, String nick, String password, List<Card> cards, AsyncCallback<Void> callback);

	public void registerPlayer(String nick, String password, AsyncCallback<Void> callback);
}
