package wcg.client;

import java.util.List;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class CardsVertical extends AbstractCards {
	
	private Widget cardsVertical;
	
	private final VerticalPanel cardPlacement = new VerticalPanel();

	CardsVertical(List<Card> cards) {
		super(cards);
		this.cardsVertical = createVerticalCards();
		// TODO Auto-generated constructor stub
	}
	
	public Widget getCardsVertical() {
		return cardsVertical;
	}
	
	private Widget createVerticalCards() {
		for(Card card: cards) {
			Image cardImage = createBackCard();
			cardPlacement.add(cardImage);
		}
		return cardPlacement;
	}

}
