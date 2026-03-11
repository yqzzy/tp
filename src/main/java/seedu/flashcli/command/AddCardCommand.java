package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;
public class AddCardCommand implements Command {
    private String deck;
    private String question;
    private String answer;

    public AddCardCommand(String deck, String question, String answer) {
        this.deck = deck;
        this.question = question;
        this.answer = answer;
    }

    public boolean execute(DeckManager deckManager) {
        deckManager.addCardToDeck(deck, question, answer);
        return false;
    }
}
