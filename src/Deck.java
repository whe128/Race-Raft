

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents a deck of pathway cards for a game.
 */
public class Deck {
    private final DeckType  deckType;          //final
    ArrayList<PathwayCard> cardList = new ArrayList<>();

    /**
     * Enum representing different types of decks.
     */
    public enum DeckType{
        A_CROSS,B_SQUARE,C_CIRCLE,D_TRIANGLE;

        /**
         * Converts the deck type enum value to a character representation.
         *
         * @return The character corresponding to the deck type
         */
        public char toChar(){
            //give a initial value
            char ch = '0';
            switch (this){
                case A_CROSS   -> ch = 'A';
                case B_SQUARE    -> ch = 'B';
                case C_CIRCLE   -> ch = 'C';
                case D_TRIANGLE -> ch = 'D';
                default         -> {}
            }
            return ch;
        }

        /**
         * Converts a character to the corresponding DeckType enum value.
         *
         * @param ch The character representing the deck type ('A', 'B', 'C', 'D')
         * @return The DeckType enum corresponding to the character
         */
        public static DeckType charToDeckType(char ch){
            DeckType deckType;
            switch (Character.toUpperCase(ch)){
                case 'A' -> deckType = DeckType.A_CROSS;
                case 'B' -> deckType = DeckType.B_SQUARE;
                case 'C' -> deckType = DeckType.C_CIRCLE;
                case 'D' -> deckType = DeckType.D_TRIANGLE;
                default  -> deckType = DeckType.A_CROSS;
            }
            return deckType;
        }
    }

    /**
     * Constructs a new deck of pathway cards based on the specified deck type.
     * this constructor is for the start of game, and should add all card into the deck
     *
     * @param type The type of the deck (A_CIRCLE, B_CROSS, C_SQUARE, D_TRIANGLE)
     */
    public Deck(DeckType type){
        this.deckType = type;
        //calculate the number of all card, then create and add to the deck
        int MaxCardNum = 0;
        switch (this.deckType){
            case A_CROSS   -> MaxCardNum = PathwayCard.MAX_PATHWAY_CARD_DECK_A;
            case B_SQUARE    -> MaxCardNum = PathwayCard.MAX_PATHWAY_CARD_DECK_B;
            case C_CIRCLE   -> MaxCardNum = PathwayCard.MAX_PATHWAY_CARD_DECK_C;
            case D_TRIANGLE -> MaxCardNum = PathwayCard.MAX_PATHWAY_CARD_DECK_D;
        }

        //add pathwayCard, initialization
        //pathway card is from 0~24 (number of all is 25)
        for(int i = 0; i< MaxCardNum;i++ ){
            this.cardList.add(new PathwayCard(type,i));
        }
    }

    /**
     * Constructs a new deck of pathway cards from a string representation.
     * this is for the start from the middle state
     * original string e.g., "AabcdBCfghDafh"
     * there has been separated to the sub string of each deck e.g. Aabc or Cfgh
     *
     * @param deckString The string representing the deck
     */
    public Deck(String deckString){
        deckString = deckString.replace(" ","");
        deckString = deckString.replace("\n","");

        char[] chars = deckString.toCharArray();
        this.deckType = DeckType.charToDeckType(chars[0]);
        for(int i=1;i<chars.length;i++){
            this.cardList.add(new PathwayCard(this.deckType,chars[i]-'a'));
        }
    }

    /**
     * Checks if the provided deck string is well-formed.
     * A well-formed string must:
     *      - Have all the Decks (A,B,C,D)
     *      - Not have other decks (E,F,G...)
     *      - Be a 1 line
     *      - Not have repeated Decks (eg. AABBCCDD)
     *      - Not have repeated Cards on a Deck (eg. AaaBbbCccDdd)
     *      - Not have cards other than (a to y)
     *
     * @param deckString The string representing the deck
     * @return true if the deck string is well-formed, false otherwise
     */
    public static boolean isDeckStringsWellFormed(String deckString){
        //judge 1 line
        if (deckString.indexOf('\n')>0){
            return false;
        }

        String validChars = "ABCDabcdefghijklmnopqrstuvwxy";

        //valid number
        for(char ch:deckString.toCharArray()){
            if(validChars.indexOf(ch)<0){
                return false;
            }
        }
        char[] typeChars = {'A','B','C','D'};
        int repeatNum =0;
        //must have A B C D, and can not repeat
        for(char ch:typeChars){
            repeatNum = 0;
            for (char deckChar:deckString.toCharArray()){
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
        for(char ch:deckString.toCharArray()){
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
        return true;
    }


    /**
     * randomly get a card from the list, and remove it,
     * means the action of draw card and get it from the deck
     *
     * @return the randomly chosen card or null if there is no card in the deck
     */
    public PathwayCard removeCard(){
        int num = this.cardList.size();
        if(num>0){
            Random random = new Random();
            int index = random.nextInt(num);
            PathwayCard card = this.cardList.get(index);
            this.cardList.remove(index);
            return card;
        }
        else {
            return null;
        }
    }

    /**
     * get the validity of the request of drawing card
     * if card number in the deck less than request num, then it is false request
     *
     * @param requestNum the number of drawing card
     * @return true if the request is valid, false otherwise
     */
    public boolean isRequestValid (int requestNum){ return this.cardList.size() >= requestNum; }

    // Getter Methods

    /**
     * get the card num in the deck
     *
     * @return card num in the deck
     */
    public int getCardNum() { return this.cardList.size(); }

    /**
     * get the deck type of the deck
     *
     * @return deck type of the deck
     */
    public DeckType getDeckType() { return deckType; }

    /**
     * get the list of card in the deck
     *
     * @return list of card in the deck
     */
    public ArrayList<PathwayCard> getPathwayCards() { return cardList; }

    /**
     * judgement whether the deck is empty
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty(){ return this.cardList.size() == 0; }

    /**
     * Generates a string representation of the deck's pathway cards.
     *
     * @return A string representing the deck's pathway cards
     */
    public String getDeckString(){
        char[] chars = new char[this.cardList.size()];
        int index = 0;
        for(PathwayCard p:this.cardList){
            chars[index] = p.getTypeChar();
            index++;
        }
        return this.deckType.toChar()+String.valueOf(chars);
    }

    /**
     * overwrite the toString method to output the deck status which includes deckChar, card number and card string
     * e.g. Deck A 25: Aabcdefghijklmnopqrstuvwxy
     *
     * @return deck status
     */
    @Override
    public String toString() {
        int cardNum = this.cardList.size();
        //the first letter is the type char of Decks---so if the size < 1, it means there is no card
        if(cardNum == 0)
        {
            return "Deck "+this.deckType.toChar()+" "+cardNum+"： there is no pathWay card on this Deck " ;
        }
        return  "Deck "+(this.deckType.toChar()) +" "+cardNum+": "+ getDeckString();
    }

}
