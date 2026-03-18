package seedu.flashcli;

import java.util.List;
import java.util.Scanner;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.storage.Storage;
import seedu.flashcli.exception.FlashException;

public class FlashCLI {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("What is your name?");

        Scanner in = new Scanner(System.in);
        System.out.println("Hello " + in.nextLine());
        //testStorageFunctionality();
        //testStorageHistory();
    }

    private static void testStorageFunctionality() throws FlashException{
        System.out.println("=== Storage Test ===");

        // 1. Initialize
        Storage storage = new Storage("data/flashcards.json");

        // 2. Create test data
        DeckManager testManager = new DeckManager();
        testManager.createDeck("math");
        testManager.createDeck("English");
        Deck mathDeck = testManager.getDeck("math");
        mathDeck.addCard("triangle", "S=0.5*a*b");
        mathDeck.addCard("rectangle","S=a*b");

        Deck englishDeck = testManager.getDeck("english");
        englishDeck.addCard("hi", "hello");

        System.out.println("Create " + testManager.getDeck("math").getSize() + " decks");

        // 3. save data
        storage.save(testManager);
        System.out.println("data saved");

        // 4. load data
        DeckManager loadedManager = storage.load();

        // 5. simple test
        if (loadedManager.getDeck("math") != null) {
            System.out.println("first deck: " + loadedManager.getDeck("math").getDeckName());
            System.out.println("this deck includes " + loadedManager.getDeck("math").getSize() + " cards");
        } else {
            System.out.println("fail");
        }

        System.out.println("=== finish test ===");

        System.out.println("\ndata saved in " + System.getProperty("user.dir") + "/data/flashcards.json");
        System.out.println("You can view JSON file there");
    }

    /**
     * Tests the Storage and HistoryManager functionality.
     * Creates test data, saves multiple versions, lists them, and cleans up.
     * Run with: java FlashCLI --test-storage
     */
    private static void testStorageHistory() {
        System.out.println("=== Storage History Functionality Test ===");

        // Use a test file to avoid interfering with actual data
        String testFilePath = "./data/test_flashcards.json";
        Storage storage = new Storage(testFilePath);

        DeckManager deckManager = new DeckManager();

        // Create first deck and save
        System.out.println("\n1. Creating and saving first deck...");
        deckManager.createDeck("Mathematics");
        deckManager.addCardToDeck("Mathematics", "What is 2+2?", "4");
        storage.save(deckManager);

        // Create second deck and save (should create first history version)
        System.out.println("2. Creating and saving second deck...");
        deckManager.createDeck("Science");
        deckManager.addCardToDeck("Science", "What is H₂O?", "Water");
        storage.save(deckManager);

        // List all history versions
        System.out.println("3. Listing all history versions...");
        List<String> versions = storage.getHistoryVersions();
        System.out.println("   Found " + versions.size() + " history versions:");
        for (int i = 0; i < versions.size(); i++) {
            System.out.println("   [" + i + "] " + versions.get(i));
        }

        // Test retrieval by index
        if (!versions.isEmpty()) {
            System.out.println("\n4. Retrieving latest history version (index 0)...");
            DeckManager retrieved = storage.retrieveHistoryByIndex(0);
            System.out.println("   Successfully retrieved historical version.");
        }

        // Test retrieval by timestamp
        if (!versions.isEmpty()) {
            System.out.println("\n5. Retrieving by timestamp: " + versions.get(0));
            DeckManager retrievedByTime = storage.retrieveHistoryByTimestamp(versions.get(0));
            System.out.println("   Successfully retrieved version by timestamp.");
        }

        // Clean all history
        System.out.println("\n6. Cleaning all history (moving to waste)...");
        storage.cleanAllHistory();

        // Verify cleanup
        List<String> remainingVersions = storage.getHistoryVersions();
        System.out.println("   Remaining versions after cleanup: " + remainingVersions.size());

        // Create more test data
        System.out.println("\n7. Creating more test data for selective cleanup...");
        for (int i = 1; i <= 3; i++) {
            deckManager.createDeck("TestDeck" + i);
            deckManager.addCardToDeck("TestDeck" + i, "Question " + i, "Answer " + i);
            storage.save(deckManager);
        }

        // List new versions
        List<String> newVersions = storage.getHistoryVersions();
        System.out.println("   Created " + newVersions.size() + " new history versions.");

        // Test selective cleanup
        if (newVersions.size() >= 2) {
            System.out.println("\n8. Testing selective cleanup...");
            List<String> toDelete = newVersions.subList(0, 2);
            storage.cleanSelectHistory(toDelete);
            System.out.println("   Deleted " + toDelete.size() + " selected versions.");

            List<String> finalVersions = storage.getHistoryVersions();
            System.out.println("   Final version count: " + finalVersions.size());
        }

        System.out.println("\n=== Test Complete ===");
        System.out.println("Check the following directories for files:");
        System.out.println("1. ./data/ - Main data file");
        System.out.println("2. ./data/history/ - History versions");
        System.out.println("3. ./data/waste/ - Cleaned history versions");
    }

}
