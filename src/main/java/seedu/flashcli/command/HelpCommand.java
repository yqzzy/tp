package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.ui.Ui;

public class HelpCommand implements Command {
    /**
     * Displays a help guide containing all the command options and their formats.
     *
     * @param deckManager Represents the current deckManager state.
     * @param ui Prints out the help guide.
     * @return false, indicating the program should not terminate after executing this object.
     */
    @Override
    public boolean execute(DeckManager deckManager, Ui ui) {
        ui.showHelp();
        return false;
    }
}
