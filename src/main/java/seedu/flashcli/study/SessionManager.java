package seedu.flashcli.study;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.Card;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

public class SessionManager {
    private StudySession activeSession;

    public SessionManager() {
        this.activeSession = null;
    }

    /**
     * Creates a new StudySession. Throws exception if one is already running.
     */
    public void startSession(Deck deck) throws FlashException {
        if (hasActiveSession()) {
            throw new FlashException(ErrorType.SESSION_ALREADY_IN_PROGRESS);
        }
        this.activeSession = new StudySession(deck);
    }

    /**
     * Advances to the next card. Throws exception if no session is active.
     */
    public boolean nextCard() throws FlashException {
        if (!hasActiveSession()) {
            throw new FlashException(ErrorType.NO_ACTIVE_SESSION);
        }
        return activeSession.nextCard();
    }

    /**
     * Ends the session, clears the state, and returns count of cards reviewed.
     */
    public int finishSession() {
        if (!hasActiveSession()) {
            return 0;
        }
        int count = activeSession.finish();
        activeSession = null; // Clear the session state
        return count;
    }

    public boolean hasActiveSession() {
        return activeSession != null;
    }

    public StudySession getCurrentSession() {
        return activeSession;
    }

    public Card getCurrentCard() throws FlashException {
        if (!hasActiveSession()) {
            throw new FlashException(ErrorType.NO_ACTIVE_SESSION);
        }
        return activeSession.getCurrentCard();
    }
}
