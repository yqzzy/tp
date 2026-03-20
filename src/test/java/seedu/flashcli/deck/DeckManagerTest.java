package seedu.flashcli.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckManagerTest {

    private DeckManager manager;

    @BeforeEach
    public void setUp() {
        manager = new DeckManager();
    }

    @Test
    public void createDeck_validName_success() throws FlashException {
        manager.createDeck("Biology");
        assertNotNull(manager.getDeck("Biology"));
        assertEquals("Biology", manager.getDeck("Biology").getDeckName());
    }

    @Test
    public void createDeck_duplicateName_throwsException() throws FlashException {
        manager.createDeck("Biology");
        FlashException ex = assertThrows(FlashException.class, () -> manager.createDeck("Biology"));
        assertEquals(ErrorType.DUPLICATE_NAME, ex.getErrorType());
    }

    @Test
    public void getDeck_nonExistent_throwsException() {
        assertThrows(FlashException.class, () -> manager.getDeck("Math"));
    }

    @Test
    public void deleteDeck_existing_returnsTrue() throws FlashException {
        manager.createDeck("History");
        assertTrue(manager.deleteDeck("History"));
        assertThrows(FlashException.class, () -> manager.getDeck("History"));
    }

    @Test
    public void listDecks_multipleDecks_returnsAll() throws FlashException {
        manager.createDeck("A");
        manager.createDeck("B");
        assertEquals(2, manager.listDecks().size());
        assertTrue(manager.listDecks().contains("A"));
    }
}
