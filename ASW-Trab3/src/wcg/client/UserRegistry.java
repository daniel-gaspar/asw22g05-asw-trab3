package wcg.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.FieldVerifier;

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
	private final HorizontalPanel userPanel = new HorizontalPanel();
	private final HTML newUsernameLabel = new HTML("");
	private final Button registerButton = new Button("Register", new ClickHandler() {
		public void onClick(ClickEvent event) {
			registerPlayer();
		}
	});

	public UserRegistry(TabPanelIds tabPanel, CardGameServiceAsync cardGameService, HTML messages) {
		super(tabPanel, cardGameService, messages);
		userRegistry = onRegisterInitialize();
	}

	public Widget getUserRegistry() {
		return userRegistry;
	}

	public Widget onRegisterInitialize() {
		// Starts by applying StyleNames and other layout settings to the elements
		applyStylizingSettings();

		// Adds the elements of Username to the VerticalPanel
		usernameBox.ensureDebugId("regUsrTxtBox");
		vPanel.add(usernameLabel);
		vPanel.add(usernameBox);

		// Adds the elements of Password to the VerticalPanel
		passwordBox.ensureDebugId("regPwdBox");
		vPanel.add(passwordLabel);
		vPanel.add(passwordBox);

		// Adds the Button to the VerticalPanel
		registerButton.ensureDebugId("cwBasicButton-normal");
		vPanel.add(registerButton);

		// Return the panel
		return vPanel;
	}

	/**
	 * <p>
	 * Prompts the server to Register a Player, after verifying that the Fields are
	 * valid
	 * </p>
	 * <p>
	 * When Successful, Creates new Tabs
	 * </p>
	 */
	private void registerPlayer() {
		String username = usernameBox.getText().toString();
		String password = passwordBox.getText().toString();

		if (!FieldVerifier.isValidName(username))
			systemMessages.setHTML("Invalid user, must have 4 or more characters");
		else if (!FieldVerifier.isValidName(password))
			systemMessages.setHTML("Invalid password, must have 4 or more characters");
		else {
			cardGameService.registerPlayer(username, password, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					systemMessages.setHTML("Login not successful: " + caught.getMessage());
				}

				@Override
				public void onSuccess(Void result) {
					systemMessages.setHTML("Login successful");

					// As Login/Registry was successful, the existing tabs are cleared, and a new
					// Tab with just the username is created, as well as another with the proper
					// GameCreation
					tabPanel.clear();

					// Adds a new tab with only the Username
					newUsernameLabel.setHTML("Username: " + username);
					userPanel.add(newUsernameLabel);
					tabPanel.add(userPanel, "User");

					// Adds a new "Select Game" tab
					tabPanel.add(new GameCreation(tabPanel, username, password, cardGameService).getGameCreation(),
							"Select Game", SELECT_GAME_TAB);

					tabPanel.selectTab(SELECT_GAME_TAB);
				}
			});
		}
	}

	/**
	 * Applies the diverse StyleNames and other layout settings to the elements
	 */
	private void applyStylizingSettings() {
		vPanel.setSpacing(5);
		vPanel.setStyleName("wcg-Panel");
		vPanel.addStyleName("wcg-UserRegistry");

		usernameLabel.setStyleName("wcg-Text");
		usernameBox.setStyleName("wcg-Text");

		passwordLabel.setStyleName("wcg-Text");
		passwordBox.setStyleName("wcg-Text");

		userPanel.setStyleName("wcg-Panel");
		newUsernameLabel.setStyleName("wcg-Text");

		registerButton.addStyleName("wcg-Text");
	}
}