/**
 * 
 */
package wcg.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import wcg.shared.CardGameException;
import wcg.shared.GameInfo;
import wcg.shared.cards.Card;
import wcg.shared.events.GameEvent;

@RemoteServiceRelativePath("wcg")
public interface CardGameService extends RemoteService {

	public List<String> getGameNames();

	public String createGame(String name) throws CardGameException;

	public List<GameInfo> getAvailableGameInfos();

	public void registerPlayer(String nick, String password) throws CardGameException;

	public void addPlayer(String gameId, String nick, String password) throws CardGameException;

	public void addBotPlayer(String gameId) throws CardGameException;

	public void playCards(String gameId, String nick, String password, List<Card> cards) throws CardGameException;

	public List<GameEvent> getRecentEvents(String gameID, String nick, String password) throws CardGameException;
}
