

/**
 * Represents a generic game card used in the game board, which can be extended by specific card types
 * such as pathway cards, fire cards, cat location cards, etc.
 */
public class Card {
    public int sizeRow;                     //the size of row
    public int sizeCol;                  //the size of column
    public CardType cardType;     //final
    public final int cardID;            //final

    public Orientation orientation;
    public boolean isFlipped;
    public Location placedLocation;     //the location is on the whole board that needs us to put the card on this location
    public Square[][] cardSquare;       //the card layout of squares itself

    /**
     * Enum defining different card types.
     */
    public enum CardType{
        PATHWAY,CATLOCATION,FIRECARD,FIRETILE,RAFT,NONE;

        /**
         * overWrite toString method to output cardType string.
         *
         * @return string of the cardType
         */
        @Override
        public String toString() {
            String string;
            switch (this){
                case PATHWAY        -> string = "PATHWAY";
                case CATLOCATION    -> string = "CATLOC ";
                case FIRECARD       -> string = " FCARD ";
                case FIRETILE       -> string = " FTILE ";
                case RAFT           -> string = " RAFT  ";
                default             -> string = " NONE  ";
            }
            return string;
        }
    }

    /**
     * Constructs a new Card object with the specified type and ID.
     *
     * @param type The type of the card
     * @param cardID The unique ID of the card
     */
    public Card(CardType type,int cardID){
        this.cardType = type;
        this.cardID = cardID;
        this.orientation =Orientation.NORTH;
        this.placedLocation = new Location();
        this.isFlipped = false;
        //only fireTile card can exceed 3 rows/cols. other cards are 3x3
        if(this.cardType != CardType.FIRETILE){
            this.sizeRow = 3;
            this.sizeCol = 3;
        }
    }

    // Getter methods
    /**
     * Gets the sizeRow of the card.
     *
     * @return The sizeRow of the card.
     */
    public int getSizeRow() { return sizeRow; }

    /**
     * Gets the sizeCol of the card.
     *
     * @return The sizeCol of the card.
     */
    public int getSizeCol() { return sizeCol; }

    /**
     * Gets the cardType of the card.
     *
     * @return The cardType of the card.
     */
    public CardType getCardType() { return cardType; }

    /**
     * Gets the cardID of the card.
     *
     * @return The cardID of the card.
     */
    public int getCardID() { return cardID; }

    /**
     * Gets the orientation of the card.
     *
     * @return The orientation of the card.
     */
    public Orientation getOrientation() { return orientation; }

    /**
     * Gets the whether the card is flipped.
     *
     * @return true if the card is flipped, false otherwise.
     */
    public boolean isFlipped() { return isFlipped; }

    /**
     * Gets the placedLocation of the card.
     *
     * @return the placedLocation of the card.
     */
    public Location getPlacedLocation() { return placedLocation; }

    /**
     * Gets the original 2D array of card squares without rotation and flip
     *
     * @return original 2D array of card squares without rotation and flip
     */
    public Square[][] getCardSquare() { return cardSquare; }

    /**
     * give the determined orientation, and get the specific the 2D array of square
     * from each square of the original card squares[][], put it into a new array according
     * to the rule of rotation. Describe the rotation.
     *
     * @param orientation the orientation of the card
     * @return 2D array of square after the rotation.
     */
    public Square[][] getCardSquare(Orientation orientation){
        Square[][] rotateSquare;
        switch (orientation){
            case EAST,WEST -> rotateSquare = new Square[sizeCol][sizeRow];
            default -> rotateSquare = new Square[sizeRow][sizeCol];
        }

        //this is loop in original
        for(int row = 0; row < sizeRow; row++){
            for (int col=0;col < sizeCol;col++){
                int rotateRow;
                int rotateCol;
                switch (orientation){
                    case EAST -> {
                        rotateRow = col;
                        rotateCol = sizeRow-1-row;
                    }
                    case SOUTH -> {
                        rotateRow = sizeRow-1-row;
                        rotateCol = sizeCol-1-col;
                    }
                    case WEST -> {
                        rotateRow = sizeCol-1-col;
                        rotateCol = row;
                    }
                    default -> {
                        rotateRow = row;
                        rotateCol = col;
                    }
                }
                rotateSquare[rotateRow][rotateCol] = this.cardSquare[row][col];
            }
        }
        return rotateSquare;
    }

    /**
     * give the determined orientation, and get the specific the 2D array of square
     * from the original card squares[][]. first flip it then rotate it.
     * describe the rotation.
     *
     * @param orientation the orientation of the card
     * @param isFlipped the flag whether the card is flipped
     * @return 2D array of square after the flip and rotation.
     */
    public Square[][] getCardSquare(Orientation orientation, boolean isFlipped){
        if(cardType != CardType.FIRETILE){
            isFlipped = false;
        }
        Square[][] fllipedSquare = new Square[sizeRow][sizeCol];
        for(int row = 0; row < sizeRow; row++){
            for (int col=0;col < sizeCol;col++){
                if(isFlipped){
                    fllipedSquare[row][sizeCol-1-col] = this.cardSquare[row][col];
                }
                else {
                    fllipedSquare[row][col] = this.cardSquare[row][col];
                }
            }
        }
        //Then rotate
        Square[][] rotateSquare;
        switch (orientation){
            case EAST,WEST -> rotateSquare = new Square[sizeCol][sizeRow];
            default -> rotateSquare = new Square[sizeRow][sizeCol];
        }

        //this is loop in original
        for(int row = 0; row < sizeRow; row++){
            for (int col=0;col < sizeCol;col++){
                int rotateRow;
                int rotateCol;
                switch (orientation){
                    case EAST -> {
                        rotateRow = col;
                        rotateCol = sizeRow-1-row;
                    }
                    case SOUTH -> {
                        rotateRow = sizeRow-1-row;
                        rotateCol = sizeCol-1-col;
                    }
                    case WEST -> {
                        rotateRow = sizeCol-1-col;
                        rotateCol = row;
                    }
                    default -> {
                        rotateRow = row;
                        rotateCol = col;
                    }
                }
                rotateSquare[rotateRow][rotateCol] = fllipedSquare[row][col];
            }
        }
        return rotateSquare;
    }

    // Setter methods
    /**
     * Sets the orientation of the card.
     *
     * @param orientation The new location of the cat.
     */
    public void setOrientation(Orientation orientation) { this.orientation = orientation; }

    /**
     * Sets the flag of flip of the card.
     *
     * @param flipped The flag of flip of the card.
     */
    public void setFlipped(boolean flipped) { isFlipped = flipped; }

    /**
     * Set the placed location of the card
     *
     * @param row input the placed row of the card
     * @param col input the placed col of the card
     */
    public void setPlacedLocation(int row, int col) {
        this.placedLocation.setRow(row);
        this.placedLocation.setColumn(col);
    }

    /**
     * print each square of the card with matrix
     * visually see the card
     */
    public void printWithMatrix(){
        for(int row = 0; row < sizeRow; row++) {
            for (int col = 0; col < sizeCol; col++) {
                if(this.cardSquare[row][col] != null) {
                    System.out.print(this.cardSquare[row][col].getStateChar() + " ");
                }
                else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    /**
     * overwrite the toString method
     *
     * @return string about the card ID
     */
    @Override
    public String toString() {
        return "ID:" + cardID;
    }
}
