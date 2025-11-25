

/**
 * this class describe the property of the island.
 * contain the size, ID, square of every grid, orientation, hasFire flag
 */
public class IslandBoard {
    public static final int MAX_ISLAND_NUMBER_L = Utility.SQUARE_BOARDS.length;
    public static final int MAX_ISLAND_NUMBER_S = Utility.RECTANGLE_BOARDS.length;
    private final int sizeRow;                          //final
    private final int sizeCol;                       //final
    private final int boardID;
    private final IslandBoardType boardType;
    private final Square[][] boardSquares;           //final
    private Orientation orientation;
    private boolean isUseFireSide;

    /**
     * board has two size, large and small
     */
    public enum IslandBoardType {
        LARGE, SMALL;
    }

    /**
     * constructor of this class, initialization of islandBoard
     * board ID is from 1~4
     *
     * @param type the type of board large and small
     * @param ID the board ID(range from 1 to 4)
     * @param isUseFireSide set true when use the side that has fire, false otherwise
     */
    public IslandBoard(IslandBoardType type, int ID, boolean isUseFireSide) {
        String islandString;
        //find the string of the board
        if (type == IslandBoardType.LARGE) {
            if (isUseFireSide) {
                islandString = Utility.SQUARE_BOARDS[ID - 1][0];
            } else {
                islandString = Utility.SQUARE_BOARDS[ID - 1][1];
            }
        } else {
            if (isUseFireSide) {
                islandString = Utility.RECTANGLE_BOARDS[ID - 1][0];
            } else {
                islandString = Utility.RECTANGLE_BOARDS[ID - 1][1];
            }
        }

        String[] strings = islandString.split("\n");
        this.sizeRow = strings.length;
        this.sizeCol = strings[0].length();
        this.boardID = ID;
        this.boardType = type;
        this.orientation = Orientation.NONE;
        this.isUseFireSide = isUseFireSide;
        //create the boardSquares--new
        this.boardSquares = createIslandBoardSquaresFromString(strings);
    }

