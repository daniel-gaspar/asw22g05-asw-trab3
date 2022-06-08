package wcg.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class GamePlayWAR extends GamePlay {
	
	private final DeckPanel cardsOnHandPanel = new DeckPanel();
	private final DockPanel cardsOnTablePanel = new DockPanel();

	public GamePlayWAR(String gameId) {
		super(gameId);
	}

	@Override
	protected Widget drawCardsOnHand() {
		cardsOnHandPanel.clear();
		for(Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					List<Card> cardsToPlay = new ArrayList<>();
					
					if("War".equals(getMode())) {
						for(int i = 0; i < 2; i++)
							cardsToPlay.add(getCardsOnHand().get(i));
					}
					else {
						cardsToPlay.add(getCardsOnHand().get(0));
					}
					
					cardGameService.playCards(getGameId(), username, password, cardsToPlay, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							messages.setHTML(caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							getCardsOnHand().removeAll(cardsToPlay);
							if("War".equals(getMode())) {
								for(int i = 0; i < 3; i++) {
									cardsOnHandPanel.remove(cardsOnHandPanel.getWidgetCount()-1);
								}
							}
							else {
								cardsOnHandPanel.remove(cardsOnHandPanel.getWidgetCount()-1);
							}
							cardsOnHandPanel.showWidget(cardsOnHandPanel.getWidgetCount()-1);
						}
					});
				}
			});
			
			cardsOnHandPanel.insert(card, 0);
		}
		cardsOnHandPanel.showWidget(getCardsOnHand().size()-1);
		return cardsOnHandPanel;
	}

	@Override
	protected Widget drawCardsOnTable() {
		cardsOnTablePanel.clear();
		cardsOnTablePanel.setSpacing(0);
		cardsOnTablePanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		
		for(String key : getOnTable().keySet()) {
			VerticalPanel playerContainer = new VerticalPanel();
			HorizontalPanel cardsContainer = new HorizontalPanel();
			for(Card card : getOnTable().get(key)) {
				cardsContainer.add(Cards.createCard(card));
			}
			playerContainer.add(cardsContainer);
			playerContainer.add(new HTML("Player: " + key));
			cardsOnTablePanel.add(playerContainer, (username.equals(key) ? DockPanel.SOUTH : DockPanel.CENTER));
		}
		
		return cardsOnTablePanel;
	}
}
