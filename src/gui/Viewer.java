package gui;

import Board;
import Hand;
import Square;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Viewer extends Application {

    private final Group root = new Group();
    private static final int VIEWER_WIDTH = 1100;
    private static final int VIEWER_HEIGHT = 650;
    private static final int MARGIN_X = 20;

    private final Group controls = new Group(); // a group
    private TextArea handTextField;             //a label 1 for hand
    private TextArea boardTextField;            //another label 2 for board

    private ImageView[][] boardViews;
    private ImageView[][][] cardViews;          //first is card, second is row of each card, third is col of each card

    /**
     * Draw the given board and hand in the window, removing any previously drawn boards/hands.
     *
     * @param boardstate newline separated string representing each row of the board (the board string, see the STRING-REPRESENTATION.md for more details
     * @param hand A string representing the cards in a player's hand (the hand string, see the STRING-REPRESENTATION.md for more details)
     *
     */
    void displayState(String boardstate, String hand) {
        Board boardIns = new Board(boardstate);
        Hand handIns = new Hand(hand);

        int sizeRowBoard = boardIns.getSizeRow();
        int sizeColBoard = boardIns.getSizeCol();
        int rectWidthBoard = 40;
        int rectHeightBoard = 40;
        int baseXBoard = 300;
        int baseYBoard = 0;

        int currentViewSizeRow = 0;
        int currentViewSizeCol = 0;
        if(this.boardViews != null){
            currentViewSizeRow = this.boardViews.length;
            currentViewSizeCol = this.boardViews[0].length;
        }

        //the board views can only need build once
        //after changing the picture, the views do not need to be added again, just change the image in it
        //if bigger, than re-new the cardViews
        if ((this.boardViews == null && sizeColBoard > 0 && sizeColBoard > 0) || (currentViewSizeRow < sizeRowBoard || currentViewSizeCol < sizeColBoard)) {
            //this is remove the oldImageview
            if(this.boardViews != null) {
                for (int row = 0; row < this.boardViews.length; row++) {
                    for (int col = 0; col < this.boardViews[0].length; col++) {
                        root.getChildren().remove(this.boardViews[row][col]);
                    }
                }
            }
            //this two case we all need new
            this.boardViews = new ImageView[sizeRowBoard][sizeColBoard];
            for (int row = 0; row < sizeRowBoard; row++) {
                for (int col = 0; col < sizeColBoard; col++) {
                    //set each image view
                    this.boardViews[row][col] = new ImageView();
                    this.boardViews[row][col].setFitWidth(rectWidthBoard);
                    this.boardViews[row][col].setFitHeight(rectHeightBoard);
                    this.boardViews[row][col].setX(col * rectWidthBoard + baseXBoard);
                    this.boardViews[row][col].setY(row * rectHeightBoard + baseYBoard);
                    this.root.getChildren().add(this.boardViews[row][col]);
                }
            }
        }
        //first clear the view
        if(this.boardViews != null){
            for (int row = 0; row < this.boardViews.length; row++) {
                for (int col = 0; col < this.boardViews[0].length; col++) {
                    if(this.boardViews[row][col]!=null){
                        this.boardViews[row][col].setImage(null);
                    }
                }
            }
        }

        //board set picture
        if(this.boardViews != null){
            for (int row = 0; row < sizeRowBoard; row++) {
                for (int col = 0; col < sizeColBoard; col++) {
                    char state = 0;
                    Image image;
                    //set each image view
                    if(boardIns.getStateSquares()[row][col] != null){
                        state =boardIns.getStateSquares()[row][col].getStateChar();
                    }
                    image = Picture.getPicture(state);

                    if(image!=null && this.boardViews[row][col]!=null){
                        this.boardViews[row][col].setImage(image);
                    }
                }
            }
        }

        //the card views of hand can only need build once
        //card view we know it has 6 cards and every card has 3x3 square
        //after changing the picture, the views do not need to added again, changing the image in it
        int cardDistance = 130;
        int rectWidthCard = 40;
        int rectHeightCard = 40;
        int baseXCard = 20;
        int baseYCard = 0;

        //new the views of card
        if(this.cardViews == null) {
            this.cardViews = new ImageView[6][3][3];
            for (int i = 0; i < 6; i++) {
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        //set each image view
                        this.cardViews[i][row][col] = new ImageView();
                        this.cardViews[i][row][col].setFitWidth(rectWidthCard);
                        this.cardViews[i][row][col].setFitHeight(rectHeightCard);

                        int cardX = baseXCard + (i % 2) * cardDistance;
                        int cardY = baseYCard + (i / 2) * cardDistance;

                        this.cardViews[i][row][col].setX(col * rectWidthCard + cardX);
                        this.cardViews[i][row][col].setY(row * rectHeightCard + cardY);
                        this.root.getChildren().add(this.cardViews[i][row][col]);
                    }
                }
            }
        }

        //first clear the view
        if (this.cardViews != null) {
            for (int i = 0; i < 6; i++) {
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        if (this.cardViews[i][row][col] != null) {
                            this.cardViews[i][row][col].setImage(null);
                        }
                    }
                }
            }
        }

        //here is to show the card of hand, also can show the card less than the number of 6
        for(int i=0;i<(Math.min(handIns.getCardNum(),6));i++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    char state = 0;
                    Image image;
                    Square square = handIns.getCardsList().get(i).getCardSquare()[row][col];
                    //set each image view
                    if(square != null){
                        state = square.getStateChar();
                    }
                    image = Picture.getPicture(state);
                    //set each image view
                    if(image != null && this.cardViews[i][row][col] !=null) {
                        this.cardViews[i][row][col].setImage(image);
                    }
                }
            }
        }  // FIXME TASK 4
    }

    /**
     * Generate controls for Viewer
     */
    private void makeControls() {
        //label 1
        Label playerLabel = new Label("Cards in hand:");
        //a text edit
        handTextField = new TextArea();
        handTextField.setPrefWidth(100);

        //label 2
        Label boardLabel = new Label("Board State:");
        //a text edit
        boardTextField = new TextArea();
        boardTextField.setPrefWidth(100);

        //a refresh button, that can trigger an event
        Button button = refreshButton();
        button.setLayoutY(VIEWER_HEIGHT - 250);
        button.setLayoutX(MARGIN_X);

        //HBox is for children in a horizontal row
        HBox fields = new HBox();
        fields.getChildren().addAll(handTextField, boardTextField);         //put the 2 text edit in a horizontal row
        fields.setSpacing(20);                                              //the space between the 2 label
        fields.setLayoutX(MARGIN_X);                                        //set the position of layout
        fields.setLayoutY(VIEWER_HEIGHT - 200);

        //HBox is for children in a horizontal row
        HBox labels = new HBox();
        labels.getChildren().addAll(playerLabel, boardLabel);               //this HBox is for store labels
        labels.setSpacing(45);
        labels.setLayoutX(MARGIN_X);
        labels.setLayoutY(VIEWER_HEIGHT - 220);
        controls.getChildren().addAll(fields, labels, button);
    }

    /**
     * Create refresh button. Upon pressing, capture the textFields and call displayState
     * @return the created button
     */
    private Button refreshButton() {
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            //when refreshing the button, we get the text from this two text edit
            String boardText = boardTextField.getText();
            String handCards = handTextField.getText();
            //then convey the two string into the displayState
            displayState(boardText, handCards);
        });
        return button;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Race to the Raft Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        //make a control element
        makeControls();
        displayState("", "");
        root.getChildren().add(controls);
        makeControls();
        stage.setScene(scene);
        stage.show();
    }
}

