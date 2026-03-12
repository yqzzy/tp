package seedu.flashcli.deck;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeckManagerTest {

    @Test
    public void createDeck_newDeckName_success() {
        DeckManager manager = new DeckManager();
        manager.createDeck("CS2040C");
        assertNotNull(manager.getDeck("CS2040C"));
        assertEquals("CS2040C", manager.getDeck("CS2040C").getDeckName());
    }

    @Test
    public void createDeck_duplicateDeckName_handled() {
        DeckManager manager = new DeckManager();
        manager.createDeck("EE2026");
        manager.createDeck("EE2026");
        // Logic check: ensure it doesn't crash or create a second instance
        assertNotNull(manager.getDeck("EE2026"));
    }

    @Test
    public void deleteDeck_existingDeck_success() {
        DeckManager manager = new DeckManager();
        manager.createDeck("CG2111A");
        manager.deleteDeck("CG2111A");
        assertNull(manager.getDeck("CG2111A"));
    }

    @Test
    public void deleteDeck_nonExistentDeck_handled() {
        DeckManager manager = new DeckManager();
        manager.deleteDeck("NonExistent");
        assertNull(manager.getDeck("NonExistent"));
    }

    @Test
    public void addCardToDeck_existingDeck_success() {
        DeckManager manager = new DeckManager();
        manager.createDeck("EE2026");

        // Add a card through the manager
        manager.addCardToDeck("EE2026", "What is an FPGA?", "Field Programmable Gate Array");

        // Verify the card was added to the correct deck
        Deck deck = manager.getDeck("EE2026");
        assertEquals(1, deck.getSize());
        assertEquals("Question: What is an FPGA?", deck.getCard(0).getQuestionString());
    }

    @Test
    public void addCardToDeck_nonExistentDeck_doesNotThrow() {
        DeckManager manager = new DeckManager();
        // Attempting to add to a deck that hasn't been created
        manager.addCardToDeck("MissingDeck", "Q", "A");

        // Logic check: Ensure no deck was accidentally created
        assertEquals(null, manager.getDeck("MissingDeck"));
    }

    @Test
    public void deleteCardFromDeck_validIndex_success() {
        DeckManager manager = new DeckManager();
        manager.createDeck("CS2113");
        manager.addCardToDeck("CS2113", "What is Unit Testing?", "Testing individual components");

        // Delete the card we just added
        manager.deleteCardFromDeck("CS2113", 0);

        assertEquals(0, manager.getDeck("CS2113").getSize());
    }
}
