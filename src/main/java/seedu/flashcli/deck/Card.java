package seedu.flashcli.deck;

public class Card {
    private String question;
    private String answer;
    private Integer confidenceLevel;

    public Card(String question, String answer, Integer confidenceLevel){
        this.question = question;
        this.answer = answer;
        this.confidenceLevel = confidenceLevel;
    }

    // ----- overloaded constructors -----
    public Card(String question, String answer){
        this(question, answer, 0);
    }

    public Card() {
        this("", "", 0);
    }
    // ------------------------------------

    public String getQuestion(){
        return question;
    }

    public String getAnswer(){
        return answer; 
    }
    
    public Integer getConfidenceLevel(){
        return confidenceLevel;
    }
}
