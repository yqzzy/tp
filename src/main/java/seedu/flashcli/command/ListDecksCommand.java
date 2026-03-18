package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.ui.Ui;

import java.util.List;

public class ListDecksCommand implements Command {

    /**
     * Lists the names of all the decks for the user to see.
     *
     * @param deckManager Represents the current deckManager state.
     * @return false, false, indicating the program should not terminate after executing this object.
     */
    @Override
    public boolean execute(DeckManager deckManager, Ui ui) {
        List<String> deckNameList = deckManager.listDecks();
        ui.showDeckList(deckNameList);
        return false;
    }
}
