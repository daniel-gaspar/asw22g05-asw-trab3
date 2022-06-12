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
	private final TabPanelIds tabPanel = new TabPanelIds();
	private final TabBar tabPanelBar = tabPanel.getTabBar();
	private final DeckPanel tabPanelDeck = tabPanel.getDeckPanel();
	private final HorizontalPanel systemMessagesPanel = new HorizontalPanel();
	private final HTML systemMessagesStatic = new HTML("System Messages: ");
	private final HTML systemMessages = new HTML("");
	private final HorizontalPanel selectGameNotLoggedIn = new HorizontalPanel();
	private final HTML notLoggedIn = new HTML("Not logged in yet");

	@Override
	public void onModuleLoad() {
		// Starts by applying StyleNames and other layout settings to the elements
		applyStylizingSettings();

		// Create a tab panel with two tabs, each of which displays a different
		// piece of text.
		tabPanel.add(new UserRegistry(tabPanel, cardGameService, systemMessages).getUserRegistry(),
				"Login/Registration");

		// Prepares the elements for the 'Select Game' tab, and adds it to tabPanel
		selectGameNotLoggedIn.add(notLoggedIn);
		tabPanel.add(selectGameNotLoggedIn, "Select Game");

		// Show the 'Login/Registry' tab initially.
		tabPanel.selectTab(0);

		// Prepares a Panel for System Messages (caught errors, and others)
		systemMessagesPanel.add(systemMessagesStatic);
		systemMessagesPanel.add(systemMessages);

		// Add the elements to the root panel
		rootPanel.add(systemMessagesPanel);
		rootPanel.add(tabPanel);
	}

	/**
	 * Applies the diverse StyleNames and other layout settings to the elements
	 */
	private void applyStylizingSettings() {
		tabPanel.setStyleName("wcg-TabPanel");

		tabPanelBar.addStyleName("wcg-TabPanelBar");
		tabPanelDeck.addStyleName("wcg-TabPanelDeck");

		systemMessagesPanel.setSpacing(5);
		systemMessagesStatic.setStyleName("wcg-Text");
		systemMessages.setStyleName("wcg-Text");

		selectGameNotLoggedIn.setStyleName("wcg-Panel");
		notLoggedIn.setStyleName("wcg-Text");
	}
}