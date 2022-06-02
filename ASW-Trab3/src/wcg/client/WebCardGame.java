package wcg.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

public class WebCardGame implements EntryPoint {

	private final CardGameServiceAsync cardGameService = GWT.create(CardGameService.class);

	@Override
	public void onModuleLoad() {
		// Create a tab panel with three tabs, each of which displays a different
		// piece of text.
		TabPanel tabPanel = new TabPanel();
		tabPanel.add(new UserRegistry(tabPanel, cardGameService).getUserRegistry(), "Login/Registration");
		tabPanel.add(new HTML(""), "Select Game");
		tabPanel.add(new HTML(""), "Play");

		// Show the 'bar' tab initially.
		tabPanel.selectTab(0);

		// Add it to the root panel.
		RootPanel.get().add(tabPanel);
	}
}