    /**
     * create the new square[][] for island
     *
     * @param strings the boardString that describes each square of the island
     * @return the 2D array of squares that belongs to this island
     */
    private Square[][] createIslandBoardSquaresFromString(String[] strings) {
        int rowNum = strings.length;
        int colNum = strings[0].length();
        Square[][] islandBoards = new Square[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                Color color = Color.NONE;
                Boolean isHasFire = false;
                //get the char--know the color or fire
                char chOfBoard = strings[i].charAt(j);
                switch (chOfBoard) {
                    case 'b' -> color = Color.BLUE;
                    case 'r' -> color = Color.RED;
                    case 'y' -> color = Color.YELLOW;
                    case 'p' -> color = Color.PURPLE;
                    case 'g' -> color = Color.GREEN;
                    case 'f' -> isHasFire = true;
                    default -> {}
                }

                //first new the object, then if the square has fire, set the hasFire flag
                islandBoards[i][j] = new Square(new Location(i,j),null,color);
                islandBoards[i][j].setHasFire(isHasFire);
            }
        }
        return islandBoards;
    }

    /**
     * get the orientation of the island
     *
     * @return orientation of the island
     */
    public Orientation getOrientation() {return orientation; }

    /**
     * get the size of the row of the island
     *
     * @return the size of the row of the island
     */
    public int getSizeRow() {return this.sizeRow; }

    /**
     * get the size of the col of the island
     *
     * @return the size of the col of the island
     */
    public int getSizeCol() {return this.sizeCol; }

    /**
     * get the original squares of the island of the col of the island
     *
     * @return the original squares of the island
     */
    public Square[][] getBoardSquares() { return boardSquares; }


    /**
     * get the squares of the island through the orientation
     *
     * @param orientation island orientation
     * @return the squares of the island after rotate to the orientation
     */
    public Square[][] getBoardSquares(Orientation orientation) {
        Square[][] rotateSquare;
        //set the new array size
        switch (orientation){
            case EAST,WEST -> rotateSquare = new Square[sizeCol][sizeRow];
            default -> rotateSquare = new Square[sizeRow][sizeCol];
        }

        //this is loop in original
        //map the original position to new position in new squre
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
                rotateSquare[rotateRow][rotateCol] = this.boardSquares[row][col];
            }
        }
        return rotateSquare;
    }

    /**
     * get the ID of the island board
     *
     * @return ID of the island board
     */
    public int getBoardID() { return boardID; }

    /**
     * get the boardType of the island board
     *
     * @return boardType of the island board
     */
    public IslandBoardType getBoardType() { return boardType; }

    /**
     * get the flag whether the fireside is used
     *
     * @return true if fireside is used, false otherwise
     */
    public boolean isUseFireSide() { return isUseFireSide; }

    /**
     * set the orientation of the island
     *
     * @param orientation orientation that should be set for island
     */
    public void setOrientation(Orientation orientation) { this.orientation = orientation; }


    /**
     * overwrite the toString method, output the island string which includes size, ID, orientation, island char of each squares
     *
     * @return String island string
     */
    @Override
    public String toString() {
        return "IslandBoard: Size:[" + this.sizeRow + "x" + this.sizeCol + " " + this.boardType + "] ID:" + this.boardID + "" + "--" + orientation + "\n" + squareToString();
    }


    /**
     * test method for the board test, check whether the island is well-done created
     */
    public static void islandBoardTest() {
        String islandString = "";
        IslandBoard islandBoard;
        int i;

        //board ID can only be 1,2,3,4
        for (i = 0; i < 4; i++) {

            islandBoard = (new IslandBoard(IslandBoardType.LARGE, i + 1, true));
            if (!islandBoard.squareToString().equals(Utility.SQUARE_BOARDS[i][0])) {
                System.out.println("error Large: has fire side " + i);
            }
        }
        for (i = 0; i < 4; i++) {
            islandBoard = (new IslandBoard(IslandBoardType.LARGE, i + 1, false));
            if (!islandBoard.squareToString().equals(Utility.SQUARE_BOARDS[i][1])) {
                System.out.println("error Large: no fire side " + i);
            }
        }
        for (i = 0; i < 4; i++) {
            islandBoard = (new IslandBoard(IslandBoardType.SMALL, i + 1, true));
            if (!islandBoard.squareToString().equals(Utility.RECTANGLE_BOARDS[i][0])) {
                System.out.println("error Small: has fire side " + i);
            }
        }
        for (i = 0; i < 4; i++) {
            islandBoard = (new IslandBoard(IslandBoardType.SMALL, i + 1, false));
            if (!islandBoard.squareToString().equals(Utility.RECTANGLE_BOARDS[i][1])) {
                System.out.println("error Small: no fire side " + i);
            }
        }
    }

    /**
     * this is for input the array of square, then get the string like utility, inorder to test whether the initialization is right
     *
     * @return island board string
     */
    public String squareToString() {
        char[] chars = new char[sizeCol + 1];
        String string = "";
        for (int row = 0; row < sizeRow; row++) {
            for (int col = 0; col < sizeCol; col++) {
                //if there has fire then, we do not need to consider the color
                if (this.boardSquares[row][col].isHasFire()) {
                    chars[col] = 'f';
                } else {
                    switch (this.boardSquares[row][col].getColor()) {
                        case BLUE -> { chars[col] = 'b';}
                        case RED -> { chars[col] = 'r';}
                        case YELLOW -> { chars[col] = 'y';}
                        case PURPLE -> { chars[col] = 'p';}
                        case GREEN -> {  chars[col] = 'g';}
                        default -> {chars[col] = 'n';}
                    }
                }
            }
            chars[this.boardSquares[0].length] = '\n';
            string = string + new String(chars);
        }
        return string;
    }

    /**
     * inner main method to test and check whether the class is well design without error
     */
    public static void main(String[] args) {
        IslandBoard islandBoard = new IslandBoard(IslandBoardType.LARGE,3,true);
        System.out.println(islandBoard.squareToString());
        //for test
        islandBoardTest();
    }
}
