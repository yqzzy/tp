package seedu.flashcli.command;

import seedu.flashcli.command.Command;
import seedu.flashcli.deck.DeckManager;

public class ExitCommand implements Command {
    @Override
    public boolean execute(DeckManager deckManager) {
        System.out.println("Exiting. Goodbye!");
        return true;
    }
}
