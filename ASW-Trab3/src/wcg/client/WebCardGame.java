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
		// TODO Auto-generated method stub
		// Create a tab panel with three tabs, each of which displays a different
	    // piece of text.
	    TabPanel tp = new TabPanel();
	    tp.add(new HTML("Foo"), "foo");
	    tp.add(new HTML("Bar"), "bar");
	    tp.add(new HTML("Baz"), "baz");

	    // Show the 'bar' tab initially.
	    tp.selectTab(1);

	    // Add it to the root panel.
	    RootPanel.get().add(tp);
	}

}
