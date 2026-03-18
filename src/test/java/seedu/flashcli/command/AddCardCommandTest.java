package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.ui.Ui;

import static org.junit.jupiter.api.Assertions.*;

public class AddCardCommandTest {


    @Test
    public void execute_validCard_cardAddedToDeck() {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        try {
            Command commandCreateDeck = new CreateDeckCommand("MA1513");
            commandCreateDeck.execute(deckManager, ui);
            AddCardArgs addCardArgs = new AddCardArgs("MA1513", "1+1", "2");
            Command commandAddCard = new AddCardCommand(addCardArgs);
            assertFalse(commandAddCard.execute(deckManager, ui));
            assertEquals("Question: 1+1",deckManager.getDeck("MA1513").getCard(0).getQuestionString());
            assertEquals("Answer: 2",deckManager.getDeck("MA1513").getCard(0).getAnswerString());
            assertEquals(1, deckManager.getDeck("MA1513").getSize());
        } catch (FlashException e) {
            System.out.println(e.getMessage());
        }

    }


}
