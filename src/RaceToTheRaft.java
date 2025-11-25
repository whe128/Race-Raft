

import java.sql.SQLOutput;
import java.util.Random;

/**
 * This class is for testing purposes only. You should create and use your own objects to solve the tasks below
 * instead of directly using the strings provided. Note that Task 2 is the only task you are expected to use string
 * manipulation to solve.
 */

import java.util.Random;
import java.util.ArrayList;

public class RaceToTheRaft {

    /**
     * Determine whether a boardState string is well-formed.
     * To be well-formed the string must satisfy all the following conditions:
     * <p>
     * 1. Each line is terminated by a newline character `\n`
     * 2. The number of printable (non-newline) characters in each line is equal AND is either 9 or 18.
     * 3. Each character (except the newline character) is one of the following:
     * - 'b' (blue)
     * - 'B' (blue with blue cat)
     * - 'f' (fire)
     * - 'g' (green)
     * - 'G' (green with green cat)
     * - 'n' (none)
     * - 'o' (objective)
     * - 'p' (purple)
     * - 'P' (purple with purple cat)
     * - 'r' (red)
     * - 'R' (red with red cat)
     * - 'w' (wild)
     * - 'W' (wild with a cat of any colour)
     * - 'y' (yellow)
     * - 'Y' (yellow with yellow cat)
     * 4. The number of lines is either 12, 15 or 18.
     * </p>
     *
     * @param boardString A string representing the boardState
     * @return True if the boardState is well-formed, otherwise false.
     */
    public static boolean isBoardStringWellFormed(String boardString) {
        String[] lines = boardString.split("(?<=\n)");
        int numOfLines = lines.length;

        // Check condition 4: number of lines is either 12, 15, or 18
        if (numOfLines != 12 && numOfLines != 15 && numOfLines != 18){
            return false;
        }

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
                if (!validCharacter(c)) {
                    return false;
                }
            }
        }

        return true; // FIXME TASK 2
    }

    // Given a character, checks if it is valid character (condition 3)
    private static boolean validCharacter (char c){
        String validChars = "bBfgGnopPrRwWyY";
        return validChars.indexOf(c) != -1;
    }

    /**
     * Make Constructors for each of your objects.
     */
    // FIXME TASK 3

    /**
     * Draws a random fire tile from those remaining in the bag.
     *
     * @param gameState the current state of the game, including the fire tile bag
     * @return a random fire tile from those remaining, in string form. If there are no tiles remaining, return the
     * empty string.
     */
    public static String drawFireTile(String[] gameState) {
        GameRun gameRun = new GameRun(gameState);
        FireTile fireTile = gameRun.getFireTileBag().removeFireTile();
        if(fireTile!=null){
            return String.valueOf(fireTile.getTypeChar());
        }
        else {
            return "";
        }
        // FIXME TASK 5
    }

    /**
     * Chooses a random challenge from those available in the Utility class according
     * to the given difficulty.
     *
     * @param difficulty the desired difficulty of the challenge
     * @return a random challenge of the given difficulty
     */
    public static String chooseChallenge(int difficulty) {
        /**
         *   difficulty 0     challenge number 1~4
         *   difficulty 1     challenge number 5~8
         *   difficulty 2     challenge number 9~16
         *   difficulty 3     challenge number 17~24
         *   difficulty 4     challenge number 25~32
         *   difficulty 5     challenge number 33~39
         */
        GameRun gameRun = new GameRun(difficulty);
        String string;
        if(gameRun.getChallenge() != null){
            string = gameRun.getChallenge().getChallengeString();
        }
        else {
            string = "";
        }
        return string;
        // FIXME TASK 6
    }

    /**
     * Draw random cards from the specified decks.
     * The decks string denotes what decks to draw from and how many cards to draw from that deck.
     * <p>
     * For example the drawRequest string "A4B1D1" tells us that we should draw 4 cards from deck A, 1 card from deck B
     * and
     * 1 card from deck D.
     * <p>
     * If I draw cards a, b, d, l, from deck A, card a from deck B and card s from deck D, I would return the string:
     * "AabdlBaCDs".
     * Decks should be listed in alphabetical order, with cards drawn from that deck also listed in alphabetical order.
     * </p>
     * Recall the mapping between deck and char:
     * A -> CIRCLE;
     * B -> CROSS;
     * C -> SQUARE;
     * D -> TRIANGLE;
     *
     * @param gameState   the current state of the game, including the current state of the decks
     * @param drawRequest A string representing the decks to draw from and the number of cards to draw from each respective
     *                    deck.
     * @return The updated gameState array after the cards have been drawn. (Remove all cards drawn from decks and
     * add them to the Hand string). If it is not possible
     * to draw all the specified cards, you should return the original gameState.
     */
    public static String[] drawHand(String[] gameState, String drawRequest) {
        GameRun gameRun = new GameRun(gameState);
        gameRun.drawPathwayCardsByString(drawRequest);

        return gameRun.getStateString();
        // FIXME TASK 7
    }

    /**
     * Place the given card or fire tile as described by the placement string and return the updated gameState array.
     * See the README for details on these two strings.
     * You may assume that the placements given are valid.
     * <p>
     * When placing a card, you should update both the Board string and remove the corresponding card from the Hand
     * string in the gameState array.
     *
     * @param gameState       An array representing the game state.
     * @param placementString A string representing a Fire Tile Placement or a Card Placement.
     * @return the updated gameState array after this placement has been made
     */
    public static String[] applyPlacement(String[] gameState, String placementString) {
        GameRun gameRun = new GameRun(gameState);

        char ch0 = placementString.charAt(0);
        char ch1 = placementString.charAt(1);
        //card placement Ab1208S
        if(ch1>='a' && ch1<='y'){
            Deck deck = gameRun.getDecks()[ch0-'A'];
            gameRun.placePathwayCardByString(placementString);
        }
        else if(Character.isDigit(ch1)){
            //fire placement l0003TW
                gameRun.placeFireTileByString(placementString);
        }

        return gameRun.getStateString();
        // FIXME TASK 8
    }

    /**
     * Move the given cat as described by the cat movement string and return the updated gameState array. You may
     * assume that the cat movement is valid.
     * <p>
     * You should both move the cat (updating the Board string) and also add the cat to the Exhausted Cats string, or
     * update that cat's reference in the Exhausted Cats string if it was already exhausted.
     *
     * @param gameState      An array representing the game state.
     * @param movementString A string representing the movement of a cat and the cards discarded to allow this move.
     * @return the updated gameState array after this movement has been made.
     */

    public static String[] moveCat(String[] gameState, String movementString) {
        //{colour}{startLocation}{endLocation}
        GameRun gameRun = new GameRun(gameState);
        gameRun.moveCatByString(movementString);
        return gameRun.getStateString();
        // FIXME TASK 9
    }

    /**
     * Given a challengeString, construct a board string that satisfies the challenge requirements.
     * <p>
     * Each board in the `squareBoard` or `rectangleBoard` arrays may only be used once. For example: if the
     * challenge requires 4 Large (square) boards, you must use all 4 square boards. You may not use the same board
     * twice, even in different orientations.
     * The cat, fire card and raft card placements should all match the challenge string.
     *
     * @param challengeString A sialitring representing the challenge to initse
     * @return A board string for this challenge.
     */
    public static String initialiseChallenge(String challengeString) {
        GameRun gameRun = new GameRun(challengeString);
        return gameRun.getBoard().toString();
        // FIXME 10
    }



    /**
     * Given a card placement string or a fire tile placement string, check if that placement is valid.
     * <p>
     * A card placement is valid if all the following conditions are met:
     * <p>
     * 1. No part of the card is off-board
     * 2. No part of the card is overlapping fire.
     * 3. No part of the card is overlapping a cat.
     * 4. No part of the card is overlapping part of a Raft card (any of the 8 squares surrounding the `o`
     * state)
     * </p>
     * A fire tile placement is valid if all the following conditions are met:
     * <p>
     * 1. No part of the fire tile is off-board
     * 2. No part of the fire tile overlaps fire
     * 3. No part of the fire tile overlaps a cat
     * 4. No part of the fire tile overlaps part of a Raft card (any of the 8 squares surrounding the `o` state)
     * 5. The Fire tile is orthogonally adjacent to another fire square.
     * </p>
     *
     * @param gameState       An array representing the gameState
     * @param placementString A string representing a card placement or a fire tile placement
     * @return True if the placement is valid, otherwise false.
     */
    public static boolean isPlacementValid(String[] gameState, String placementString) {
        GameRun gameRun = new GameRun(gameState);
        char ch0 = placementString.charAt(0);
        char ch1 = placementString.charAt(1);

        //card placement Ab1208S
        if(ch1>='a' && ch1<='y'){
            return gameRun.isPlacePathwayCardValidByString(placementString);
        }
        else if(Character.isDigit(ch1)){
            //fire placement l0003TW
            return gameRun.isPlaceFireTileValidByString(placementString);
        }
        return false;
        // FIXME TASK 12
    }


    /**
     * Given a cat movement string, check if the cat movement is valid.
     * <p>
     * A cat movement is valid if:
     * 1. The end location is the same colour as the cat.
     * 2. There is a path from start location to the end location, consisting only of squares the same colour as the
     * cat.
     * 3. The path does not include diagonal movements.
     *
     * @param gameState         An array representing the gameState
     * @param catMovementString A string representing a cat movement.
     * @return True if the cat movement is valid, otherwise false
     */
    public static boolean isCatMovementValid(String[] gameState, String catMovementString) {
        GameRun gameRun = new GameRun(gameState);
        return gameRun.isMoveCatValidByString(catMovementString);
         // FIXME TASK 14
    }


    /**
     * Determine whether the game is over. The game ends if any of the following conditions are met:
     * <p>
     * Fire tile placement:
     * 1. If this placement action is not valid AND there is no other position this tile could be placed validly
     * (the game is lost).
     * 2. If placing this fire tile makes it impossible for any one cat to reach the raft (the game is lost).
     * <p>
     * Cat movement:
     * 1. If after moving this cat, all cats have safely reached the raft (the game is won).
     * <p>
     * Card placement:
     * 1. If after placing this card, there are no more fire tiles left in the bag (the game is lost).
     * </p>
     *
     * @param gameState An array of strings representing the game state
     * @param action    A string representing a fire tile placement, cat movement or card placement action.
     * @return True if the game is over (regardless of whether it is won or lost), otherwise False.
     */
    public static boolean isGameOver(String[] gameState, String action) {
        GameRun gameRun = new GameRun(gameState);
        GameStep gameStep = new GameStep(gameRun);

        char ch1 = action.charAt(1);
        char chEnd = action.charAt(action.length()-1);
        if(Character.isDigit(ch1)){
            if(action.length()>7){
                //moveCat  Y13041415Ad   or   Y13041415AdCe
                //set the initialization
                gameStep.setNeedDrawPathwayCard(false);
                gameStep.setNeedMoveCat(true);
                gameStep.setStateType(GameStep.StateType.PLAY_PATH_OR_MOVE_CAT);
                if(gameStep.judgeWin() || gameStep.judgeLoss()){
                    return true;
                }
                //run the action
                gameRun.moveCatByString(action);
                //update the action
                gameStep.updateStateType();
            }
            else {
                gameRun.createDrawnFireTileByType(action.charAt(0));
                //fire     b1203FN
                //set the initialization--then run the update first
                gameStep.setNeedDrawPathwayCard(false);
                gameStep.setNeedPlaceFireTile(true);
                gameStep.setStateType(GameStep.StateType.PLAY_FIRE);
                if(gameStep.judgeWin() || gameStep.judgeLoss()){
                    return true;
                }
                //run the action
                gameRun.placeFireTileByString(action);
                //update the action
                gameStep.updateStateType();
            }
        }
        else if(ch1>='a' && ch1<='y'){
            //pathCard Am0506N
            //set the initialization--then run the update first
            gameStep.setNeedDrawPathwayCard(false);
            gameStep.setNeedPlacePathwayCard(true);
            gameStep.setStateType(GameStep.StateType.PLAY_PATH_OR_MOVE_CAT);

            //run the action
            gameRun.placePathwayCardByString(action);
            //update the action
            gameStep.updateStateType();
        }

        if(gameStep.isWin() || gameStep.isLoss()){
            return true;
        }
        else {
            return false;
        }
        // FIXME TASK 15
    }
}
