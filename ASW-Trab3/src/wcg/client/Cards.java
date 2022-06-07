package wcg.client;

import com.google.gwt.user.client.ui.Image;

import wcg.shared.cards.Card;
import wcg.shared.cards.CardSuit;
import wcg.shared.cards.CardValue;

public class Cards {
	
	private static final String FILE_EXTENSION = ".png";
	private static final String SEPARATOR = "_of_";
	private static final String SEPARATOR_JOKER = "_joker";
	private static final String IMAGE_LOCATION = "images/";
	
	Cards() {
	}

	protected static Image createCard(Card card) {
		String cardFile;
		
		if(card.getValue() == null) {
			cardFile = getJokerColorText(card.getSuit()) + SEPARATOR_JOKER + FILE_EXTENSION;
		}
		else {
			cardFile = getValueText(card.getValue()) + SEPARATOR + getSuitText(card.getSuit()) + FILE_EXTENSION;
		}
			
		return new Image(IMAGE_LOCATION + cardFile);
	}
	
	private static String getJokerColorText(CardSuit suit) {
		if(suit.equals(CardSuit.CLUBS) || suit.equals(CardSuit.SPADES))
			return "black";
		return "red";
	}
	
	private static String getValueText(CardValue value) {
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
	
	private static String getSuitText(CardSuit suit) {
		if(suit.equals(CardSuit.CLUBS))
			return "clubs";
		if(suit.equals(CardSuit.DIAMONDS))
			return "diamonds";
		if(suit.equals(CardSuit.HEARTS))
			return "hearts";
		return "spades";
	}
}
