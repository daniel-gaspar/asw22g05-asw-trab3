package wcg.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

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
		// Create a tab panel with two tabs, each of which displays a different
		// piece of text.
		tabPanel.add(new UserRegistry(tabPanel, cardGameService, systemMessages).getUserRegistry(),
				"Login/Registration");
		tabPanel.add(new HTML("Not logged in yet"), "Select Game");

		// Show the 'Login/Registry' tab initially.
		tabPanel.selectTab(0);

		// Create a panel for System Messages (caught errors, and others)
		systemMessagesPanel.setSpacing(5);
		systemMessagesPanel.add(systemMessagesStatic);
		systemMessagesPanel.add(systemMessages);

		// Add the elements to the root panel
		rootPanel.add(systemMessagesPanel);
		rootPanel.add(tabPanel);
	}
}
