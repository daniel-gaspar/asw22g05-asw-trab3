package wcg.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import wcg.shared.GameInfo;

/**
 * 
 * Extends SubPanel by adding the Widget gameCreation
 * 
 * Creates a Panel with a list of Available Game Names and a button to Start the
 * Game
 *
 */
public class GameCreation extends SubPanel {

	private Widget gameCreation;

	public GameCreation(TabPanel tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(tabPanel, username, password, cardGameService);
		this.gameCreation = onCreationInitialize();
	}

	public Widget getGameCreation() {
		return gameCreation;
	}

	public Widget onCreationInitialize() {

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);

		// Attempts to Fetch the list of Available Game Names
		cardGameService.getGameNames(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				vPanel.add(new HTML("Couldn't fetch list of games"));
			}

			// Creates a ListBox with the returned list and a Button to start the game
			@Override
			public void onSuccess(List<String> result) {
				final ListBox gameList = new ListBox();
				gameList.setMultipleSelect(false);
				gameList.setVisibleItemCount(1);

				for (String gameName : result)
					gameList.addItem(gameName);

				vPanel.add(new HTML("List of Games: "));
				vPanel.add(gameList);

				// Checks which game is selected in gameList and starts a game (or joins one)
				Button startGameButton = new Button("Start Game", new ClickHandler() {
					public void onClick(ClickEvent event) {
						String selectedGame = gameList.getSelectedItemText();

						cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
							@Override
							public void onFailure(Throwable caught) {
								// TODO - implement onFailure getAvailableGameInfos
							}

							@Override
							public void onSuccess(List<GameInfo> result) {
								joinGame(result, selectedGame);
							}
						});
					}
				});

				startGameButton.ensureDebugId("cwBasicButton-normal");
				vPanel.add(startGameButton);
			}
		});

		return vPanel;
	}

	/**
	 * 
	 * Checks if there are any games of the selected type already available, and if
	 * they can be joined
	 * 
	 * If one can be joined, join it. Otherwise, create a game and then join it.
	 * 
	 * @param gameInfos    - from getAvailableGameInfos
	 * @param selectedGame - from gameList selection
	 */
	private void joinGame(List<GameInfo> gameInfos, String selectedGame) {
		List<GameInfo> gamesMatchingName = new ArrayList<>();

		for (GameInfo game : gameInfos) {
			if (game.getGameName().equals(selectedGame))
				gamesMatchingName.add(game);
		}

		String gameToJoin = null;

		if (gamesMatchingName.size() > 0)
			for (GameInfo game : gamesMatchingName) {
				if (isJoinable(selectedGame, game.getPlayersCount())) {
					gameToJoin = game.getGameId();
					break;
				}
			}

		if (gameToJoin == null)
			gameToJoin = createGame(selectedGame);

		addToGame(gameToJoin, selectedGame);
	}

	/**
	 * Prompts the server to create a game, and returns the new gameId on success
	 * 
	 * @param gameName - Name of Game to create
	 * @return Id of new game
	 */
	private String createGame(String gameName) {

		final List<String> gameToJoin = new ArrayList<>();

		cardGameService.createGame(gameName, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO - implement onFailure createGame
			}

			@Override
			public void onSuccess(String gameId) {
				gameToJoin.add(gameId);
			}
		});

		return gameToJoin.get(0);
	}

	/**
	 * Verifies whether the game can be joined or not, depending on it's name and
	 * the current amount players
	 * 
	 * @param name           - of game
	 * @param currentPlayers - already in the game
	 * @return Whether the game can be joined or not
	 */
	private boolean isJoinable(String name, int currentPlayers) {
		if ("HEARTS".equals(name) && currentPlayers < 4)
			return true;
		if ("WAR".equals(name) && currentPlayers < 2)
			return true;
		return false;
	}

	/**
	 * Prompts the server to add the player to a game
	 * 
	 * @param gameId - of game
	 * @param name   - of game
	 */
	private void addToGame(String gameId, String name) {

		cardGameService.addPlayer(gameId, username, password, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO - implement onFailure addPlayer
			}

			@Override
			public void onSuccess(Void result) {
				tabPanel.remove(2);
				if ("WAR".equals(name)) {
					tabPanel.add(new GamePlayWAR(tabPanel, username, password, cardGameService).getGamePlayWAR(),
							"Play");
				}
				if ("HEARTS".equals(name)) {
					tabPanel.add(new GamePlayHEARTS(tabPanel, username, password, cardGameService).getGamePlayHEARTS(),
							"Play");
				}
			}
		});
	}
}
