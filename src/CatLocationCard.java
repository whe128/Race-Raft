

/**
 * Represents a specific type of game card known as a CatLocationCard.
 * Inherits from the base class Card.
 */
public class CatLocationCard extends Card {
    public static final int MAX_CATLOCATION_CARD_NUMBER = Utility.CAT_CARDS.length;

    /**
     * Constructor of CatLocationCard
     * Initializes a catLocationCard based on the provided ID.
     *
     * @param ID is the integer ID (range:0 to 6)
     */
    public CatLocationCard(int ID){
        super(CardType.CATLOCATION,ID);
        String string = Utility.CAT_CARDS[ID];
        this.cardSquare = getCatLocationCardSquareFromString(string);
    }

    /**
     * this is use the string to create the instance of object of square
     * @param string :like "0rrfrRfrrf",
     * @return the 2D array of square, position each row and col of square
     *         has the information of this unit
     */
    private Square[][] getCatLocationCardSquareFromString(String string){
        char[] charArray = string.substring(1).toCharArray();
        Square[][] squares = new Square[3][3];
        //3x3 double loop
        for(int row=0;row<3;row++){
            for (int col=0;col<3;col++){
                // get each char
                char ch = charArray[3*row+col];
                boolean isHasFire = (ch == 'f');
                boolean isHasCat = (Character.isUpperCase(ch));
                Color color = Color.charToColor(ch);
                squares[row][col] = new Square(new Location(row,col),this, color);
                squares[row][col].setHasCat(isHasCat);
                squares[row][col].setHasFire(isHasFire);
            }
        }
        return squares;
    }

    /**
     * Gets the ID of catLocationCard of the fire tile.
     *
     * @return The ID of catLocationCard.
     */
    public int getCardID() { return this.cardID; }

    /**
     * get the cat location card string by squares
     *
     * @return the 9 length string
     */
    public String getString(){
        char[] ch = new char[9];
        Square square;

        //reBecome the representing string
        for(int i=0;i<9;i++){
            square = this.cardSquare[i/3][i%3];
            if(square.isHasFire()){
                ch[i]='f';
            }
            else{
                ch[i] = square.getColor().toChar(square.isHasCat());
            }
        }
        return new String(ch);
    }
    /**
     * Returns a string representation of the catLocationCard, including its location, ID and each square char status.
     * example: g[0,0] CATLOC  Green fire:false  o:false w:false
     *
     * @return A string representation of the catLocationCard.
     */
    @Override
    public String toString() {
        return "CatLocationCard: place"+this.placedLocation+" "+ super.toString() + " " + getString();
    }

    /**
     * inner main method to check and test, to check whether the design is well done.
     *
     * @param args
     */
    public static void main(String[] args) {
        CatLocationCard catLocationCard = new CatLocationCard(2);
        for(Square[] s: catLocationCard.cardSquare){
            for(Square ss: s){
                System.out.println(ss);
            }
        }
        //System.out.println(MAX_CATLOCATION_CARD_NUMBER);
        //System.out.println(catLocationCard.catLocationInCard);
        System.out.println(catLocationCard);
    }
}
