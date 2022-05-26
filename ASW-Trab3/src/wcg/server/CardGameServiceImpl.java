package wcg.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import wcg.client.CardGameService;
import wcg.main.Manager;
import wcg.shared.CardGameException;
import wcg.shared.GameInfo;
import wcg.shared.cards.Card;
import wcg.shared.events.GameEvent;

public class CardGameServiceImpl extends RemoteServiceServlet implements CardGameService {

	private static final long serialVersionUID = 1L;
	
	private Manager manager;
	
	CardGameServiceImpl(){
		try {
			manager = Manager.getInstance();
		} catch (CardGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getGameNames() {
		return manager.getGameNames();
	}

	@Override
	public String createGame(String name) throws CardGameException {
		return manager.createGame(name);
	}

	@Override
	public List<GameInfo> getAvailableGameInfos() {
		return manager.getAvailableGameInfos();
	}

	@Override
	public void registerPlayer(String nick, String password) throws CardGameException {
		manager.registerPlayer(nick, password);
	}

	@Override
	public void addPlayer(String gameId, String nick, String password) throws CardGameException {
		manager.addPlayer(gameId,nick,password);
	}

	@Override
	public void addBotPlayer(String gameId) throws CardGameException {
		manager.addBotPlayer(gameId);
	}

	@Override
	public void playCards(String gameId, String nick, String password, List<Card> cards) throws CardGameException {
		manager.playCards(gameId, nick, password, cards);
	}

	@Override
	public List<GameEvent> getRecentEvents(String nick, String password) throws CardGameException {
		return manager.getRecentEvents(nick, password);
	}

}
