package wcg.client;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class GamePlayHEARTS extends GamePlay {

	/**
	 * The Widgets which will belong in centerPanel and southPanel of GamePlay,
	 * respectively
	 */
	private final DockPanel cardsOnTablePanel = new DockPanel();
	private final HorizontalPanel cardsOnHandPanel = new HorizontalPanel();

	private final Map<String, DockLayoutConstant> playerPosition = new HashMap<>();

	public GamePlayHEARTS(String gameId) {
		super(gameId);
	}

	/**
	 * Clears the current Panel containing Cards on Hand, and then uses the List of
	 * Cards to draw them, adding a ClickHandler to each Image Card, which prompts
	 * the server to Play the selected card
	 */
	@Override
	protected Widget drawCardsOnHand() {
		cardsOnHandPanel.clear();
		for (Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Logger logger = Logger.getLogger("nameOfLogger");

					logger.log(Level.SEVERE, c.toString());
					logger.log(Level.SEVERE, getCardsOnHand().toString());

					cardGameService.playCards(getGameId(), username, password, Arrays.asList(c),
							new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									systemMessages
											.setHTML(getGameId() + ": Failed to play card. " + caught.getMessage());
									logger.log(Level.SEVERE, getGameId());
									logger.log(Level.SEVERE, username);
									logger.log(Level.SEVERE, password);
									logger.log(Level.SEVERE, c.toString());
									logger.log(Level.SEVERE, "Not managing to play");
								}

								@Override
								public void onSuccess(Void result) {
									logger.log(Level.SEVERE, "Successful play");
									getCardsOnHand().remove(c);
									card.removeFromParent();
								}
							});
				}
			});

			cardsOnHandPanel.add(card);
		}
		return cardsOnHandPanel;
	}

	/**
	 * <p>
	 * Clears the Panel and then
	 * </p>
	 * <p>
	 * Places the Cards on Table in a South, East, North, West cross pattern, with
	 * the Player always being placed in South
	 * </p>
	 * <p>
	 * The following players get added in order, given by nextPos()
	 * </p>
	 */
	@Override
	protected Widget drawCardsOnTable() {
		cardsOnTablePanel.clear();
		cardsOnTablePanel.setSpacing(0);
		cardsOnTablePanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		cardsOnTablePanel.add(new HorizontalPanel(), DockPanel.CENTER);

		playerPosition.put(username, DockPanel.SOUTH);

		for (String key : getOnTable().keySet()) {
			if (!playerPosition.containsKey(key)) {
				playerPosition.put(key, nextPos());
			}

			Card card = getOnTable().get(key).get(0);
			VerticalPanel playerContainer = new VerticalPanel();
			playerContainer.add(Cards.createCard(card));
			playerContainer.add(new HTML("Player: " + key));
			cardsOnTablePanel.add(playerContainer, playerPosition.get(key));
		}

		return cardsOnTablePanel;
	}

	/**
	 * Simply returns the next position which should be added to the Map
	 * 
	 * @return the next position
	 */
	private DockPanel.DockLayoutConstant nextPos() {
		if (playerPosition.containsValue(DockPanel.NORTH)) {
			return DockPanel.WEST;
		}
		if (playerPosition.containsValue(DockPanel.EAST)) {
			return DockPanel.NORTH;
		}
		if (playerPosition.containsValue(DockPanel.SOUTH)) {
			return DockPanel.EAST;
		}
		return null;
	}
}
