

/**
 * location class is the basic class for the game, it is useful to store the pair of position
 * like row and col
 */
public class Location {
    private int row;
    private int column;

    /**
     * constructor without parameter, just set row and column be the negative numbers
     */
    public Location(){
        this.row = -1;
        this.column = -1;
    }

    /**
     * constructor with parameter, set the row and col to the instance variables
     * @param row the row position
     * @param column the column of the position
     */
    public Location(int row,int column){
        this.row = row;
        this.column = column;
    }

    /**
     * four location of a specific location, include NORTH,SOUTH,EAST,WEST four orientation
     *
     * @return an array of the adjacent location
     */
    public Location[] getAdjacentLocation(){
        Location[] locations = new Location[4];
        //East
        locations[0]=new Location(this.row,this.column+1);
        //South
        locations[1]=new Location(this.row+1,this.column);
        //North
        locations[2]=new Location(this.row-1,this.column);
        //West
        locations[3]=new Location(this.row,this.column-1);
        return locations;
    }

    /**
     * get the row of this location
     *
     * @return location's row
     */
    public int getRow() {
        return row;
    }
    /**
     * get the column of this location
     *
     * @return location's column
     */
    public int getColumn(){
        return column;
    }

    /**
     * set the row of this location
     *
     * @param row the row should be set
     */
    public void setRow(int row) {
        this.row = row;
    }
    /**
     * set the column of this location
     *
     * @param column the column should be set
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * judge whether this location in the specific size board
     * @param sizeRow size row of the board
     * @param sizeCol size col of the board
     *
     * @return true if the location is in board, false is out of the board
     */
    public boolean isOnBoard(int sizeRow, int sizeCol){
        //location use <0.
        return row >= 0 && row < sizeRow && column >= 0 && column < sizeCol;
    }

    /**
     * judge whether the other location is orthogonal with this location
     * means they are on the same row or same col
     *
     * @param other other location
     * @return true if the these two location is orthogonal, false otherwise
     */
    boolean isOrthogonal(Location other){
        if((other.getRow()-this.row)*(other.getColumn()-this.column)!=0){
            return false;
        }
        return true;
    }
    /**
     * overwrite equals method, should judge the two locations' row and column
     *
     * @return true if these two location has the same row and same column
     */
    @Override
    public boolean equals(Object other){
        if(((Location)other).getRow() == this.row && ((Location)other).getColumn() == this.column){
            return true;
        }
        return false;
    }

    /**
     * overwrite the toString method to output the row and col of the location
     *
     * @return location String about the row and col
     */
    @Override
    public String toString() {
        return "["+this.row+"," + this.column+ "]";
    }

    /**
     * inner main method to test and check whether the class is well-designed
     *
     * @param args
     */
    public static void main(String[] args) {
        Location l1=new Location(1,2);
        Location l2=new Location(5,1);
        System.out.println(l1.isOrthogonal(l2));
    }
}
