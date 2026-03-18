package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.parser.DeleteCardArgs;
import seedu.flashcli.ui.Ui;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteCardCommandTest {
    @Test
    public void execute_validCard_cardDeletedFromDeck() {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        try {
            Command commandCreateDeck = new CreateDeckCommand("MA1513");
            commandCreateDeck.execute(deckManager, ui);
            AddCardArgs addCardArgs = new AddCardArgs("MA1513", "1+1", "2");
            Command commandAddCard = new AddCardCommand(addCardArgs);
            commandAddCard.execute(deckManager, ui);
            DeleteCardArgs deleteCardArgs = new DeleteCardArgs("MA1513", 0);
            Command commandDeleteCard = new DeleteCardCommand(deleteCardArgs);
            assertFalse(commandDeleteCard.execute(deckManager, ui));
            assertEquals(0, deckManager.getDeck("MA1513").getSize());
        } catch (FlashException e) {
            System.out.println(e.getMessage());
        }

    }
}
