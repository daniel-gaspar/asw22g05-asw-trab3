package wcg.client;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import wcg.shared.GameInfo;

/**
 * 
 * Extends SubPanel by adding the Widget gameCreation
 * 
 * Creates a Panel with a Dropbox of Available Game Names, a ListBox of
 * Available Games with that Name and buttons to Join and Start the Game
 *
 */
public class GameCreation extends SubPanel {

	private static final String NEW_GAME_STRING = "New Game...";
	private static final boolean FLAG_IS_OWNER = true;

	private Widget gameCreation;
	private Widget selectGameModeWidget;

	/**
	 * All the Widgets necessary to create the GameCreation tab
	 */
	private final VerticalPanel vPanel = new VerticalPanel();
	private final HTML fetchError = new HTML("Couldn't fetch list of games");
	private final HorizontalPanel selectGamePanel = new HorizontalPanel();
	private final VerticalPanel selectGameNamePanel = new VerticalPanel();
	private final HTML gameNameListLabel = new HTML("List of Games:");
	private final ListBox gameNameList = new ListBox();
	private final VerticalPanel selectGameIdPanel = new VerticalPanel();
	private final HTML avlbGamesLabel = new HTML("Available Games:");
	private final ListBox gameIdList = new ListBox();
	private final Button btnJoinGame = new Button("Join Game", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			joinGame();
		}
	});
	private final VerticalPanel selectGameToAddBotsPanel = new VerticalPanel();
	private final HTML ownedGamesLabel = new HTML("Owned Games:");
	private final ListBox ownedGameIdList = new ListBox();
	private final Button btnAddBots = new Button("Add Bots to Game", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			forceStartGame();
		}
	});

	public GameCreation(TabPanelIds tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(username, password);
		gameCreation = onCreationInitialize();
	}

	public Widget getGameCreation() {
		return gameCreation;
	}

	public Widget onCreationInitialize() {
		// Attempts to Fetch the list of Available Game Names
		cardGameService.getGameNames(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				vPanel.add(fetchError);
			}

			@Override
			public void onSuccess(List<String> gameNames) {
				initSelectGameMode(gameNames);
				vPanel.add(selectGameModeWidget);
			}
		});

		return vPanel;
	}

	private void initSelectGameMode(List<String> gameNames) {
		// Starts by applying StyleNames and other layout settings to the elements
		applyStylizingSettings();

		// Iterates through the list of Game Names, adding each item to gameList
		for (String gameName : gameNames)
			gameNameList.addItem(gameName);

		// Creates a Handler, making it so that whenever the selected Game Name is
		// changed, the list of Available Games is refreshed
		gameNameList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				populateGameIdList();
			}
		});

		// Adds the Game Names label and list to the corresponding Panel
		selectGameNamePanel.add(gameNameListLabel);
		selectGameNamePanel.add(gameNameList);

		populateGameIdList();

		// Adds the Available Games label and list to the corresponding Panel, as well
		// as the Button to Join the Game
		selectGameIdPanel.add(avlbGamesLabel);
		selectGameIdPanel.add(gameIdList);
		selectGameIdPanel.add(btnJoinGame);

		// Adds the Sub-Panels to the Main-Panel of the Widget
		selectGamePanel.add(selectGameNamePanel);
		selectGamePanel.add(selectGameIdPanel);

		selectGameModeWidget = selectGamePanel;
	}

	/**
	 * Prompts the server to create a game, and adds Player to it
	 * 
	 * @param gameName - Name of Game to create
	 */
	private void createGame(String gameName) {

		cardGameService.createGame(gameName, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML("Failed to create a game: " + caught.getMessage());
			}

			@Override
			public void onSuccess(String gameId) {

				addToGame(gameId, gameName, FLAG_IS_OWNER);

				if (!selectGameToAddBotsPanel.isAttached()) {

					// Clears the Panel, since it won't always be present, removing any previously
					// present elements
					selectGameToAddBotsPanel.clear();
					ownedGameIdList.clear();

					// Adds the Owned Games label and list to the corresponding Panel, as well as
					// the Button to Add Bots
					selectGameToAddBotsPanel.add(ownedGamesLabel);
					selectGameToAddBotsPanel.add(ownedGameIdList);
					selectGameToAddBotsPanel.add(btnAddBots);

					// Adds the Sub-Panel to the Main-Panel of the Widget
					selectGamePanel.add(selectGameToAddBotsPanel);
				}

				// Adds the newly created gameId to the list of owned Games
				String itemForList = gameId + " - 1/" + AuxMethods.numberOfPlayers(gameName);
				ownedGameIdList.addItem(itemForList, gameId);
				ownedGameIdList.setSelectedIndex(0);
			}
		});
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
		if (currentPlayers < AuxMethods.numberOfPlayers(name))
			return true;
		return false;
	}

	/**
	 * Prompts the server to add the player to a game
	 * 
	 * @param gameId - of game
	 * @param name   - of game
	 */
	private void addToGame(String gameId, String name, boolean isOwner) {

		cardGameService.addPlayer(gameId, username, password, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(gameId + ": Failed adding to game. " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				systemMessages.setHTML(gameId + ": Game successfully joined.");
				tabPanel.add(new WaitingTab(gameId, isOwner).getWaitingTab(), "Play: " + gameId, gameId);
				tabPanel.selectTab(gameId);
				populateGameIdList();
			}
		});
	}

	/**
	 * Prompts the server for a List of Available Game Infos, and populates the
	 * gameIdList, depending on the currently selected game
	 */
	private void populateGameIdList() {
		String name = gameNameList.getSelectedItemText();

		gameIdList.clear();

		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML("Fetching available game infos failure: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					if (gameInfo.getGameName().equals(name)) {
						String itemForList = gameInfo.getGameId() + " - " + gameInfo.getPlayersCount() + "/"
								+ AuxMethods.numberOfPlayers(name);
						gameIdList.addItem(itemForList, gameInfo.getGameId());
					}
				}
			}
		});

		gameIdList.addItem(NEW_GAME_STRING, NEW_GAME_STRING);
		gameIdList.setSelectedIndex(0);
	}

	/**
	 * <ul>
	 * Checks the selected gameID
	 * <li>If it's been selected to create a new game, prompt the server to do so
	 * <li>If an already existing game is selected, prompts the server to add the
	 * player to it, after checking whether it's joinable
	 * </ul>
	 */
	private void joinGame() {
		String gameId = gameIdList.getSelectedValue();
		String gameName = gameNameList.getSelectedItemText();

		if (gameId.equals(NEW_GAME_STRING)) {
			createGame(gameName);
		} else {
			cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {

				@Override
				public void onFailure(Throwable caught) {
					systemMessages.setHTML(
							gameId + ": Joining game. Failed to get available game infos. " + caught.getMessage());
				}

				@Override
				public void onSuccess(List<GameInfo> availableGameInfos) {
					for (GameInfo gameInfo : availableGameInfos) {
						if (gameInfo.getGameId().equals(gameId)
								&& isJoinable(gameInfo.getGameName(), gameInfo.getPlayersCount())) {
							addToGame(gameId, gameName, !FLAG_IS_OWNER);
						}
					}
				}
			});
		}
	}

	/**
	 * <p>
	 * Checks the current Number of Players in a Game with gameId, and adds Bots for
	 * the rest of missing players
	 * </p>
	 * <p>
	 * Then removes the gameId from the list of Owned Games, so that the User can't
	 * attempt to add Bots again
	 * </p>
	 */
	private void forceStartGame() {
		String gameId = ownedGameIdList.getSelectedValue();

		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages
						.setHTML(gameId + ": Adding bots, failed to get available game infos. " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					if (gameInfo.getGameId().equals(gameId)) {
						for (int i = gameInfo.getPlayersCount(); i < AuxMethods
								.numberOfPlayers(gameInfo.getGameName()); i++) {
							addBot(gameId);
						}
					}
				}

				ownedGameIdList.removeItem(ownedGameIdList.getSelectedIndex());
				if (ownedGameIdList.getItemCount() == 0)
					selectGamePanel.remove(selectGameToAddBotsPanel);
			}
		});
	}

	/**
	 * Applies the diverse StyleNames and other layout settings to the elements
	 */
	private void applyStylizingSettings() {
		gameCreation.setStyleName("wcg-Panel");

		vPanel.setSpacing(0);

		selectGameNamePanel.setSpacing(10);

		gameNameListLabel.setStyleName("wcg-Text");

		gameNameList.setMultipleSelect(false);
		gameNameList.setVisibleItemCount(1);
		gameNameList.addStyleName("wcg-Text");

		selectGameIdPanel.setSpacing(10);

		avlbGamesLabel.setStyleName("wcg-Text");

		gameIdList.ensureDebugId("cwListBox-multiBox");
		gameIdList.setWidth("11em");
		gameIdList.setMultipleSelect(false);
		gameIdList.setVisibleItemCount(10);
		gameIdList.addStyleName("wcg-Text");

		btnJoinGame.addStyleName("wcg-Text");

		selectGameToAddBotsPanel.setSpacing(10);

		ownedGamesLabel.setStyleName("wcg-Text");

		ownedGameIdList.ensureDebugId("cwListBox-multiBox");
		ownedGameIdList.setWidth("11em");
		ownedGameIdList.setMultipleSelect(false);
		ownedGameIdList.setVisibleItemCount(10);
		ownedGameIdList.addStyleName("wcg-Text");

		btnAddBots.addStyleName("wcg-Text");
	}
}