

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents a bag of fire tiles containing various FireTile objects.
 */
public class FireTileBag {
    // List to store the fire tiles
    ArrayList<FireTile> fireTilesList = new ArrayList<>();

    /**
     * Constructor for FireTileBag.
     * Initializes the fire tile bag with all possible fire tiles (IDs: 0 to 30).
     */
    public FireTileBag(){
        // Maximum number of fire tiles
        int maxTileNum = FireTile.MAX_FIRE_TILE_CARD;
        // Add fire tiles to the list (IDs: 0 to 30)
        for(int i = 0; i < maxTileNum; i++) {
            this.fireTilesList.add(new FireTile(i));
        }
    }

    /**
     * Constructor for FireTileBag.
     * Initializes the fire tile bag based on a string representation of fire tile IDs.
     *
     * @param bagString The string representing fire tile IDs (e.g., "abcdefghijklmnopqrstuvwxyzABCDE")
     */
    public FireTileBag(String bagString){
        // Remove any whitespace and newlines from the string
        bagString = bagString.replace(" ", "").replace("\n", "");
        char[] chars = bagString.toCharArray();
        int fireTileID;

        // Convert characters to corresponding fire tile IDs and add them to the list
        for(char ch : chars) {
            if(ch >= 'a' && ch <= 'z') {
                fireTileID = ch - 'a';
            } else {
                fireTileID = ch - 'A' + ('z' - 'a' + 1);
            }
            this.fireTilesList.add(new FireTile(fireTileID));
        }
    }

    /**
     * Checks if the provided bag string is well-formed.
     * A well-formed bag string contains valid characters and no character repeats listed alphabetically and by case.
     *
     * @param bagString The string representing fire tile IDs
     * @return true if the bag string is well-formed, false otherwise
     */
    public static boolean isBagStringsWellFormed(String bagString) {
        // Check for multiple lines
        if (bagString.indexOf('\n') > 0) {
            return false;
        }

        String validChars = "abcdefghijklmnopqrstuvwxyzABCDE";

        // Check for valid characters
        for(char ch : bagString.toCharArray()) {
            if(validChars.indexOf(ch) < 0) {
                return false;
            }
        }

        // Check for duplicate characters
        HashMap<Character, Integer> hashMap = new HashMap<>();
        for(char ch : bagString.toCharArray()) {
            if(hashMap.getOrDefault(ch, 0) >= 1) {
                return false;
            }
            hashMap.put(ch, hashMap.getOrDefault(ch, 0) + 1);
        }
        return true;
    }

    /**
     * Removes a random fire tile from the bag.
     *
     * @return The removed FireTile object, or null if the bag is empty.
     */
    public FireTile removeFireTile() {
        int num = this.fireTilesList.size();
        if(num > 0) {
            Random random = new Random();
            int index = random.nextInt(num);
            FireTile fireTile = this.fireTilesList.get(index);
            this.fireTilesList.remove(index);
            return fireTile;
        } else {
            return null;
        }
    }

    /**
     * Checks if the fire tile bag is empty.
     *
     * @return true if the bag is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.fireTilesList.size() == 0;
    }

    /**
     * Gets the number of fire tiles in the bag.
     *
     * @return The number of fire tiles.
     */
    public int getTileNum() {
        return this.fireTilesList.size();
    }

    /**
     * Gets the list of fire tiles in the bag.
     *
     * @return An ArrayList of FireTile objects.
     */
    public ArrayList<FireTile> getFireTiles() {
        return fireTilesList;
    }

    /**
     * Gets the string representation of the fire tile bag.
     *
     * @return A string representing the fire tile IDs in the bag.
     */
    public String getFireTileBagString() {
        char[] chars = new char[this.fireTilesList.size()];
        int index = 0;
        for(FireTile p : this.fireTilesList) {
            chars[index] = p.getTypeChar();
            index++;
        }
        return String.valueOf(chars);
    }

    /**
     * Override the toString() method to provide a string representation of the FireTileBag object.
     * e.g. abcdefghijklmnopqrstuvwxyzABCDE
     *
     * @return A string representation of the FireTileBag object.
     */
    @Override
    public String toString() {
        if(fireTilesList.size() == 0) {
            return "FireTileBag: there is no FireTile card in the Fire Bag";
        }
        return "FireTileBag: " + getFireTileBagString();
    }

    /**
     * The main method is to test and run the single class Challenge, see and look whether the design is well done
     * @param args
     */
    public static void main(String[] args) {
        FireTileBag bag = new FireTileBag("abcdfghjklmnopqrstuvwxyzBCE");
        System.out.println(bag);
        bag.removeFireTile();
        System.out.println(bag);
        System.out.println(isBagStringsWellFormed("abcdefghijklmnopqrstuvwxyzAdBCDE"));
    }
}
