public class Card implements Comparable<Card> {

    private final CardSuit suit;
    private final CardRank rank;

    public Card(CardSuit cardSuit, CardRank cardRank) {
        this.suit = cardSuit;
        this.rank = cardRank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public boolean equals(Card other) {
        return this.rank.value == other.rank.value;
    }

    @Override
    public int compareTo(Card other) {
        return this.rank.compareTo(other.rank);
    }

    public int getValue() {
        return rank.value;
    }

    public enum Rank {
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13),
        ACE(14);

        private final int value;
        Rank(int value) {
            this.value = value;
        }
    }

    public enum Suit {
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS
    }
}
