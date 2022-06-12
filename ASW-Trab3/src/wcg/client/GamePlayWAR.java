package wcg.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class GamePlayWAR extends GamePlay {

	/**
	 * The Widgets which will belong in centerPanel and southPanel of GamePlay,
	 * respectively
	 */
	private final DockPanel cardsOnTablePanel = new DockPanel();
	private final DeckPanel cardsOnHandPanel = new DeckPanel();

	private final Map<String, DockLayoutConstant> playerPosition = new HashMap<>();
	private final Map<DockLayoutConstant, Widget> cardsPlacement = new HashMap<>();
	private static final DockLayoutConstant[] order = { DockPanel.SOUTH, DockPanel.NORTH };

	public GamePlayWAR(String gameId) {
		super(gameId);
		cardsOnHandPanel.setStyleName("wcg-CardsOnHand");
	}

	/**
	 * <p>
	 * Clears the current Panel containing Cards on Hand, and then uses the List of
	 * Cards to draw them, placing them in a DeckPanel.
	 * </p>
	 * <p>
	 * Adds a ClickHandler to each Image Card, which prompts the server to Play the
	 * card on top, or the three cards on top if in War mode
	 * </p>
	 */
	@Override
	protected Widget drawCardsOnHand() {
		cardsOnHandPanel.clear();
		for (Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (!hasPlayedThisTurn) {
						List<Card> cardsToPlay = new ArrayList<>();

						if ("War".equals(getMode())) {
							for (int i = 0; i < 3; i++)
								cardsToPlay.add(getCardsOnHand().get(i));
						} else {
							cardsToPlay.add(getCardsOnHand().get(0));
						}

						cardGameService.playCards(getGameId(), username, password, cardsToPlay,
								new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable caught) {
										systemMessages.setHTML(
												getGameId() + ": Failed to play cards. " + caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										getCardsOnHand().removeAll(cardsToPlay);
										if ("War".equals(getMode())) {
											for (int i = 0; i < 3; i++) {
												cardsOnHandPanel.remove(cardsOnHandPanel.getWidgetCount() - 1);
											}
										} else {
											cardsOnHandPanel.remove(cardsOnHandPanel.getWidgetCount() - 1);
										}
										cardsOnHandPanel.showWidget(cardsOnHandPanel.getWidgetCount() - 1);

										hasPlayedThisTurn = true;
									}
								});
					} else {
						systemMessages.setHTML(getGameId() + ": You have already played this turn.");
					}
				}
			});

			cardsOnHandPanel.insert(card, 0);
		}
		cardsOnHandPanel.showWidget(getCardsOnHand().size() - 1);
		return cardsOnHandPanel;
	}

	/**
	 * <p>
	 * Clears the panel and then adds the Cards currently on Table
	 * </p>
	 * <p>
	 * The player is always placed in the South position, and the opponent is always
	 * placed in the Center position
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

		for (String key : getOnTable().keySet()) {
			if (!playerPosition.containsKey(key))
				playerPosition.put(key, DockPanel.NORTH);

			VerticalPanel playerContainer = new VerticalPanel();
			playerContainer.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
			HorizontalPanel cardsContainer = new HorizontalPanel();
			cardsContainer.clear();
			for (Card card : getOnTable().get(key)) {
				cardsContainer.add(Cards.createCard(card));
			}
			playerContainer.add(cardsContainer);
			playerContainer.add(new HTML("Player: " + key));

			cardsPlacement.put(playerPosition.get(key), playerContainer);
		}

		for (DockLayoutConstant position : order) {
			if (!cardsPlacement.containsKey(position)) {
				VerticalPanel playerContainer = new VerticalPanel();
				playerContainer.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
				HorizontalPanel cardsContainer = new HorizontalPanel();
				cardsContainer.clear();

				for (int i = 0; i < ("War".equals(getMode()) ? 3 : 1); i++) {
					cardsContainer.add(Cards.createCard("facedown"));
				}

				playerContainer.add(cardsContainer);

				HTML playerName = new HTML("Player: Opponent");

				for (String player : playerPosition.keySet()) {
					if (position.equals(playerPosition.get(player)))
						playerName = new HTML("Player: " + player);
				}

				playerContainer.add(playerName);

				cardsPlacement.put(position, playerContainer);
			}
			cardsOnTablePanel.add(cardsPlacement.get(position), position);
		}

		return cardsOnTablePanel;
	}
}
