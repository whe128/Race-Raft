
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is for the challenge, this class use challenge number or challenge to initialize the game
 * the challenge create the instance of islandBoards, fireCards, catLocationCards, raftCard, and store them
 */
public class Challenge {
    // Start with 1; range 1~39 (included)
    private final int challengeNumber;
    private final String challengeString;

    // Different challenge has 2 or 4 islandBoards
    private final IslandBoard[] islandBoards;
    // The fire Card is the same type of 3x3, filled with fire, the cardType is FIRECARD
    private final FireCard[] fireCards;

    // Store the Cat location card
    private final CatLocationCard[] catLocationCards;

    // Store the Raft card
    private final RaftCard raftCard;

    /**
     * Difficulty levels:
     * difficulty 0 - challenge number 1~4
     * difficulty 1 - challenge number 5~8
     * difficulty 2 - challenge number 9~16
     * difficulty 3 - challenge number 17~24
     * difficulty 4 - challenge number 25~32
     * difficulty 5 - challenge number 33~39
     *
     * Initialization of challenge: give a challengeString and challengeNumber
     * Sample of challenge string: "LNSNLASA F000300060012001506030903 C000093030341203 R11215"
     *
     * @param challenge an Object representing either a challenge num (Integer 1~39) or a challenge string (String)
     */
    public Challenge(Object challenge) {
        int challengeNumTemp = 0;
        String challengeStrTemp;

        // Determine challenge number and string based on input type
        if (challenge instanceof Integer) {
            challengeNumTemp = (Integer) challenge;
            challengeStrTemp = Utility.CHALLENGES[challengeNumTemp - 1];
        } else if (challenge instanceof String) {
            challengeStrTemp = (String) challenge;
            for (int i = 0; i < Utility.CHALLENGES.length; i++) {
                if (challengeStrTemp.equals(Utility.CHALLENGES[i])) {
                    challengeNumTemp = i + 1;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }

        this.challengeNumber = challengeNumTemp;
        this.challengeString = challengeStrTemp;

        // Parse challenge string to initialize islandBoards, fireCards, catLocationCards, and raftCard
        String[] splitString = this.challengeString.split("F");
        this.islandBoards = getIslandBoardFromString(splitString[0]);

        splitString = splitString[1].split("C");
        this.fireCards = getFireCardFromString(splitString[0]);

        splitString = splitString[1].split("R");
        this.catLocationCards = getCatLocationCardFromString(splitString[0]);

        this.raftCard = getRaftCardFromString(splitString[1]);
    }

    /**
     * Parse the challenge string to create an array of IslandBoard objects
     * string (e.g. LNSNLASA)
     *
     * @param string the part of the challenge string corresponding to island boards
     * @return an array of IslandBoard objects
     */
    private IslandBoard[] getIslandBoardFromString(String string) {
        Random random = new Random();
        ArrayList<Integer> randomNumberLarge = new ArrayList<>();
        ArrayList<Integer> randomNumberSmall = new ArrayList<>();

        // Populate random number lists
        for (int i = 1; i <= 4; i++) {
            randomNumberLarge.add(i);
            randomNumberSmall.add(i);
        }

        int islandNumber = string.length() / 2;
        IslandBoard[] islandBoards = new IslandBoard[islandNumber];

        for (int i = 0; i < islandNumber; i++) {
            boolean isUseFireSide = string.charAt(2 * i + 1) != 'A';
            char islandTypeChar = string.charAt(2 * i);
            char islandOrientationChar = string.charAt(2 * i + 1);

            //get the random card id from the rest id
            if (islandTypeChar == 'L') {
                int largeRandomID = randomNumberLarge.remove(random.nextInt(randomNumberLarge.size()));
                islandBoards[i] = new IslandBoard(IslandBoard.IslandBoardType.LARGE, largeRandomID, isUseFireSide);
            } else {
                int smallRandomID = randomNumberSmall.remove(random.nextInt(randomNumberSmall.size()));
                islandBoards[i] = new IslandBoard(IslandBoard.IslandBoardType.SMALL, smallRandomID, isUseFireSide);
            }

            Orientation orientation = Orientation.NORTH;
            if (islandTypeChar == 'L') {
                //large board can rotate 4 direction
                switch (islandOrientationChar) {
                    case 'N' -> orientation = Orientation.NORTH;
                    case 'E' -> orientation = Orientation.EAST;
                    case 'S' -> orientation = Orientation.SOUTH;
                    case 'W' -> orientation = Orientation.WEST;
                    case 'A' -> orientation = Orientation.random();
                    default -> {}
                }
            } else {
                //small island can only rotate N and S
                switch (islandOrientationChar) {
                    case 'N' -> orientation = Orientation.NORTH;
                    case 'S' -> orientation = Orientation.SOUTH;
                    case 'A' -> orientation = Orientation.randomNS();
                    default -> {}
                }
            }
            islandBoards[i].setOrientation(orientation);
        }
        return islandBoards;
    }

    /**
     * Parse the challenge string to create an array of FireCard objects
     * string e.g. F000300060012001506030903
     *
     * @param string the part of the challenge string corresponding to fire cards
     * @return an array of FireCard objects
     */
    private FireCard[] getFireCardFromString(String string) {
        int fireCardNum = string.length() / 4;
        FireCard[] fireCards = new FireCard[fireCardNum];
        int row, col;
        for (int i = 0; i < fireCardNum; i++) {
            row = Integer.parseInt(string.substring(4 * i, 4 * i + 2));
            col = Integer.parseInt(string.substring(4 * i + 2, 4 * i + 4));
            fireCards[i] = new FireCard();
            fireCards[i].setPlacedLocation(row, col);
        }
        return fireCards;
    }

    /**
     * Parse the challenge string to create an array of CatLocationCard objects
     * string e.g. C000093030341203
     *
     * @param string the part of the challenge string corresponding to cat location cards
     * @return an array of CatLocationCard objects
     */
    private CatLocationCard[] getCatLocationCardFromString(String string) {
        int catCardNumber = string.length() / 5;
        CatLocationCard[] catCards = new CatLocationCard[catCardNumber];
        int row, col, cardID;
        for (int i = 0; i < catCardNumber; i++) {
            cardID = Integer.parseInt(string.substring(5 * i, 5 * i + 1));
            row = Integer.parseInt(string.substring(5 * i + 1, 5 * i + 3));
            col = Integer.parseInt(string.substring(5 * i + 3, 5 * i + 5));
            catCards[i] = new CatLocationCard(cardID);
            catCards[i].setPlacedLocation(row, col);
        }
        return catCards;
    }

    /**
     * Parse the challenge string to create a RaftCard object
     * string e.g. R11215
     *
     * @param string the part of the challenge string corresponding to the raft card
     * @return a RaftCard object
     */
    private RaftCard getRaftCardFromString(String string) {
        int cardID = Integer.parseInt(string.substring(0, 1));
        int row = Integer.parseInt(string.substring(1, 3));
        int col = Integer.parseInt(string.substring(3, 5));
        RaftCard raftCard = new RaftCard(cardID);
        raftCard.setPlacedLocation(row, col);
        return raftCard;
    }

    /**
     * Get the challenge number
     *
     * @return the challenge number
     */
    public int getChallengeNumber() {
        return this.challengeNumber;
    }

    /**
     * Get the challenge string
     *
     * @return the challenge string
     */
    public String getChallengeString() {
        return this.challengeString;
    }

    /**
     * Get the array of IslandBoard objects
     *
     * @return the array of IslandBoard objects
     */
    public IslandBoard[] getIslandBoards() {
        return this.islandBoards;
    }

    /**
     * Get the array of FireCard objects
     *
     * @return the array of FireCard objects
     */
    public FireCard[] getFireCards() {
        return this.fireCards;
    }

    /**
     * Get the array of CatLocationCard objects
     *
     * @return the array of CatLocationCard objects
     */
    public CatLocationCard[] getCatLocationCards() {
        return this.catLocationCards;
    }

    /**
     * Get the RaftCard object
     *
     * @return the RaftCard object
     */
    public RaftCard getRaftCard() {
        return this.raftCard;
    }

    /**
     * Choose a random challenge number based on the difficulty level
     *
     * @param difficulty the difficulty level (0 to 5)
     * @return a random challenge number within the range for the given difficulty level
     */
    public static int chooseRandomChallengeNum(int difficulty) {
        Random r = new Random();
        int randomChallengeNumber = 0;

        switch (difficulty) {
            case 0 -> randomChallengeNumber = r.nextInt(4 - 1 + 1) + 1;
            case 1 -> randomChallengeNumber = r.nextInt(8 - 5 + 1) + 5;
            case 2 -> randomChallengeNumber = r.nextInt(16 - 9 + 1) + 9;
            case 3 -> randomChallengeNumber = r.nextInt(24 - 17 + 1) + 17;
            case 4 -> randomChallengeNumber = r.nextInt(32 - 25 + 1) + 25;
            case 5 -> randomChallengeNumber = r.nextInt(39 - 33 + 1) + 33;
            default -> randomChallengeNumber = 1;
        }
        return randomChallengeNumber;
    }

    /**
     * Override the toString() method to provide a string representation of the Challenge object
     * example: ChallengeNum: 1 LNSNLASAF000300060012001503030903C112033060340009R01215
     *
     * @return a string representation of the Challenge object
     */
    @Override
    public String toString() {
        return "ChallengeNum: " + this.challengeNumber + " " + challengeString;
    }

    /**
     * The main method is to test and run the single class Challenge, see and look whether the design is well done
     */
    public static void main(String[] args) {
        int ChallengeNumber = 2;
        Challenge challenge = new Challenge("LNSNLASAF000300060012001506030903C000093030341203R11215");
        FireCard[] card = challenge.getFireCards();
        System.out.println(challenge.getChallengeString());
        System.out.println(challenge.getIslandBoards().length);
        for (IslandBoard a : challenge.getIslandBoards()) {
            System.out.println(a);
        }
    }
}




