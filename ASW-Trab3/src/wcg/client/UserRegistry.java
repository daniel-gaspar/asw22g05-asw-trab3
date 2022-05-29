package wcg.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserRegistry {

	private Widget userRegistry;

	public UserRegistry() {
		this.userRegistry = OnRegisterInitialize();
	}

	public Widget getUserRegistry() {
		return userRegistry;
	}

	private final CardGameServiceAsync cgsa = GWT.create(CardGameService.class);

	public Widget OnRegisterInitialize() {
		// Create a panel to layout the widgets
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setSpacing(5);

		// Username
		TextBox username = new TextBox();
		username.ensureDebugId("regUsrTxtBox");
		vpanel.add(new HTML("Username: "));
		vpanel.add(username);
		
		// Password
		PasswordTextBox pwd = new PasswordTextBox();
		pwd.ensureDebugId("regPwdBox");
		vpanel.add(new HTML("Password: "));
		vpanel.add(pwd);
		
		// Add a normal button
	    Button normalButton = new Button(
	        "Register", new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	cgsa.registerPlayer(username.getText().toString(), pwd.getText().toString(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						System.out.println(caught);
						vpanel.add(new HTML("Login not sucessful"));
					}

					@Override
					public void onSuccess(Void result) {
						vpanel.add(new HTML("Login sucessful"));
					}
	        		
	        	});
	        	
	          }
	        });
	    normalButton.ensureDebugId("cwBasicButton-normal");
	    vpanel.add(normalButton);
				

		// Return the panel
		return vpanel;
	}

}
