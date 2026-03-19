package seedu.flashcli;

import java.util.Scanner;

import seedu.flashcli.command.Command;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.Parser;
import seedu.flashcli.storage.Storage;
import seedu.flashcli.ui.Ui;

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
        flashCLI.ui.hello();
        Scanner in = new Scanner(System.in);
        String userInput;
        while (!(userInput = in.nextLine()).equals("exit")) {
            if (flashCLI.executeCommand(userInput, in)) break;
        }
    }


    /**
     * Executes command the user wants (add item to list, print list, exit).
     *
     * @param userInput The command input by the user.
     * @return true if program should exit after executing this command.
     */
    // Change executeCommand to accept the scanner
    public boolean executeCommand(String userInput, Scanner in) {
        Command command;
        try {
            command = Parser.parse(userInput);
            boolean exitProgram = command.execute(deckManager, ui, in);
            storage.save(deckManager);
            return exitProgram;
        } catch (FlashException e) {
            ui.showError(userInput, e.getMessage());
            return false;
        }
    }


}
