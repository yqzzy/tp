package seedu.flashcli.deck;
import java.util.ArrayList;

public class Deck {
    private String deckName;
    private ArrayList<Card> cardList = new ArrayList<>();

    public Deck(String deckName){
        this.deckName = deckName;
    }

    public Deck(){}

    public String getDeckName(){
        return deckName;
    }

    public int getSize(){
        return cardList.size();
    }
    
    // takes in the card question and answer, creates a new Card object 
    // and adds it to the cardList
    public void addCard(String question, String answer){
        Card newCard = new Card(question, answer);
        cardList.add(newCard);
        System.out.println("Card added!");
    }

    //prints out the index and question of every question in the deck
    public void listCards(){
        final String listLine = "%d. %s%n";
        int count = 1;
        for (Card card: cardList){
            System.out.printf(listLine, count, card.getQuestion());
            count++;
        }
    }
    
    //deletes the card at the index specified by the user
    public void deleteCard(int cardIndex){
        cardList.remove(cardIndex);
        System.out.println("Card deleted!");
    }

    //returns the Card object at the specified index of the cardList
    public Card getCard(int cardIndex){
        return cardList.get(cardIndex);
    }
}
