

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a player's hand of pathway cards in a game.
 */
public class Hand {
    public static final int MAXCARDNUM = 6;
    private ArrayList<PathwayCard> cardList = new ArrayList<>();        //can up or not

    /**
     * Constructs an empty hand.
     * Used for initialising hand at the start of the game, where the cardNum is 0.
     */
    public Hand() { }

    /**
     * Constructs a hand from the given string representation.
     * The string must follow specific format rules to be parsed correctly.
     *
     * @param handStr The string representation of the hand, containing deck type and card IDs.
     */
    public Hand(String handStr) {
        this.cardList = new ArrayList<>();
        char[] deckTypeArray = {'A', 'B', 'C', 'D'};
        char deckTypeChar = 0;
        handStr = handStr.replace(" ","");
        handStr = handStr.replace("\n","");
        for (Character ch: handStr.toCharArray()) {
            // Find the type
            if (Arrays.binarySearch(deckTypeArray, ch) >= 0) {
                deckTypeChar = ch;
            } else {
                // Create pathway card if deck type is valid and character is within 'a' to 'y'
                if (Arrays.binarySearch(deckTypeArray, deckTypeChar) >= 0 && ch >= 'a' && ch <= 'y') {
                    cardList.add(new PathwayCard(Deck.DeckType.charToDeckType(deckTypeChar), ch - 'a'));
                }
            }
        }
    }

    /**
     * Checks if the provided hand string is well-formed according to specific criteria.
     *
     * @param handString The string representation of the hand to be validated.
     * @return true if the hand string is well-formed, false otherwise.
     */
    public static boolean isHandStringsWellFormed(String handString){
        //judge 1 line
        if (handString.indexOf('\n')>0){
            return false;
        }

        String validChars = "ABCDabcdefghijklmnopqrstuvwxy";

        //valid number
        for(char ch:handString.toCharArray()){
            if(validChars.indexOf(ch)<0){
                return false;
            }
        }
        char[] typeChars = {'A','B','C','D'};
        int repeatNum =0;
        //must have A B C D, and can not repeat
        for(char ch:typeChars){
            repeatNum = 0;
            for (char deckChar:handString.toCharArray()){
                if(deckChar==ch){
                    repeatNum++;
                }
            }
            if(repeatNum==0 || repeatNum>1){
                return false;
            }
        }

        //can not repeat
        HashMap<Character,Integer> hashMap=new HashMap<>();
        char deckType='A';
        for(char ch:handString.toCharArray()){
            if(ch>='A'||ch<='D'){
                if(deckType!=ch){
                    hashMap.clear();
                    deckType=ch;
                }
            }
            if(hashMap.getOrDefault(ch,0)>=1){
                return false;
            }
            hashMap.put(ch, hashMap.getOrDefault(ch,0)+1);
        }
        //num must less than or equal 6
        handString = handString.replace("A","");
        handString = handString.replace("B","");
        handString = handString.replace("C","");
        handString = handString.replace("D","");
        if(handString.length()>6){
            return false;
        }
        return true;
    }
    // Action Methods

    /**
     * Adds a pathway card to the hand.
     */
    public void addCard(PathwayCard card) {
            this.cardList.add(card);
    }

    /**
     * Removes a pathway card from the hand at the specified index.
     *
     * @param index The index of the card to remove.
     * @return The removed pathway card.
     */
    public PathwayCard removeCard(int index) {
        PathwayCard card = cardList.get(index);
        this.cardList.remove(index);
        return card;
    }

    /**
     * Removes a specific card from the hand.
     *
     * @param card The card to remove from the hand.
     */
    public void removeCard(Card card) {
        this.cardList.remove(card);
    }


    // Getter Methods

    /**
     * Retrieves the number of cards in the hand.
     *
     * @return The number of cards in the hand.
     */
    public int getCardNum() {
        return this.cardList.size();
    }

    /**
     * Retrieves the number of cards in the hand.
     *
     * @return The max number of cards in the hand.
     */
    public int getMaxCardNum(){return MAXCARDNUM;}


    /**
     * Retrieves the list of cards in the hand.
     *
     * @return The list of cards in the hand.
     */
    public ArrayList<PathwayCard> getCardsList() { return cardList; }

    /**
     * Retrieves a card from the hand at the specified index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index, or null if the index is out of bounds.
     */
    public Card getCard(int index) {
        if (index >= 0 && index < this.cardList.size()) {
            return this.cardList.get(index);
        }
        return null;
    }

    /**
     * Checks if the hand is empty (contains no cards).
     *
     * @return true if the hand is empty, false otherwise.
     */
    public boolean isEmpty(){ return this.cardList.size() == 0; }

    /**
     * Generates a string representation of the hand.
     *
     * @return The string representation of the hand.
     */
    public String getHandString(){

        StringBuffer stringBuffer = new StringBuffer();
        char[] typeChar = {'A','B','C','D'};
        //Search all pathwayCards on the hand,then store
        for (int i = 0; i < 4; i++) {
            stringBuffer.append(typeChar[i]);
            //this loop is for store {deck char} + {card id}
            for (PathwayCard p : this.cardList) {
                if (p.getDeckType().toChar() == typeChar[i]) {
                   stringBuffer.append(p.getTypeChar());
                }
            }
        }
        return stringBuffer.toString();
    }

    /**
     * Returns a string representation of the hand with number of card in a hand
     *
     * @return A string representing the hand.
     */
    @Override
    public String toString() {
        return "Hand " + cardList.size() + ": " + getHandString();
    }

}
