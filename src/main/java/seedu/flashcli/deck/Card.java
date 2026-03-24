package seedu.flashcli.deck;

public class Card {
    private String question;
    private String answer;
    private int confidenceLevel;

    public Card(String question, String answer){
        this.question = question;
        this.answer = answer;
        this.confidenceLevel = 0;
    }

    public Card(){}

    public void setConfidenceLevel(Integer confidenceLevel){
        this.confidenceLevel = confidenceLevel;
    }

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
