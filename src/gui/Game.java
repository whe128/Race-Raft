package gui;


import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import Board;
import Card;
import Cat;
import Deck;
import FireTile;
import FireTileBag;
import GameRun;
import GameStep;
import Hand;
import Location;
import Orientation;
import PathwayCard;
import Square;

/**
 * This class represents a "Race to the Raft" game implemented using JavaFX.
 * The game involves moving cat to the raft to win.
 */
public class Game extends Application {
    //java --module-path %PATH_TO_FX% --add-modules=javafx.controls,javafx.fxml,javafx.media -jar out/artifacts/game/game.jar
    // Root of every node groups
    private final Group root = new Group();

    //Constant values, such as size of window or grid
    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 650;

    public static final int GRID_CELL_WIDTH = 35;  // Grid cell width in pixels
    public static final int GRID_CELL_HEIGHT = 35; // Grid cell height in pixels

    public static final int ANIMATION_TIME_MS = 500;

    private GameRun gameRun;
    private GameStep gameStep;

    //Node groups that represent different part of game. All of them are children of Group root
    private final Group resetGroup = new Group();
    private BoardGroup boardGroup;
    private final Group handGroup = new Group();        //add cardGroup
    private final ArrayList<CardGroup> pathwayCardGroups = new ArrayList<>();   //store cardGroup
    private final Group deckGroup = new Group();        //add deck button
    private final Group fireTileBagGroup = new Group(); //add fireTileBagButton, fireTile cardGroup
    private CardGroup fireTileGroup;
    private final ArrayList<CatGroup> catGroups = new ArrayList<>();

    //Different nodes of the groups above
    private RadioButton difficultyButton;      //choose difficulty
    private RadioButton stateButton;           //choose stateString
    private ChoiceBox<Integer> difficultyChoice;
    private TextArea[] stateTextField;
    private final Button[] deckButtons = new Button[4];
    private Button drawFireTileButton;
    private Button fireTilePlaceHintButton;
    private Text fireTileHintText;
    //for display
    private ImageView rubbishBin;

    //For helper group
    private Group phaseGroup;
    private Text phaseText;
    private Group errorGroup;
    private Text hintText;
    private Text errorText;
    private Alert alertWindow;
    private ImageView background;
    private Button debugButton;   //enter F8 to show the button

    // FIXME TASK 11 Basic game
    // FIXME TASK 13 Fully working game

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method initialise and visualise the Decks on the Stage as a button.
     * Upon clicking each deck button, it draws pathway card from that deck and add to hand.
     */
    private void initializeDeck() {
        this.deckGroup.setLayoutX(WINDOW_WIDTH - 140);
        this.deckGroup.setLayoutY(WINDOW_HEIGHT - 420);
        this.deckGroup.setVisible(false);
        this.deckGroup.setOpacity(0);

        deckButtons[0] = new Button();
        deckButtons[1] = new Button();
        deckButtons[2] = new Button();
        deckButtons[3] = new Button();

        // Events handling upon clicking each the deck buttons
        deckButtons[0].setOnAction(event -> deckButtonClick(Deck.DeckType.A_CROSS));
        deckButtons[1].setOnAction(event -> deckButtonClick(Deck.DeckType.B_SQUARE));
        deckButtons[2].setOnAction(event -> deckButtonClick(Deck.DeckType.C_CIRCLE));
        deckButtons[3].setOnAction(event -> deckButtonClick(Deck.DeckType.D_TRIANGLE));

        Button button;
        int buttonWidth = 55;
        int buttonHeight = 55;
        int spaceDistance = 10;
        int Xpos = 0;
        int Ypos = 0;
        button = deckButtons[0];
        button.setStyle("-fx-background-image: url('/gui/assets/deckA.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;");

        button.setOnMouseEntered(e -> deckButtons[0].setOpacity(0.8));
        button.setOnMouseExited(e -> deckButtons[0].setOpacity(1));
        button.setOnMousePressed(e -> deckButtons[0].setOpacity(0.6));
        button.setOnMouseReleased(e -> deckButtons[0].setOpacity(0.8));
        button.setText("Deck A\n\n0");
        button.setFont(new Font("Tahoma", 12));
        button.setLayoutX(Xpos);
        button.setLayoutY(Ypos);
        button.setPrefWidth(buttonWidth);
        button.setPrefHeight(buttonHeight);

        Xpos += buttonWidth + spaceDistance;
        button = deckButtons[1];
        button.setStyle("-fx-background-image: url('/gui/assets/deckB.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;");

        button.setOnMouseEntered(e -> deckButtons[1].setOpacity(0.8));
        button.setOnMouseExited(e -> deckButtons[1].setOpacity(1));
        button.setOnMousePressed(e -> deckButtons[1].setOpacity(0.6));
        button.setOnMouseReleased(e -> deckButtons[1].setOpacity(0.8));
        button.setText("Deck B\n\n0");
        button.setFont(new Font("Tahoma", 12));
        button.setLayoutX(Xpos);
        button.setLayoutY(Ypos);
        button.setPrefWidth(buttonWidth);
        button.setPrefHeight(buttonHeight);

        Xpos = 0;
        Ypos += buttonHeight + spaceDistance;
        button = deckButtons[2];
        button.setStyle("-fx-background-image: url('/gui/assets/deckC.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;");

        button.setOnMouseEntered(e -> deckButtons[2].setOpacity(0.8));
        button.setOnMouseExited(e -> deckButtons[2].setOpacity(1));

        button.setOnMousePressed(e -> deckButtons[2].setOpacity(0.6));
        button.setOnMouseReleased(e -> deckButtons[2].setOpacity(0.8));
        button.setText("Deck C\n\n0");
        button.setFont(new Font("Tahoma", 12));
        button.setLayoutX(Xpos);
        button.setLayoutY(Ypos);
        button.setPrefWidth(buttonWidth);
        button.setPrefHeight(buttonHeight);

        Xpos += buttonWidth + spaceDistance;
        button = deckButtons[3];
        button.setStyle("-fx-background-image: url('/gui/assets/deckD.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;");

        button.setOnMouseEntered(e -> deckButtons[3].setOpacity(0.8));
        button.setOnMouseExited(e -> deckButtons[3].setOpacity(1));
        button.setOnMousePressed(e -> deckButtons[3].setOpacity(0.6));
        button.setOnMouseReleased(e -> deckButtons[3].setOpacity(0.8));
        button.setText("Deck D\n\n0");
        button.setFont(new Font("Tahoma", 12));
        button.setLayoutX(Xpos);
        button.setLayoutY(Ypos);
        button.setPrefWidth(buttonWidth);
        button.setPrefHeight(buttonHeight);

        deckGroup.getChildren().addAll(deckButtons);
        root.getChildren().add(deckGroup);
    }


    /**
     * This method initialise and visualise the handGroup on the root
     */
    private void initializeHand() {
        handGroup.setLayoutX(750);
        handGroup.setLayoutY(0);
        root.getChildren().add(handGroup);
    }

    /**
     * This method initialise and visualise the FireTileBag on the Stage.
     * Upon clicking fire tile bag button, on DRAW_FIRE phase, it draws random fire tile and visualise it on Scene.
     */
    private void initializeFireTileBag() {
        fireTileBagGroup.setLayoutX(770);
        fireTileBagGroup.setLayoutY(WINDOW_HEIGHT - 420);
        fireTileBagGroup.setVisible(false);
        fireTileBagGroup.setOpacity(0);
        root.getChildren().add(fireTileBagGroup);


        drawFireTileButton = new Button("FireTileBag\n    0");
        drawFireTileButton.setStyle("-fx-background-image: url('/gui/assets/fireTileBag.png');" +
                "-fx-background-size: stretch ;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;");

        drawFireTileButton.setOnMouseEntered(e -> drawFireTileButton.setOpacity(0.8));
        drawFireTileButton.setOnMouseExited(e -> drawFireTileButton.setOpacity(1));
        drawFireTileButton.setOnMousePressed(e -> drawFireTileButton.setOpacity(0.6));
        drawFireTileButton.setOnMouseReleased(e -> drawFireTileButton.setOpacity(0.8));
        drawFireTileButton.setLayoutX(90);
        drawFireTileButton.setLayoutY(0);

        drawFireTileButton.setFont(new Font("Tahoma", 12));
        fireTileBagGroup.getChildren().add(drawFireTileButton);

        //Events handling upon clicking the fire tile bag button
        drawFireTileButton.setOnAction(event -> fireTileBagButtonClick());
        fireTilePlaceHintButton = new Button("placeHint");
        fireTilePlaceHintButton.setFont(new Font("Tahoma", 12));
        fireTilePlaceHintButton.setVisible(false);
        fireTilePlaceHintButton.setLayoutX(0);
        fireTilePlaceHintButton.setLayoutY(10);
        fireTileBagGroup.getChildren().add(fireTilePlaceHintButton);
        fireTilePlaceHintButton.setOnAction(event -> fireTileHintButtonClick());

        fireTileHintText = new Text();
        fireTileHintText.setLayoutX(720);
        fireTileHintText.setLayoutY(WINDOW_HEIGHT - 400);
        root.getChildren().add(fireTileHintText);
    }

