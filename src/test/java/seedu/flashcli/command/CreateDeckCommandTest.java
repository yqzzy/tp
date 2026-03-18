package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import static org.junit.jupiter.api.Assertions.*;

public class CreateDeckCommandTest {
    @Test
    public void execute_validDeck_deckCreated() {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        try {
            Command command = new CreateDeckCommand("MA1513");
            assertFalse(command.execute(deckManager, ui));
            assertNotNull(deckManager.getDeck("MA1513"));
        } catch (FlashException e) {
            System.out.println(e.getMessage());
        }
    }

}
