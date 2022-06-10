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
import com.google.gwt.user.client.ui.TabPanel;
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
	private final VerticalPanel selectGameModePanel = new VerticalPanel();
	private final ListBox gameList = new ListBox();
	private final VerticalPanel selectGameIDPanel = new VerticalPanel();
	private final HTML gameModeListLabel = new HTML("List of Games:");
	private final ListBox gameIDList = new ListBox();
	private final HTML avlbGamesLabel = new HTML("Available Games:");
	private final Button btnJoinGame = new Button("Join Game");
	private final VerticalPanel selectGameToAddBotsPanel = new VerticalPanel();
	private final HTML ownedGamesLabel = new HTML("Owned Games:");
	private final ListBox ownedGameIDList = new ListBox();
	private final Button btnAddBots = new Button("Add Bots to Game");

	public GameCreation(TabPanel tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(username, password);
		this.gameCreation = onCreationInitialize();
	}

	public Widget getGameCreation() {
		return gameCreation;
	}

	public Widget onCreationInitialize() {
		vPanel.setSpacing(0);

		// Attempts to Fetch the list of Available Game Names
		cardGameService.getGameNames(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				vPanel.add(fetchError);
			}

			@Override
			public void onSuccess(List<String> result) {
				initSelectGameMode(result);
				vPanel.add(selectGameModeWidget);
			}
		});

		return vPanel;
	}

	private void initSelectGameMode(List<String> result) {
		// Panel for choosing gameMode
		selectGameModePanel.setSpacing(10);

		gameList.setMultipleSelect(false);
		gameList.setVisibleItemCount(1);

		for (String gameName : result)
			gameList.addItem(gameName);

		selectGameModePanel.add(gameModeListLabel);
		selectGameModePanel.add(gameList);

		selectGamePanel.add(selectGameModePanel);

		// Panel for list of existing games
		selectGameIDPanel.setSpacing(10);

		gameIDList.ensureDebugId("cwListBox-multiBox");
		gameIDList.setWidth("11em");
		gameIDList.setMultipleSelect(false);
		gameIDList.setVisibleItemCount(10);

		gameList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				populateGameIDList();
			}
		});

		populateGameIDList();

		// Joins registered player to selected game
		btnJoinGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				joinGame();
			}
		});

		selectGameIDPanel.add(avlbGamesLabel);
		selectGameIDPanel.add(gameIDList);
		selectGameIDPanel.add(btnJoinGame);
		selectGamePanel.add(selectGameIDPanel);

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
			public void onSuccess(String gameID) {
				tabPanel.add(new WaitingTab(gameID, FLAG_IS_OWNER).getWaitingTab(), "Play: " + gameID, gameID);
				tabPanel.selectTab(gameID);

				addToGame(gameID, gameName);

				if (!selectGameToAddBotsPanel.isAttached()) {

					selectGameToAddBotsPanel.clear();
					ownedGameIDList.clear();

					selectGameToAddBotsPanel.setSpacing(10);

					ownedGameIDList.ensureDebugId("cwListBox-multiBox");
					ownedGameIDList.setWidth("11em");
					ownedGameIDList.setMultipleSelect(false);
					ownedGameIDList.setVisibleItemCount(10);

					btnAddBots.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							forceStartGame();
						}
					});

					selectGameToAddBotsPanel.add(ownedGamesLabel);
					selectGameToAddBotsPanel.add(ownedGameIDList);
					selectGameToAddBotsPanel.add(btnAddBots);

					selectGamePanel.add(selectGameToAddBotsPanel);
				}

				String itemForList = gameID + " - 1/" + AuxMethods.numberOfPlayers(gameName);
				ownedGameIDList.addItem(itemForList, gameID);
				ownedGameIDList.setSelectedIndex(0);
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
	private void addToGame(String gameId, String name) {

		cardGameService.addPlayer(gameId, username, password, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(gameId + ": Failed adding to game. " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				systemMessages.setHTML(gameId + ": Game successfully joined.");
				populateGameIDList();
			}
		});
	}

	/**
	 * Prompts the server for a List of Available Game Infos, and populates the
	 * GameIDList, depending on the currently selected game
	 */
	private void populateGameIDList() {
		String name = gameList.getSelectedItemText();

		gameIDList.clear();

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
						gameIDList.addItem(itemForList, gameInfo.getGameId());
					}
				}
			}
		});

		gameIDList.addItem(NEW_GAME_STRING, NEW_GAME_STRING);

		gameIDList.setSelectedIndex(0);
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
		String gameID = gameIDList.getSelectedValue();
		String gameName = gameList.getSelectedItemText();

		if (gameID.equals(NEW_GAME_STRING)) {
			createGame(gameName);
		} else {
			cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {

				@Override
				public void onFailure(Throwable caught) {
					systemMessages.setHTML(
							gameID + ": Joining game. Failed to get available game infos. " + caught.getMessage());
				}

				@Override
				public void onSuccess(List<GameInfo> availableGameInfos) {
					for (GameInfo gameInfo : availableGameInfos) {
						if (gameInfo.getGameId().equals(gameID)
								&& isJoinable(gameInfo.getGameName(), gameInfo.getPlayersCount())) {
							addToGame(gameID, gameName);
							tabPanel.add(new WaitingTab(gameID, !FLAG_IS_OWNER).getWaitingTab(), "Play: " + gameID,
									gameID);
							tabPanel.selectTab(gameID);
						}
					}
				}
			});
		}
	}

	/**
	 * Creates a new Tab to Play, depending on the type of game. If the selected
	 * game doesn't currently have enough players to start, adds bots until it does
	 */
	private void forceStartGame() {
		String gameID = ownedGameIDList.getSelectedValue();

		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages
						.setHTML(gameID + ": Adding bots, failed to get available game infos. " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for (GameInfo gameInfo : availableGameInfos) {
					if (gameInfo.getGameId().equals(gameID)) {
						for (int i = gameInfo.getPlayersCount(); i < AuxMethods
								.numberOfPlayers(gameInfo.getGameName()); i++) {
							addBot(gameID);
						}
					}
				}

				ownedGameIDList.removeItem(ownedGameIDList.getSelectedIndex());
				if (ownedGameIDList.getItemCount() == 0)
					selectGamePanel.remove(selectGameToAddBotsPanel);
			}
		});
	}

	/**
	 * Prompts the server to add a bot to a Game with gameID
	 * 
	 * @param gameID - of game
	 */
	private void addBot(String gameID) {
		cardGameService.addBotPlayer(gameID, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(gameID + ": Adding bots, failed to add bot. " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				systemMessages.setHTML(gameID + ": Bot added.");
			}
		});
	}
}