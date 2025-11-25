

import java.util.*;

/**
 * this class is the integer all simple classes and make the core. It can run
 * every separated method and
 * recall the method of other simple classes. For outside, it just gives a
 * simple interface for upper developers.
 * Represents the current state and logic of a game run, where different methods
 * can change this state during gameplay.
 * The game can be initialized either by a challenge or a state string.
 * The state string array contains:
 * - Index 0: Board state
 * - Index 1: Deck
 * - Index 2: Hand
 * - Index 3: Exhausted Cats
 * - Index 4: FireTileBag
 */
public class GameRun {
    /*
     * this is initialization by challenge
     */
    private final Challenge challenge;
    private final IslandBoard[] islandBoards; // from challenge
    private final FireCard[] fireCards; // from challenge
    private final CatLocationCard[] catLocationCards; // from challenge
    private final RaftCard raftCard; // from challenge

    /*
     * other system like hand, deck, fireBag
     * pathwayCards are in deck and hand
     * fireTiles are in fireBag
     */
    private Board board;
    private Deck[] decks; // ABCD
    private Hand hand;
    private FireTileBag fireTileBag;
    private FireTile drawnFireTile;
    private int needDisCardNum;

    /*
     * small system and is very independent
     */
    private ArrayList<Cat> cats = new ArrayList<>();
    private String placePathwayCardErrorString;
    private String placeFireTireErrorString;
    private String moveCatErrorString;

    /**
     * Construct for the GameRun, use the challenge to start the game.
     * initialize the challenge(islandBoards, fireCards, catLocationCars, raftCard),
     * decks, hand, fireTileBag,
     * after creating all objects, then put island, fireCards, catCards and raftCat
     * and cats on board
     *
     * @param challenge Either a difficulty or challenge string
     */
    public GameRun(Object challenge) {
        /*
         * initialization for challenge
         */
        Object challengeTemp;
        if (challenge instanceof Integer) {
            challengeTemp = Challenge.chooseRandomChallengeNum((Integer) challenge);
        } else if (challenge instanceof String) {
            challengeTemp = challenge;
        } else {
            throw new IllegalArgumentException();
        }

        this.challenge = new Challenge(challengeTemp);
        this.islandBoards = this.challenge.getIslandBoards(); // from challenge
        this.fireCards = this.challenge.getFireCards();
        ; // from challenge
        this.catLocationCards = this.challenge.getCatLocationCards(); // from challenge
        this.raftCard = this.challenge.getRaftCard();
        ; // from challenge
        /*
         * initialization for systems
         */
        this.decks = new Deck[4];
        this.decks[0] = new Deck(Deck.DeckType.A_CROSS);
        this.decks[1] = new Deck(Deck.DeckType.B_SQUARE);
        this.decks[2] = new Deck(Deck.DeckType.C_CIRCLE);
        this.decks[3] = new Deck(Deck.DeckType.D_TRIANGLE);
        this.hand = new Hand();
        this.fireTileBag = new FireTileBag();
        this.drawnFireTile = null;
        this.needDisCardNum = 0;

        /*
         * initialization for cat
         */
        createCatFromCatLocationCard();

        /*
         * initialize the board
         */
        int islandsNum = islandBoards.length;
        int boardSizeRow = islandBoards[0].getSizeRow() + islandBoards[1].getSizeRow();
        int boardSizeCol = 0;
        if (islandsNum == 2) {
            boardSizeCol = islandBoards[0].getSizeCol();
        } else {
            boardSizeCol = islandBoards[0].getSizeCol() + islandBoards[2].getSizeCol();
        }
        this.board = new Board(boardSizeRow, boardSizeCol);
        putIslandsOnBoard();
        putFireCardOnBoard();
        putCatCardsOnBoard();
        putRaftCardOnBoard();
        putCatsOnBoard();
    }

    /**
     * Constructor to use the stateString to create the game from the middle state
     * stateString array ( 0 Board state + 1 Deck + 2 HAND + 3 exhaustedCat + 4
     * FireTileBag )
     *
     * @param gameState Either a difficulty or challenge string
     */
    public GameRun(String[] gameState) {
        this.board = new Board(gameState[0]);
        this.challenge = null;
        this.islandBoards = null;
        this.fireCards = null;
        this.catLocationCards = null;
        this.raftCard = createRaftCardFromString(gameState[0]);
        this.decks = createDeckFromString(gameState[1]);
        this.hand = new Hand(gameState[2]);
        this.fireTileBag = new FireTileBag(gameState[4]);
        this.drawnFireTile = null;
        this.needDisCardNum = 0;
        this.cats = createCatFromString(gameState[0], gameState[3]);

        putRaftCardOnBoard();
        putCatsOnBoard();
    }

