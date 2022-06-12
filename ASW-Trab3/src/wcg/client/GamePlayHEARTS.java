package wcg.client;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class GamePlayHEARTS extends GamePlay {

	private static final int CARDS_ON_HAND_PANEL_WIDTH = 900;
	private static final int CARD_POSITION_OFFSET = 60;
	private static final int CARD_WIDTH = 83;
	/**
	 * The Widgets which will belong in centerPanel and southPanel of GamePlay,
	 * respectively
	 */
	private final DockPanel cardsOnTablePanel = new DockPanel();
	private final AbsolutePanel cardsOnHandPanel = new AbsolutePanel();

	private final Map<String, DockLayoutConstant> playerPosition = new HashMap<>();
	private final Map<DockLayoutConstant, Widget> cardsPlacement = new HashMap<>();
	private static final DockLayoutConstant[] POSITION_ORDER = { DockPanel.SOUTH, DockPanel.NORTH, DockPanel.EAST,
			DockPanel.WEST };

	public GamePlayHEARTS(String gameId) {
		super(gameId);
		cardsOnHandPanel.setStyleName("wcg-GameAreaCardsOnHand");
	}

	/**
	 * Clears the current Panel containing Cards on Hand, and then uses the List of
	 * Cards to draw them, adding a ClickHandler to each Image Card, which prompts
	 * the server to Play the selected card
	 */
	@Override
	protected Widget drawCardsOnHand() {
		cardsOnHandPanel.clear();

		int startingLeftPosition = getInitialLeftPosition();

		int i = 0;
		for (Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.getElement().getStyle().setZIndex(1);
			card.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					cardGameService.playCards(getGameId(), username, password, Arrays.asList(c),
							new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									systemMessages
											.setHTML(getGameId() + ": Failed to play card. " + caught.getMessage());
								}

								@Override
								public void onSuccess(Void result) {
									getCardsOnHand().remove(c);
									drawCardsOnHand();
								}
							});
				}
			});

			card.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					card.getElement().getStyle().setZIndex(Integer.MAX_VALUE);
				}
			});

			card.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					card.getElement().getStyle().setZIndex(1);
				}
			});

			cardsOnHandPanel.add(card, startingLeftPosition + i * CARD_POSITION_OFFSET, 0);

			i++;
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
		cardsOnTablePanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		HorizontalPanel centerPanel = new HorizontalPanel();
		centerPanel.setStyleName("wcg-GameAreaOnTableCenterPanel");
		cardsOnTablePanel.add(centerPanel, DockPanel.CENTER);

		playerPosition.put(username, DockPanel.SOUTH);

		cardsPlacement.clear();

		for (String player : getOnTable().keySet()) {
			if (!playerPosition.containsKey(player)) {
				playerPosition.put(player, nextPos());
			}

			Card card = getOnTable().get(player).get(0);
			VerticalPanel playerContainer = new VerticalPanel();
			playerContainer.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
			playerContainer.add(Cards.createCard(card));
			HTML playerName = new HTML("Player: " + player);
			playerName.setStyleName("wcg-Text");
			if (player.equals(getHasTurn()))
				playerName.addStyleName("wcg-TextPlayerWithTurn");
			playerContainer.add(playerName);

			cardsPlacement.put(playerPosition.get(player), playerContainer);
		}

		for (DockLayoutConstant position : POSITION_ORDER) {
			if (!cardsPlacement.containsKey(position)) {
				VerticalPanel playerContainer = new VerticalPanel();
				playerContainer.setStyleName("wcg-GameAreaOnTablePlayer");
				playerContainer.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
				playerContainer.add(Cards.createCard("facedown"));

				HTML playerName = new HTML("Player: Opponent");
				playerName.setStyleName("wcg-Text");

				for (String player : playerPosition.keySet()) {
					if (position.equals(playerPosition.get(player))) {
						playerName.setHTML("Player: " + player);
						if (player.equals(getHasTurn()))
							playerName.addStyleName("wcg-TextPlayerWithTurn");
					}
				}

				playerContainer.add(playerName);

				cardsPlacement.put(position, playerContainer);
			}
			cardsOnTablePanel.add(cardsPlacement.get(position), position);
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

	/**
	 * Auxiliary method to calculate the position where the group of cards on Hand
	 * should start
	 * 
	 * @return position in pixels
	 */
	private int getInitialLeftPosition() {
		int panelWidth = CARDS_ON_HAND_PANEL_WIDTH;
		int numberOfCards = getCardsOnHand().size();
		int spaceForCards = CARD_POSITION_OFFSET * (numberOfCards - 1) + CARD_WIDTH;

		return (panelWidth - spaceForCards) / 2;
	}
}
