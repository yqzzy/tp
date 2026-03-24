package seedu.flashcli.study;

import java.util.ArrayList;
import java.util.Comparator;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.Card;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StudySession {
    private static final Logger LOGGER = Logger.getLogger(StudySession.class.getName());

    private final Deck deck;
    private int currentIndex = 0;

    /**
     * Constructs a new StudySession for the given deck, starting at the first card.
     *
     * @param deck the {@link Deck} to study; must not be null
     * @throws IllegalArgumentException if {@code deck} is null
     */
    public StudySession(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("StudySession: deck must not be null");
        }

        ArrayList<Card> tempCards = deck.listCards();
        Deck tempDeck = new Deck(deck.getDeckName());
        tempCards.sort(Comparator.comparing(Card::getConfidenceLevel));
        tempDeck.setCards(tempCards);
        this.deck = tempDeck;

        // Post-condition: index must start at 0
        assert currentIndex == 0 : "currentIndex must be initialised to 0";

        LOGGER.log(Level.INFO, "StudySession created for deck ''{0}'' with {1} card(s)",
                new Object[]{deck.getDeckName(), deck.getSize()});
    }

    /**
     * Returns the card at the current index without advancing the session.
     *
     * @return the current {@link Card}
     * @throws FlashException if the current index is out of bounds ({@code CARD_NOT_FOUND})
     */
    public Card getCurrentCard() throws FlashException {
        LOGGER.log(Level.FINE, "getCurrentCard() called. currentIndex={0}, deckSize={1}",
                new Object[]{currentIndex, deck.getSize()});

        if (currentIndex < 0 || currentIndex >= deck.getSize()) {
            LOGGER.log(Level.WARNING,
                    "getCurrentCard: index {0} is out of bounds for deck of size {1}",
                    new Object[]{currentIndex, deck.getSize()});
            throw new FlashException(ErrorType.CARD_NOT_FOUND);
        }

        Card card = deck.getCard(currentIndex);

        // Post-condition: returned card must not be null
        assert card != null : "deck.getCard() returned null at index " + currentIndex;

        return card;
    }

    /**
     * Advances the session to the next card by incrementing the current index.
     *
     * @return {@code true} if the end of the deck has been reached, {@code false} otherwise
     */
    public boolean nextCard() {
        // Pre-condition: index must not already be negative (session not yet finished/reset)
        assert currentIndex >= 0 : "nextCard called on an already-finished session (index=" + currentIndex + ")";

        int previousIndex = currentIndex;
        currentIndex++;

        // Post-condition: index must have advanced by exactly one
        assert currentIndex == previousIndex + 1
                : "currentIndex did not advance correctly. Expected "
                + (previousIndex + 1) + ", got " + currentIndex;

        boolean finished = isFinished();
        LOGGER.log(Level.FINE, "Advanced to index {0}. Session finished: {1}",
                new Object[]{currentIndex, finished});

        return finished;
    }

    /**
     * Marks the session as complete, resets internal state, and returns the number of cards reviewed.
     *
     * @return the total number of cards reviewed, capped at the deck size
     */
    public int finish() {
        LOGGER.log(Level.FINE, "finish() called. currentIndex={0}, deckSize={1}",
                new Object[]{currentIndex, deck.getSize()});

        // Pre-condition: index must be non-negative for a meaningful review count
        assert currentIndex >= 0 : "finish() called with a negative index; session may have already been finished";

        int cardsReviewed = currentIndex + 1;
        int finalCount = Math.min(cardsReviewed, deck.getSize());

        // Invariant: finalCount must be within [0, deckSize]
        assert finalCount >= 0 && finalCount <= deck.getSize()
                : "finalCount out of expected range: " + finalCount + " (deckSize=" + deck.getSize() + ")";

        currentIndex = -1; // Sentinel: marks the session as consumed

        LOGGER.log(Level.INFO, "Session for deck ''{0}'' finished. Cards reviewed: {1}",
                new Object[]{deck.getDeckName(), finalCount});
        return finalCount;
    }

    /**
     * Checks whether the session has reached or passed the last card in the deck.
     *
     * @return {@code true} if the current index is at or beyond the deck size, {@code false} otherwise
     */
    public boolean isFinished() {
        return currentIndex >= deck.getSize();
    }

    /**
     * Returns the deck associated with this session.
     *
     * @return the {@link Deck} being studied
     */
    public Deck getDeck() {
        return deck;
    }

    public String getDeckName(){
        return deck.getDeckName();
    }
}
