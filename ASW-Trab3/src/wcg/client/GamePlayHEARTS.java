package wcg.client;

import java.util.Arrays;
import java.util.Map;
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
	
	private final HorizontalPanel cardsOnHandPanel = new HorizontalPanel();
	private final DockPanel cardsOnTablePanel = new DockPanel();
	
	private Map<String, DockLayoutConstant> playerPosition = new HashMap<>();
	
	public GamePlayHEARTS(String gameId) {
		super(gameId);
	}
	
	@Override
	protected Widget drawCardsOnHand() {
		cardsOnHandPanel.clear();
		for (Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					cardGameService.playCards(getGameId(), username, password, Arrays.asList(c), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							messages.setHTML(caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							getCardsOnHand().remove(c);
							Image cardToRemove = (Image) event.getSource();
							cardToRemove.removeFromParent();
						}
					});
				}
			});

			cardsOnHandPanel.add(card);
		}
		return cardsOnHandPanel;
	}

	@Override
	protected Widget drawCardsOnTable() {
		cardsOnTablePanel.clear();
		cardsOnTablePanel.setSpacing(0);
		cardsOnTablePanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

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
