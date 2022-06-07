package wcg.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class GamePlayHEARTS extends GamePlay {

	public GamePlayHEARTS(String gameId) {
		super(gameId);
	}
	
	@Override
	protected Widget drawCardsOnHand() {
		HorizontalPanel cards = new HorizontalPanel();
		for (Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.addClickHandler(new ClickHandler() {

				// TODO - gives error when for raw input
				@Override
				public void onClick(ClickEvent event) {
					cardGameService.playCards(getGameId(), username, password, Arrays.asList(c), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub
							
						}
					});

				}

			});

			cards.add(card);
		}
		return cards;

	}

	private HashMap<String, DockLayoutConstant> playerPosition = new HashMap<>();

	@Override
	protected Widget drawCardsOnTable() {

		DockPanel dock = new DockPanel();
		dock.setSpacing(0);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		playerPosition.put(username, DockPanel.SOUTH);

		for (String key : getOnTable().keySet()) {
			if (!playerPosition.containsKey(key)) {
				playerPosition.put(key, nextPos());
			}

			Card card = getOnTable().get(key).get(0);
			VerticalPanel playerContainer = new VerticalPanel();
			SimplePanel imageContainer = new SimplePanel();
			imageContainer.setWidget(Cards.createCard(card));
			playerContainer.add(imageContainer);
			playerContainer.add(new HTML("player: " + key));
			dock.add(playerContainer, playerPosition.get(key));
		}

		return dock;
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
