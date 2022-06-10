package wcg.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * Extends MainPanel adding the Widget userRegistry
 *
 */
public class UserRegistry extends MainPanel {

	private Widget userRegistry;

	/**
	 * All the Widgets necessary to create the UserRegistry tab
	 */
	private final VerticalPanel vPanel = new VerticalPanel();
	private final HTML usernameLabel = new HTML("Username: ");
	private final TextBox usernameBox = new TextBox();
	private final HTML passwordLabel = new HTML("Password: ");
	private final PasswordTextBox passwordBox = new PasswordTextBox();

	public UserRegistry(TabPanelTitles tabPanel, CardGameServiceAsync cardGameService, HTML messages) {
		super(tabPanel, cardGameService, messages);
		this.userRegistry = onRegisterInitialize();
	}

	public Widget getUserRegistry() {
		return userRegistry;
	}

	public Widget onRegisterInitialize() {
		// Create a panel to layout the widgets
		vPanel.setSpacing(5);

		// Username
		usernameBox.ensureDebugId("regUsrTxtBox");
		vPanel.add(usernameLabel);
		vPanel.add(usernameBox);

		// Password
		passwordBox.ensureDebugId("regPwdBox");
		vPanel.add(passwordLabel);
		vPanel.add(passwordBox);

		// Add a normal button
		Button registerButton = new Button("Register", new ClickHandler() {
			public void onClick(ClickEvent event) {
				String username = usernameBox.getText().toString();
				String password = passwordBox.getText().toString();
				System.out.println("username: " + username + " | pwd: " + password);
				cardGameService.registerPlayer(username, password, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						systemMessages.setHTML("Login not successful: " + caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						systemMessages.setHTML("Login successful");

						tabPanel.clear();

						// Adds a new tab with only the Username
						tabPanel.add(new HTML("Username: " + username), "User");

						// Adds a new "Select Game" tab
						tabPanel.add(new GameCreation(tabPanel, username, password, cardGameService).getGameCreation(),
								"Select Game", SELECT_GAME_TAB);

						tabPanel.selectTab(1);
					}
				});
			}
		});
		registerButton.ensureDebugId("cwBasicButton-normal");
		vPanel.add(registerButton);

		// Return the panel
		return vPanel;
	}
}
