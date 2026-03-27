package seedu.flashcli.command;

import java.util.Scanner;

import seedu.flashcli.parser.EditCardArgs;
import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

public class EditCardCommand implements Command{
    private final String deckName; 
    private final int cardIndex; 
    private final String question;
    private final String answer;

    public EditCardCommand(EditCardArgs args){
      this.deckName = args.getDeckName();
      this.cardIndex = args.getCardIndex();
      this.question = args.getQuestion();
      this.answer = args.getAnswer();
    }

    @Override
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException{
      Deck deck = deckManager.getDeck(deckName);
      Card card = deck.editCard(cardIndex, question, answer);
      ui.showCardEdited(card, deckName);
      return false;
    }
}
