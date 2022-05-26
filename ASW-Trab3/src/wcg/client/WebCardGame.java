package wcg.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class WebCardGame implements EntryPoint {
	
	private final CardGameServiceAsync cardGameService = GWT.create(CardGameService.class);

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub

	}

}
