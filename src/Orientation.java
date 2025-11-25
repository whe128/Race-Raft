

import java.util.Random;

/**
 * location class is the basic enum for the game, it is useful to store the pair of position
 * like row and col
 */
public enum Orientation {
    NORTH, EAST, SOUTH, WEST, NONE;

    /**
     * can make the char to Orientation enum
     *
     * @param ch the char of orientation nsew or NSEW
     * @return enum of orientation
     */
    public static Orientation charToOrientation(char ch){
        Orientation o;
        switch (Character.toUpperCase(ch)){
            case 'N' -> o = Orientation.NORTH;
            case 'E' -> o = Orientation.EAST;
            case 'S' -> o = Orientation.SOUTH;
            case 'W' -> o = Orientation.WEST;
            default  -> o = Orientation.NONE;
        }
        return o;
    }

    /**
     * create a random orientation in any directions
     *
     * @return : random orientation in 4 directions
     */
    public static Orientation random(){
        Random random = new Random();
        return Orientation.values()[random.nextInt(4)];
    }
    /**
     * create a random orientation only in E and W directions
     *
     * @return : random orientation in E and W directions
     */
    public static Orientation randomNS(){
        Random random = new Random();
        Orientation orientation;
        orientation = Orientation.NORTH;
        switch(random.nextInt(2)){
            case 0 -> orientation = Orientation.NORTH;
            case 1 -> orientation = Orientation.SOUTH;
        }
        return orientation;
    }

    /**
     * get the orientation after rotating 90 degrees through clockwise
     *
     * @return orientation that rotated after 90 degrees through clockwise
     */
    public Orientation rotateClockWise(){
        Orientation o;
        switch (values()[this.ordinal()]){
            case NORTH -> o = EAST;
            case EAST -> o = SOUTH;
            case SOUTH -> o = WEST;
            case WEST -> o = NORTH;

            default  -> o = NORTH;
        }
        return o;
    }
}
