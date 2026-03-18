package seedu.flashcli;

import java.util.Scanner;

import seedu.flashcli.command.Command;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.Parser;
import seedu.flashcli.storage.Storage;
import seedu.flashcli.exception.FlashException;

public class FlashCLI {
    private final DeckManager deckManager;
    Storage storage = new Storage("data/storage.json");
    private final Ui ui = new Ui();

    public FlashCLI() {
        deckManager = storage.load();
    }
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        FlashCLI flashCLI = new FlashCLI();
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

        Command command;
        String userInput;
        while (true) {
            userInput = in.nextLine().trim();
            if (!flashCLI.executeCommand(userInput)) {
                break;
            }
        }
        in.close();
    }

    /**
     * Executes command the user wants (add item to list, print list, exit).
     * @param userInput The command input by the user.
     * @return true if program should exit after executing this command.
     */
    public boolean executeCommand(String userInput) {
        Command command;
        try {
            command = Parser.parse(userInput);
            boolean exitProgram = command.execute(deckManager, ui);
            storage.save(deckManager);
            return exitProgram;
        } catch (FlashException e) {
            ui.showError(userInput, e.getMessage());
            return false;
        }
    }


}
