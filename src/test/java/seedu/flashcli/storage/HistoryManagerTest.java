package seedu.flashcli.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.flashcli.deck.DeckManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryManagerTest {

    @TempDir
    Path tempDir;
    private HistoryManager historyManager;
    private final String testBaseName = "flashcards";

    @BeforeEach
    void setUp() throws IOException {
        String dataDirPath = tempDir.toString();
        historyManager = new HistoryManager(dataDirPath, testBaseName);
    }

    @Test
    void testConstructorCreatesDirectories() {
        Path historyDir = tempDir.resolve("history");
        Path wasteDir = tempDir.resolve("waste");

        assertTrue(Files.exists(historyDir), "History directory should exist");
        assertTrue(Files.exists(wasteDir), "Waste directory should exist");
    }

    @Test
    void testSaveVersionCreatesHistoricalFile() throws IOException {
        DeckManager testData = createTestDeckManager();

        historyManager.saveVersion(testData);

        List<String> versions = historyManager.listVersions();
        assertEquals(1, versions.size(), "One version should be saved");
    }

    @Test
    void testListVersionsOrder() throws IOException, InterruptedException {
        DeckManager data1 = createTestDeckManager();
        DeckManager data2 = createTestDeckManager();
        data2.createDeck("Second Deck");

        historyManager.saveVersion(data1);
        Thread.sleep(10); // Ensure different timestamps
        historyManager.saveVersion(data2);

        List<String> versions = historyManager.listVersions();
        assertEquals(2, versions.size(), "Should have two versions");
    }

    @Test
    void testRetrieveByIndex() throws IOException {
        DeckManager original = createTestDeckManager();
        original.createDeck("Test Deck");

        historyManager.saveVersion(original);
        DeckManager retrieved = historyManager.retrieveByIndex(0);

        assertNotNull(retrieved, "Retrieved data should not be null");
    }

    @Test
    void testRetrieveByTime() throws IOException {
        DeckManager original = createTestDeckManager();
        original.createDeck("TimeTest");

        historyManager.saveVersion(original);
        List<String> versions = historyManager.listVersions();
        String timestamp = versions.get(0);

        DeckManager retrieved = historyManager.retrieveByTime(timestamp);
        assertNotNull(retrieved, "Retrieved data should not be null");
    }

    @Test
    void testDeleteAllHistory() throws IOException {
        historyManager.saveVersion(createTestDeckManager());
        historyManager.saveVersion(createTestDeckManager());

        assertEquals(2, historyManager.listVersions().size(),
                "Should have two versions before deletion");

        historyManager.deleteAllHistory();

        assertTrue(historyManager.listVersions().isEmpty(),
                "History should be empty after deletion");
    }

    @Test
    void testInvalidIndexThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            historyManager.retrieveByIndex(0);
        }, "Should throw for non-existent index");
    }

    private DeckManager createTestDeckManager() {
        DeckManager deckManager = new DeckManager();
        deckManager.createDeck("Mathematics");
        deckManager.addCardToDeck("Mathematics", "2+2?", "4");
        return deckManager;
    }
}
