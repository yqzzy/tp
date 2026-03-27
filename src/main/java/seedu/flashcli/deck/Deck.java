package seedu.flashcli.deck;
import java.util.ArrayList;
import java.util.Collections;

import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.List;

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
    
    public Card addCard(String question, String answer){
        int oldSize = cardList.size();
        Card newCard = new Card(question, answer);
        cardList.add(newCard);
        
        assert cardList.size() == oldSize + 1;
        assert cardList.contains(newCard);

        return newCard;
    }

    public ArrayList<Card> listCards(){
        return cardList;
    }
    
    public Card deleteCard(int cardIndex) throws FlashException{
        if (cardIndex >= cardList.size() || cardIndex < 0){
            throw new FlashException(ErrorType.INVALID_INDEX);
        } else { 
            int oldSize = cardList.size();
            Card removed = cardList.get(cardIndex);
            cardList.remove(cardIndex);

            assert cardList.size() == oldSize -1;
            assert !cardList.contains(removed);

            return removed;
        }
    }

    public Card getCard(int cardIndex) throws FlashException{
        if (cardIndex >= cardList.size() || cardIndex < 0){
            throw new FlashException(ErrorType.INVALID_INDEX);
        } else { 
            Card card = cardList.get(cardIndex);

            assert card != null; 

            return card;
        }
        
    }

    public List<Card> getCards(){
        return Collections.unmodifiableList(cardList);
    }

    public void clearCards(){
        cardList.clear();
    }

    public void setCards(ArrayList<Card> cardList){
        this.cardList = cardList;
    }

    public Card editCard(int cardIndex, String newQuestion, String newAnswer){
        if (cardIndex >= cardList.size() || cardIndex < 0){
            throw new FlashException(ErrorType.INVALID_INDEX);
        }

        Card card = cardList.get(cardIndex);
        card.setQuestion(newQuestion);
        card.setAnswer(newAnswer);
        return card;
    }
}
