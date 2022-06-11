package wcg.client;

import com.google.gwt.user.client.ui.Image;

import wcg.shared.cards.Card;
import wcg.shared.cards.CardSuit;
import wcg.shared.cards.CardValue;

/**
 * Auxiliary class to create Image objects, already containing the correct image
 * file
 */
public class Cards {

	/**
	 * Several constants to help building the filename of the image
	 */
	private static final String FILE_EXTENSION = ".png";
	private static final String SEPARATOR = "_of_";
	private static final String SEPARATOR_JOKER = "_joker";
	private static final String IMAGE_LOCATION = "images/";

	Cards() {
	}

	/**
	 * Given a Card, returns an Image object with the correct png file
	 * 
	 * @param card
	 * @return Image with correct png file
	 */
	protected static Image createCard(Card card) {
		String cardFile;

		if (card.getValue() == null) {
			cardFile = getJokerColorText(card.getSuit()) + SEPARATOR_JOKER + FILE_EXTENSION;
		} else {
			cardFile = getValueText(card.getValue()) + SEPARATOR + getSuitText(card.getSuit()) + FILE_EXTENSION;
		}

		Image image = new Image(IMAGE_LOCATION + cardFile);
		image.setPixelSize(83, 120);

		return image;
	}

	protected static Image createCard(String name) {

		Image image = new Image(IMAGE_LOCATION + name + FILE_EXTENSION);
		image.setPixelSize(83, 120);

		return image;
	}

	/**
	 * Given the CardSuit, returns whether the joker should be black or red
	 * 
	 * @param suit
	 * @return black or red
	 */
	private static String getJokerColorText(CardSuit suit) {
		if (suit.equals(CardSuit.CLUBS) || suit.equals(CardSuit.SPADES))
			return "black";
		return "red";
	}

	/**
	 * Given the CardValue, represents itself in a String compatible with the png
	 * filenames
	 * 
	 * @param value
	 * @return String with card value
	 */
	private static String getValueText(CardValue value) {
		if (value.equals(CardValue.ACE) || value.equals(CardValue.JACK) || value.equals(CardValue.KING)
				|| value.equals(CardValue.QUEEN)) {
			if (value.equals(CardValue.ACE))
				return "ace";
			if (value.equals(CardValue.JACK))
				return "jack";
			if (value.equals(CardValue.KING))
				return "king";
			if (value.equals(CardValue.QUEEN))
				return "queen";
		}
		return value.toString();
	}

	/**
	 * Given the CardSuit, represents itself in a String compatible with the png
	 * filenames
	 * 
	 * @param suit
	 * @return String with card suit
	 */
	private static String getSuitText(CardSuit suit) {
		if (suit.equals(CardSuit.CLUBS))
			return "clubs";
		if (suit.equals(CardSuit.DIAMONDS))
			return "diamonds";
		if (suit.equals(CardSuit.HEARTS))
			return "hearts";
		return "spades";
	}
}
