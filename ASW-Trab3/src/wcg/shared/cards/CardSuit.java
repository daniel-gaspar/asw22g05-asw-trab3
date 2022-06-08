package wcg.shared.cards;

/**
 * Enumeration with the 4 suits in cards
 */
public enum CardSuit {

	CLUBS {
		@Override
		public int getScore() {
			return 1;
		}

		@Override
		public String toString() {
			return "♣";
		}
	},
	DIAMONDS {
		@Override
		public int getScore() {
			return 2;
		}

		@Override
		public String toString() {
			return "♦";
		}
	},
	HEARTS {
		@Override
		public int getScore() {
			return 3;
		}

		@Override
		public String toString() {
			return "♥";
		}
	},
	SPADES {
		@Override
		public int getScore() {
			return 4;
		}

		@Override
		public String toString() {
			return "♠";
		}
	};

	public abstract int getScore();
}