    /**
     * This method initialise and visualise the Hint on the Stage, which gives the user clue about what to do.
     */
    private void initializeHelper() {

        // Create a place for representing current phase

        phaseGroup = new Group();
        phaseGroup.setLayoutX(800);
        phaseGroup.setLayoutY(450);

        Label p_Label = new Label("Current Phase:");
        p_Label.setFont(new Font(15));
        p_Label.setStyle("-fx-font-weight: bold");

        phaseText = new Text("NONE");
        phaseText.setFont(new Font("Tahoma", 15));
        phaseText.setFill(Color.GREEN);
        phaseText.setWrappingWidth(105);
        phaseText.setLayoutY(30);
        phaseText.setLayoutX(5);

        phaseGroup.getChildren().add(p_Label);
        phaseGroup.getChildren().add(phaseText);
        phaseGroup.setVisible(false);
        phaseGroup.setOpacity(0);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setColor(Color.GRAY);
        phaseGroup.setEffect(dropShadow);


        root.getChildren().add(phaseGroup);

        // Create a place for representing hint
        Group hint = new Group();
        hint.setLayoutX(350);
        hint.setLayoutY(540);

        Rectangle hintBox = new Rectangle();
        hintBox.setLayoutX(55);
        hintBox.setWidth(285);
        hintBox.setHeight(70);
        hintBox.setFill(Color.PINK);
        hintBox.setArcWidth(15);
        hintBox.setArcHeight(15);
        hintBox.setOpacity(0.4);

        ImageView lightBulb = new ImageView(new Image("/gui/assets/lightbulb.png"));
        lightBulb.setLayoutX(0);
        lightBulb.setLayoutY(0);
        lightBulb.setFitHeight(70);
        lightBulb.setFitWidth(50);

        Label h_Label = new Label("Hint:");
        h_Label.setFont(new Font(15));
        h_Label.setLayoutX(55);
        h_Label.setLayoutY(25);
        h_Label.setStyle("-fx-font-weight: bold");

        hintText = new Text("Please Click START Button to Run the Game, Choose Difficulty or State String.");
        hintText.setOpacity(0);
        hintText.setFont(new Font("Tahoma", 15));
        hintText.setFill(Color.BLUE);
        hintText.setWrappingWidth(235);
        hintText.setLayoutY(20);
        hintText.setLayoutX(100);
        hintText.setEffect(dropShadow);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(4*ANIMATION_TIME_MS), phaseText);
        fadeTransition.setFromValue(phaseText.getOpacity());
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        fadeTransition = new FadeTransition(Duration.millis(4*ANIMATION_TIME_MS), hintText);
        fadeTransition.setFromValue(hintText.getOpacity());
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        Reflection reflection = new Reflection();
        reflection.setFraction(0.3);
        hint.setEffect(reflection);

        hint.getChildren().add(hintBox);
        hint.getChildren().add(h_Label);
        hint.getChildren().add(hintText);
        hint.getChildren().add(lightBulb);
        root.getChildren().add(hint);

        // Create a place for representing error
        errorGroup = new Group();
        errorGroup.setLayoutX(700);
        errorGroup.setLayoutY(542);

        Rectangle errorBox = new Rectangle();
        errorBox.setWidth(195);
        errorBox.setHeight(80);
        errorBox.setStroke(Color.RED);
        errorBox.setFill(null);
        errorBox.setArcWidth(15);
        errorBox.setArcHeight(15);


        Label e_Label = new Label("Error");
        e_Label.setFont(new Font(15));
        e_Label.setStyle("-fx-font-weight: bold");
        e_Label.setLayoutX(80);


        errorText = new Text("");
        errorText.setFont(new Font("Tahoma", 13));
        errorText.setFill(Color.RED);
        errorText.setWrappingWidth(180);
        errorText.setLayoutY(35);
        errorText.setLayoutX(10);

