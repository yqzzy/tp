package seedu.flashcli.parser;

public class EditCardArgs {
    private final String deckName; 
    private final int cardIndex; 
    private final String question;
    private final String answer; 

    public EditCardArgs(String deckName, int cardIndex, String question, String answer){
        this.deckName = deckName;
        this.cardIndex = cardIndex;
        this.question = question;
        this.answer = answer;
    }

    public String getDeckName(){
        return deckName;
    }

    public int getCardIndex(){
        return cardIndex;
    }

    public String getQuestion(){
        return question;
    }

    public String getAnswer(){
        return answer; 
    }
}
