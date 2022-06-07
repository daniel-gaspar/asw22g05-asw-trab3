package wcg.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

public class WebCardGame implements EntryPoint {

	private final CardGameServiceAsync cardGameService = GWT.create(CardGameService.class);

	private final TabPanel tabPanel = new TabPanel();
	private final RootPanel rootPanel = RootPanel.get();
	private final HTML systemMessages = new HTML("System Messages: ");
	private final HTML messages = new HTML("");

	@Override
	public void onModuleLoad() {
		// Create a tab panel with three tabs, each of which displays a different
		// piece of text.
		tabPanel.add(new UserRegistry(tabPanel, cardGameService, messages).getUserRegistry(), "Login/Registration");
		tabPanel.add(new HTML("Not logged in yet"),"Select Game");
		tabPanel.add(new HTML("No game has yet started"),"Play");
		// Show the 'bar' tab initially.
		tabPanel.selectTab(0);

		// Add it to the root panel.
		rootPanel.add(systemMessages);
		rootPanel.add(messages);
		rootPanel.add(tabPanel);
	}
}
