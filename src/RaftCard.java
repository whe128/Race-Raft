

/**
 * Represents a Raft card used in the game. Each Raft card has a specific configuration
 * of squares and may or may not have color information associated with it.

 */
public class RaftCard extends Card {
    // Maximum number of Raft cards available
    private static final int MAX_RAFT_CARD_NUM = Utility.RAFT_CARDS.length;
    // Flag to indicate if the Raft card has color information
    private final boolean isRaftCardHasColor;

    /**
     * Constructs a RaftCard object with a specified ID.
     *
     * @param ID The ID of the Raft card (0 to 3)
     */
    public RaftCard(int ID) {
        super(CardType.RAFT, ID);
        String string = Utility.RAFT_CARDS[ID];
        this.cardSquare = getRaftCardSquareFromString(string);
        this.isRaftCardHasColor = getIsHasColorFromString(string);
    }

    /**
     * Constructs a RaftCard object from a string representation and ID.
     *
     * @param raftString The string representation of the Raft card
     * @param ID The ID of the Raft card (0 to 3)
     */
    public RaftCard(String raftString, int ID) {
        super(CardType.RAFT, ID);
        this.cardSquare = getRaftCardSquareFromString(raftString);
        this.isRaftCardHasColor = getIsHasColorFromString(raftString);
    }

    /**
     * Retrieves the ID of a Raft card from its string representation.
     *
     * @param string The string representation of the Raft card
     * @return The ID of the Raft card (0 to 3) or -1 if not found
     */
    public static int getIDFromString(String string) {
        for (int i = 0; i < Utility.RAFT_CARDS.length; i++) {
            if (Utility.RAFT_CARDS[i].substring(1).equals(string)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Constructs the square configuration of a Raft card from its string representation.
     *
     * @param string The string representation of the Raft card
     * @return The 2D array of Square objects representing the card's squares
     */
    private Square[][] getRaftCardSquareFromString(String string) {
        char[] charArray = string.substring(1).toCharArray();
        Square[][] squares = new Square[3][3];

        // Parse the string to create squares with appropriate properties
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                char ch = charArray[3 * row + col];
                Color color = Color.charToColor(ch);
                boolean isObjective = ch == 'o';
                boolean isWildForRaft = ch == 'w';
                boolean isHasFire = false;

                squares[row][col] = new Square(new Location(row, col), this, color);
                squares[row][col].setIsWildForRaft(isWildForRaft);
                squares[row][col].setIsObjectiveRaft(isObjective);
                squares[row][col].setHasFire(isHasFire);
            }
        }
        return squares;
    }

    /**
     * Determines whether a Raft card has color information.
     *
     * @param string The string representation of the Raft card
     * @return true if the card has color information, false otherwise
     */
    private boolean getIsHasColorFromString(String string) {
        char[] charArray = string.substring(1).toCharArray();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                char ch = charArray[3 * row + col];
                if (Color.charToColor(ch) != Color.NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the Raft card has color information.
     *
     * @return true if the Raft card has color information, false otherwise
     */
    public boolean doesRaftHaveColour() {
        return isRaftCardHasColor;
    }

    /**
     * output the string, each character is the square state
     *
     * @return the square state String
     */
    public String getString(){
        Square square;
        StringBuffer stringBuffer = new StringBuffer();
        // Construct the string representation of the Raft card
        for (int i = 0; i < 9; i++) {
            square = this.cardSquare[i / 3][i % 3];
            if (square.getColor() != Color.NONE) {
                stringBuffer.append(square.getColor().toChar(false));
            } else {
                stringBuffer.append(square.isObjectiveRaft() ? 'o' : 'w');
            }
        }
        return stringBuffer.toString();
    }
    /**
     * Provides a string representation of the RaftCard object.
     *
     * @return A string representation of the RaftCard object
     */
    @Override
    public String toString() {
        return "RaftCard: place" + this.placedLocation + " " + super.toString() + " " + getString() + (isRaftCardHasColor ? "   HasColor" : "   NoColor");
    }

    /**
     * The main method is to test and run the single class Challenge, see and look whether the design is well done
     *
     * @param args
     */
    public static void main(String[] args) {
        RaftCard raftCard0 = new RaftCard(0);
        RaftCard raftCard1 = new RaftCard(1);
        RaftCard raftCard2 = new RaftCard(2);
        RaftCard raftCard3 = new RaftCard(3);
        System.out.println(raftCard0);
        System.out.println(raftCard1);
        System.out.println(raftCard2);
        System.out.println(raftCard3);
    }
}