        errorGroup.getChildren().add(errorBox);
        errorGroup.getChildren().add(e_Label);
        errorGroup.getChildren().add(errorText);
        errorGroup.setVisible(false);
        root.getChildren().add(errorGroup);

    }

    /**
     * This method initialise and visualise the Rubbish on the Stage,
     * which is where the Pathway Cards get discarded into for moving cat.
     */
    private void initializeDiscardBin() {
        rubbishBin = new ImageView();
        rubbishBin.setImage(Picture.BIN_PICTURE);
        rubbishBin.setLayoutX(700);
        rubbishBin.setLayoutY(400);
        rubbishBin.setFitHeight(90);
        rubbishBin.setFitWidth(90);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setColor(Color.GRAY);
        rubbishBin.setEffect(dropShadow);
        Reflection reflection = new Reflection();
        reflection.setFraction(0.5);
        reflection.setTopOffset(3);
        dropShadow.setInput(reflection);

        root.getChildren().add(rubbishBin);
        rubbishBin.toBack();
        rubbishBin.setVisible(false);
        rubbishBin.setOpacity(0);
    }


    /**
     * Initializes an alert window to display game end notifications (win or loss).
     */
    private void initializeAlert() {
        alertWindow = new Alert(Alert.AlertType.INFORMATION);
        alertWindow.setHeaderText("NONE");
        alertWindow.setContentText("NONE");
        alertWindow.setTitle("Game End");
        Button button = (Button) alertWindow.getDialogPane().lookupButton(alertWindow.getButtonTypes().get(0));
        button.setText("OK");
        button.setFont(new Font(15));
    }

    /**
     * Initializes a text with the group name.
     */
    private void initializeGroupInformation() {
        Text text = new Text("Group: tue08b1");
        text.setFont(new Font(20));
        text.setFill(Color.BLUE);
        text.setLayoutX(400);
        text.setLayoutY(WINDOW_HEIGHT - 15);
        root.getChildren().add(text);
    }


    /**
     * Initializes a button that opens the rule book when clicked.
     *
     * @throws RuntimeException if an error occurs while attempting to open the rule book URL.
     */
    private void initializeRuleBook() {
        // Create Button for Rule Book
        Button rule = new Button("Rule Book");
        rule.setFont(new Font("Tahoma", 15));
        rule.setLayoutX(580);
        rule.setLayoutY(WINDOW_HEIGHT - 35);

        // Event upon clicking the button
        rule.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://thecityofkings.com/wp-content/uploads/2023/06/Race-to-the-raft-v1-compressed.pdf"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        root.getChildren().add(rule);
    }

    /**
     * Initializes the background of the Game window.
     */
    private void initializeBackGround(Scene scene){
        scene.setFill(new Color(0.98039216f, 0.92156863f, 0.84313726f, 0.2));
        background  = new ImageView(Picture.GAME_VIEW);
        background.setVisible(true);
        background.setOpacity(1);
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        root.getChildren().add(0,background);
        background.toBack();
    }

    /**
     * Initializes the debug button to display game debug information in the terminal.
     */
    private void showDebug() {
        if (debugButton == null) {
            debugButton = new Button("debug");
            debugButton.setFont(new Font("Tahoma", 20));
            debugButton.setOnAction(eventButton -> {
                if (gameStep == null) {
                    return;
                }
                System.out.println(gameRun);
                System.out.println(gameStep);
            });
            debugButton.setLayoutX(660);
            debugButton.setLayoutY(0);

            debugButton.toFront();

            root.getChildren().add(debugButton);
        } else {
            if (root.getChildren().contains(debugButton)) {
                root.getChildren().remove(debugButton);
            } else {
                root.getChildren().add(debugButton);
            }
        }
    }

    /**
     * This method initialise and visualise the Reset on the Stage.
     * At first, the Reset is referred to as START and upon clicking RESET button, it will start a new game.
     */
    private void initializeReset() {
        root.getChildren().add(resetGroup);
        // reset button
        Button button = new Button("START");

        //Event handling upon clicking reset button
        button.setOnAction(e -> resetButtonClick(button));

        button.setFont(new Font("Tahoma", 12));
        button.setLayoutX(0);
        button.setLayoutY(20);

        ToggleGroup modeGroup = new ToggleGroup();
        //can only choose one button
        difficultyButton = new RadioButton("difficulty");
        difficultyButton.setLayoutX(60);
        difficultyButton.setLayoutY(0);
        difficultyButton.setToggleGroup(modeGroup);
        difficultyButton.setSelected(true);

        stateButton = new RadioButton("stateString");
        stateButton.setLayoutX(60);
        stateButton.setLayoutY(20);
        stateButton.setToggleGroup(modeGroup);

        //choose for difficulty
        difficultyChoice = new ChoiceBox<>(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5));
        difficultyChoice.setValue(0);
        difficultyChoice.setLayoutX(140);
        difficultyChoice.setLayoutY(0);


        //input text board,deck,hand,bag
        stateTextField = new TextArea[5];
        TextArea textArea;

        int yPos = 0;
        int height;
        //input state
        yPos += 40;
        height = 90;
        stateTextField[0] = new TextArea("""
                fffffffffrrfffffff
                fffffffffrRfffffff
                fffffffffrrfffffff
                fffgffpbrrgbyybyrg
                fffggfgrrpypbgrggy
                fffggggrrygygpgbgp
                ffffffgggbprbgybpb
                ffffffbfGrybpyrgyg
                fffffffffbgygppbrr
                ffffffbfrbyrprgbbp
                ffffffrypbpybgpryg
                ffffffyggrggrbgyby
                fffffyypypryybpgyp
                ffffYypbbgbprygrow
                fffyyygyrygbygybww""");
        textArea = stateTextField[0];
        textArea.setLayoutX(60);
        textArea.setLayoutY(yPos);
        textArea.setPrefWidth(130);
        textArea.setPrefHeight(height);

        //input text deck
        yPos += (height + 2);
        height = 10;
        stateTextField[1] = new TextArea("AabcdefghijklmnopqrstuvwxyBbcdeghijkmnopqrsuvwxyCabcdeghijklmnopqrstuwxyDabcdefghijklmnopqrstuvwxy");
        textArea = stateTextField[1];
        textArea.setLayoutX(60);
        textArea.setLayoutY(yPos);
        textArea.setPrefWidth(130);
        textArea.setPrefHeight(height);

        //input text hand
        yPos += (height + 29);
        stateTextField[2] = new TextArea("ABalCfvD");
        textArea = stateTextField[2];
        textArea.setLayoutX(60);
        textArea.setLayoutY(yPos);
        textArea.setPrefWidth(130);
        textArea.setPrefHeight(height);

        //input text exhausted cat
        yPos += (height + 29);
        stateTextField[3] = new TextArea("G0708");
        textArea = stateTextField[3];
        textArea.setLayoutX(60);
        textArea.setLayoutY(yPos);
        textArea.setPrefWidth(130);
        textArea.setPrefHeight(height);

        //input text fireBag
        yPos += (height + 29);
        stateTextField[4] = new TextArea("abcdefghijklmnopqrstuvwyzABCDE");
        textArea = stateTextField[4];
        textArea.setLayoutX(60);
        textArea.setLayoutY(yPos);
        textArea.setPrefWidth(130);
        textArea.setPrefHeight(height);

        Label[] labels = new Label[5];
        labels[0] = new Label("Board");
        labels[0].setLayoutX(20);
        labels[0].setLayoutY(60);

        labels[1] = new Label(" Deck");
        labels[1].setLayoutX(20);
        labels[1].setLayoutY(135);

        labels[2] = new Label(" Hand");
        labels[2].setLayoutX(20);
        labels[2].setLayoutY(175);

        labels[3] = new Label("Exhausted\n         Cat");
        labels[3].setLayoutX(0);
        labels[3].setLayoutY(213);

        labels[4] = new Label("    Bag");
        labels[4].setLayoutX(20);
        labels[4].setLayoutY(253);

        //add these groups to the reset group
        resetGroup.getChildren().addAll(button, difficultyButton, stateButton, difficultyChoice);
        resetGroup.getChildren().addAll(stateTextField);
        resetGroup.getChildren().addAll(labels);
        resetGroup.setLayoutX(WINDOW_WIDTH - 200);
        resetGroup.setLayoutY(WINDOW_HEIGHT - 290);
    }

    /**
     * Initialises keyboard event handlers for rotating, flipping, and discarding cards using specific key presses.
     *  - R for rotate
     *  - T for flip
     *  - D for discard
     */
    private void initializeRotateFlipDeleteCard() {
        root.setOnKeyReleased(event -> {
            if (gameStep == null) {
                return;
            }
            //code R - for rotate
            if (event.getCode() == KeyCode.R) {
                rotatePathwayCardGroupByKeyBoard();
                rotateFireTileGroupByKeyBoard();
            }
            //code T - for flip
            if (event.getCode() == KeyCode.T) {
                flipFireTileGroupByKeyBoard();
            }
            //code D - for discard
            if (event.getCode() == KeyCode.D) {
                discardPathwayCardByKeyBoard();
            }
        });
    }

    /**
     * Initializes and starts the game based on selected settings (difficulty or input strings)
     */
    private void startGame() {
        gameRun = null;
        gameStep = null;

        //create the game through two ways, determine which button we choose
        if (this.difficultyButton.isSelected()) {
            gameRun = new GameRun(difficultyChoice.getValue());
        } else if (this.stateButton.isSelected()) {
            String[] strings = new String[5];
            boolean isStringValid = true;

            for (TextArea textArea : stateTextField) {
                if (textArea.getText() == null) {
                    errorText.setText("Can not Initialize, not All Text Areas Have String.");
                    errorGroup.setVisible(true);
                    isStringValid = false;
                }
            }

            if (isStringValid) {
                strings[0] = stateTextField[0].getText();
                if (!Board.isBoardStringWellFormed(strings[0])) {
                    errorText.setText("Can not Initialize, Board String is not Well Formed.");
                    errorGroup.setVisible(true);
                    isStringValid = false;
                }

                strings[1] = stateTextField[1].getText();
                if (!Deck.isDeckStringsWellFormed(strings[1])) {
                    errorText.setText("Can not Initialize, Deck String is not Well Formed.");
                    errorGroup.setVisible(true);
                    isStringValid = false;
                }

                strings[2] = stateTextField[2].getText();
                if (!Hand.isHandStringsWellFormed(strings[2])) {
                    errorText.setText("Can not Initialize, Hand String is not Well Formed.");
                    errorGroup.setVisible(true);
                    isStringValid = false;
                }

                strings[3] = stateTextField[3].getText();
                if (!Cat.isExhaustedStringsWellFormed(strings[3], strings[0])) {
                    errorText.setText("Can not Initialize, exhausted cat String is not Well Formed.");
                    errorGroup.setVisible(true);
                    isStringValid = false;
                }

                strings[4] = stateTextField[4].getText();
                if (!FireTileBag.isBagStringsWellFormed(strings[4])) {
                    errorText.setText("Can not Initialize, FireTileBag String is not Well Formed.");
                    errorGroup.setVisible(true);
                    isStringValid = false;
                }
            }

            if (isStringValid) {
                gameRun = new GameRun(strings);
            } else {
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                fadeTransition.setFromValue(hintText.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();
                //animation to show
                fadeTransition.setOnFinished(event -> {
                    hintText.setText("change the value and click Reset to restart the game.");
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                    f.setFromValue(hintText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });
            }
            errorGroup.setVisible(true);
        }

        //this is means the game start
        if (gameRun != null) {
            gameStep = new GameStep(gameRun);
            //this is for the initialization--from reset or NONE to draw or play
            FadeTransition fadeTransition;
            errorText.setText("");
            errorGroup.setVisible(false);
            gameStep.updateStateType();
            judgeWinOrLoss();

            //show phase text by animation
            fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
            fadeTransition.setFromValue(phaseText.getOpacity());
            fadeTransition.setToValue(0);
            fadeTransition.play();
            fadeTransition.setOnFinished(event -> {
                phaseText.setText(gameStep.getStateType().toString());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                f.setFromValue(phaseText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });

            //show hint text by animation
            fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
            fadeTransition.setFromValue(hintText.getOpacity());
            fadeTransition.setToValue(0);
            fadeTransition.play();
            fadeTransition.setOnFinished(event -> {
                hintText.setText(gameStep.getHint());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                f.setFromValue(hintText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });

            //show rubbishBin by animation
            if(!rubbishBin.isVisible()) {
                rubbishBin.setVisible(true);
                fadeTransition =new FadeTransition (Duration.millis(ANIMATION_TIME_MS), rubbishBin);
                fadeTransition.setFromValue(rubbishBin.getOpacity());
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
            }

            //show phaseGroup by animation
            if(!phaseGroup.isVisible()) {
                phaseGroup.setVisible(true);
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseGroup);
                fadeTransition.setFromValue(phaseGroup.getOpacity());
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
            }

            //show fireTileBag by animation
            if(!fireTileBagGroup.isVisible()) {
                fireTileBagGroup.setVisible(true);
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), fireTileBagGroup);
                fadeTransition.setFromValue(fireTileBagGroup.getOpacity());
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
            }

            //show deckGroup by animation
            if(!deckGroup.isVisible()) {
                deckGroup.setVisible(true);
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), deckGroup);
                fadeTransition.setFromValue(deckGroup.getOpacity());
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
            }

            //transparent the background by animation
            if(background.getOpacity()>0.95) {
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), background);
                fadeTransition.setFromValue(background.getOpacity());
                fadeTransition.setToValue(0.2);
                fadeTransition.play();
            }
        }
        else {
            FadeTransition fadeTransition;
            //disappear the rubbish bin by animation
            if (rubbishBin.isVisible()) {
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), rubbishBin);
                fadeTransition.setFromValue(rubbishBin.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> rubbishBin.setVisible(false));
            }

            //disappear phaseGroup by animation
            if (phaseGroup.isVisible()){
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseGroup);
                fadeTransition.setFromValue(phaseGroup.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> phaseGroup.setVisible(false));
            }

            //disappear fireTileBagGroup by animation
            if(fireTileBagGroup.isVisible()) {
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), fireTileBagGroup);
                fadeTransition.setFromValue(fireTileBagGroup.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> fireTileBagGroup.setVisible(false));

            }

            //disappear deckGroup by animation
            if(deckGroup.isVisible()) {
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), deckGroup);
                fadeTransition.setFromValue(deckGroup.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> deckGroup.setVisible(false));
            }

            //show background by animation
            if(background.getOpacity()<0.4) {
                fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), background);
                fadeTransition.setFromValue(background.getOpacity());
                fadeTransition.setToValue(1);
                fadeTransition.play();
            }

        }

        //according to the gameRun - to update the following element
        updateBoardGroup();
        updateDeckButton();
        updateHand();
        updateFireTileBag();
        clearFireTile();
        makeCatOnBoard();
    }

    // Helper Functions
    /**
     * Updates the game board visuals by clearing the existing board and creating a new board based on the game state.
     */
    private void updateBoardGroup() {
        //first clear the old boardGroup
        if (boardGroup != null) {
            //give the other name, separate it with the class variables, avoid the confusion operation when using same.
            //we can think this group as an older group that we just ust another variable to operation it, and release
            //after the animation
            BoardGroup needClearBoardGroup = boardGroup;
            boardGroup = null;

            //transparent background by animation
            if(needClearBoardGroup.isVisible()) {
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), needClearBoardGroup);
                fadeTransition.setFromValue(needClearBoardGroup.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();
                fadeTransition.setOnFinished(event -> {
                    needClearBoardGroup.setVisible(false);
                    needClearBoardGroup.clearImage();
                    needClearBoardGroup.getChildren().clear();
                    root.getChildren().remove(needClearBoardGroup);
                });
            }
        }

        //create the new boardGroup
        if (gameRun != null) {
            boardGroup = new BoardGroup(gameRun.getBoard());
            boardGroup.setLayoutX(20);
            boardGroup.setLayoutY(10);
            root.getChildren().add(boardGroup);
        }
    }

    /**
     * Updates the deck buttons to display the updated number of cards in each deck
     */
    private void updateDeckButton() {
        if (gameRun != null) {
            deckButtons[0].setText("Deck A\n\n" + gameRun.getDecks()[0].getCardNum());
            deckButtons[1].setText("Deck B\n\n" + gameRun.getDecks()[1].getCardNum());
            deckButtons[2].setText("Deck C\n\n" + gameRun.getDecks()[2].getCardNum());
            deckButtons[3].setText("Deck D\n\n" + gameRun.getDecks()[3].getCardNum());
        } else {
            deckButtons[0].setText("Deck A\n\n0");
            deckButtons[1].setText("Deck B\n\n0");
            deckButtons[2].setText("Deck C\n\n0");
            deckButtons[3].setText("Deck D\n\n0");
        }
    }

    /**
     * Updates the player's hand by clearing existing pathway card groups and adding new cards to the hand.
     */
    private void updateHand(){
        if(!pathwayCardGroups.isEmpty()) {
            //use another groups to store the older group.
            //Then, they are no need for operation
            //putting them in another list, we can avoid the confused operation.
            //for these useless card group, we just let them do the animation
            //then release, remove
            ArrayList<CardGroup> cardGroups = new ArrayList<>(pathwayCardGroups);
            pathwayCardGroups.clear();

            //first clear the old cardGroup
            for (Group group : cardGroups) {
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), group);
                fadeTransition.setFromValue(group.getOpacity());
                fadeTransition.setToValue(0);
                fadeTransition.play();

                //after animation stop, clear the old cardGroup
                fadeTransition.setOnFinished(event -> {
                    group.getChildren().clear();
                    handGroup.getChildren().remove(group);
                });
            }
        }

        if (gameRun != null) {
            for (int i = 0; i < gameRun.getHand().getCardNum(); i++) {
                //create cardGroup to show the card on hand
                addCardToHand();
            }
        }
    }

    /**
     * Clear up the existing Fire Tiles
     */
    private void clearFireTile() {
        if (fireTileGroup != null) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), fireTileGroup);
            fadeTransition.setFromValue(fireTileGroup.getOpacity());
            fadeTransition.setToValue(0);
            fadeTransition.play();

            //after the stop of animation, release the fireTile group
            fadeTransition.setOnFinished(event -> {
                fireTileGroup.getChildren().clear();
                fireTileBagGroup.getChildren().remove(fireTileGroup);
                fireTileGroup = null;
            });
        }
    }

    /**
     * Updates the fire tile bay to display the updated number of cards in bag.
     * can clearly look the number to know how many fire tiles on the fireTile bag.
     */
    private void updateFireTileBag() {
        if (gameRun != null) {
            drawFireTileButton.setText("FireTileBag\n  " + gameRun.getFireTileBag().getTileNum());
        } else {
            drawFireTileButton.setText("FireTileBag\n   0");
        }

        if (gameStep == null) {
            fireTilePlaceHintButton.setVisible(false);
        } else {
            fireTilePlaceHintButton.setVisible(gameStep.getStateType() == GameStep.StateType.DRAW_FIRE);
        }
        fireTileHintText.setText("");
    }

    /**
     * Create a cats on the board according to updated game state
     * it should be added to the boardGroup
     */
    private void makeCatOnBoard() {
        if (gameRun == null) {
            return;
        }

        CatGroup catGroup;
        for (Cat cat : gameRun.getCats()) {
            catGroup = new CatGroup(cat);
            enableDragCat(catGroup);
            catGroup.setLayoutX(cat.getPlacedLocation().getColumn() * GRID_CELL_WIDTH);
            catGroup.setLayoutY(cat.getPlacedLocation().getRow() * GRID_CELL_HEIGHT);
            catGroup.setLastPos(catGroup.getLayoutX(), catGroup.getLayoutY());
            boardGroup.getChildren().add(catGroup);
            //if we start with no card in hand, then we should recover all cats, and draw card
            //event though the initialized state shows that the cat is exhausted
            if (gameStep.getStateType() == GameStep.StateType.DRAW_PATH) {
                cat.recoverExhausted();
            }
            //after creating, first show the state
            catGroup.showState();
            catGroup.toFront();
            catGroups.add(catGroup);
        }
    }

    /**
     * Adds a pathway card to the player's hand visual and manages its visibility based on game state.
     * If the maximum number of cards (6) has been drawn or the game state allows showing cards,
     * the newly added card's visual is displayed. Otherwise, the card's visual remains hidden.
     */
    private void addCardToHand() {
        if (gameRun == null) {
            return;
        }

        int showedCardNum = pathwayCardGroups.size();
        //max draw card num is 6
        if (showedCardNum >= 6) {
            return;
        }
        PathwayCard card = gameRun.getHand().getCardsList().get(showedCardNum);  // Get the card

        //find a place not overlap
        int cardSpaceDistance = 10;
        double newPosX = 0;
        double newPosY = 0;
        if (showedCardNum > 0) {
            for (int i = 0; i < 6; i++) {
                newPosX = (i % 3 == 0) ? 0 : (i % 3) * (GRID_CELL_WIDTH * 3 + cardSpaceDistance);
                newPosY = (i / 3 == 0) ? 0 : (i / 3) * (GRID_CELL_HEIGHT * 3 + cardSpaceDistance);

                boolean noOverlap = true;
                for (int j = 0; j < showedCardNum; j++) {
                    double posX = pathwayCardGroups.get(j).getLayoutX();
                    double posY = pathwayCardGroups.get(j).getLayoutY();
                    //if exist one, means the pos is not valid
                    if (newPosX == posX && newPosY == posY) {
                        noOverlap = false;
                        break;
                    }
                }
                if (noOverlap) {
                    break;
                }
            }
        }

        // Create a CardGroup with the card
        CardGroup cardGroup = new CardGroup(card);
        pathwayCardGroups.add(cardGroup);
        handGroup.getChildren().add(cardGroup);
        cardGroup.hideCard();
        //show card until draw 6 cards
        //must in the draw state, we hide the cards first
        if (gameRun.getHand().getCardNum() >= 6 || gameStep.getStateType() != GameStep.StateType.DRAW_PATH) {
            for (CardGroup group : pathwayCardGroups) {
                group.showCard();
                enableDragPathwayCard(group);
            }
        }
        //when card num all in hand

        cardGroup.setLayoutX(newPosX);
        cardGroup.setLayoutY(newPosY);
        cardGroup.setOriginalPos(newPosX, newPosY);
    }


    /**
     * Discards a pathway card from the player's hand, updating game state and visuals accordingly.
     */
    private void discardCard(CardGroup cardGroup) {
        if(gameRun.getNeedDisCardNum()<=0){
            errorText.setText("Need Discard Num is 0, do not need discard card.");
            errorGroup.setVisible(true);
            return;
        }
        errorText.setText("");
        errorGroup.setVisible(false);

        //do the true action
        pathwayCardGroups.remove(cardGroup);
        gameRun.disCardPathwayCard((PathwayCard) cardGroup.getCard());

        //disappear the discard cardGroup by animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), cardGroup);
        fadeTransition.setFromValue(cardGroup.getOpacity());
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> {
            //after finish the animation
            //in it just clean the image
            cardGroup.getChildren().clear();
            handGroup.getChildren().remove(cardGroup);
        });

        //update for the action, should follow directly follow the action
        gameStep.updateStateType();
        judgeWinOrLoss();
        //disappear the old phaseText, show the new phaseText by animation
        if (gameStep.isStateChanged()) {
            FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
            fade.setFromValue(phaseText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                //after finish, then show new text
                phaseText.setText(gameStep.getStateType().toString());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                f.setFromValue(phaseText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });

            //disappear the old hintText, show the new hintText by animation
            fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
            fade.setFromValue(hintText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                //after finish, then show new
                hintText.setText(gameStep.getHint());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                f.setFromValue(hintText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });

            //this means new round turn, and should recover all cats
            if (gameStep.getStateType() == GameStep.StateType.DRAW_PATH) {
                for (CatGroup group : catGroups) {
                    Cat cat = group.getCat();
                    cat.recoverExhausted();
                    group.showState();
                }
            }
        }
        else if(gameStep.getStateType() == GameStep.StateType.DISCARD_PATH){
            FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
            fade.setFromValue(phaseText.getOpacity());
            fade.setToValue(0);
            fade.play();

            //show the phase text
            fade.setOnFinished(e -> {
                phaseText.setText("Discard " + gameRun.getNeedDisCardNum() + ((gameRun.getNeedDisCardNum()>1)?" cards":" card"));
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                f.setFromValue(phaseText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });

            //for the change of number of discard card,first disappear the old, then show the new
            fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
            fade.setFromValue(hintText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                hintText.setText(gameStep.getHint());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                f.setFromValue(hintText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });
        }
    }


    /**
     * Initialise the fire tile that is drawn from the bag
     * if the game is running, no fire tile is already visualised, and there is fire tile drawn at current state.
     */
    private void addFireTile() {
        if (gameRun == null) {
            return;
        }
        if (fireTileGroup != null) {
            return;
        }
        if (gameRun.getDrawnFireTile() == null) {
            return;
        }

        fireTileGroup = new CardGroup(gameRun.getDrawnFireTile());
        enableDragFireTile(fireTileGroup);
        fireTileGroup.showCard();

        fireTileBagGroup.getChildren().add(fireTileGroup);
        fireTileGroup.setLayoutX(0);
        fireTileGroup.setLayoutY(45);
        fireTileGroup.setOriginalPos(0, 45);
    }

    /**
     * Judges the game's win or loss condition and displays an alert window accordingly.
     */
    private void judgeWinOrLoss() {
        if (gameStep.isWin()) {
            ImageView imageView = new ImageView(Picture.WIN_ICON);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            alertWindow.setHeaderText("  WIN!");
            alertWindow.setContentText(gameStep.getEndString());
            alertWindow.setGraphic(imageView);
            alertWindow.show();
            return;
        }

        if (gameStep.isLoss()) {
            ImageView imageView = new ImageView(Picture.LOSS_ICON);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            alertWindow.setHeaderText("  LOSS!");
            alertWindow.setContentText(gameStep.getEndString());
            alertWindow.setGraphic(imageView);
            alertWindow.show();
        }
    }

    /**
     * Resets the game state when the reset button is clicked and updates the button text if the Text is "START".
     */
    private void resetButtonClick(Button button){
        startGame();
        if (button.getText().equals("START")) {
            button.setText("RESET");
        }
    }

    /**
     * Handles the button click event for drawing pathway cards from a specified deck type.
     *
     * @param deckType The type of deck from which pathway cards are to be drawn
     */
    private void deckButtonClick(Deck.DeckType deckType) {
        if (gameRun == null) {
            errorText.setText("Not start the game, please start game first.");
            errorGroup.setVisible(true);
            return;
        }
        if (gameStep.getStateType() != GameStep.StateType.DRAW_PATH) {
            errorText.setText("Cannot draw Pathway Card at Current Phase");
            errorGroup.setVisible(true);
            return;
        }
        if (!gameRun.isDrawPathwayCardsValid(gameRun.getDecks()[deckType.toChar() - 'A'], 1)) {
            errorText.setText("The Deck A is Empty, Draw from other Decks that are not Empty");
            errorGroup.setVisible(true);
            return;
        }

        int buttonIndex = deckType.toChar()-'A';
        //actually do, then clear the error
        //create the card group
        errorText.setText("");
        errorGroup.setVisible(false);
        gameRun.drawPathwayCards(gameRun.getDecks()[buttonIndex], 1);
        deckButtons[buttonIndex].setText("Deck "+ deckType.toChar() +"\n\n" + gameRun.getDecks()[buttonIndex].getCardNum());
        addCardToHand();

        //update for the action, should follow directly follow the action
        gameStep.updateStateType();
        judgeWinOrLoss();

        if (gameStep.isStateChanged()) {
            //disappear the old text, then show the new text by animation
            FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
            fade.setFromValue(phaseText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                //after finish the animation show the new text by animation
                phaseText.setText(gameStep.getStateType().toString());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                f.setFromValue(phaseText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });
            //disappear the old text, then show the new text by animation
            fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
            fade.setFromValue(hintText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                //after finish the animation show the new text by animation
                hintText.setText(gameStep.getHint());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                f.setFromValue(hintText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });
        }
    }

    /**
     * Handles the button click event for drawing a fire tile from the fire tile bag.
     */
    private void fireTileBagButtonClick() {
        if (gameRun == null) {
            errorText.setText("Game has not started, please start the game first.");
            errorGroup.setVisible(true);
            return;
        }

        if (gameStep.getStateType() != GameStep.StateType.DRAW_FIRE) {
            errorText.setText("Cannot Draw Fire Tile at Current Phase");
            errorGroup.setVisible(true);
            return;
        }
        if (gameRun.getFireTileBag().isEmpty()) {
            errorText.setText("Fire Tile Bag is Empty");
            errorGroup.setVisible(true);
            return;
        }
        //actually do, then clear the error
        //draw the fireTile and create the cardGroup show on the scene
        errorText.setText("");
        errorGroup.setVisible(false);

        gameRun.drawFileTile();
        updateFireTileBag();
        addFireTile();
        //update for the action, should follow directly follow the action
        gameStep.updateStateType();
        judgeWinOrLoss();
        if (gameStep.isStateChanged()) {
            //disappear the old text, show the new text by animation
            FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
            fade.setFromValue(phaseText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                //after finishing the animation, show the new text
                phaseText.setText(gameStep.getStateType().toString());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                f.setFromValue(phaseText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });

            //disappear the old text, show the new text by animation
            fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
            fade.setFromValue(hintText.getOpacity());
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(e -> {
                //after finishing the animation, show the new text
                hintText.setText(gameStep.getHint());
                FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                f.setFromValue(hintText.getOpacity());
                f.setToValue(1.0);
                f.play();
            });
        }
    }

    /**
     * Handles the button click event for toggling fire tile placement hints on the game board.
     * can hint or remove the hint
     */
    void fireTileHintButtonClick() {
        if (gameRun.getDrawnFireTile() == null) {
            errorText.setText("Not start the game, please start game first.");
            return;
        }

        //give the hint for the placement of fireTile
        if (boardGroup.isHinted()) {
            fireTilePlaceHintButton.setText("placeHint");
            fireTileHintText.setText("");
            boardGroup.recoverShow();
        } else {
            //remove the hint for the placement of fireTile
            fireTilePlaceHintButton.setText("RemoveHint");
            Object[] fireTilePlaceInformation = gameRun.findValidPlaceForFireTile(gameRun.getDrawnFireTile(), true);
            if (fireTilePlaceInformation == null) {
                fireTileHintText.setText("no valid\nplace");
                return;
            }

            Location placedLocation = (Location) fireTilePlaceInformation[0];
            Orientation orientation = (Orientation) fireTilePlaceInformation[1];
            boolean isFlipped = (boolean) fireTilePlaceInformation[2];
            Square[][] squares = (Square[][]) fireTilePlaceInformation[3];
            //remove the hint
            fireTileHintText.setText("  X, Y\n[ " + placedLocation.getColumn() + ", " + placedLocation.getRow() + " ]\n" + orientation + "\nFlip: " + (isFlipped ? "T" : "F"));
            boardGroup.hideSquaresForHint(squares, placedLocation);
        }
    }

    /**
     * Drag Event Functions
     **/

    /**
     * Enables event handling for pathway cards in the hand, allowing them to be interacted with using mouse actions.
     * - Mouse Press: Initiates the dragging of the pathway card.
     * - Mouse Drag: Moves the pathway card while dragging.
     * - Mouse Release: placing the pathway card or discarding the pathway card
     * @param cardGroup representing the pathway card to enable dragging for
     */
    private void enableDragPathwayCard(CardGroup cardGroup) {
        cardGroup.setCursor(Cursor.HAND);
        cardGroup.setOnMousePressed(event -> pathwayCardMousePress(cardGroup,event));
        cardGroup.setOnMouseDragged(event -> pathwayCardMouseDrag(cardGroup,event));
        cardGroup.setOnMouseReleased(event -> pathwayCardMouseRelease(cardGroup,event));
    }

    /**
     * Handles the mouse press event on a pathway card in the hand.
     * Can only initiate if the Phase is at DISCARD_PATH or PLAY_PATH_OR_MOVE_CAT
     *
     * @param cardGroup representing the pathway card being interacted with
     * @param event     MouseEvent that triggered the mouse press
     */
    private void pathwayCardMousePress(CardGroup cardGroup, MouseEvent event){
        if (gameStep.getStateType() != GameStep.StateType.DISCARD_PATH && gameStep.getStateType() != GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            errorText.setText("Cannot move Pathway Card at Current State");
            errorGroup.setVisible(true);
            return;
        }
        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);

        cardGroup.toFront();
        handGroup.toFront();
        //store the offset distance between the mouse and the layout of the card
        double offsetX = cardGroup.getLayoutX() - event.getSceneX();
        double offsetY = cardGroup.getLayoutY() - event.getSceneY();
        cardGroup.setOffset(offsetX, offsetY);
        cardGroup.setCursor(Cursor.MOVE);
    }

    /**
     * Handles the mouse drag event on a pathway card in the hand.
     * Can only drag if the Phase is at DISCARD_PATH or PLAY_PATH_OR_MOVE_CAT.
     * Glows the square of placement on the board if the game phase is PLAY_PATH_OR_MOVE_CAT and Pathway card is dragged above the board.
     * Makes the card transparent if the card is over the discard bin and the game phase is DISCARD_PATH.
     *
     * @param cardGroup The CardGroup representing the pathway card being dragged
     * @param event     The MouseEvent that triggered the mouse drag
     */
    private void pathwayCardMouseDrag(CardGroup cardGroup,MouseEvent event) {
        if (gameStep.getStateType() != GameStep.StateType.DISCARD_PATH && gameStep.getStateType() != GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            return;
        }

        cardGroup.setLayoutX(event.getSceneX() + cardGroup.getOffsetX());
        cardGroup.setLayoutY(event.getSceneY() + cardGroup.getOffsetY());
        if (gameStep.getStateType() == GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            double boardBaseX = boardGroup.getLayoutX();
            double boardBaseY = boardGroup.getLayoutY();

            double posInBoardX = cardGroup.getLayoutX() + handGroup.getLayoutX() - boardBaseX;
            double posInBoardY = cardGroup.getLayoutY() + handGroup.getLayoutY() - boardBaseY;

            //use the central position of the [0,0] square
            int col = ((int) posInBoardX + (GRID_CELL_WIDTH / 2)) / GRID_CELL_WIDTH;
            int row = ((int) posInBoardY + (GRID_CELL_HEIGHT / 2)) / GRID_CELL_HEIGHT;
            Card card = cardGroup.getCard();
            Orientation orientation = card.getOrientation();
            card.setPlacedLocation(row, col);
            Location placedLocation = card.getPlacedLocation();
            Square[][] squares = card.getCardSquare(orientation);
            //show the glow effect that know where can place
            boardGroup.glowSquares(squares,placedLocation);
        }


        if (gameStep.getStateType() == GameStep.StateType.DISCARD_PATH) {
            double absoluteCenterX = event.getSceneX();
            double absoluteCenterY = event.getSceneY();
            if (absoluteCenterX < rubbishBin.getLayoutX()
                    || absoluteCenterX > rubbishBin.getLayoutX() + rubbishBin.getFitWidth()
                    || absoluteCenterY < rubbishBin.getLayoutY()
                    || absoluteCenterY > rubbishBin.getLayoutY() + rubbishBin.getFitHeight()) {
                cardGroup.setOpacity(1);
                return;
            }
            cardGroup.setOpacity(0.6);
        }
    }

    /**
     * Handles the mouse release event on a pathway card in the hand.
     * Can only release to place the card on the board if the game state is PLAY_PATH_OR_MOVE_CAT.
     * Can only release to discard the card to the bin if the game state is DISCARD_PATH.
     *
     * @param cardGroup The CardGroup representing the pathway card being released
     * @param event     The MouseEvent that triggered the mouse release
     */
    private void pathwayCardMouseRelease(CardGroup cardGroup,MouseEvent event) {
        // Reset the cursor to the hand symbol first
        cardGroup.setCursor(Cursor.HAND);
        boardGroup.clearGlowSquares();

        if (gameStep.getStateType() == GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            double boardBaseX = boardGroup.getLayoutX();
            double boardBaseY = boardGroup.getLayoutY();

            double posInBoardX = cardGroup.getLayoutX() + handGroup.getLayoutX() - boardBaseX;
            double posInBoardY = cardGroup.getLayoutY() + handGroup.getLayoutY() - boardBaseY;

            //use the central position of the [0,0] square
            int col = ((int) posInBoardX + (GRID_CELL_WIDTH / 2)) / GRID_CELL_WIDTH;
            int row = ((int) posInBoardY + (GRID_CELL_HEIGHT / 2)) / GRID_CELL_HEIGHT;

            cardGroup.setLayoutX(boardBaseX + col * GRID_CELL_WIDTH);
            cardGroup.setLayoutY(boardBaseY + row * GRID_CELL_HEIGHT);

            // Get the associated Card object from the CardGroup
            Card card = cardGroup.getCard();
            Orientation orientation = card.getOrientation();
            card.setPlacedLocation(row, col);
            Location placedLocation = card.getPlacedLocation();
            // Check if the grid indices are within valid board limits
            if (!gameRun.getBoard().isInBounds(row,col)) {
                errorText.setText("Card is out of the board.");
                errorGroup.setVisible(true);
                cardGroup.returnToOriginalPos();
                return;
            }

            // Validate the card placement according to the game rules
            if (!gameRun.isPlacePathwayCardValid((PathwayCard) card, placedLocation, orientation)) {
                errorText.setText(gameRun.getPlacePathwayCardErrorString());
                errorGroup.setVisible(true);
                cardGroup.returnToOriginalPos();
                return;
            }
            //actually do, then clear the error
            errorText.setText("");
            errorGroup.setVisible(false);
            card.setPlacedLocation(row, col);
            // If valid, place the card and refresh the view
            gameStep.setNeedPlacePathwayCard(true);
            gameRun.placePathwayCard((PathwayCard) card, placedLocation, orientation);

            Square[][] squares = card.getCardSquare(orientation);
            boardGroup.drawSquares(squares, card.getPlacedLocation());

            // This method should update the GUI to reflect the new state
            cardGroup.getChildren().clear();
            pathwayCardGroups.remove(cardGroup);
            handGroup.getChildren().remove(cardGroup);

            //update for the action, should follow directly follow the action
            gameStep.updateStateType();
            judgeWinOrLoss();
            if (gameStep.isStateChanged()) {
                //disappear the old text, show the new text by animation
                FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                fade.setFromValue(phaseText.getOpacity());
                fade.setToValue(0);
                fade.play();
                fade.setOnFinished(e -> {
                    //after finishing the animation, show the new text
                    phaseText.setText(gameStep.getStateType().toString());
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                    f.setFromValue(phaseText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });

                //disappear the old text, show the new text by animation
                fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                fade.setFromValue(hintText.getOpacity());
                fade.setToValue(0);
                fade.play();
                fade.setOnFinished(e -> {
                    //after finishing the animation, show the new text
                    hintText.setText(gameStep.getHint());
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                    f.setFromValue(hintText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });
            }
        }


        //in the bin
        if (gameStep.getStateType() == GameStep.StateType.DISCARD_PATH) {
            double absoluteCenterX = event.getSceneX();
            double absoluteCenterY = event.getSceneY();
            if (absoluteCenterX < rubbishBin.getLayoutX()
                    || absoluteCenterX > rubbishBin.getLayoutX() + rubbishBin.getFitWidth()
                    || absoluteCenterY < rubbishBin.getLayoutY()
                    || absoluteCenterY > rubbishBin.getLayoutY() + rubbishBin.getFitHeight()) {
                // Reset the card to its original position if it's out of bounds
                errorText.setText("card is out of the Bin.");
                errorGroup.setVisible(true);
                cardGroup.returnToOriginalPos();
                return;
            }
            //actually do, then clear the error
            errorText.setText("");
            errorGroup.setVisible(false);
            discardCard(cardGroup);
        }
    }

    /**
     * Enables event handling for fire tile drawn from fire tile bag, allowing them to be interacted with using mouse actions.
     * - Mouse Press: Initiates the dragging of the fire tile.
     * - Mouse Drag: Moves the fire tile while dragging.
     * - Mouse Release: placing the fire tile
     *
     * @param cardGroup The CardGroup representing the fire tile
     */
    private void enableDragFireTile(CardGroup cardGroup) {
        cardGroup.setCursor(Cursor.HAND);
        cardGroup.setOnMousePressed(event -> fireTileMousePress(cardGroup,event));
        cardGroup.setOnMouseDragged(event -> fireTileMouseDrag(cardGroup,event));
        cardGroup.setOnMouseReleased(event -> fireTileMouseRelease(cardGroup));
    }

    /**
     * Handles the mouse press event on a fire tile drawn.
     * Can only initiate if the Phase is at PLAY_FIRE
     *
     * @param cardGroup The CardGroup representing the fire tile being interacted with
     * @param event     MouseEvent that triggered the mouse press
     */
    void fireTileMousePress(CardGroup cardGroup, MouseEvent event){
        if (gameStep.getStateType() != GameStep.StateType.PLAY_FIRE) {
            errorText.setText("Cannot move Fire Tile at Current State");
            errorGroup.setVisible(true);
            return;
        }
        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);

        cardGroup.toFront();
        fireTileBagGroup.toFront();
        //set the offset of the distance between mouse and layout, for dragging
        double offsetX = cardGroup.getLayoutX() - event.getSceneX();
        double offsetY = cardGroup.getLayoutY() - event.getSceneY();
        cardGroup.setOffset(offsetX, offsetY);
        cardGroup.setCursor(Cursor.MOVE);
    }

    /**
     * Handles the mouse drag event on a fire tile drawn.
     * Can only drag if the Phase is at PLAY_FIRE.
     * Glows the square of placement on the board if the game phase is PLAY_FIRE and fire tile is dragged above the board.
     *
     * @param cardGroup The CardGroup representing the fire tile being dragged
     * @param event     MouseEvent that triggered the mouse drag
     */
    void fireTileMouseDrag(CardGroup cardGroup,MouseEvent event){
        if (gameStep.getStateType() != GameStep.StateType.PLAY_FIRE) {
            return;
        }
        fireTileGroup.setLayoutX(event.getSceneX() + fireTileGroup.getOffsetX());
        fireTileGroup.setLayoutY(event.getSceneY() + fireTileGroup.getOffsetY());

        double boardBaseX = boardGroup.getLayoutX();
        double boardBaseY = boardGroup.getLayoutY();

        double posInBoardX = cardGroup.getLayoutX() + fireTileBagGroup.getLayoutX() - boardBaseX;
        double posInBoardY = cardGroup.getLayoutY() + fireTileBagGroup.getLayoutY() - boardBaseY;

        //use the central position of the [0,0] square
        int col = ((int) posInBoardX + (GRID_CELL_WIDTH / 2)) / GRID_CELL_WIDTH;
        int row = ((int) posInBoardY + (GRID_CELL_HEIGHT / 2)) / GRID_CELL_HEIGHT;

        Card card = cardGroup.getCard();
        card.setPlacedLocation(row, col);
        Orientation orientation = card.getOrientation();
        boolean isFlipped = card.isFlipped();
        Location placedLocation = card.getPlacedLocation();

        Square[][] squares = card.getCardSquare(orientation, isFlipped);
        boardGroup.glowSquares(squares,placedLocation);

    }

    /**
     * Handles the mouse release event on a fire tile drawn.
     * Can only release to place the card on the board if the game state is PLAY_FIRE.
     *
     * @param cardGroup The CardGroup representing the fire tile being released
     */
    void fireTileMouseRelease(CardGroup cardGroup) {
        // Reset the cursor to the hand symbol first
        cardGroup.setCursor(Cursor.HAND);
        boardGroup.clearGlowSquares();

        if (gameStep.getStateType() == GameStep.StateType.PLAY_FIRE) {
            double boardBaseX = boardGroup.getLayoutX();
            double boardBaseY = boardGroup.getLayoutY();

            double posInBoardX = cardGroup.getLayoutX() + fireTileBagGroup.getLayoutX() - boardBaseX;
            double posInBoardY = cardGroup.getLayoutY() + fireTileBagGroup.getLayoutY() - boardBaseY;

            //use the central position of the [0,0] square
            int col = ((int) posInBoardX + (GRID_CELL_WIDTH / 2)) / GRID_CELL_WIDTH;
            int row = ((int) posInBoardY + (GRID_CELL_HEIGHT / 2)) / GRID_CELL_HEIGHT;

            fireTileGroup.setLayoutX(boardBaseX + col * GRID_CELL_WIDTH);
            fireTileGroup.setLayoutY(boardBaseY + row * GRID_CELL_HEIGHT);

            // Get the associated Card object from the CardGroup
            Card card = cardGroup.getCard();
            card.setPlacedLocation(row, col);
            Orientation orientation = card.getOrientation();
            boolean isFlipped = card.isFlipped();
            Location placedLocation = card.getPlacedLocation();

            // Check if the grid indices are within valid board limits
            if (!gameRun.getBoard().isInBounds(row, col)) {
                errorText.setText("FireTile is out out the Board");
                errorGroup.setVisible(true);
                cardGroup.returnToOriginalPos();
                return;
            }

            // Validate the card placement according to the game rules
            if (!gameRun.isPlaceFireTileValid((FireTile) card, placedLocation, orientation, isFlipped)) {
                // Handle invalid placement
                errorText.setText(gameRun.getPlaceFireTireErrorString());
                errorGroup.setVisible(true);
                cardGroup.returnToOriginalPos();
                return;
            }
            //actually do, then clear the error
            errorText.setText("");
            errorGroup.setVisible(false);
            fireTilePlaceHintButton.setVisible(false);
            fireTileHintText.setText("");
            // If valid, place the card and refresh the view
            gameRun.placeFireTile((FireTile) card, placedLocation, orientation, isFlipped);

            Square[][] squares = card.getCardSquare(orientation, isFlipped);
            boardGroup.drawSquares(squares, placedLocation);
            // This method should update the GUI to reflect the new state
            cardGroup.getChildren().clear();
            fireTileBagGroup.getChildren().remove(cardGroup);
            fireTileGroup = null;

            //update for the action, should follow directly follow the action
            gameStep.updateStateType();
            judgeWinOrLoss();
            if (gameStep.isStateChanged()) {
                //disappear the old text, show the new text by animation

                FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                fade.setFromValue(phaseText.getOpacity());
                fade.setToValue(0);
                fade.play();
                fade.setOnFinished(e -> {
                    //after finishing the animation, show the new text
                    phaseText.setText(gameStep.getStateType().toString());
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                    f.setFromValue(phaseText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });

                //disappear the old text, show the new text by animation
                fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                fade.setFromValue(hintText.getOpacity());
                fade.setToValue(0);
                fade.play();
                fade.setOnFinished(e -> {
                    //after finishing the animation, show the new text
                    hintText.setText(gameStep.getHint());
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                    f.setFromValue(hintText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });

                //this means new round turn, should recover the cat
                if (gameStep.getStateType() == GameStep.StateType.DRAW_PATH) {
                    for (CatGroup group : catGroups) {
                        Cat cat = group.getCat();
                        cat.recoverExhausted();
                        group.showState();
                    }
                }
            }
        }
    }

    /**
     * Enables mouse interaction events for a cat represented by the given CatGroup.
     * - Mouse Press: Initiates dragging of the cat.
     * - Mouse Drag: Moves the cat while dragging.
     * - Mouse Release: Places the cat.
     *
     * @param catGroup The CatGroup representing the cat
     */
    private void enableDragCat(CatGroup catGroup){
        catGroup.setCursor(Cursor.HAND);
        catGroup.setOnMousePressed(event -> catMousePress(catGroup,event));
        catGroup.setOnMouseDragged(event -> catMouseDrag(catGroup,event));
        catGroup.setOnMouseReleased(event -> catMouseRelease(catGroup));
    }

    /**
     * Handles the mouse press event on a cat in the game.
     * Can only initiate dragging if the game phase is PLAY_PATH_OR_MOVE_CAT.
     *
     * @param catGroup The CatGroup representing the cat being interacted with
     * @param event    The MouseEvent that triggered the mouse press
     */
    void catMousePress(CatGroup catGroup, MouseEvent event) {
        if (gameStep.getStateType() != GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            errorText.setText("Cannot move Cat at Current Phase");
            errorGroup.setVisible(true);
            return;
        }
        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);

        catGroup.toFront();
        //set the offset for dragging
        double offsetX = catGroup.getLayoutX() - event.getSceneX();
        double offsetY = catGroup.getLayoutY() - event.getSceneY();
        catGroup.setOffset(offsetX, offsetY);
        catGroup.setCursor(Cursor.MOVE);
    }

    /**
     * Handles the mouse drag event on a cat during the game.
     * Can only drag the cat if the game phase is PLAY_PATH_OR_MOVE_CAT.
     * Glows the square of placement and path to it if the placement is on board.
     *
     * @param catGroup The CatGroup representing the cat being dragged
     * @param event    The MouseEvent that triggered the mouse drag
     */
    void catMouseDrag(CatGroup catGroup,MouseEvent event) {
        if (gameStep.getStateType() != GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            return;
        }
        catGroup.setLayoutX(event.getSceneX() + catGroup.getOffsetX());
        catGroup.setLayoutY(event.getSceneY() + catGroup.getOffsetY());

        double posInBoardX = catGroup.getLayoutX();
        double posInBoardY = catGroup.getLayoutY();

        //use the central position of the [0,0] square
        int col = ((int) posInBoardX + (GRID_CELL_WIDTH / 2)) / GRID_CELL_WIDTH;
        int row = ((int) posInBoardY + (GRID_CELL_HEIGHT / 2)) / GRID_CELL_HEIGHT;

        Cat cat=catGroup.getCat();
        Location aimLocation = new Location(row,col);

        boardGroup.clearGlowSquares();
        ArrayList<Integer>[] pathOutput = new ArrayList[1];
        if(!gameRun.isMoveCatValid(cat,aimLocation,pathOutput)){
            return;
        }
        boardGroup.glowSquaresByNodeList(pathOutput[0]);
    }

    /**
     * Handles the mouse release event on a cat during the game.
     * Can only release to place the cat on the board if the game state is PLAY_PATH_OR_MOVE_CAT.
     *
     * @param catGroup The CatGroup representing the cat being released
     */
    void catMouseRelease(CatGroup catGroup) {
        // Reset the cursor to the hand symbol first
        catGroup.setCursor(Cursor.HAND);
        boardGroup.clearGlowSquares();

        if (gameStep.getStateType() == GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            double posInBoardX = catGroup.getLayoutX();
            double posInBoardY = catGroup.getLayoutY();

            //use the central position of the [0,0] square
            int col = ((int) posInBoardX + (GRID_CELL_WIDTH / 2)) / GRID_CELL_WIDTH;
            int row = ((int) posInBoardY + (GRID_CELL_HEIGHT / 2)) / GRID_CELL_HEIGHT;

            catGroup.setLayoutX(col * GRID_CELL_WIDTH);
            catGroup.setLayoutY(row * GRID_CELL_HEIGHT);
            // Get the associated Card object from the CardGroup
            Cat cat = catGroup.getCat();

            // Check if the grid indices are within valid board limits
            if (!gameRun.getBoard().isInBounds(row,col)){
                // Reset the card to its original position if it's out of bounds
                errorText.setText("Aim Location is out of the board.");
                errorGroup.setVisible(true);
                catGroup.returnToLastPos();
                return;
            }

            Location startLocation = cat.getPlacedLocation();
            Location aimLocation = new Location(row, col);
            // Validate the card placement according to the game rules
            if (!gameRun.isMoveCatValid(cat, aimLocation, null)) {
                errorText.setText(gameRun.getMoveCatErrorString());
                errorGroup.setVisible(true);
                // Handle invalid placement
                catGroup.returnToLastPos();
                return;
            }

            //actually do, then clear the error
            errorText.setText("");
            errorGroup.setVisible(false);

            Square starSquare = gameRun.getBoard().getSingleSquare(startLocation);
            Square aimSquare = gameRun.getBoard().getSingleSquare(aimLocation);
            // If valid, place the card and refresh the view
            gameStep.setNeedMoveCat(true);
            gameRun.moveCat(cat, aimLocation);

            //update for the action, should follow directly follow the action
            gameStep.updateStateType();
            judgeWinOrLoss();
            if (gameStep.isStateChanged()) {
                //disappear the old text, show the new text by animation
                FadeTransition fade= new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                fade.setFromValue(phaseText.getOpacity());
                fade.setToValue(0);
                fade.play();
                fade.setOnFinished(e -> {
                    //after finishing the animation, show the new text
                    //may go to win or loss, we need to judge the type of step
                    if(gameStep.getStateType() == GameStep.StateType.DISCARD_PATH){
                        phaseText.setText("Discard " + gameRun.getNeedDisCardNum() + ((gameRun.getNeedDisCardNum()>1)?" cards":" card"));
                    }
                    else {
                        phaseText.setText(gameStep.getStateType().toString());
                    }
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), phaseText);
                    f.setFromValue(phaseText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });

                //disappear the old text, show the new text by animation
                fade = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                fade.setFromValue(hintText.getOpacity());
                fade.setToValue(0);
                fade.play();
                fade.setOnFinished(e -> {
                    //after finishing the animation, show the new text
                    hintText.setText(gameStep.getHint());
                    FadeTransition f = new FadeTransition(Duration.millis(ANIMATION_TIME_MS), hintText);
                    f.setFromValue(hintText.getOpacity());
                    f.setToValue(1.0);
                    f.play();
                });
            }
            //after making action of the cat, update the showed state
            catGroup.showState();
            //cat move to a new place, remember it for the last positiono
            catGroup.setLastPos(col * GRID_CELL_WIDTH, row * GRID_CELL_HEIGHT);
            // This method should update the GUI to reflect the new state
            boardGroup.drawSingleSquare(starSquare, startLocation);
            boardGroup.drawSingleSquare(aimSquare, aimLocation);
        }
    }

    /**
     * Rotates the orientation of the currently pressed pathway card when a keyboard key is pressed.
     * Only rotates the card if the game state is PLAY_PATH_OR_MOVE_CAT.
     * The method assumes that a card group is pressed (selected) before the key is pressed.
     */
    private void rotatePathwayCardGroupByKeyBoard(){
        //first find the pressed cardGroup, otherwise, it will show error when we not pressed
        CardGroup pressedGroup = null;
        for (CardGroup cardGroup : pathwayCardGroups) {
            if (cardGroup.isPressed()) {
                pressedGroup = cardGroup;
            }
        }
        if(pressedGroup==null){
            return;
        }

        if (gameStep.getStateType()  != GameStep.StateType.PLAY_PATH_OR_MOVE_CAT) {
            errorText.setText("cannot rotate the card at current phase");
            errorGroup.setVisible(true);
            return;
        }

        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);

        //re show the card for each imageViews
        Card card = pressedGroup.getCard();
        Orientation originalOrientation = card.getOrientation();
        card.setOrientation(originalOrientation.rotateClockWise());
        pressedGroup.showCard();
    }

    /**
     * Rotates the orientation of the currently pressed fire tile when a keyboard key is pressed.
     * Only rotates the fire tile if the game state is PLAY_FIRE.
     * The method assumes that a fire tile group is pressed (selected) before the key is pressed.
     */
    private void rotateFireTileGroupByKeyBoard(){
        if(fireTileGroup==null) {
            return;
        }
        if(!fireTileGroup.isPressed()) {
            return;
        }
        if (gameStep.getStateType() != GameStep.StateType.PLAY_FIRE) {
            errorText.setText("Cannot Rotate Fire Tile or Pathway Card at Current Phase");
            errorGroup.setVisible(true);
            return;
        }
        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);

        //reshow the fireTile of the fireTile Group--imageViews
        Card card = fireTileGroup.getCard();
        Orientation originalOrientation = card.getOrientation();
        card.setOrientation(originalOrientation.rotateClockWise());
        fireTileGroup.showCard();
    }

    /**
     * Flips the currently pressed fire tile group when a keyboard key is pressed.
     * Only flips the fire tile if the game state is PLAY_FIRE.
     * The method assumes that a fire tile group is pressed (selected) before the key is pressed.
     */
    private void flipFireTileGroupByKeyBoard() {
        if(fireTileGroup==null) {
            return;
        }
        if(!fireTileGroup.isPressed()) {
            return;
        }
        if (gameStep.getStateType() != GameStep.StateType.PLAY_FIRE) {
            errorText.setText("Cannot Flip Fire Tile at Current Phase");
            errorGroup.setVisible(true);
            return;
        }

        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);

        //reshow the cardGroup
        Card card = fireTileGroup.getCard();
        card.setFlipped(!card.isFlipped());
        fireTileGroup.showCard();

    }

    /**
     * Discards (removes) the currently pressed pathway card when a keyboard key is pressed.
     * Only discards the pathway card if the game state is DISCARD_PATH.
     * Assumes that a pathway card group is pressed (selected) before the key is pressed.
     */
    private void discardPathwayCardByKeyBoard() {
        //first find the pressed cardGroup, otherwise, it will show error when it's not pressed
        CardGroup pressedGroup = null;
        for (CardGroup cardGroup : pathwayCardGroups) {
            if (cardGroup.isPressed()) {
                pressedGroup = cardGroup;
            }
        }
        if(pressedGroup==null){
            return;
        }
        if (gameStep.getStateType() != GameStep.StateType.DISCARD_PATH) {
            errorText.setText("Cannot Discard a Pathway Card at Current Phase");
            errorGroup.setVisible(true);
            return;
        }
        //actually do, then clear the error
        errorText.setText("");
        errorGroup.setVisible(false);
        //the action--discard the card
        discardCard(pressedGroup);
    }

    /**
     * Initializes the "Race to the Raft" game by setting up the main stage and calling
     * different initialization methods to set up various parts of the game.
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception If an exception occurs during the initialisation process
     */
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Race to the Raft");
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);

        //Initialize/create different parts of the game
        initializeReset();
        initializeDeck();
        initializeHand();
        initializeFireTileBag();
        initializeHelper();
        initializeDiscardBin();
        initializeAlert();
        initializeGroupInformation();
        initializeRotateFlipDeleteCard();
        initializeRuleBook();
        initializeBackGround(scene);

        root.setFocusTraversable(true);
        scene.setOnKeyReleased(event -> {
            root.requestFocus();
            if (event.getCode() == KeyCode.F8){
            showDebug();
            }
        });
        stage.getIcons().add(Picture.GAME_ICON);
        //root.set
        stage.show();
    }
}