    /**
     * use the boardString to find the raftCard and create the raftCard object
     * focus the objective char 'o', and combine the surrounded 8 chars to be a
     * raftCard
     *
     * @param stateString board string which includes the wild and objective of the
     *                    raftCard
     * @return the created raftCard
     */
    public RaftCard createRaftCardFromString(String stateString) {
        stateString = stateString.replace("\"", "");
        stateString = stateString.replace(" ", "");
        String[] lines = stateString.split("\n");
        int rowSize = lines.length;
        int colSize = 0;
        for (String s : lines) {
            colSize = Math.max(colSize, s.length());
        }

        int rowPos = -1;
        int colPos = -1;
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (lines[row].charAt(col) == 'o') {
                    rowPos = row;
                    colPos = col;
                }
            }
        }

        char[] chars = new char[9];
        int index = 0;
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                chars[index] = lines[rowPos + row].charAt(colPos + col);
                index++;
            }
        }
        String raftString = new String(chars);
        int cardID = RaftCard.getIDFromString(raftString);
        raftString = Integer.toString(cardID).substring(0, 1) + raftString;
        RaftCard raftCard = new RaftCard(raftString, cardID);
        raftCard.setPlacedLocation(rowPos - 1, colPos - 1);
        return raftCard;
    }

    /**
     * Create an array of deck based on given a string representation of multiple
     * decks and cards from the deck.
     *
     * @param deckString The string containing the representation of decks, with
     *                   deck types labeled as 'A', 'B', 'C', 'D'.
     *                   {deck ID}{cards left in deck}
     *                   - deck ID is a char A, B, C and D representing each deck
     *                   - cards left in deck are represented by lowercase of letter
     *                   'a' to 'y'
     * @return An array of Deck objects created from the provided deckString.
     */
    public static Deck[] createDeckFromString(String deckString) {
        Deck[] decks = new Deck[4];
        deckString = deckString.replace(" ", "");
        deckString = deckString.replace("\n", "");
        int indexA = deckString.indexOf('A');
        int indexB = deckString.indexOf('B');
        int indexC = deckString.indexOf('C');
        int indexD = deckString.indexOf('D');
        // this subString includes the type of deck (first letter)
        decks[0] = new Deck(deckString.substring(indexA, indexB));
        decks[1] = new Deck(deckString.substring(indexB, indexC));
        decks[2] = new Deck(deckString.substring(indexC, indexD));
        decks[3] = new Deck(deckString.substring(indexD));
        return decks;
    }

    /**
     * Creates a list of Cat based on the given state string and exhausted cat
     * string.
     *
     * @param state              The string representation of the game board, where
     *                           each character represents a tile. Character is
     *                           capitalised if a cat is located on a square.
     * @param exhaustedCatString A string containing information about exhausted
     *                           cats in the format:
     *                           {Color}{Row}{Column}
     *                           - colour is a char B, G, P, R or Y representing the
     *                           colour of the cat.
     *                           - row is two characters representing the row
     *                           coordinate of the cat.
     *                           - column is two characters representing the column
     *                           coordinate of the cat.
     * @return An ArrayList of Cat representing the cats on the game board, with
     *         exhausted status updated based on the exhaustedCatString.
     */
    public static ArrayList<Cat> createCatFromString(String state, String exhaustedCatString) {
        String[] lines = state.split("\n");
        ArrayList<Cat> catList = new ArrayList<>();
        int rowSize = lines.length;
        int colSize = lines[0].length();
        char stateCh;
        // Given the board string, create a cat and add it to a list
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                stateCh = lines[row].charAt(col);
                if (stateCh >= 'A' && stateCh <= 'Z') {
                    catList.add(new Cat(Color.charToColor(stateCh), row, col));
                }
            }
        }

        // If there is not exhausted cat, return a cat list
        if (exhaustedCatString == null) {
            Collections.sort(catList);
            return catList;
        }
        // If the exhaustedCatString is not well-formed, not divisible by 5, return a
        // cat list
        if (exhaustedCatString.length() % 5 != 0) {
            Collections.sort(catList);
            return catList;
        }

        int exhaustedCatNum = exhaustedCatString.length() / 5;
        for (int i = 0; i < exhaustedCatNum; i++) {
            Color color = Color.charToColor(exhaustedCatString.charAt(i * 5));

            int rowPos = Integer.parseInt(exhaustedCatString.substring(i * 5 + 1, i * 5 + 3));
            int colPos = Integer.parseInt(exhaustedCatString.substring(i * 5 + 3, i * 5 + 5));
            for (Cat cat : catList) {
                // find the exhaust cat
                if (cat.getColor().equals(color) && cat.getPlacedLocation().getRow() == rowPos
                        && cat.getPlacedLocation().getColumn() == colPos) {
                    cat.setExhausted(true);
                }
            }
        }

        Collections.sort(catList);
        return catList;
    }

    /**
     * find the cat from the catLocationCard, the position of the cat on board is
     * the sum of
     * relative position in catLocation card and placed location of catLocationCard
     */
    private void createCatFromCatLocationCard() {
        Color colorForCat;
        for (CatLocationCard card : catLocationCards) {
            Square[][] squares = card.getCardSquare();
            Square square;
            char stateCh;
            for (int row = 0; row < card.sizeRow; row++) {
                for (int col = 0; col < card.getSizeCol(); col++) {
                    square = squares[row][col];
                    stateCh = square.getStateChar();
                    if (Character.isUpperCase(stateCh)) {
                        colorForCat = square.getColor();
                        int placedRow = card.placedLocation.getRow() + row;
                        int placedCol = card.placedLocation.getColumn() + col;
                        this.cats.add(new Cat(colorForCat, placedRow, placedCol));
                    }
                }
            }
        }
    }

    /**
     * Place island boards onto the main board based on their index positions.
     * Island boards are arranged on the main board as follows:
     *
     * Index 0: Placed in the top-left corner of the main board.
     * Index 1: Placed in the bottom-left corner of the main board.
     * Index 2: Placed in the top-right corner of the main board.
     * Index 3: Placed in the bottom-right corner of the main board.
     *
     * The positioning of island boards corresponds to the layout:
     * 0 2
     * 1 3
     */
    private void putIslandsOnBoard() {
        int rowPos;
        int colPos;
        Square[][] squares;
        for (int i = 0; i < this.islandBoards.length; i++) {
            rowPos = (i % 2) * this.islandBoards[0].getSizeRow();
            colPos = (i / 2) * this.islandBoards[0].getSizeCol();
            Orientation orientation = this.islandBoards[i].getOrientation();
            squares = this.islandBoards[i].getBoardSquares(orientation);
            this.board.setSquares(new Location(rowPos, colPos), squares);
        }
    }

    /**
     * Place fire cards onto the main board at their specified locations.
     * get the squares of the fireTile and set the according squares on the board
     * through these squares.
     * the according squares can be calculated by the sum of relative location in
     * fireCard and the placedLocation
     */
    private void putFireCardOnBoard() {
        Square[][] squares;
        for (int i = 0; i < this.fireCards.length; i++) {
            squares = this.fireCards[i].getCardSquare();
            Location placeLocation = this.fireCards[i].getPlacedLocation();
            this.board.setSquares(placeLocation, squares);
        }
    }

    /**
     * Place cat location cards onto the main board at their specified locations.
     * get the squares of the cat location cards and set the according squares on
     * the board
     * through these squares
     * the according squares can be calculated by the sum of relative location in
     * cat location card and the placedLocation
     */
    private void putCatCardsOnBoard() {
        Square[][] squares;
        Location placedLocation;
        for (CatLocationCard card : catLocationCards) {
            squares = card.getCardSquare();
            placedLocation = card.getPlacedLocation();
            this.board.setSquares(placedLocation, squares);
        }
    }

    /**
     * Places the raft card onto the main board at its specified location.
     * get the squares of the raft card and set the according squares on the board
     * through these squares
     * the according squares can be calculated by the sum of relative location in
     * the
     * raft card and the placedLocation
     */
    private void putRaftCardOnBoard() {
        Square[][] squares = this.raftCard.getCardSquare();
        Location placeLocation = this.raftCard.getPlacedLocation();
        this.board.setSquares(placeLocation, squares);
    }

    /**
     * Places cats on the game board at their specified locations.
     * If the cat had arrived to Raft, set its arrival state to true
     * set the board has cat
     */
    private void putCatsOnBoard() {
        for (Cat cat : cats) {
            Location location = cat.getPlacedLocation();
            board.getSingleSquare(location).setHasCat(true);
            if (board.getSingleSquare(location).getCardType() == Card.CardType.RAFT) {
                cat.setArrived(true);
            }
        }
    }

    // Action and Judgement Methods
    /**
     * Draws a specified number of pathway cards from a deck and adds them to the
     * player's hand.
     * The draw must be valid in order to draw else it will throw Exception
     *
     * @param deck    The deck from which pathway cards will be drawn.
     * @param drawNum The number of pathway cards to draw from the deck.
     */
    public void drawPathwayCards(Deck deck, int drawNum) {
        if (deck.isRequestValid(drawNum) && (drawNum + hand.getCardNum()) <= hand.getMaxCardNum()) {
            PathwayCard pathwayCard;
            for (int i = 0; i < drawNum; i++) {
                pathwayCard = deck.removeCard();
                this.hand.addCard(pathwayCard);
            }
        }
    }

    /**
     * Draw random cards from the specified decks.
     * The decks string denotes what decks to draw from and how many cards to draw
     * from that deck.
     *
     * @param drawRequest A string representing the decks to draw from and the
     *                    number of cards to draw from each respective deck.
     */
    public void drawPathwayCardsByString(String drawRequest) {
        String[] requests = new String[drawRequest.length() / 2];

        // Break down drawRequest string into request for each deck
        for (int i = 0; i < requests.length; i++) {
            int startIndex = i * 2;
            int endIndex = i * 2 + 2;
            if (endIndex >= drawRequest.length()) {
                requests[i] = drawRequest.substring(startIndex);
            } else {
                requests[i] = drawRequest.substring(startIndex, endIndex);
            }
        }

        // Judge whether the draw is valid
        int drawSum = 0;
        boolean isDrawValid = true;
        for (String request : requests) {
            char deckName = request.charAt(0);
            drawSum += Character.getNumericValue(request.charAt(1));
            // Draw is invalid if draw some is greater than max card number of hand or when
            // deck draw is invalid
            if (drawSum > hand.getMaxCardNum()
                    || !decks[deckName - 'A'].isRequestValid(Character.getNumericValue(request.charAt(1)))) {
                isDrawValid = false;
            }
        }

        if (isDrawValid) {
            // Take the broken down requests and update gameState according to it
            for (String request : requests) {
                char deckName = request.charAt(0);
                int drawNum = Character.getNumericValue(request.charAt(1));
                drawPathwayCards(decks[deckName - 'A'], drawNum);
            }
        }
    }

    /**
     * judgement of the validity of draw card,
     *
     * @param deck       the aim deck that want to draw
     * @param requestNum the num of request of drawing
     * @return true if the draw card valid, false otherwise
     */
    public boolean isDrawPathwayCardsValid(Deck deck, int requestNum) {
        return deck.isRequestValid(requestNum);
    }

    /**
     * a main interface of place pathwayCard, first judge the validity of placement,
     * then run
     * set squares of the board by the card, finally remove it from the hand
     *
     * @param pathwayCard the object of pathwayCard, the aim card that should be
     *                    placed
     * @param location    the placedLocation on the board
     * @param orientation the Orientation of the card
     */
    public void placePathwayCard(PathwayCard pathwayCard, Location location, Orientation orientation) {
        Square[][] squares = pathwayCard.getCardSquare(orientation);

        if (isPlacePathwayCardValid(pathwayCard, location, orientation)) {
            this.board.setSquares(location, squares);

            // after playing, remove it from the hand
            int index = 0;
            for (PathwayCard card : this.hand.getCardsList()) {
                if (card.getTypeChar() == pathwayCard.getTypeChar()
                        && card.getDeckType() == pathwayCard.getDeckType()) {
                    this.hand.removeCard(index);
                    break;
                }
                index++;
            }
        }
    }

    /**
     * place pathwayCard by string, use the action string of placement, first change
     * it to every
     * useful values of deck, card, location, and recall the placement method
     * without string.
     *
     * @param placeRequest placement string
     *                     e.g.{Deck}{ID}{row}{column}{orientation}
     *                     Av0308N
     */
    public void placePathwayCardByString(String placeRequest) {
        char deckChar = placeRequest.charAt(0);
        char cardChar = placeRequest.charAt(1);
        int rowPos = Integer.parseInt(placeRequest.substring(2, 4));
        int colPos = Integer.parseInt(placeRequest.substring(4, 6));
        Orientation orientation = Orientation.charToOrientation(placeRequest.charAt(6));

        for (PathwayCard card : this.hand.getCardsList()) {
            if (card.getTypeChar() == cardChar && card.getDeckType().toChar() == deckChar) {
                placePathwayCard(card, new Location(rowPos, colPos), orientation);
                break;
            }
        }
    }

    /**
     * judgement of placement of pathwayCard. the card must be in the board
     * and cannot overlap cat, fire and raftCard
     *
     * @param card        the object of pathwayCard, the aim card that should be
     *                    placed
     * @param location    the placement of location on the board
     * @param orientation the orientation of the card
     * @return true if placement is valid, false otherwise.
     */
    public boolean isPlacePathwayCardValid(PathwayCard card, Location location, Orientation orientation) {
        int rowPos = location.getRow();
        int colPos = location.getColumn();
        Square[][] squares = card.getCardSquare(orientation);

        if (!board.isPlaceSquaresInBounds(squares, location)) {
            placePathwayCardErrorString = "Card is out of the board.";
            return false;
        }
        // can not overlap fire or raft card
        Square squareOnBoard;
        int placeRow = location.getRow();
        int placeCol = location.getColumn();
        for (int row = 0; row < card.getSizeRow(); row++) {
            for (int col = 0; col < card.getSizeCol(); col++) {
                squareOnBoard = this.board.getSingleSquare(placeRow + row, placeCol + col);
                if (squareOnBoard.isHasFire()) {
                    placePathwayCardErrorString = "Card overlaps the Fire";
                    return false;
                } else if (squareOnBoard.getCardType() == Card.CardType.RAFT) {
                    placePathwayCardErrorString = "Card overlaps the Raft Card";
                    return false;
                } else if (squareOnBoard.isHasCat()) {
                    placePathwayCardErrorString = "Card overlaps the Cat";
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * the judgement of placement of pathwayCard by string, use the action string of
     * placement,
     * first change it to every useful value of deck, card, location, and recall the
     * judgement
     * method without string.
     *
     * @param placeRequest placement string
     *                     e.g.{Deck}{ID}{row}{column}{orientation}
     *                     Av0308N
     * @return true if the placement is valid, false otherwise
     */
    public boolean isPlacePathwayCardValidByString(String placeRequest) {
        char deckChar = placeRequest.charAt(0);
        char cardChar = placeRequest.charAt(1);
        int rowPos = Integer.parseInt(placeRequest.substring(2, 4));
        int colPos = Integer.parseInt(placeRequest.substring(4, 6));
        Orientation orientation = Orientation.charToOrientation(placeRequest.charAt(6));
        PathwayCard placedCard = null;
        for (PathwayCard card : this.hand.getCardsList()) {
            if (card.getTypeChar() == cardChar && card.getDeckType().toChar() == deckChar) {
                placedCard = card;
            }
        }
        if (placedCard == null) {
            placedCard = new PathwayCard(Deck.DeckType.charToDeckType(deckChar), cardChar - 'a');
        }
        return isPlacePathwayCardValid(placedCard, new Location(rowPos, colPos), orientation);
    }

    /**
     * it is the interface that encapsulate the method of discard card. it
     * influences the hand and
     * needDiscardNum of card. remove the card from hand and let the needDiscardNum
     * - 1, for every time
     *
     * @param pathwayCard the object of pathwayCard, the aim card that should be
     *                    discarded
     */
    public void disCardPathwayCard(PathwayCard pathwayCard) {
        if (needDisCardNum <= 0) {
            return;
        }
        int index = 0;
        PathwayCard existCard = null;
        for (int i = 0; i < this.hand.getCardsList().size(); i++) {
            PathwayCard card = this.hand.getCardsList().get(i);
            if (card.getTypeChar() == pathwayCard.getTypeChar()) {
                existCard = card;
                break;
            }
        }
        if (existCard != null) {
            this.hand.getCardsList().remove(existCard);
        }
        needDisCardNum--;
    }

    /**
     * the following action is for fireTile, and store it into the
     * drawnFireTile(instance variables)
     * it is an interface that upper level can recall
     */
    public void drawFileTile() {
        if (!fireTileBag.isEmpty() && drawnFireTile == null) {
            drawnFireTile = fireTileBag.removeFireTile();
        }
    }

    /**
     * placeFireTile method lets a fireTile be placed on the board. first judge the
     * validity of judgement and
     * run the placement, finally release it from the drawnFireTile.
     * it is an interface that upper level can recall
     *
     * @param fireTile    the fireTile that needs to be placed
     * @param location    the placed location
     * @param orientation the orientation of the fireTile
     * @param isFlipped   the flag of whether the fireTile if flipped
     */
    public void placeFireTile(FireTile fireTile, Location location, Orientation orientation, boolean isFlipped) {
        Square[][] squares = fireTile.getCardSquare(orientation, isFlipped);
        if (isPlaceFireTileValid(fireTile, location, orientation, isFlipped)) {
            this.board.setSquares(location, squares);

            // release the fireTile
            if (fireTile.getTypeChar() == drawnFireTile.getTypeChar()) {
                drawnFireTile = null;
            }
        }
    }

    /**
     * for the action of placement of fire, firstly create the DrawnFireTile, then
     * we can successfully run the
     * placement method
     *
     * @param typeChar the deck type char of A,B,C,D
     */
    public void createDrawnFireTileByType(char typeChar) {
        int fireTileID;
        if (typeChar >= 'a' && typeChar <= 'z') {
            fireTileID = typeChar - 'a';
        } else {
            fireTileID = typeChar - 'A' + ('z' - 'a' + 1);
        }

        if (drawnFireTile == null) {
            drawnFireTile = new FireTile(fireTileID);
        }
    }

    /**
     * use string to place, use the action string of placement, first change it to
     * every
     * useful values of fireTile, location, and recall the placement method without
     * string.
     *
     * @param placeRequest the string action of placeFireTile
     *                     {ID}{row}{column}{flipped}{orientation}
     *                     b1010TE
     */
    public void placeFireTileByString(String placeRequest) {
        char tileChar = placeRequest.charAt(0);
        int rowPos = Integer.parseInt(placeRequest.substring(1, 3));
        int colPos = Integer.parseInt(placeRequest.substring(3, 5));
        boolean isFlipped = placeRequest.charAt(5) == 'T';
        Orientation orientation = Orientation.charToOrientation(placeRequest.charAt(6));

        if (drawnFireTile == null) {
            createDrawnFireTileByType(tileChar);
        }

        placeFireTile(drawnFireTile, new Location(rowPos, colPos), orientation, isFlipped);

    }

    /**
     * judgment of the placement of fireTile. cannot out of the board, cannot
     * overlap the fire, raftCard, cat
     * must adjoin to fire
     *
     * @param fireTile    the fireTile that needs to be placed
     * @param location    the placed location
     * @param orientation the orientation of the fireTile
     * @param isFlipped   the orientation of the fireTile
     * @return ture if placeFireTile is valid, false otherwise
     */
    public boolean isPlaceFireTileValid(FireTile fireTile, Location location, Orientation orientation,
            boolean isFlipped) {
        Square[][] squares = fireTile.getCardSquare(orientation, isFlipped);
        if (!board.isPlaceSquaresInBounds(squares, location)) {
            placeFireTireErrorString = "FireTile is out of the board.";
            return false;
        }
        // can not overlap fire or raft card
        Square squareOnBoard;
        Square adjacentSquare;
        Location locationOnBoard;
        Location[] adjacentLocationOnBoard;
        int placeRow = location.getRow();
        int placeCol = location.getColumn();
        boolean existOneAdjoinFire = false;
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[0].length; col++) {
                // why judge null, because,fire tile is not fully filled
                if (squares[row][col] != null) {
                    locationOnBoard = new Location(placeRow + row, placeCol + col);
                    adjacentLocationOnBoard = locationOnBoard.getAdjacentLocation();

                    squareOnBoard = this.board.getSingleSquare(locationOnBoard);
                    // not fire, not raft, not cat
                    if (squareOnBoard.isHasFire()) {
                        placeFireTireErrorString = "FireTile overlaps Fire.";
                        return false;
                    } else if (squareOnBoard.getCardType() == Card.CardType.RAFT) {
                        placeFireTireErrorString = "FireTile overlaps RaftCard.";
                        return false;
                    } else if (squareOnBoard.isHasCat()) {
                        placeFireTireErrorString = "FireTile overlaps Cat.";
                        return false;
                    }
                    // find if there exists a square adjoin the fire
                    for (Location l : adjacentLocationOnBoard) {
                        if (l.isOnBoard(board.getSizeRow(), board.getSizeCol())) {
                            Square square = board.getSingleSquare(l);
                            if (square != null) {
                                if (board.getSingleSquare(l).isHasFire()) {
                                    existOneAdjoinFire = true;
                                }
                            }
                        }

                    }
                }
            }
        }
        // no adjacent fireTile
        if (!existOneAdjoinFire) {
            placeFireTireErrorString = "FireTile does not adjoin the Fire.";
            return false;
        }

        return true;
    }

    /**
     * judgement the validity of placeFireTile by string, fire change the action
     * string to the useful values
     * like tile, location, isFlipped, orientation, then recall the method without
     * string.
     *
     * @param placeRequest the string action of placeFireTile
     *                     {ID}{row}{column}{flipped}{orientation}
     *                     b1010TE
     */
    public boolean isPlaceFireTileValidByString(String placeRequest) {
        char tileChar = placeRequest.charAt(0);
        int rowPos = Integer.parseInt(placeRequest.substring(1, 3));
        int colPos = Integer.parseInt(placeRequest.substring(3, 5));
        boolean isFlipped = placeRequest.charAt(5) == 'T';
        Orientation orientation = Orientation.charToOrientation(placeRequest.charAt(6));
        int fireTileID;
        if (tileChar >= 'a' && tileChar <= 'z') {
            fireTileID = tileChar - 'a';
        } else {
            fireTileID = tileChar - 'A' + ('z' - 'a' + 1);
        }

        drawnFireTile = new FireTile(fireTileID);
        return isPlaceFireTileValid(drawnFireTile, new Location(rowPos, colPos), orientation, isFlipped);
    }

    /**
     * move cat first judge the validity of movement
     * it is the interface for the usage upper lever
     *
     * @param cat         the cat that should be moved
     * @param aimLocation the location that the cat will move to.
     */
    public void moveCat(Cat cat, Location aimLocation) {
        if (isMoveCatValid(cat, aimLocation, null)) {
            // we should remove and move the cat
            board.getSingleSquare(cat.getPlacedLocation()).removeCat();
            if (cat.isExhausted()) {
                needDisCardNum = 2;
            } else {
                needDisCardNum = 1;
            }

            cat.moveCat(aimLocation);
            Square aimSquare = board.getSingleSquare(aimLocation);
            if (aimSquare.getCardType() == Card.CardType.RAFT) {
                cat.setArrived(true);
            }
            aimSquare.addCat();
        }
        Collections.sort(cats);
    }

    /**
     * use string to move a cat, use the action string of movement, first change it
     * to every
     * useful values of cat color, start location, aim location,deck and card
     * and recall the placement method without string.
     *
     * @param moveRequest the string action of move cat
     *                    {color}{startRow}{startColumn}{aimRow}{aimColumn}{deckType}{cardType}
     *                    G01070611Cc
     */
    public void moveCatByString(String moveRequest) {
        char catColor = moveRequest.charAt(0);
        int startRowPos = Integer.parseInt(moveRequest.substring(1, 3));
        int startColPos = Integer.parseInt(moveRequest.substring(3, 5));
        int endRowPos = Integer.parseInt(moveRequest.substring(5, 7));
        int endColPos = Integer.parseInt(moveRequest.substring(7, 9));
        Location startLocation = new Location(startRowPos, startColPos);
        Location aimLocation = new Location(endRowPos, endColPos);

        for (Cat cat : cats) {
            // find the cat
            if (cat.getPlacedLocation().equals(startLocation) && cat.getColor().equals(Color.charToColor(catColor))) {
                board.getSingleSquare(cat.getPlacedLocation()).removeCat();
                if (cat.isExhausted()) {
                    needDisCardNum = 2;
                } else {
                    needDisCardNum = 1;
                }
                cat.moveCat(aimLocation);
                board.getSingleSquare(aimLocation).addCat();
                break;
            }
        }
        Collections.sort(cats);
    }

    /**
     * judgement of the movement of cat, cannot move out of board,
     * when cat is on the raft, same location, aim location has cat,
     * aim square is objective part of raftCard, aim square has fire,
     * need to discardCard, aim square is not the same color,
     * no way to the aim location(use bfs judgement the valid path)
     *
     * @param cat         the cat that needs to be moved
     * @param aimLocation the location that the cat will move to
     * @param pathOutput  a ArrayList<Integer>[1] to store the path that the
     *                    aim location, get from pathOutput[0]
     *                    if movement is invalid, then pathOutput[0] will be null
     * @return true if movement is valid, false otherwise
     */
    public boolean isMoveCatValid(Cat cat, Location aimLocation, ArrayList<Integer>[] pathOutput) {
        // Aim location is out of the board
        if (!aimLocation.isOnBoard(board.getSizeRow(), board.getSizeCol())) {
            moveCatErrorString = "Aim location is out of the board.";
            return false;
        }

        // Cat is not on the board
        if (!cat.getPlacedLocation().isOnBoard(board.getSizeRow(), board.getSizeCol())) {
            moveCatErrorString = "Cat is not on the board.";
            return false;
        }

        // after judge the situation of out of the board, then can use the
        // board.getSingleSquare(aimLocation);
        Location catLocation = cat.getPlacedLocation();
        Square aimSquare = board.getSingleSquare(aimLocation);

        // cat is on raft can not move
        if (board.getSingleSquare(catLocation).getCardType() == Card.CardType.RAFT) {
            moveCatErrorString = "Cat is on RaftCard.";
            return false;
        }
        // same location
        if (catLocation.equals(aimLocation)) {
            moveCatErrorString = "Aim location is same of the original location.";
            return false;
        }

        // not on board
        if (!aimLocation.isOnBoard(board.getSizeRow(), board.getSizeCol())) {
            moveCatErrorString = "Aim location is out of the board.";
            return false;
        }

        // not have cat, objective raft, fire
        if (aimSquare.isHasCat()) {
            moveCatErrorString = "Aim location has Cat.";
            return false;
        } else if (aimSquare.isObjectiveRaft()) {
            moveCatErrorString = "Aim location is objective of the RaftCard.";
            return false;
        } else if (aimSquare.isHasFire()) {
            moveCatErrorString = "Aim location has Fire.";
            return false;
        }
        // not have the enough card in hand for discard
        if (cat.isExhausted()) {
            if (hand.getCardNum() < 2) {
                moveCatErrorString = "Cat is tired and need discard 2 cards, there is "
                        + (hand.isEmpty() ? "no card" : "only 1 card");
                return false;
            }
        } else {
            if (hand.isEmpty()) {
                moveCatErrorString = "Cat is not exhausted and need discard 1 PathwayCards, there is no card on hand.";
                return false;
            }
        }

        // raft cat judge if it has the color string
        if (aimSquare.getColor() != cat.getColor() && !aimSquare.isWildForRaft()) {
            moveCatErrorString = "Color of aim location is not same of the cat.";
            return false;
        }

        // judge the route
        if (!judgeMoveValidByBFS(cat, aimLocation, pathOutput)) {
            moveCatErrorString = "Route exist different Color.";
            return false;
        }

        return true;
    }

    /**
     * judgement the validity of movement by string, first change the action string
     * to the useful values like
     * cat color, start location, aim location,deck and card and recall the
     * placement method without string.
     *
     * @param requestString the string action of move cat
     *                      {color}{startRow}{startColumn}{aimRow}{aimColumn}{deckType}{cardType}
     *                      G01070611Cc
     * @return true if the movement is valid, false otherwise
     */
    boolean isMoveCatValidByString(String requestString) {
        Color catColor = Color.charToColor(requestString.charAt(0));
        int startRowPos = Integer.parseInt(requestString.substring(1, 3));
        int startColPos = Integer.parseInt(requestString.substring(3, 5));
        int aimRowPos = Integer.parseInt(requestString.substring(5, 7));
        int aimColPos = Integer.parseInt(requestString.substring(7, 9));

        Deck.DeckType deckType1 = Deck.DeckType.charToDeckType(requestString.charAt(9));
        char cardType1 = requestString.charAt(10);

        int disCardNum = requestString.length() > 11 ? 2 : 1;
        boolean isExistCard1 = false;
        boolean isExistCard2 = false;

        for (PathwayCard card : hand.getCardsList()) {
            if (card.getDeckType() == deckType1 && card.getTypeChar() == cardType1) {
                isExistCard1 = true;
            }
            // it means we should discard two card
            if (requestString.length() > 11) {
                Deck.DeckType deckType2 = Deck.DeckType.charToDeckType(requestString.charAt(11));
                char cardType2 = requestString.charAt(12);
                if (card.getDeckType() == deckType2 && card.getTypeChar() == cardType2) {
                    isExistCard2 = true;
                }
            }
        }

        if (!isExistCard1) {
            return false;
        }

        if (requestString.length() > 11) {
            if (!isExistCard2) {
                return false;
            }
        }

        Location startLocation = new Location(startRowPos, startColPos);
        Location aimLocation = new Location(aimRowPos, aimColPos);

        for (Cat cat : cats) {
            if (cat.getColor().equals(catColor) && cat.getPlacedLocation().equals(startLocation)) {
                if (cat.isExhausted() && disCardNum == 1) {
                    return false;
                }
                return isMoveCatValid(cat, aimLocation, null);
            }
        }
        return false;
    }

    /**
     * judge whether the cat have the path to the aim location. use the bfs to
     * search every valid path.
     * judge whether we find the aim location
     *
     * @param cat         the cat that needs to be moved
     * @param aimLocation the location that the cat should move to
     * @param pathOutput  a ArrayList<Integer>[1] to store the path that the
     *                    aim location, get from pathOutput[0]
     *                    if movement is invalid, then pathOutput[0] will be null
     * @return true if the cat have the valid path to the aim location, false
     *         otherwise.
     */
    public boolean judgeMoveValidByBFS(Cat cat, Location aimLocation, ArrayList<Integer>[] pathOutput) {
        ArrayList<ArrayList<Integer>> neighbourList = board.getNeighbourList();
        ArrayList<Integer> preNodeList = new ArrayList<>();

        for (int row = 0; row < board.getSizeRow(); row++) {
            for (int col = 0; col < board.getSizeCol(); col++) {
                preNodeList.add(-1);
            }
        }

        // create a queue to run the bfs
        Queue<Integer> queue = new LinkedList();
        HashSet<Integer> visited = new HashSet<>();
        // store the start and aim node
        // we change the location to integer, because if we judge whether we use visit
        // the node, location should
        // be the same object, not the same value of the location
        int startNode = cat.getPlacedLocation().getRow() * board.getSizeCol() + cat.getPlacedLocation().getColumn();
        int aimNode = aimLocation.getRow() * board.getSizeCol() + aimLocation.getColumn();
        Color catColor = cat.getColor();

        queue.add(startNode);

        while (!queue.isEmpty()) {
            int fatherNode = queue.remove();

            // if we push the expected node, so it means the cat can move to the aim
            // location
            if (fatherNode == aimNode) {

                // for save the path, for output
                if (pathOutput != null && pathOutput.length >= 1) {
                    ArrayList<Integer> outputList = new ArrayList<>();
                    pathOutput[0] = outputList;

                    int currentNode = aimNode;
                    int preNode = preNodeList.get(currentNode);
                    outputList.add(currentNode);
                    while (preNode != startNode) {
                        currentNode = preNode;
                        preNode = preNodeList.get(preNode);
                        outputList.add(currentNode);
                    }
                }

                return true;
            }
            visited.add(fatherNode);
            for (int childNode : neighbourList.get(fatherNode)) {

                // transfer the node to row and col. and use them to find the square.
                int childRowPos = childNode / board.getSizeCol();
                int childColPos = childNode % board.getSizeCol();
                Square childsquare = board.getSingleSquare(childRowPos, childColPos);

                int fatherRowPos = fatherNode / board.getSizeCol();
                int fatherColPos = fatherNode % board.getSizeCol();
                Square fatherSquare = board.getSingleSquare(fatherRowPos, fatherColPos);

                // can not go out of the raft card
                // can go into but cannot go out
                if (fatherSquare.getCardType() == Card.CardType.RAFT
                        && childsquare.getCardType() != Card.CardType.RAFT) {
                    continue;
                }

                // add the valid and not visit node into the queue
                if ((childsquare.getColor().equals(catColor) || childsquare.isWildForRaft())
                        && !visited.contains(childNode)) {
                    preNodeList.set(childNode, fatherNode);
                    queue.add(childNode);
                }
            }
        }
        return false;
    }

    /**
     * search all cats and find whether there exists a cat that has no way to the
     * raftCard.
     * use the bfs algorithm to complete this search.
     * invalid path, meet fire, meet a different color square that which can not be
     * overlapped by any card
     *
     * @param forJudgeWinOrLoss when give the forJudgeWinOrLoss is true, we truly
     *                          use this method to judge win or loss
     *                          when we use it to judge whether the placement of
     *                          fireTire that influence the
     *                          movement of a cat, we give the forJudgeWinOrLoss is
     *                          false
     *
     * @return true if the there exists at least one cat cannot move to the raft,
     *         false otherwise
     */
    public boolean isNowayForOneCatToRaft(boolean forJudgeWinOrLoss) {
        // get the neighbour list
        ArrayList<ArrayList<Integer>> neighbourList = board.getNeighbourList();
        // store the node that has cat
        HashSet<Integer> catSquareNode = new HashSet<>();
        // store the node that belongs to the raft
        HashSet<Integer> raftSquareNode = new HashSet<>();
        // store the color of all nodes on the board
        ArrayList<Color> nodeColor = new ArrayList<>();
        // store whether the node has fire of all nodes
        ArrayList<Boolean> nodeFire = new ArrayList<>();
        // store whether the node can be overlapped by a card of all nodes
        ArrayList<Boolean> nodeCanBeOverlappedByCard = new ArrayList<>();
        // objective node can be only one
        int objectiveNode = -1;

        // loop all the node to store the useful information of the node
        // and store them in each set or list
        for (int row = 0; row < board.getSizeRow(); row++) {
            for (int col = 0; col < board.getSizeCol(); col++) {
                int node = row * board.getSizeCol() + col;
                Square square = board.getSingleSquare(row, col);
                nodeColor.add(square.getColor());

                // store cat node
                if (square.isHasCat()) {
                    catSquareNode.add(node);
                }
                // store raft node
                if (square.getCardType() == Card.CardType.RAFT) {
                    raftSquareNode.add(node);
                }
                // store objective node
                if (square.isObjectiveRaft()) {
                    objectiveNode = node;
                }
                // create the fire node
                nodeFire.add(square.isHasFire());

                // judge whether the node can be overlapped
                boolean isExistValidPlacedCard;
                // fire and raft node can not be overlapped
                if (square.isHasCat() || square.isHasFire() || square.getCardType() == Card.CardType.RAFT) {
                    isExistValidPlacedCard = false;
                } else {
                    // first think the node cannot be overlapped, then search to find one
                    isExistValidPlacedCard = false;
                    for (int i = -2; i <= 0; i++) {
                        for (int j = -2; j <= 0; j++) {
                            int placedRow = row + i;
                            int placedCol = col + j;
                            // use the method to judge, if valid, then we find a card that can overlap the
                            // square
                            if (placeCardValid(placedRow, placedCol)) {
                                isExistValidPlacedCard = true;
                                break;
                            }
                        }
                        if (isExistValidPlacedCard) {
                            break;
                        }
                    }
                }
                // set all node about the validity of overlap
                nodeCanBeOverlappedByCard.add(isExistValidPlacedCard);
            }
        }

        // there is a loop for cat---search all cats to find a cat that has no way to
        // move to the raft
        for (Cat cat : cats) {
            Location catLocation = cat.getPlacedLocation();
            Square catSquare = board.getSingleSquare(catLocation);
            int catNode = catLocation.getRow() * board.getSizeCol() + catLocation.getColumn();

            // the cat which is already on the raft, not need move, it's true to the raft
            if (catSquare.getCardType() == Card.CardType.RAFT) {
                continue;
            } else {
                // if cat is near the valid square
                // the cat can directly move to, so it has the way to the raft
                boolean isNearValidSquare = false;
                for (Location neighLocation : catLocation.getAdjacentLocation()) {
                    if (neighLocation.isOnBoard(board.getSizeRow(), board.getSizeCol())) {
                        Square square = board.getSingleSquare(neighLocation);
                        if (square.getCardType() == Card.CardType.RAFT
                                && (square.getColor().equals(cat.getColor()) || square.isWildForRaft())) {
                            isNearValidSquare = true;
                        }
                    }
                }
                if (isNearValidSquare) {
                    continue;
                }
            }

            // raft card
            // not has cat
            // create the valid node(can be success)
            HashSet<Integer> validSquareNode = new HashSet<>();
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    int rowPos = row + raftCard.getPlacedLocation().getRow();
                    int colPos = col + raftCard.getPlacedLocation().getColumn();
                    Square square = board.getSingleSquare(rowPos, colPos);
                    // valid node must not have cat,
                    // must same color or the wild part of raft
                    if (!square.isHasCat() && (cat.getColor().equals(square.getColor()) || square.isWildForRaft())) {
                        validSquareNode.add(rowPos * board.getSizeCol() + colPos);
                    }
                }
            }

            // use the queue for bfs search the path
            Queue<Integer> queue = new LinkedList<>();
            HashSet<Integer> visited = new HashSet<>();

            queue.add(catNode);
            boolean canGotoValidNode = false;
            while (!queue.isEmpty()) {
                int fatherNode = queue.remove();
                visited.add(fatherNode);

                // find the path, this cat can go to the raft, break the bfs search
                if (validSquareNode.contains(fatherNode)) {
                    canGotoValidNode = true;
                    break;
                }

                // pop the child node of the father node(neighbour list)
                for (Integer childNode : neighbourList.get(fatherNode)) {
                    // can not be visited, not fire, not objective
                    if (visited.contains(childNode) || nodeFire.get(childNode) || childNode == objectiveNode) {
                        continue;
                    }

                    // next square in the raft
                    if (raftSquareNode.contains(childNode)) {
                        // has color but the color is not same as cat
                        if (nodeColor.get(childNode) != Color.NONE
                                && !nodeColor.get(childNode).equals(cat.getColor())) {
                            continue;
                        }
                    } else {
                        // next square is not in the raft
                        // color is not same and the square can not be changed the color anymore(cannot
                        // be overlapped)
                        if (!nodeColor.get(childNode).equals(cat.getColor())
                                && !nodeCanBeOverlappedByCard.get(childNode)) {
                            continue;
                        }
                    }
                    // not the same color should judge whether it can be overlapped by a card
                    // raft card don't consider
                    queue.add(childNode);
                }
            }

            // find a cat that cannot have path go to the raft, if for the actually running,
            // not test, we record cat for display of loss information
            if (!canGotoValidNode) {
                if (forJudgeWinOrLoss) {
                    cat.setCanGetToRaft(false);
                }
                return true;
            }
        } // for loop of cat

        // not find a cat that has no way to the raft
        return false;
    }

    /**
     * This method is for check whether there exists a valid placement for the
     * fireTile, there are 2 judgment rules
     * consider the cause of loss or just consider the rule for placement
     *
     * @param fireTile
     * @param needJudgeCatCannotMoveAtSameTime
     *                                         set true we should consider the
     *                                         placement of fireTile will not let
     *                                         the game loss
     *                                         it is for hint the user, and give a
     *                                         valid placement
     *                                         set false, just consider the
     *                                         placement of fireTile obey the
     *                                         placement rules, may let the game
     *                                         loss
     *                                         it is for judgment for loss about the
     *                                         rules (there is no placement for a
     *                                         fireTile if we need to place it)
     * @return Object[] the valid information of placement of the fireTile
     *         it is null if there is no valid placement
     *         not null if there is at least one valid placement
     */
    public Object[] findValidPlaceForFireTile(FireTile fireTile, boolean needJudgeCatCannotMoveAtSameTime) {
        Object[] objects = null;
        Location location;
        boolean existValidPlace = false;
        Location placedLocation;

        Square[][] fireTileSquares;
        Square[][] preSquares;
        Square[][] tryPlaceSquares;

        for (int row = 0; row < board.getSizeRow(); row++) {
            for (int col = 0; col < board.getSizeCol(); col++) {
                placedLocation = new Location(row, col);
                for (Orientation orientation : Orientation.values()) {
                    for (int i = 0; i < 2; i++) {
                        boolean isFlipped = (i == 0) ? false : true;

                        /*
                         * there is a switch
                         * every time, when place the fire, I try to judge whether this placement let
                         * the game loss
                         */
                        if (needJudgeCatCannotMoveAtSameTime) {
                            // run the valid judgement method
                            if (isPlaceFireTileValid(fireTile, placedLocation, orientation, isFlipped)) {

                                // first place the fireTile, judge whether cat is surrounded, if not, then
                                // return ture, if surrounded, then recover the preSquare
                                fireTileSquares = fireTile.getCardSquare(orientation, isFlipped);
                                preSquares = new Square[fireTileSquares.length][fireTileSquares[0].length];
                                tryPlaceSquares = new Square[fireTileSquares.length][fireTileSquares[0].length];

                                // first place the fireTile
                                for (int innerRow = 0; innerRow < preSquares.length; innerRow++) {
                                    for (int innerCol = 0; innerCol < preSquares[0].length; innerCol++) {
                                        preSquares[innerRow][innerCol] = board.getSingleSquare(innerRow + row,
                                                innerCol + col);
                                        tryPlaceSquares[innerRow][innerCol] = fireTileSquares[innerRow][innerCol];
                                    }
                                }

                                // first place-then should recover immediately
                                this.board.setSquares(placedLocation, tryPlaceSquares);
                                boolean isOneCatHasNoWay = isNowayForOneCatToRaft(false);
                                // recover the squares on the board
                                this.board.setSquares(placedLocation, preSquares);

                                // surround the cat the place is not valid,place the preSquares back
                                if (!isOneCatHasNoWay) {
                                    objects = new Object[4];
                                    objects[0] = placedLocation;
                                    objects[1] = orientation;
                                    objects[2] = isFlipped;
                                    objects[3] = fireTileSquares;

                                    return objects;
                                }
                            }
                        } else {
                            // just only consider the placement validity
                            if (isPlaceFireTileValid(fireTile, placedLocation, orientation, isFlipped)) {
                                fireTileSquares = fireTile.getCardSquare(orientation, isFlipped);
                                objects = new Object[4];
                                objects[0] = placedLocation;
                                objects[1] = orientation;
                                objects[2] = isFlipped;
                                objects[3] = fireTileSquares;

                                return objects;
                            }

                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * judgement whether there is a valid placement for the fireTile
     *
     * @param fireTile the fireTile that needs to be placed
     * @return true if there has a valid placement, false otherwise
     */
    public boolean isNoPlaceForFireTile(FireTile fireTile) {
        return findValidPlaceForFireTile(fireTile, false) == null;
    }

    /**
     * judge is all squares are placed on the board, it is just for the normal card,
     * we don't determine
     * which card, but it's size is 3X3. And it should obey the pathwayCard
     * placement rule.
     * not out of board, not overlap fire, cat, raft
     *
     * @param placeRow  the placement of the row
     * @param placedCol the placement of the col
     * @return true if the placeCard is valid, false otherwise
     */
    public boolean placeCardValid(int placeRow, int placedCol) {
        // out of the board
        if (placeRow < 0 || placeRow + 3 > board.getSizeRow() || placedCol < 0 || placedCol + 3 > board.getSizeCol()) {
            return false;
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Square squareOnBoard = board.getSingleSquare(row + placeRow, col + placedCol);
                if (squareOnBoard.isHasFire()) {
                    return false;
                } else if (squareOnBoard.getCardType() == Card.CardType.RAFT) {
                    return false;
                }
            }
        }
        return true;
    }

    // Getter Methods
    /**
     * get the board in gameRun
     *
     * @return board object in gameRun
     */
    public Board getBoard() {
        return board;
    }

    /**
     * get the challenge in gameRun
     *
     * @return the challenge object in gameRun
     */
    public Challenge getChallenge() {
        return challenge;
    }

    /**
     * get the array of island in gameRun
     *
     * @return the array of island in gameRun
     */
    public IslandBoard[] getIslandBoards() {
        return islandBoards;
    }

    /**
     * get the array of fireCard in gameRun
     *
     * @return the array of fireCard in gameRun
     */
    public FireCard[] getFireCards() {
        return fireCards;
    }

    /**
     * get the array of catLocationCard in gameRun
     *
     * @return the array of catLocationCards in gameRun
     */
    public CatLocationCard[] getCatLocationCards() {
        return catLocationCards;
    }

    /**
     * get the raftCard in gameRun
     *
     * @return the raftCard object in gameRun
     */
    public RaftCard getRaftCard() {
        return raftCard;
    }

    /**
     * get the array of deck in gameRun
     *
     * @return the array of deck in gameRun
     */
    public Deck[] getDecks() {
        return decks;
    }

    /**
     * get the total card num in all decks, add card of 4 decks
     *
     * @return the total card num in all decks
     */
    public int getTotalCardInDecks() {
        return decks[0].getCardNum() + decks[1].getCardNum() + decks[2].getCardNum() + decks[3].getCardNum();
    }

    /**
     * get the hand in gameRun
     *
     * @return the hand object in gameRun
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * get the fireTileBag in gameRun
     *
     * @return the fireTileBag object in gameRun
     */
    public FireTileBag getFireTileBag() {
        return fireTileBag;
    }

    /**
     * get the drawnFireTile in gameRun
     *
     * @return the drawnFireTile object in gameRun,
     *         it could be null, means that there is no drawnFireTile
     */
    public FireTile getDrawnFireTile() {
        return drawnFireTile;
    }

    /**
     * judge whether we should discard a card, it depends on the needDisCardNum
     *
     * @return how many cards that should be discarded at current state
     */
    public boolean needDisCard() {
        return needDisCardNum > 0;
    }

    /**
     * get the needDisCardNum in gameRun, means that how many cards that should be
     * discarded
     *
     * @return how many cards that should be discarded at current state
     */
    public int getNeedDisCardNum() {
        return needDisCardNum;
    }

    /**
     * get the error string of placement of the pathwayCard
     *
     * @return the error string of placement of pathwayCard
     */
    public String getPlacePathwayCardErrorString() {
        return placePathwayCardErrorString == null ? "" : placePathwayCardErrorString;
    }

    /**
     * get the error string of placement of the fireTile
     *
     * @return the error string of placement of the fireTile
     */
    public String getPlaceFireTireErrorString() {
        return placeFireTireErrorString == null ? "" : placeFireTireErrorString;
    }

    /**
     * get the error string of movement of the cat
     *
     * @return the error string of movement of the cat
     */
    public String getMoveCatErrorString() {
        return moveCatErrorString == null ? "" : moveCatErrorString;
    }

    /**
     * get the list of all cats in the gameRun
     *
     * @return the list that stores all cats
     */
    public ArrayList<Cat> getCats() {
        return cats;
    }

    /**
     * get the specific cat on a location of board
     *
     * @return cat object: the location has a cat on board
     *         null: the location does not have a cat on board
     */
    public Cat getCat(Location location) {
        for (Cat cat : cats) {
            if (location.equals(cat.getPlacedLocation())) {
                return cat;
            }
        }
        return null;
    }

    /**
     * create a state String that follows the rules, it has the protocol like
     * ( 0 Board state + 1 Deck + 2 HAND + 3 NONE? + 4 FireTileBag )
     *
     * @return string array of game state. the length of array is 5
     */
    public String[] getStateString() {
        String[] strings = new String[5];
        strings[0] = board.getBoardStateString();
        strings[1] = decks[0].getDeckString() + decks[1].getDeckString() + decks[2].getDeckString()
                + decks[3].getDeckString();
        strings[2] = hand.getHandString();
        StringBuffer stringBuffer = new StringBuffer();
        for (Cat cat : cats) {
            if (cat.isExhausted()) {
                stringBuffer.append(cat.getColor().toChar(true));
                stringBuffer.append(cat.getPlacedLocation().getRow() / 10);
                stringBuffer.append(cat.getPlacedLocation().getRow() % 10);
                stringBuffer.append(cat.getPlacedLocation().getColumn() / 10);
                stringBuffer.append(cat.getPlacedLocation().getColumn() % 10);
            }
        }
        strings[3] = stringBuffer.toString();
        strings[4] = fireTileBag.getFireTileBagString();
        return strings;
    }

    /**
     * print all the relevant object in gameRun
     * about the challenge, islandBoards, fireCards, catLocationCards, raftCard,
     * decks, hand, fireTileBag, cats, board
     */
    public void printAllState() {
        if (this.challenge != null) {
            System.out.println(this.challenge);
            for (IslandBoard l : this.islandBoards) {
                System.out.println(l);
            }
            for (FireCard l : this.fireCards) {
                System.out.println(l);
            }
            for (CatLocationCard l : this.catLocationCards) {
                System.out.println(l);
            }
        } else {
            System.out.println("ChallengeNum: is none\nno challenge, islandBoard, fireCard, catLocationCard"); // number
        }
        System.out.println(this.raftCard);

        for (Deck f : this.decks) {
            System.out.println(f);
        }
        System.out.println(this.hand);
        System.out.println(this.fireTileBag);
        for (Cat f : this.cats) {
            System.out.println(f);
        }
        System.out.println(board);
    }

    /**
     * overwrite the toString method, and can output all the state about the game
     *
     * @return state string of the game
     */
    @Override
    public String toString() {
        String string = new String();
        if (this.challenge != null) {
            string += challenge.toString() + "\n";
            for (IslandBoard l : this.islandBoards) {
                string += l.toString() + "\n";
            }
            for (FireCard l : this.fireCards) {
                string += l.toString() + "\n";
            }
            for (CatLocationCard l : this.catLocationCards) {
                string += l.toString() + "\n";
            }
            string += this.raftCard + "\n";
        } else {
            string += ("ChallengeNum: is none\nno challenge, islandBoard, fireCard, catLocationCard, raftCard\n"); // number
        }

        for (Deck l : this.decks) {
            string += l.toString() + "\n";
        }
        string += this.hand.toString() + "\n" + fireTileBag + "\n";
        for (Cat l : this.cats) {
            string += l + "\n";
        }
        string += board.toString();
        return string;
    }

    /**
     * inner main method for test and check whether the class is well-designed
     *
     * @param args
     *
     */
    public static void main(String[] args) {

        String gameState[] = new String[] { "fffffffffrrfffffff\n" +
                "fffffffffrrfffffff\n" +
                "fffffffffrrfffffff\n" +
                "fffgffyrgpygyrygbr\n" +
                "fffggfggyygprbprpg\n" +
                "fffgggbgprbpygbpyb\n" +
                "ffffffbpbpgrbrrbgy\n" +
                "ffffffgygybpgygprb\n" +
                "ffffffbrrrybgygybg\n" +
                "ffffffgpbbyrprgbbp\n" +
                "ffffffbyrbpybgpryg\n" +
                "ffffffpgyrggrbgyby\n" +
                "fffffybgbpryybpGYp\n" +
                "ffffYyybpyyyyygRow\n" +
                "fffyyyyyyygbyyywww\n",
                "AabcdstuvwxyBabcdefijklotuvwxyCabcdefvwyDabcdeghijkvwxy",
                "AmBCqDn",
                "",
                "abCDE"
        };

        GameRun gameRun = new GameRun(9);
        gameRun.printAllState();
        for (String s : gameRun.getStateString()) {
            System.out.println(s);
        }
        for (Cat cat : gameRun.cats) {
            System.out.println(cat + "    has way to the raft:    " + gameRun.isNowayForOneCatToRaft(true));
        }
        gameRun.isMoveCatValid(new Cat(Color.NONE, 0, -1), new Location(0, 0), null);

        System.out.println("aaaaaaaaaaaaaa\n" + gameRun.getBoard().getBoardStateString());
        System.out.println("bbbbbbbbbbbbbb\n" + gameRun.getBoard());

    }

}
