package wcg.main;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wcg.games.Player;
import wcg.shared.events.GameEndEvent;
import wcg.shared.events.GameEvent;
import wcg.shared.events.RoundConclusionEvent;
import wcg.shared.events.RoundUpdateEvent;
import wcg.shared.events.SendCardsEvent;

/**
 * A system user, with a user and password. This is a serializable class, hence
 * the password is recorded as a MD5 digest.
 */
public class User implements Serializable, Player {

	private static final long serialVersionUID = 1L;

	private String nick;
	private byte[] password;
	private Map<String, List<GameEvent>> mapOfEvents = new HashMap<>();

	/**
	 * Instantiates a user with given nick and password.
	 * 
	 * @param nick     - of user
	 * @param password - of user
	 */
	User(String nick, String password) {
		this.nick = nick;
		this.password = digest(password);
	}

	/**
	 * Checks if given password authenticates this user.
	 * 
	 * @param password - to check
	 * @return true if passwords match; false otherwise
	 */
	boolean authenticate(String password) {
		if (password == null)
			return false;

		String hashedPassword = Base64.getEncoder().encodeToString(this.password).toUpperCase();
		byte[] digestedPassword = digest(password);
		String hashedDigestedPassword = Base64.getEncoder().encodeToString(digestedPassword).toUpperCase();
		return hashedPassword.equals(hashedDigestedPassword);
	}

	/**
	 * Converts a passwords in a byte array using a MD5 digest (MessageDigest,
	 * MessageDigest.digest()).
	 * 
	 * @param password - to digest
	 * @return byte array
	 */
	byte[] digest(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		return md.digest();
	}

	@Override
	public String getNick() {
		return nick;
	}

	@Override
	public void notify(SendCardsEvent event) {
		String gameID = event.getGameID();
		if (!mapOfEvents.containsKey(gameID)) {
			List<GameEvent> listOfEvents = new ArrayList<>();
			mapOfEvents.put(gameID, listOfEvents);
		}

		mapOfEvents.get(gameID).add(event);
	}

	@Override
	public void notify(RoundUpdateEvent event) {
		String gameID = event.getGameID();
		if (!mapOfEvents.containsKey(gameID)) {
			List<GameEvent> listOfEvents = new ArrayList<>();
			mapOfEvents.put(gameID, listOfEvents);
		}

		mapOfEvents.get(gameID).add(event);
	}

	@Override
	public void notify(RoundConclusionEvent event) {
		String gameID = event.getGameID();
		if (!mapOfEvents.containsKey(gameID)) {
			List<GameEvent> listOfEvents = new ArrayList<>();
			mapOfEvents.put(gameID, listOfEvents);
		}

		mapOfEvents.get(gameID).add(event);
	}

	@Override
	public void notify(GameEndEvent event) {
		String gameID = event.getGameID();
		if (!mapOfEvents.containsKey(gameID)) {
			List<GameEvent> listOfEvents = new ArrayList<>();
			mapOfEvents.put(gameID, listOfEvents);
		}

		mapOfEvents.get(gameID).add(event);
	}

	/**
	 * A list of pending events received by this user. The returned events are
	 * cleared and will not be returned again by this method.
	 * 
	 * @return list of events
	 */
	List<GameEvent> getRecentEvents(String gameID) {

		List<GameEvent> eventsToReturn = new ArrayList<>();
		if (mapOfEvents.containsKey(gameID)) {
			eventsToReturn.addAll(mapOfEvents.get(gameID));
			mapOfEvents.remove(gameID);
		}

		return eventsToReturn;
	}
}
