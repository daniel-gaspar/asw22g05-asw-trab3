package wcg.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * Abstract panel with references to tabPanel and cardGameService
 *
 */
public abstract class MainPanel {

	protected static TabPanel tabPanel;
	protected static CardGameServiceAsync cardGameService;
	protected static HTML messages;

	public MainPanel(TabPanel tabPanel, CardGameServiceAsync cardGameService, HTML messages) {
		MainPanel.tabPanel = tabPanel;
		MainPanel.cardGameService = cardGameService;
		MainPanel.messages = messages;
	}
}
