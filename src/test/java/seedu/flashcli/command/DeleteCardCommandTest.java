package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.parser.DeleteCardArgs;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCardCommandTest {
    @Test
    public void execute_validCard_cardDeletedFromDeck() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner(System.in);
        Command commandCreateDeck = new CreateDeckCommand("MA1513");
        commandCreateDeck.execute(deckManager, ui, in);
        AddCardArgs addCardArgs = new AddCardArgs("MA1513", "1+1", "2");
        Command commandAddCard = new AddCardCommand(addCardArgs);
        commandAddCard.execute(deckManager, ui, in);
        DeleteCardArgs deleteCardArgs = new DeleteCardArgs("MA1513", 0);
        Command commandDeleteCard = new DeleteCardCommand(deleteCardArgs);
        assertFalse(commandDeleteCard.execute(deckManager, ui, in));
        assertEquals(0, deckManager.getDeck("MA1513").getSize());


    }
}
