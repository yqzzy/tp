package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateDeckCommandTest {
    @Test
    public void execute_validDeck_deckCreated() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner(System.in);
        Command command = new CreateDeckCommand("MA1513");
        assertFalse(command.execute(deckManager, ui, in));
        assertNotNull(deckManager.getDeck("MA1513"));

    }

}
