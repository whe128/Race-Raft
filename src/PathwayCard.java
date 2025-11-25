

/**
 * Represents a pathway card in the game.
 * Each pathway card belongs to a specific deck type (A, B, C, or D) and has a unique ID.
 * Inherits from the base class Card.
 */
public class PathwayCard extends Card {

    // Constants for maximum number of cards in each deck type
    public static final int MAX_PATHWAY_CARD_DECK_A = Utility.DECK_A.length;
    public static final int MAX_PATHWAY_CARD_DECK_B = Utility.DECK_B.length;
    public static final int MAX_PATHWAY_CARD_DECK_C = Utility.DECK_C.length;
    public static final int MAX_PATHWAY_CARD_DECK_D = Utility.DECK_D.length;

    private final Deck.DeckType deckType;
    private final char typeChar;

    /**
     * Constructs a new PathwayCard instance with the specified deck type and ID.
     *
     * @param type The deck type of the pathway card (A, B, C, or D).
     * @param ID   The ID of the pathway card within the specified deck type (0 to 24).
     */
    public PathwayCard(Deck.DeckType type,int ID){
        super(CardType.PATHWAY,ID);
        String string;
        switch (type) {
            case A_CROSS   -> string = Utility.DECK_A[ID];
            case B_SQUARE    -> string = Utility.DECK_B[ID];
            case C_CIRCLE   -> string = Utility.DECK_C[ID];
            case D_TRIANGLE -> string = Utility.DECK_D[ID];
            default -> string = "";
        }
        this.deckType = type;
        this.typeChar = string.charAt(0);
        this.cardSquare = getPathwayCardFromString(string);
    }

    /**
     * Constructs a 3x3 array of Square objects representing the pathway card layout.
     *  The first character is a letter from a to y that represents the ID of the card.
     *  The second to tenth characters (b, g, p, r or y) give the state of each square in row-major.
     *      b g p
     *      p r y
     *      b b g
     *
     * @param string The string representation of the pathway card layout.
     * @return A 2D array of Square objects representing the pathway card layout.
     */
    private Square[][] getPathwayCardFromString(String string){
        Square[][] squares = new Square[3][3];
        char ch;
        for(int row=0;row<3;row++) {
            for (int col = 0; col < 3; col++) {
                ch = string.charAt(3 * row + col+1);        //because there is a type char at first, so +1
                Color color = Color.charToColor(ch);
                squares[row][col] = new Square(new Location(row,col),this,color);
            }
        }
        return squares;
    }

    /**
     * Retrieves the deck type of the pathway card.
     *
     * @return The deck type of the pathway card.
     */
    public Deck.DeckType getDeckType() {
        return deckType;
    }

    /**
     * Retrieves the character representation of the pathway card type.
     *
     * @return The character representing the pathway card type.
     */
    public char getTypeChar() { return this.typeChar; }


    /**
     * Returns a string representation of the pathway card.
     *
     * @return A string representing the pathway card.
     */
    @Override
    public String toString() {
        char[] ch = new char[10];
        Square square;

        //ch[0] is stored the typeChar, others are stored the color of squares
        ch[0] = this.typeChar;
        for(int i=0;i<9;i++){
            square = this.cardSquare[i/3][i%3];
            ch[i+1]=square.getColor().toChar(false);
        }
        return "PathwayCard: "+this.placedLocation+" "+ super.toString() + " "+ new String(ch);
    }
}
