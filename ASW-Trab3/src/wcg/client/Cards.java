package wcg.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

import wcg.shared.cards.Card;
import wcg.shared.cards.CardSuit;
import wcg.shared.cards.CardValue;

public class Cards {
	
	protected List<Card> cards;
	
	private static final String FILE_EXTENSION = ".png";
	private static final String SEPARATOR = "_of_";
	private static final String SEPARATOR_JOKER = "_joker";
	private static final String IMAGE_LOCATION = GWT.getModuleBaseURL() + "imgs/";
	
	Cards(List<Card> cards) {
		this.cards = cards;
	}

	protected Image createCard(Card card) {
		String cardFile;
		
		if(card.getValue() == null) {
			cardFile = getJokerColorText(card.getSuit()) + SEPARATOR_JOKER + FILE_EXTENSION;
		}
		else {
			cardFile = getValueText(card.getValue()) + SEPARATOR + getSuitText(card.getSuit()) + FILE_EXTENSION;
		}
			
		return new Image(IMAGE_LOCATION + cardFile);
	}
	
	private String getJokerColorText(CardSuit suit) {
		if(suit.equals(CardSuit.CLUBS) || suit.equals(CardSuit.SPADES))
			return "black";
		return "red";
	}
	
	private String getValueText(CardValue value) {
		if(value.equals(CardValue.ACE) || value.equals(CardValue.JACK) || value.equals(CardValue.KING) || value.equals(CardValue.QUEEN)) {
			if(value.equals(CardValue.ACE))
				return "ace";
			if(value.equals(CardValue.JACK))
				return "jack";
			if(value.equals(CardValue.KING))
				return "king";
			if(value.equals(CardValue.QUEEN))
				return "queen";
		}
		return value.toString();
	}
	
	private String getSuitText(CardSuit suit) {
		if(suit.equals(CardSuit.CLUBS))
			return "clubs";
		if(suit.equals(CardSuit.DIAMONDS))
			return "diamonds";
		if(suit.equals(CardSuit.HEARTS))
			return "hearts";
		return "spades";
	}
}
