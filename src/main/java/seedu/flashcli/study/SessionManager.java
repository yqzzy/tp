package seedu.flashcli.study;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.Card;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionManager {
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());

    private StudySession activeSession;

    /**
     * Constructs a SessionManager with no active session.
     */
    public SessionManager() {
        this.activeSession = null;
        LOGGER.log(Level.FINE, "SessionManager initialised with no active session");
    }

    /**
     * Creates a new StudySession for the given deck.
     *
     * @param deck the {@link Deck} to study; must not be null
     * @throws FlashException if {@code deck} is null ({@code INVALID_ARGUMENTS})
     *                        or a session is already in progress ({@code SESSION_ALREADY_IN_PROGRESS})
     */
    public void startSession(Deck deck) throws FlashException {
        if (deck == null) {
            LOGGER.log(Level.WARNING, "startSession called with null deck");
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }

        LOGGER.log(Level.FINE, "Attempting to start session for deck: {0}", deck.getDeckName());

        if (hasActiveSession()) {
            LOGGER.log(Level.WARNING,
                    "startSession failed — session already in progress for deck: {0}",
                    activeSession.getDeck().getDeckName());
            throw new FlashException(ErrorType.SESSION_ALREADY_IN_PROGRESS);
        }

        this.activeSession = new StudySession(deck);

        // Post-condition: session must now be active
        assert hasActiveSession() : "activeSession is null immediately after startSession";
        assert activeSession.getDeck() == deck : "Active session deck does not match the provided deck";

        LOGGER.log(Level.INFO, "Study session started for deck: {0}", deck.getDeckName());
    }

    /**
     * Advances the active session to the next card.
     *
     * @return {@code true} if the end of the deck has been reached, {@code false} otherwise
     * @throws FlashException if no session is currently active ({@code NO_ACTIVE_SESSION})
     */
    public boolean nextCard() throws FlashException {
        LOGGER.log(Level.FINE, "nextCard() called");

        if (!hasActiveSession()) {
            LOGGER.log(Level.WARNING, "nextCard called with no active session");
            throw new FlashException(ErrorType.NO_ACTIVE_SESSION);
        }

        boolean finished = activeSession.nextCard();

        if (finished) {
            LOGGER.log(Level.INFO, "End of deck reached after advancing card");
        } else {
            LOGGER.log(Level.FINE, "Advanced to next card; session still in progress");
        }

        return finished;
    }

    /**
     * Ends the active session, clears internal state, and returns the number of cards reviewed.
     * Returns {@code 0} and logs a warning if no session is currently active.
     *
     * @return the number of cards reviewed in the session, or {@code 0} if no session was active
     */
    public int finishSession() {
        LOGGER.log(Level.FINE, "finishSession() called");

        if (!hasActiveSession()) {
            LOGGER.log(Level.WARNING, "finishSession called with no active session; returning 0");
            return 0;
        }

        String deckName = activeSession.getDeck().getDeckName();
        int count = activeSession.finish();

        assert count >= 0 : "finishSession returned a negative card count: " + count;

        activeSession = null; // Clear the session state

        // Post-condition: no session must remain after finishing
        assert !hasActiveSession() : "activeSession is not null after finishSession";

        LOGGER.log(Level.INFO, "Session finished for deck ''{0}''. Cards reviewed: {1}",
                new Object[]{deckName, count});
        return count;
    }

    /**
     * Returns whether a study session is currently in progress.
     *
     * @return {@code true} if a session is active, {@code false} otherwise
     */
    public boolean hasActiveSession() {
        return activeSession != null;
    }

    /**
     * Returns the currently active {@link StudySession}, or {@code null} if none exists.
     *
     * @return the active {@link StudySession}, or {@code null}
     */
    public StudySession getCurrentSession() {
        LOGGER.log(Level.FINE, "getCurrentSession() called. Has session: {0}", hasActiveSession());
        return activeSession;
    }

    /**
     * Returns the card at the current position in the active session.
     *
     * @return the current {@link Card}
     * @throws FlashException if no session is currently active ({@code NO_ACTIVE_SESSION})
     *                        or the current index is out of bounds ({@code CARD_NOT_FOUND})
     */
    public Card getCurrentCard() throws FlashException {
        LOGGER.log(Level.FINE, "getCurrentCard() called");

        if (!hasActiveSession()) {
            LOGGER.log(Level.WARNING, "getCurrentCard called with no active session");
            throw new FlashException(ErrorType.NO_ACTIVE_SESSION);
        }

        Card card = activeSession.getCurrentCard();

        // Post-condition: returned card must not be null
        assert card != null : "getCurrentCard returned null from an active session";

        return card;
    }
}
