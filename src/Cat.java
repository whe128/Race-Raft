

/**
 * Represents a cat character in the game, possessing a specific color and location on the game board.
 * Cats can move to different locations on the board and may become exhausted after certain actions.
 */
public class Cat implements Comparable<Cat> {
    private boolean canGetToRaft;
    private final Color color;        //final
    private boolean isExhausted;
    private boolean isArrived;
    //need an independent location,because it will change when a cat move
    private Location placedLocation;        //this is the cat on which position of the whole board

    /**
     * Constructs a new Cat with the specified color and initial location on the game board.
     *
     * @param color The color of the cat.
     * @param row   The initial row position of the cat on the game board.
     * @param col   The initial column position of the cat on the game board.
     */
    public Cat(Color color,int row, int col){
        this.canGetToRaft = true;
        this.color = color;
        //why use new, because the change of cat's location can not influence others
        this.placedLocation = new Location(row,col);
        this.isExhausted = false;
    }

    /**
     * Checks if the provided Exhausted cat string is well-formed.
     *
     * @param exhaustedCatString The string representing exhausted cat
     * @param boardString The string representing board states
     * @return true if the bag string is well-formed, false otherwise
     */
    public static boolean isExhaustedStringsWellFormed(String exhaustedCatString,String boardString){
        // The string must be 1 line
        if (exhaustedCatString.indexOf('\n')>0){
            return false;
        }

        // The string length must be dividable by 5
        if(exhaustedCatString.length() % 5 != 0){
            return false;
        }

        int exhaustedCatNum = exhaustedCatString.length()/5;

        // The cat from string must appear on the board
        String[] lines = boardString.split("(?<=\n)");
        for(int i=0;i<exhaustedCatNum;i++){
            char colorChar = exhaustedCatString.charAt(5*i);
            int rowPos = Integer.parseInt(exhaustedCatString.substring(5*i+1,5*i+3));
            int colPos = Integer.parseInt(exhaustedCatString.substring(5*i+3,5*i+5));
            if(colorChar != lines[rowPos].charAt(colPos) && lines[rowPos].charAt(colPos)!='W' ){
                return false;
            }
        }
        return true;
    }

    // Action Methods

    /**
     * Moves the cat to a new location on the game board and marks it as exhausted.
     *
     * @param aimLocation The new location where the cat will be moved.
     */
    public void moveCat(Location aimLocation){
        this.placedLocation = aimLocation;
        this.isExhausted = true;
    }

    // Getter Methods
    /**
     * Gets the color of the cat.
     *
     * @return The color of the cat.
     */
    public Color getColor() { return color; }

    /**
     * Checks if the cat is exhausted.
     *
     * @return true if the cat is exhausted, false otherwise.
     */
    public boolean isExhausted() { return isExhausted; }

    /**
     * Checks if the cat has arrived.
     *
     * @return true if the cat has arrived, false otherwise.
     */
    public boolean isArrived() { return isArrived; }

    /**
     * Gets the current location of the cat on the game board.
     *
     * @return The current location of the cat.
     */
    public Location getPlacedLocation() { return placedLocation; }

    /**
     * Checks if the cat can reach the raft.
     *
     * @return true if the cat can reach the raft, false otherwise.
     */
    public boolean isCanGetToRaft() {  return canGetToRaft; }

    //Setter Method

    /**
     * Recovers the cat from exhaustion, setting isExhausted to false
     */
    public void recoverExhausted(){isExhausted = false;}

    /**
     * Sets the exhausted status of the cat.
     *
     * @param exhausted true to mark the cat as exhausted, false otherwise.
     */
    public void setExhausted(Boolean exhausted) { isExhausted = exhausted; }

    /**
     * Sets the arrived status of the cat.
     *
     * @param arrived true to mark the cat as arrived, false otherwise.
     */
    public void setArrived(boolean arrived) { isArrived = arrived;  }

    /**
     * Sets the location of the cat on the game board.
     *
     * @param location The new location of the cat.
     */
    public void setLocation(Location location) { this.placedLocation = location; }

    /**
     * Sets whether the cat can reach the raft.
     *
     * @param canGetToRaft true if the cat can reach the raft, false otherwise.
     */
    public void setCanGetToRaft(boolean canGetToRaft) {  this.canGetToRaft = canGetToRaft; }

    /**
     * Compares this cat with another cat based on exhaustion status and color.
     *
     * @param otherCat The other cat to compare with.
     * @return A negative integer, zero, or a positive integer if this cat is less than, equal to, or greater than
     *         the specified cat based on exhaustion and color.
     */
    @Override
    public int compareTo(Cat otherCat) {
        if(isExhausted && !otherCat.isExhausted()){
            return 1;
        } else if (!isExhausted && otherCat.isExhausted()) {
            return -1;
        } else {
            if (this.color.equals(otherCat.getColor())) {
                return 0;
            } else if (color.toChar(false) > otherCat.getColor().toChar(false)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Indicates whether some other object is "equal to" this cat.
     *
     * @param cat The reference object with which to compare.
     * @return true if this cat is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals (Object cat){
        if (!(cat.getClass() == Cat.class)){
            return false;
        }
        if (!(color.equals(((Cat) cat).getColor()))
                ||!(placedLocation.equals(((Cat) cat).getPlacedLocation()))
                ||!(isExhausted == (((Cat) cat).isExhausted()))){
            return false;
        }
        return true;
    }


    /**
     * Returns a string representation of the cat, including its color, location, and exhaustion status.
     * example: Cat B: place[13,4] Not-tired
     *
     * @return A string representation of the cat.
     */
    @Override
    public String toString() {
        return "Cat "+ this.color.toChar(true) +": place" + this.placedLocation + " " + ((this.isExhausted)?"tired":"Not-tired");
    }

}
