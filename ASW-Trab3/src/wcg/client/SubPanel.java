package wcg.client;

/**
 * 
 * Extends MainPanel by adding the fields username and password
 *
 */
public abstract class SubPanel extends MainPanel {
	
	protected static final boolean FLAG_AUTOBOTS_ROLLOUT = false;

	protected static String username;
	protected static String password;

	public SubPanel(String username, String password) {
		super(tabPanel, cardGameService, systemMessages);
		SubPanel.username = username;
		SubPanel.password = password;
	}

}
