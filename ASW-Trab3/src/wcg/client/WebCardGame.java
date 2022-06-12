package wcg.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;

public class WebCardGame implements EntryPoint {

	private final CardGameServiceAsync cardGameService = GWT.create(CardGameService.class);

	private final RootPanel rootPanel = RootPanel.get();

	/**
	 * All the Widgets necessary to add to the RootPanel tab
	 */
	private final TabPanelTitles tabPanel = new TabPanelTitles();
	private final HorizontalPanel systemMessagesPanel = new HorizontalPanel();
	private final HTML systemMessagesStatic = new HTML("System Messages: ");
	private final HTML systemMessages = new HTML("");

	@Override
	public void onModuleLoad() {
		tabPanel.setStyleName("wcg-TabPanel");
		// Create a tab panel with two tabs, each of which displays a different
		// piece of text.
		tabPanel.add(new UserRegistry(tabPanel, cardGameService, systemMessages).getUserRegistry(),
				"Login/Registration");

		HorizontalPanel selectGameNotLoggedIn = new HorizontalPanel();
		selectGameNotLoggedIn.setStyleName("wcg-Panel");
		HTML notLoggedIn = new HTML("Not logged in yet");
		notLoggedIn.setStyleName("wcg-Text");
		selectGameNotLoggedIn.add(notLoggedIn);
		tabPanel.add(selectGameNotLoggedIn, "Select Game");

		// Show the 'Login/Registry' tab initially.
		tabPanel.selectTab(0);

		// Create a panel for System Messages (caught errors, and others)
		systemMessagesPanel.setSpacing(5);
		systemMessagesStatic.setStyleName("wcg-Text");
		systemMessages.setStyleName("wcg-Text");
		systemMessagesPanel.add(systemMessagesStatic);
		systemMessagesPanel.add(systemMessages);

		// Add the elements to the root panel
		rootPanel.add(systemMessagesPanel);

		TabBar tabBar = tabPanel.getTabBar();
		DeckPanel deckPanel = tabPanel.getDeckPanel();
		tabBar.addStyleName("wcg-TabPanelBar");
		deckPanel.addStyleName("wcg-TabPanelDeck");
		rootPanel.add(tabPanel);
	}
}
