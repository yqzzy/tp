package seedu.flashcli.parser;

/**
 * Immutable object grouping the three arguments needed for the addCard comment.
 */
public class AddCardArgs {

    private final String deckName;
    private final String question;
    private final String answer;

    /**
     * Constructs an AddCardArgs with all three addCard fields.
     */
    public AddCardArgs(String deckName, String question, String answer) {
        this.deckName = deckName;
        this.question = question;
        this.answer   = answer;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
