package wcg.main;

import java.util.List;

import wcg.shared.CardGameException;
import wcg.shared.GameInfo;
import wcg.shared.cards.Card;
import wcg.shared.events.GameEvent;

public interface ManagerInterface {

	/**
	 * A list of available game names, required to create a new game instance.
	 * 
	 * @return list of game names
	 */
	List<String> getGameNames();

	/**
	 * Create a new game instance of the game with given name. The game name must be
	 * one in the list returned by getGameNames().
	 * 
	 * @param name - of game
	 * @return a game instance id
	 * @throws CardGameException - if name is invalid
	 */
	String createGame(String name) throws CardGameException;

	/**
	 * A list with information on games available to be played. Information on games
	 * includes their gameId, required join and play in a specific game instance.
	 * 
	 * @return list of games
	 */
	List<GameInfo> getAvailableGameInfos();

	/**
	 * Register a human player (an user) to participate in card games.
	 * 
	 * @param nick     - of player
	 * @param password - of player
	 * @throws CardGameException
	 */
	void registerPlayer(String nick, String password) throws CardGameException;

	/**
	 * Add a player to a given game instance. The player must have been previously
	 * registered, and the game must be accepting players, otherwise an exception is
	 * raised. The game will automatically start when the required number of players
	 * is added to the game.
	 * 
	 * @param gameId   - of game instance to join
	 * @param nick     - of player
	 * @param password - of player
	 * @throws CardGameException - if gameId is invalid or player authentication
	 *                           failed
	 */
	void addPlayer(String gameId, String nick, String password) throws CardGameException;

	/**
	 * Add a bot to the given game instance. Adding a bot will allow users to play,
	 * event if other human opponents are not available. The game will automatically
	 * start when the required number of players is added to the game.
	 * 
	 * @param gameId - of game instance to add player
	 * @throws CardGameException - if gameId is invalid
	 */
	void addBotPlayer(String gameId) throws CardGameException;

	/**
	 * Play cards on a game on behalf of an authenticated user.
	 * 
	 * @param gameId   - of game instance
	 * @param nick     - of player
	 * @param password - of player
	 * @param cards    - to play
	 * @throws CardGameException - if gameId is invalid, authentication fails, or
	 *                           given cards cannot be played
	 */
	void playCards(String gameId, String nick, String password, List<Card> cards) throws CardGameException;

	/**
	 * Get a list of recent events sent to the given user by game instances. This
	 * list may contain multiple events, sent from multiple game instances. The
	 * returned events will not returned again by this method.
	 * 
	 * @param nick     - of player
	 * @param password - of player
	 * @return list of events
	 * @throws CardGameException - if authentication fails
	 */
	List<GameEvent> getRecentEvents(String gameID, String nick, String password) throws CardGameException;

}