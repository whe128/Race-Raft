

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the game board consisting of a grid of squares that can hold various game elements.
 */
public class Board {
    private int sizeRow; // Number of rows in the board
    private int sizeCol; // Number of columns in the board
    private String stateString; // String representation of the board state
    private Square[][] stateSquares; // 2D array of squares that makes up the board
    private ArrayList<ArrayList<Integer>> neighbourList = new ArrayList<>(); // List of neighbor indices for each square
    private boolean hasFire; // Flag to indicate if there is fire on the board

    /**
     * Returns whether there is fire on the board.
     *
     * @return true if there is fire, false otherwise.
     */
    public boolean isFire() {
        return hasFire;
    }

    /**
     * Constructs a new Board with specified dimensions.
     * Initializes the board with empty squares and computes neighbors for each square.
     *
     * @param sizeRow The number of rows in the board.
     * @param sizeCol The number of columns in the board.
     */
    public Board(int sizeRow, int sizeCol){
        this.sizeRow = sizeRow;
        this.sizeCol = sizeCol;
        stateSquares = new Square[sizeRow][sizeCol];
        stateString = "";
        createNeighbourNodeList();
    }

    /**
     * Constructs a new Board from a string representation of its state.
     * Parses the string and initializes the board state accordingly.
     *
     * @param stateString A string representing the state of the board.
     */
    public Board(String stateString){
        if(!isBoardStringWellFormed(stateString)){
            this.stateSquares = null;
            this.sizeRow = 0;
            this.sizeCol = 0;
            return;
        }
        String[] lines = stateString.trim().split("\\n");
        this.sizeRow = lines.length;
        this.sizeCol = lines[0].length();
        this.stateSquares = new Square[sizeRow][sizeCol];
        this.stateString = stateString;

        for(int row = 0; row < sizeRow; row++){
            for(int col = 0; col < sizeCol; col++){
                char state = lines[row].charAt(col);
                stateSquares[row][col] = new Square(new Location(row, col), state);
            }
        }
        createNeighbourNodeList();
    }

