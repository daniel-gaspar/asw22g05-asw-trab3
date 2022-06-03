package wcg.client;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.cards.Card;

public class CardsHorizontal extends AbstractCards {
	
	private Widget cardsHorizontal;
	
	private final HorizontalPanel cardPlacement = new HorizontalPanel();

	public CardsHorizontal(List<Card> cards) {
		super(cards);
		this.cardsHorizontal = createHorizontalCards();
		// TODO Auto-generated constructor stub
	}
	
	public Widget getCardsHorizontal() {
		return cardsHorizontal;
	}
	
	private Widget createHorizontalCards() {
		for(Card card: cards) {
			Image cardImage = createCard(card);
			cardPlacement.add(cardImage);
		}
		return cardPlacement;
	}
}
