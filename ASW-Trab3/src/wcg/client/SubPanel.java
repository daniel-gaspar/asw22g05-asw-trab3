package wcg.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * Extends MainPanel by adding the fields username and password
 *
 */
public abstract class SubPanel extends MainPanel {

	protected static String username;
	protected static String password;

	public SubPanel(String username, String password) {
		super(tabPanel, cardGameService, systemMessages);
		SubPanel.username = username;
		SubPanel.password = password;
	}

	/**
	 * Prompts the server to add a bot to a Game with gameId
	 * 
	 * @param gameId - of game
	 */
	protected void addBot(String gameId) {
		cardGameService.addBotPlayer(gameId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessages.setHTML(gameId + ": Adding bots, failed to add bot. " + caught.getMessage());
				addBot(gameId);
			}

			@Override
			public void onSuccess(Void result) {
				systemMessages.setHTML(gameId + ": Bot added.");
			}
		});
	}
}
