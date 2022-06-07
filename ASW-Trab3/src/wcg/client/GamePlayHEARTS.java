package wcg.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private Widget gamePlayHEARTS;

	public GamePlayHEARTS(String gameId) {
		super(gameId);
		this.gamePlayHEARTS = onGamePlayHEARTSInitialize();
	}

	public Widget getGamePlayHEARTS() {
		return gamePlayHEARTS;
	}
	

	private Widget onGamePlayHEARTSInitialize() {

		// TODO - Create widget for game play HEARTS
		DockPanel dock = new DockPanel();
		dock.setStyleName("cw-DockPanel");
		dock.setSpacing(0);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		

		dock.add(drawCardsOnTable(), DockPanel.NORTH);
		dock.add(drawCardsOnHand(), DockPanel.SOUTH);

		return dock;
	}

	@Override
	protected Widget drawCardsOnHand() {
		HorizontalPanel cards = new HorizontalPanel();
		for(Card c : getCardsOnHand()) {
			Image card = Cards.createCard(c);
			card.addClickHandler(new ClickHandler() {
				
				//TODO - gives error when for raw input
				@Override
				public void onClick(ClickEvent event) {
					cardGameService.playCards(getGameId(), username, password, Arrays.asList(c), new AsyncCallback() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							
						}} );
					
				}
				
			});
						
			cards.add(card);
		}
		return cards;

	}

	@Override
	protected Widget drawCardsOnTable() {
		DockPanel dock = new DockPanel();
		dock.setSpacing(0);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		
		List<DockLayoutConstant> positions = new ArrayList<>();
		positions.add(DockPanel.NORTH);
		positions.add(DockPanel.SOUTH);
		positions.add(DockPanel.WEST);
		positions.add(DockPanel.EAST);
	
		
		for(String key : getOnTable().keySet()) {
			int i = 0;
			Card card = getOnTable().get(key).get(0);
			VerticalPanel playerContainer = new VerticalPanel();
			SimplePanel imageContainer = new SimplePanel();
			imageContainer.setWidget(Cards.createCard(card));
			playerContainer.add(imageContainer);
			playerContainer.add(new HTML("player: "+key));
			dock.add(playerContainer,positions.get(i));
			i++;
		}
		
		return dock;
	}
}
