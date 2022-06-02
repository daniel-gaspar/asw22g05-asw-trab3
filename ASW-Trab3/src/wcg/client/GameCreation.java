package wcg.client;

import java.util.ArrayList;
import java.util.List;

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
 * Creates a Panel with a list of Available Game Names and a button to Start the
 * Game
 *
 */
public class GameCreation extends SubPanel {

	private Widget gameCreation;
	/**
	 * Main panel of the widget
	 */
	private VerticalPanel vPanel = new VerticalPanel();
	private final HTML fetchError = new HTML("Couldn't fetch list of games");

	/**
	 * Each Widget represents a state of the gameCreation widget
	 * selectGameModeWidget: - Initial state: Shows a list of game mode available,
	 * displays an error if fails to fetch createNewGameWidget: - If there are no
	 * game available, allows the user to create a new game - Added at the final of
	 * joinExistingGameWidget joinExistingGameWidget: - Allows the user to join an
	 * existing game or create a new one
	 */
	private Widget selectGameModeWidget;

	public GameCreation(TabPanel tabPanel, String username, String password, CardGameServiceAsync cardGameService) {
		super(tabPanel, username, password, cardGameService);
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

			// Creates a ListBox with the returned list and a Button to start the game
			@Override
			public void onSuccess(List<String> result) {
				initSelectGameMode(result);
				vPanel.add(selectGameModeWidget);
			}
		});

		return vPanel;
	}

	private void initSelectGameMode(List<String> result) {
		HorizontalPanel selectGamePanel = new HorizontalPanel();

		// Panel for choosing gameMode
		VerticalPanel selectGameModePanel = new VerticalPanel();
		selectGameModePanel.setSpacing(10);
		final ListBox gameList = new ListBox();
		gameList.setMultipleSelect(false);
		gameList.setVisibleItemCount(1);

		for (String gameName : result)
			gameList.addItem(gameName);

		HTML gameModeListLabel = new HTML("List of Games: ");
		Button btn_createNewGame = new Button("Create game");
		selectGameModePanel.add(gameModeListLabel);
		selectGameModePanel.add(gameList);
		selectGameModePanel.add(btn_createNewGame);
		selectGamePanel.add(selectGameModePanel);

		// Creates player and adds him to the game
		btn_createNewGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String gameID = createGame(gameList.getSelectedItemText());
				cardGameService.addPlayer(gameID, username, password, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
					}

				});

			}
		});

		// Panel for list of existing games
		VerticalPanel selectGameIDPanel = new VerticalPanel();
		selectGameIDPanel.setSpacing(10);
		final ListBox multiBox = new ListBox();
		final HTML gamesList = new HTML("Available Games");
		final Button btn_joinGame = new Button("Join Game");
		multiBox.ensureDebugId("cwListBox-multiBox");
		multiBox.setWidth("11em");
		multiBox.setVisibleItemCount(10);

		// Get list of existing games
		gameList.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<GameInfo> result) {
						// Refresh the multibox
						for (int i = 0; i < multiBox.getItemCount(); i++) {
							multiBox.removeItem(i);
						}
						if (result.size() > 0) {
							for (GameInfo g : result) {
								if (gameList.getSelectedItemText().equals(g.getGameName())) {
									String gameLabel = g.getGameId() + ": " + g.getPlayersCount() + "/4"; // TODO - add
																											// field to
																											// get max
																											// players
																											// per this
																											// game
									multiBox.addItem(gameLabel);
								}
							}
							selectGameIDPanel.add(gamesList);
							selectGameIDPanel.add(multiBox);
							selectGameIDPanel.add(btn_joinGame);
							selectGamePanel.add(selectGameIDPanel);
						}
					}
				});
			}
		});

		// Joins registered player to selected game
		btn_joinGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String gameID = multiBox.getSelectedValue();

				if (multiBox.getSelectedValue().equals(null)) {
					// TODO - error did not select correct value
				}

				else {
					addToGame(gameID,gameList.getSelectedItemText());
				}
			}

		});

		selectGameModeWidget = selectGamePanel;
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
				vPanel.add(new HTML("Failed to create a game: " + caught.getCause()));
			}

			@Override
			public void onSuccess(String gameId) {
				vPanel.add(new HTML("Game Sucessfully created go To Play"));
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
