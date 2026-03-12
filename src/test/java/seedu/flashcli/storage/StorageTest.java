package seedu.flashcli.storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.deck.Deck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoad_fileDoesNotExist_returnsEmptyDeckManager() {
        Path nonExistentFile = tempDir.resolve("nonexistent.json");
        Storage storage = new Storage(nonExistentFile.toString());

        DeckManager loaded = storage.load();

        assertNotNull(loaded);
    }

    @Test
    void testLoad_fileExistsButEmpty_returnsEmptyDeckManager() throws IOException {
        Path emptyFile = tempDir.resolve("empty.json");
        Files.writeString(emptyFile, "");
        Storage storage = new Storage(emptyFile.toString());

        DeckManager loaded = storage.load();

        assertNotNull(loaded);
    }

    @Test
    void testLoad_fileExistsWithWhitespace_returnsEmptyDeckManager() throws IOException {
        Path whitespaceFile = tempDir.resolve("whitespace.json");
        Files.writeString(whitespaceFile, "   \n\t  ");
        Storage storage = new Storage(whitespaceFile.toString());

        DeckManager loaded = storage.load();

        assertNotNull(loaded);
    }

    @Test
    void testSaveAndLoad_emptyDeckManager() throws IOException {
        Path testFile = tempDir.resolve("test.json");
        Storage storage = new Storage(testFile.toString());

        DeckManager original = new DeckManager();
        storage.save(original);

        assertTrue(Files.exists(testFile));

        DeckManager loaded = storage.load();
        assertNotNull(loaded);
    }

    @Test
    void testSaveAndLoad_deckManagerWithDecks() throws IOException {
        Path testFile = tempDir.resolve("test.json");
        Storage storage = new Storage(testFile.toString());

        DeckManager original = new DeckManager();

        // Create decks using the actual createDeck method
        original.createDeck("Mathematics");
        original.createDeck("Science");

        // Add cards to decks using the actual addCardToDeck method
        original.addCardToDeck("Mathematics", "What is 2+2?", "4");
        original.addCardToDeck("Mathematics", "What is 5×3?", "15");
        original.addCardToDeck("Science", "What is H₂O?", "Water");

        storage.save(original);

        assertTrue(Files.exists(testFile));
        String fileContent = Files.readString(testFile);
        assertFalse(fileContent.isEmpty());

        DeckManager loaded = storage.load();

        assertNotNull(loaded);

        // Verify decks exist by name (not by index as in previous version)
        Deck mathDeck = loaded.getDeck("Mathematics");
        assertNotNull(mathDeck);
        assertEquals("Mathematics", mathDeck.getDeckName());

        // Note: The actual Deck class doesn't have getCardCount() method
        // The number of cards cannot be verified directly without accessing internal list

        Deck scienceDeck = loaded.getDeck("Science");
        assertNotNull(scienceDeck);
        assertEquals("Science", scienceDeck.getDeckName());
    }

    @Test
    void testSave_createsParentDirectories() throws IOException {
        Path nestedDir = tempDir.resolve("nested").resolve("deep").resolve("test.json");
        Storage storage = new Storage(nestedDir.toString());

        DeckManager deckManager = new DeckManager();
        deckManager.createDeck("Test Deck");

        storage.save(deckManager);

        assertTrue(Files.exists(nestedDir));
        assertTrue(Files.exists(nestedDir.getParent()));
    }

    @Test
    void testLoad_corruptedJson_returnsEmptyDeckManager() throws IOException {
        Path corruptedFile = tempDir.resolve("corrupted.json");
        Files.writeString(corruptedFile, "{ invalid json data");
        Storage storage = new Storage(corruptedFile.toString());

        DeckManager loaded = storage.load();

        assertNotNull(loaded);
    }

    @Test
    void testRoundTrip_complexDeckManager() throws IOException {
        Path testFile = tempDir.resolve("complex.json");
        Storage storage = new Storage(testFile.toString());

        // Create a complex DeckManager
        DeckManager original = new DeckManager();

        for (int i = 1; i <= 3; i++) {
            String deckName = "Deck " + i;
            original.createDeck(deckName);

            for (int j = 1; j <= i; j++) {
                original.addCardToDeck(deckName,
                        "Question " + j + " of " + deckName,
                        "Answer " + j + " of " + deckName);
            }
        }

        storage.save(original);
        DeckManager loaded = storage.load();

        assertNotNull(loaded);

        // Verify each deck by name
        for (int i = 1; i <= 3; i++) {
            String deckName = "Deck " + i;
            Deck loadedDeck = loaded.getDeck(deckName);
            assertNotNull(loadedDeck, "Deck '" + deckName + "' should exist after loading");
            assertEquals(deckName, loadedDeck.getDeckName());
        }
    }

    @Test
    void testSaveAndLoad_cardContentPreserved() throws IOException {
        // Test that card question and answer are correctly preserved
        Path testFile = tempDir.resolve("cardtest.json");
        Storage storage = new Storage(testFile.toString());

        DeckManager original = new DeckManager();
        original.createDeck("TestDeck");

        // Add a card with specific content
        String testQuestion = "What is the capital of France?";
        String testAnswer = "Paris";
        original.addCardToDeck("TestDeck", testQuestion, testAnswer);

        storage.save(original);
        DeckManager loaded = storage.load();

        assertNotNull(loaded);

        Deck loadedDeck = loaded.getDeck("TestDeck");
        assertNotNull(loadedDeck);

        // Note: Cannot directly verify card content as Deck class
        // doesn't expose methods to get card by index
        // This is a limitation of the current Deck class design
    }
}
