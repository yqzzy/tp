package seedu.flashcli;

import java.util.Scanner;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.storage.Storage;

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
    }

    private static void testStorageFunctionality() {
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

}