    /**
     * Checks if a given board string is well-formed based on specific criteria.
     * Board string must be line of 12,15 and 18.
     * each line must have the same number of character
     * the valid char must be (bBfgGnopPrRwWyY)
     *
     * @param boardString The string representation of the board to check.
     * @return true if the board string is well-formed, false otherwise.
     */
    public static boolean isBoardStringWellFormed(String boardString) {

        String[] lines = boardString.split("\n");
        int numOfLines = lines.length;

        // Check condition 4: number of lines is either 12, 15, or 18
        if (numOfLines != 12 && numOfLines != 15 && numOfLines != 18){
            return false;
        }

        String validChars = "bBfgGnopPrRwWyY";
        int lineLength = lines[0].length();
        //Iterate through each line
        for (String line : lines) {
            // Check condition 1: Each line is terminated by a newline character `\n`;
            if (lineLength != line.length()) {
                return false;
            }

            // Check condition 2: The number of printable (non-newline) characters in each line is equal AND is either 9 or 18.
            String printableCharacters = line.replace("\n", "");
            if (printableCharacters.length() != 9 && printableCharacters.length() != 18) {
                return false;
            }

            //Check condition 3: Each character (except newline character) is one of the char in list above
            for (char c : printableCharacters.toCharArray()) {
                if (validChars.indexOf(c)<0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Creates a list of neighboring nodes for each square on the board.
     * Neighbors are calculated only in four cardinal directions.
     * THe neighbors must also in the range of board
     */
    private void createNeighbourNodeList(){
        for(int row = 0; row < sizeRow; row++){
            for(int col = 0; col < sizeCol; col++){
                ArrayList<Integer> subList = new ArrayList<>();
                for(int i = -1; i <= 1; i++){
                    for(int j = -1; j <= 1; j++){
                        if (i == 0 || j == 0) { // Check orthogonal directions
                            int neighPosRow = row + i;
                            int neighPosCol = col + j;
                            if (neighPosRow >= 0 && neighPosRow < sizeRow && neighPosCol >= 0 && neighPosCol < sizeCol) {
                                subList.add(neighPosRow * sizeCol + neighPosCol);
                            }
                        }
                    }
                }
                neighbourList.add(subList);
            }
        }
    }

    /**
     * Checks if any part of a given card, when placed on the board, is adjacent to fire.
     * It checks the surrounding squares of each card square on the board.
     *
     * @param location The top-left location where the card is to be placed.
     * @param cardSquares A 2D array representing the squares of the card.
     * @return true if any part of the card is adjacent to fire, false otherwise.
     */
    public boolean isAdjacentToFire(Location location, Square[][] cardSquares) {
        for (int row = 0; row < cardSquares.length; row++) {
            for (int col = 0; col < cardSquares[row].length; col++) {
                Square cardSquare = cardSquares[row][col];
                if (cardSquare != null) { // Ensure the square is not empty
                    int boardRow = location.getRow() + row;
                    int boardCol = location.getColumn() + col;
                    if (isFireAdjacent(boardRow, boardCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper method to determine if fire is present on any of the four orthogonal adjacent squares.
     *
     * @param row Row index of the square.
     * @param col Column index of the square.
     * @return true if an adjacent square has fire, false otherwise.
     */
    private boolean isFireAdjacent(int row, int col) {
        if (row > 0 && stateSquares[row - 1][col].isHasFire()) return true;
        if (row < sizeRow - 1 && stateSquares[row + 1][col].isHasFire()) return true;
        if (col > 0 && stateSquares[col - 1][row].isHasFire()) return true;
        if (col < sizeCol - 1 && stateSquares[col + 1][row].isHasFire()) return true;
        return false;
    }

    /**
     * Determines if placing a given card or tile overlaps with forbidden elements such as fire, cats,
     * or designated non-overlappable areas defined by specific game rules.
     *
     * @param cardSquares The squares of the card or tile being placed.
     * @param location The top-left location where the card or tile is to be placed.
     * @return true if the placement overlaps with forbidden elements, false otherwise.
     */
    public boolean overlapsWithForbiddenElements(Square[][] cardSquares, Location location) {
        for (int row = 0; row < cardSquares.length; row++) {
            for (int col = 0; col < cardSquares[row].length; col++) {
                Square cardSquare = cardSquares[row][col];
                if (cardSquare != null) { // Check if the square is part of the tile
                    int boardRow = location.getRow() + row;
                    int boardCol = location.getColumn() + col;
                    if (!isInBounds(boardRow, boardCol)) continue; // Skip out-of-bounds checks

                    Square boardSquare = stateSquares[boardRow][boardCol];
                    if (overlapsForbidden(boardSquare)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper method to check if the given square contains forbidden elements for overlap.
     *
     * @param square The square to check against for forbidden elements.
     * @return true if overlapping with forbidden elements, false otherwise.
     */
    private boolean overlapsForbidden(Square square) {
        return square.isHasFire() || square.isHasCat() || (square.isObjectiveRaft() && !square.isWildForRaft());
    }

    /**
     * Helper method to check if the specified row and column index is within the bounds of the board.
     *
     * @param row The row index.
     * @param col The column index.
     * @return true if the indices are within the board's boundaries, false otherwise.
     */
    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < sizeRow && col >= 0 && col < sizeCol;
    }

    /**
     * Helper method to check if the specified row and column index is within the bounds of the board.
     * this is use the location as the argument
     *
     * @param location The placed location on board.
     * @return true if the indices are within the board's boundaries, false otherwise.
     */
    public boolean isInBounds(Location location) {
        int row = location.getRow();
        int col = location.getColumn();
        return row >= 0 && row < sizeRow && col >= 0 && col < sizeCol;
    }
    /**
     * Determines if the placement of a card or tile is completely within the boundaries of the board.
     *
     * @param cardSquares The 2D array of squares for the card or tile.
     * @param location The top-left location for the placement.
     * @return true if the entire placement is within board boundaries, false otherwise.
     */
    public boolean isPlaceSquaresInBounds(Square[][] cardSquares, Location location) {
        int startRow = location.getRow();
        int startCol = location.getColumn();
        int endRow = startRow + cardSquares.length;
        int endCol = startCol + cardSquares[0].length;
        return startRow >= 0 && startCol >= 0 && endRow <= sizeRow && endCol <= sizeCol;
    }

    //  Setter Methods
    /**
     * Sets a square at a specific row and column on the board.
     *
     * @param row The row index where the square is to be placed.
     * @param col The column index where the square is to be placed.
     * @param square The square object to place on the board.
     */
    public void setSquare(int row, int col, Square square) {
        if (square != null) {
            this.stateSquares[row][col] = square;
        }
    }

    /**
     * Sets a square at a specific location on the board.
     *
     * @param location The location object representing the row and column indices.
     * @param square The square object to place at the specified location.
     */
    public void setSquare(Location location, Square square) {
        if (location.isOnBoard(sizeRow, sizeCol) && square != null) {
            this.stateSquares[location.getRow()][location.getColumn()] = square;
        }
    }

    /**
     * Places an array of squares (representing a card or tile) at a specified location on the board.
     * Ensures that the entire card fits within the boundaries of the board before placement.
     *
     * @param location The top-left location where the card or tile is to be placed.
     * @param squares The 2D array of squares representing the card or tile.
     */
    public void setSquares(Location location, Square[][] squares) {
        int squaresSizeRow = squares.length;
        int squaresSizeCol = squares[0].length;
        if (location.isOnBoard(sizeRow, sizeCol) && location.getRow() + squaresSizeRow <= sizeRow && location.getColumn() + squaresSizeCol <= sizeCol) {
            for (int row = 0; row < squaresSizeRow; row++) {
                for (int col = 0; col < squaresSizeCol; col++) {
                    Square square = squares[row][col];
                    if (square != null) {
                        int rowPos = location.getRow() + row;
                        int colPos = location.getColumn() + col;
                        this.stateSquares[rowPos][colPos] = square;
                    }
                }
            }
        }
    }

    /**
     * Retrieves the number of rows in the board.
     *
     * @return The number of rows.
     */
    public int getSizeRow() {
        return sizeRow;
    }

    /**
     * Retrieves the number of columns in the board.
     *
     * @return The number of columns.
     */
    public int getSizeCol() {
        return sizeCol;
    }

    /**
     * Retrieves the list of neighboring squares for each square on the board.
     *
     * @return A list of lists, where each inner list contains the indices of neighboring squares for a square.
     */
    public ArrayList<ArrayList<Integer>> getNeighbourList() {
        return neighbourList;
    }

    /**
     * Retrieves the 2D array of squares making up the state of the board.
     *
     * @return The array of squares.
     */
    public Square[][] getStateSquares() {
        return stateSquares;
    }

    /**
     * Retrieves a single square from a specified location on the board.
     *
     * @param location The location from which to retrieve the square.
     * @return The square at the specified location.
     */
    public Square getSingleSquare(Location location) {
        return stateSquares[location.getRow()][location.getColumn()];
    }

    /**
     * Retrieves a single square based on row and column indices.
     *
     * @param rowPos The row index of the square.
     * @param colPos The column index of the square.
     * @return The square at the specified row and column.
     */
    public Square getSingleSquare(int rowPos, int colPos) {
        return stateSquares[rowPos][colPos];
    }

    /**
     * Generates a string representation of the board state.
     * This method formats the board state as a grid of characters, where each character represents the state of a square.
     *
     * @return A string representation of the board.
     */
    public String getBoardStateString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < sizeRow; row++) {
            for (int col = 0; col < sizeCol; col++) {
                builder.append(stateSquares[row][col] != null ? stateSquares[row][col].getStateChar() : 'n');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * Overridden toString method to provide a default string representation of the board.
     *
     * @return The string representation of the board.
     */
    @Override
    public String toString() {
        return getBoardStateString();
    }
}
