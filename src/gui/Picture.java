package gui;

import javafx.scene.image.Image;

/**
 * Store all the relevant pictures in this game, just create at once and make the uniform way
 * to get these images. they include cat, squares and so on.
 */
public class Picture {
    //square and cat image
    public static Image[] CARD_PICTURES = {
            new Image("/gui/assets/blue.png"),
            new Image("/gui/assets/blueCat.png"),
            new Image("/gui/assets/green.png"),
            new Image("/gui/assets/greenCat.png"),
            new Image("/gui/assets/purple.png"),
            new Image("/gui/assets/purpleCat.png"),
            new Image("/gui/assets/red.png"),
            new Image("/gui/assets/redCat.png"),
            new Image("/gui/assets/yellow.png"),
            new Image("/gui/assets/yellowCat.png"),
            new Image("/gui/assets/fire.png"),
            new Image("/gui/assets/objective.png")
    };
    //rubbish bin for discarding the card
    public static Image BIN_PICTURE = new Image("/gui/assets/bin.png");
    //game icon for the program
    public static Image GAME_ICON = new Image("/gui/assets/gameIcon.png");
    //for the alert window that show win
    public static Image WIN_ICON = new Image("/gui/assets/win.png");
    //for the alert window that show loss
    public static Image LOSS_ICON = new Image("/gui/assets/loss.png");
    //the background of the game
    public static Image GAME_VIEW = new Image("/gui/assets/gameView.png");
    //image for the deckButton
    public static Image DECK_ICON[] = {
            new Image("/gui/assets/deckA.png"),
            new Image("/gui/assets/deckB.png"),
            new Image("/gui/assets/deckC.png"),
            new Image("/gui/assets/deckD.png")
    };


    /**
     * use the char of the square state to the corresponding image
     *
     * @param ch the state char of the square
     * @return the corresponding image of the state char
     */
    public static Image getPicture(char ch){
        Image image;
        switch (ch){
            case 'b' -> image = CARD_PICTURES[0];
            case 'B' -> image = CARD_PICTURES[1];
            case 'g' -> image = CARD_PICTURES[2];
            case 'G' -> image = CARD_PICTURES[3];
            case 'p' -> image = CARD_PICTURES[4];
            case 'P' -> image = CARD_PICTURES[5];
            case 'r' -> image = CARD_PICTURES[6];
            case 'R' -> image = CARD_PICTURES[7];
            case 'y' -> image = CARD_PICTURES[8];
            case 'Y' -> image = CARD_PICTURES[9];
            case 'f' -> image = CARD_PICTURES[10];
            case 'o','w','W' -> image = CARD_PICTURES[11];
            default -> image = null;
        }
        return image;
    }
}
