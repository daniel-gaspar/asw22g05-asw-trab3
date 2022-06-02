package wcg.client;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * Abstract panel with references to tabPanel and cardGameService
 *
 */
public abstract class MainPanel {

	protected static TabPanel tabPanel;
	protected static CardGameServiceAsync cardGameService;

	public MainPanel(TabPanel tabPanel, CardGameServiceAsync cardGameService) {
		MainPanel.tabPanel = tabPanel;
		MainPanel.cardGameService = cardGameService;
	}
}
