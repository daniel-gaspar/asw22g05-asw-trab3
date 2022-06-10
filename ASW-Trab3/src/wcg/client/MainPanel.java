package wcg.client;

import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * Abstract panel with references to tabPanel and cardGameService
 *
 */
public abstract class MainPanel {

	protected static TabPanelTitles tabPanel;
	protected static CardGameServiceAsync cardGameService;
	protected static HTML systemMessages;

	protected static final String SELECT_GAME_TAB = "selectGameTab";

	public MainPanel(TabPanelTitles tabPanel, CardGameServiceAsync cardGameService, HTML messages) {
		MainPanel.tabPanel = tabPanel;
		MainPanel.cardGameService = cardGameService;
		MainPanel.systemMessages = messages;
	}
}
