package seedu.flashcli.study;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionManagerTest {

    private SessionManager sessionManager;
    private Deck sampleDeck;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        sampleDeck = new Deck("Test");
        sampleDeck.addCard("Q1", "A1");
    }

    @Test
    public void startSession_noActiveSession_success() throws FlashException {
        sessionManager.startSession(sampleDeck);
        assertTrue(sessionManager.hasActiveSession());
        assertEquals("Q1", sessionManager.getCurrentCard().getQuestion());
    }

    @Test
    public void startSession_alreadyActive_throwsException() throws FlashException {
        sessionManager.startSession(sampleDeck);
        FlashException ex = assertThrows(FlashException.class, () ->
                sessionManager.startSession(sampleDeck));
        assertEquals(ErrorType.SESSION_ALREADY_IN_PROGRESS, ex.getErrorType());
    }

    @Test
    public void nextCard_noSession_throwsException() {
        FlashException ex = assertThrows(FlashException.class, () ->
                sessionManager.nextCard());
        assertEquals(ErrorType.NO_ACTIVE_SESSION, ex.getErrorType());
    }

    @Test
    public void finishSession_clearsStateAndReturnsCount() throws FlashException {
        sessionManager.startSession(sampleDeck);
        int count = sessionManager.finishSession();

        assertEquals(1, count);
        assertFalse(sessionManager.hasActiveSession(), "Session should be null after finishing");
    }

    @Test
    public void getCurrentCard_noSession_throwsException() {
        assertThrows(FlashException.class, () -> sessionManager.getCurrentCard());
    }
}
