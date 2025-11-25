

/**
 * Represents a specific type of game card known as a FireCard.
 * Inherits from the base class Card.
 */
public class FireCard extends Card {

    /**
     * Constructor for FireCard.
     * Initializes a 3x3 fire card where all squares contain fire.
     * ID for FireCard is always 0 (only one type of fire card).
     */
    public FireCard(){
        super(CardType.FIRECARD,0);
        this.cardSquare = createFireCardSquare();      //it is 3x3
    }

    /**
     * Creates a 3x3 square array for the fire card, where all squares contain fire.
     *
     * @return A 2D array of Square objects representing the fire card layout
     */
    private Square[][] createFireCardSquare(){
        Square[][]  squares = new Square[3][3];
        for(int row=0;row<3;row++) {
            for (int col = 0; col < 3; col++) {
                squares[row][col] = new Square(new Location(row,col),this,Color.NONE);
                squares[row][col].setHasFire(true);
            }
        }
        return squares;
    }

    /**
     * overwrite the toString method to output the fireCard,
     *
     * @return the string of fireCard, e.g. FireCard: place[0,3] ID:0 fffffffff
     */
    @Override
    public String toString() {
        char[] ch = new char[9];
        Square square;
        for(int i=0;i<9;i++){
            square = this.cardSquare[i/3][i%3];
            ch[i]=(square.isHasFire())?'f':'n';

        }
        return "FireCard: place"+this.placedLocation+" "+ super.toString() + " " + new String(ch);
    }
}
