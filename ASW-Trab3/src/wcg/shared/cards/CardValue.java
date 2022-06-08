package wcg.shared.cards;

/**
 * Enumeration with the values of cards
 */
public enum CardValue {
	V02 {
		@Override
		public String toString() {
			return "2";
		}

		@Override
		public int getScore() {
			return 2;
		}
	},
	V03 {
		@Override
		public String toString() {
			return "3";
		}

		@Override
		public int getScore() {
			return 3;
		}
	},
	V04 {
		@Override
		public String toString() {
			return "4";
		}

		@Override
		public int getScore() {
			return 4;
		}
	},
	V05 {
		@Override
		public String toString() {
			return "5";
		}

		@Override
		public int getScore() {
			return 5;
		}
	},
	V06 {
		@Override
		public String toString() {
			return "6";
		}

		@Override
		public int getScore() {
			return 6;
		}
	},
	V07 {
		@Override
		public String toString() {
			return "7";
		}

		@Override
		public int getScore() {
			return 7;
		}
	},
	V08 {
		@Override
		public String toString() {
			return "8";
		}

		@Override
		public int getScore() {
			return 8;
		}
	},
	V09 {
		@Override
		public String toString() {
			return "9";
		}

		@Override
		public int getScore() {
			return 9;
		}
	},
	V10 {
		@Override
		public String toString() {
			return "10";
		}

		@Override
		public int getScore() {
			return 10;
		}
	},
	QUEEN {
		@Override
		public String toString() {
			return "Q";
		}

		@Override
		public int getScore() {
			return 11;
		}
	},
	JACK {
		@Override
		public String toString() {
			return "J";
		}

		@Override
		public int getScore() {
			return 12;
		}
	},
	KING {
		@Override
		public String toString() {
			return "K";
		}

		@Override
		public int getScore() {
			return 13;
		}
	},
	ACE {
		@Override
		public String toString() {
			return "A";
		}

		@Override
		public int getScore() {
			return 14;
		}
	};

	public abstract int getScore();
}
