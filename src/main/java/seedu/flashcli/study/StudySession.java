package seedu.flashcli.study;

import java.util.ArrayList;
import java.util.Comparator;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.Card;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

public class StudySession {
    private final Deck deck;
    private int currentIndex = 0;

    public StudySession(Deck deck) {
        ArrayList<Card> tempCards = deck.listCards();
        Deck tempDeck = new Deck();
        tempCards.sort(Comparator.comparing(Card::getConfidenceLevel));
        tempDeck.setCards(tempCards);
        this.deck = tempDeck;
    }

    /**
     * Returns the current card without advancing.
     */
    public Card getCurrentCard() throws FlashException { // Added throws
        if (currentIndex < 0 || currentIndex >= deck.getSize()) {
            throw new FlashException(ErrorType.CARD_NOT_FOUND);
        }
        return deck.getCard(currentIndex);
    }

    /**
     * Increments the index. Returns true if the end is reached.
     */
    public boolean nextCard() {
        currentIndex++;
        return isFinished();
    }

    /**
     * Resets session state and returns total cards reviewed.
     */
    public int finish() {
        int cardsReviewed = currentIndex + 1;
        int finalCount = Math.min(cardsReviewed, deck.getSize());
        currentIndex = -1;
        return finalCount;
    }

    /**
     * Checks if the index has reached or passed the last card.
     */
    public boolean isFinished() {
        return currentIndex >= deck.getSize();
    }
}
