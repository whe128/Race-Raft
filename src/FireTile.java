

import java.util.ArrayList;

/**
 * Represents a specific type of game card known as a FireTile.
 * Inherits from the base class Card.
 */
public class FireTile extends Card {
    public static final int MAX_FIRE_TILE_CARD = Utility.FIRE_TILES.length;

    //store the fire location in a fireTile (inner position)
    private ArrayList<Location> fireLocation;
    private final char typeChar;

    /**
     * Constructor for FireTile.
     * Initializes a fire tile based on the provided ID.
     *
     * @param ID The ID of the fire tile (range: 0 to 30)
     */
    public FireTile(int ID) {
        super(CardType.FIRETILE, ID);
        String string = Utility.FIRE_TILES[ID];
        this.typeChar = string.charAt(0);
        this.cardSquare = getFireTileFromString(string);
        this.fireLocation = getFireTileLocationFromString(string);
        this.sizeRow = getSizeFromString(string)[0];        //0 stores row
        this.sizeCol = getSizeFromString(string)[1];    //1 stores col
    }

    /**
     * Generates the fire tile layout from a string representation.
     *
     * @param string The string representation of the fire tile
     * @return A 2D array of Square objects representing the fire tile layout
     */
    private Square[][] getFireTileFromString(String string) {
        ArrayList<Location> fireLocationList = getFireTileLocationFromString(string);
        int maxRow = 0;
        int maxCol = 0;
        for (Location l : fireLocationList) {
            maxRow = Math.max(maxRow, l.getRow());
            maxCol = Math.max(maxCol, l.getColumn());
        }
        Square[][] squares = new Square[maxRow + 1][maxCol + 1];

        for (Location l : fireLocationList) {
            squares[l.getRow()][l.getColumn()] = new Square(new Location(l.getRow(), l.getColumn()), this, Color.NONE);
            squares[l.getRow()][l.getColumn()].setHasFire(true);
        }

        return squares;
    }

    /**
     * Extracts fire locations from the string representation of the fire tile.
     *
     * @param string The string representation of the fire tile
     * @return A list of fire locations (instances of Location)
     */
    private ArrayList<Location> getFireTileLocationFromString(String string) {
        ArrayList<Location> fireLocation = new ArrayList<>();
        int posX = 0;
        int posY = 0;
        int positionNumber = (string.length() - 1) / 2;

        for (int i = 0; i < positionNumber; i++) {
            posX = Integer.parseInt(string.substring(2 * i + 1, 2 * i + 2));
            posY = Integer.parseInt(string.substring(2 * i + 2, 2 * i + 3));
            fireLocation.add(new Location(posX, posY));
        }

        return fireLocation;
    }

    /**
     * Extracts the size (number of rows and columns) of the fire tile from the string representation.
     *
     * @param string The string representation of the fire tile
     * @return An array of integers representing the size [rows, columns]
     */
    private int[] getSizeFromString(String string) {
        ArrayList<Location> fireLocationList = getFireTileLocationFromString(string);
        int maxRow = 0;
        int maxCol = 0;
        int[] size = new int[2];
        // Get the max row and col
        for (Location l : fireLocationList) {
            maxRow = Math.max(maxRow, l.getRow());
            maxCol = Math.max(maxCol, l.getColumn());
        }
        size[0] = maxRow + 1;
        size[1] = maxCol + 1;
        return size;
    }

    // Getter Methods

    /**
     * Gets the type character of the fire tile.
     *
     * @return The type character of the fire tile.
     */
    public char getTypeChar() { return typeChar; }

    /**
     * Gets the location of fires in the fire tile
     * @return The location of fires in fire tile
     */
    public ArrayList<Location> getFireLocation() {return fireLocation;}

    /**
     * Returns a string representation of the fire tile, including its type character, placement,
     * card type, and size.
     *
     * @return A string representing the fire tile.
     */
    @Override
    public String toString() {
        return "FileTile "+this.typeChar+ ": place"+ this.placedLocation + " " + super.toString() + " " + this.sizeRow+"x"+this.sizeCol + " "+this.fireLocation;
    }

    public static void main(String[] args) {
        FireTile fireTile = new FireTile(21);
        System.out.println(fireTile);
        for(Square[] s: fireTile.cardSquare){
            for (Square ss: s){
                //System.out.println(ss);
            }
        }

        fireTile.printWithMatrix();

        System.out.println();

        Square[][] square = fireTile.getCardSquare(Orientation.NORTH,true);
        System.out.println(square.length);
        System.out.println(square[0].length);

        for(int row = 0; row < square.length; row++){
            for (int col=0;col < square[0].length;col++){
                if(square[row][col]!=null){
                    System.out.print(square[row][col].getStateChar()+" ");
                }
                else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        System.out.println();
        square = fireTile.getCardSquare(Orientation.EAST,true);
        for(int row = 0; row < square.length; row++){
            for (int col=0;col < square[0].length;col++){
                if(square[row][col]!=null){
                    System.out.print(square[row][col].getStateChar()+" ");
                }
                else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
}

