package gui;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;

import Board;
import Card;
import Location;
import Square;

/**
 * Inherits from the base class group.
 * represent a board group in this game.
 * the board group has the group and the board instance, and can link them together
 * it can easily draw the board squares to the group. and these function can be the method
 * of this class.
 */

public class BoardGroup extends Group {
    private Board board;
    private ImageView imageViews[][];
    private ArrayList<ImageView> hintedImageViews =new ArrayList<>();   //stored which imageViews have been hinted
    private ArrayList<ImageView> glowedImageViews =new ArrayList<>();   //store which imageViews have been glowed
    private boolean isHinted;

    /**
     * constructor of this class, show draw the board on the group
     *
     * @param board the relevant board object
     */
    public BoardGroup(Board board) {
        this.board = board;
        this.isHinted = false;
        imageViews = new ImageView[board.getSizeRow()][board.getSizeCol()];

        //set the animation of when it appears
        this.setOpacity(0);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(Game.ANIMATION_TIME_MS),this);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        for (int row = 0; row < board.getSizeRow(); row++) {
            for (int col = 0; col < board.getSizeCol(); col++) {
                imageViews[row][col] = new ImageView();
                this.getChildren().add(imageViews[row][col]);
                imageViews[row][col].setFitWidth(Game.GRID_CELL_WIDTH);
                imageViews[row][col].setFitHeight(Game.GRID_CELL_HEIGHT);

                imageViews[row][col].setX(col * Game.GRID_CELL_WIDTH);
                imageViews[row][col].setY(row * Game.GRID_CELL_HEIGHT);

                //get each square from the board
                Square square = board.getSingleSquare(row, col);
                char state = 0;
                Image image;
                if (square != null) {
                    state = square.getStateChar();
                    //if the square belongs to the raft, we just use the uppercase letter to show the cat icon
                    if (square.getCardType() == Card.CardType.RAFT && square.getColor() != Color.NONE) {
                        state = Character.toUpperCase(state);
                    }
                    image = Picture.getPicture(state);
                    //set each image view
                    if (image != null && imageViews[row][col] != null) {
                        imageViews[row][col].setImage(image);
                        if(square.getCardType() == Card.CardType.RAFT){
                            //raft square set different with the normal squares
                            imageViews[row][col].setOpacity(0.5);
                        }
                    }
                }
            }
        }

        //set the shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setColor(Color.GRAY);
        this.setEffect(dropShadow);
    }

    /**
     * draw the squares on board, show refresh the image of the corresponding imageViews
     *
     * @param squares 2D array of the square
     * @param placedLocation the placedLocation on board
     */
    public void drawSquares(Square[][] squares, Location placedLocation){
        if(imageViews==null || squares ==null){
            return;
        }

        int squareSizeRow = squares.length;
        int squareSizeCol = squares[0].length;

        //judge it is in the board
        if(!board.isPlaceSquaresInBounds(squares,placedLocation)){
            return;
        }

        //let all imageViews not hinted and not hided
        //just visible, optical(1)
        recoverShow();

        for(int row=0;row<squareSizeRow;row++){
            for(int col=0;col<squareSizeCol;col++){
                char state = 0;
                Square square = squares[row][col];
                if (square != null) {
                    state = square.getStateChar();
                    //raft square show the cat icon
                    if(square.getCardType() == Card.CardType.RAFT && square.getColor()!= Color.NONE){
                        state = Character.toUpperCase(state);
                    }
                }
                Image image = Picture.getPicture(state);
                if(image != null){
                    imageViews[row+ placedLocation.getRow()][col+placedLocation.getColumn()].setImage(image);
                    if(squares[row][col].getCardType() == Card.CardType.RAFT){
                        //raft square become different with normal square
                        imageViews[row+ placedLocation.getRow()][col+placedLocation.getColumn()].setOpacity(0.5);
                    }
                }
            }
        }
    }

    /**
     * draw a single square, just input a square and the placedLocation.
     * It is mainly used for the drawing of cat
     *
     * @param square a single square
     * @param placedLocation the placedLocation on the board
     */
    public void drawSingleSquare(Square square, Location placedLocation){
        //avoid the null
        if(imageViews==null || square == null){
            return;
        }
        //avoid out of the board
        if(!board.isInBounds(placedLocation)){
            return;
        }
        //can not draw raft square
        if(square.getCardType() == Card.CardType.RAFT){
            return;
        }

        int row = placedLocation.getRow();
        int col = placedLocation.getColumn();
        imageViews[row][col].setImage(Picture.getPicture(square.getStateChar()));
    }

    /**
     * set the corresponding imageViews opacity to differentiate with the normal square
     *
     * @param squares 2D squares that need to show the hint placement
     * @param placedLocation placedLocation on the board
     */
    public void hideSquaresForHint(Square[][] squares,Location placedLocation){
        if(imageViews==null || squares ==null){
            return;
        }

        int squareSizeRow = squares.length;
        int squareSizeCol = squares[0].length;

        //cannot out of the board
        if(!board.isPlaceSquaresInBounds(squares,placedLocation)){
            return;
        }

        ImageView imageView;
        for(int row=0;row<squareSizeRow;row++){
            for (int col=0;col<squareSizeCol;col++){
                if(squares[row][col]!=null){
                    imageView = imageViews[row+placedLocation.getRow()][col+placedLocation.getColumn()];
                    //set the opacity, become 0.18
                    imageView.setOpacity(0.18);
                    //store the imageViews to the list
                    this.hintedImageViews.add(imageView);
                }
            }
        }
        //true the flag
        this.isHinted = true;
    }

    /**
     * let the hinted imageViews become non-transparent
     */
    public void recoverShow(){
        //show all the squares
        for(ImageView imageView:hintedImageViews){
            imageView.setOpacity(1);
        }
        this.hintedImageViews.clear();
        this.isHinted=false;
    }

    /**
     * let the all imageViews do not have the image, set their image become null
     */
    public void clearImage(){
        if(imageViews==null){
            return;
        }
        //show all the squares
        for(int row = 0;row<board.getSizeRow();row++){
            for(int col=0;col<board.getSizeCol();col++) {
                if(imageViews[row][col]!=null){
                    imageViews[row][col].setImage(null);
                }
            }
        }
    }

    /**
     * glow the imageViews for the corresponding squares to enhance the interaction of play
     * use the placedLocation to determine the where should be glowed
     * it will run many times due to the drag of mouse
     *
     * @param squares 2D squares that should be glowed
     * @param placedLocation placedLocation on the board
     */
    public void glowSquares(Square[][] squares,Location placedLocation){
        for(ImageView imageView:glowedImageViews){
            imageView.setEffect(null);
        }
        //it will run many times, we should clear first and draw again for every time
        glowedImageViews.clear();

        //if overlap forbidden element, return
        for(int row=0;row<squares.length;row++) {
            for (int col = 0; col < squares[row].length; col++) {
                //must not be null
                if(squares[row][col]==null){
                    continue;
                }
                int placedRow = placedLocation.getRow() + row;
                int placedCol = placedLocation.getColumn() + col;
                //must on board
                if(!board.isInBounds(placedRow,placedCol)){
                    continue;
                }

                Square square = board.getSingleSquare(placedRow,placedCol);
                //must not hav cat, fire, raft
                if(square.isHasCat() || square.isHasFire() || square.getCardType()== Card.CardType.RAFT){
                    return;
                }
            }
        }

        //draw the glow effect
        Glow glow = new Glow(1);
        for(int row=0;row<squares.length;row++){
            for (int col=0;col<squares[row].length;col++){
                int placedRow = placedLocation.getRow()+row;
                int placedCol = placedLocation.getColumn()+col;
                if(!board.isInBounds(placedRow,placedCol)){
                    continue;
                }
                if(squares[row][col]==null){
                    continue;
                }
                imageViews[placedRow][placedCol].setEffect(glow);
                glowedImageViews.add(imageViews[placedRow][placedCol]);
            }
        }
    }

    /**
     * glow one imageView of on the board
     *
     * @param row the placed row
     * @param col the placed col
     */
    public void glowSingleSquare(int row, int col){
        for(ImageView imageView:glowedImageViews){
            imageView.setEffect(null);
        }
        glowedImageViews.clear();

        if(!board.isInBounds(row,col)){
            return;
        }
        imageViews[row][col].setEffect(new Glow(1));
        glowedImageViews.add(imageViews[row][col]);
    }

    /**
     * glow the nodeList to show the path, each element of the node is the location
     * of the board, we should transfer to row and col
     *
     * @param nodeList the list of the location(with the form row * boardSizeCol + col)
     */
    public void glowSquaresByNodeList(ArrayList<Integer> nodeList){
        for(ImageView imageView:glowedImageViews){
            imageView.setEffect(null);
        }
        if(nodeList==null){
            return;
        }

        int row;
        int col;
        int node;
        Glow glow = new Glow(0.7);
        for(int i=0;i<nodeList.size();i++){
            node = nodeList.get(i);
            row = node/board.getSizeCol();
            col = node%board.getSizeCol();
            if (i == 0) {
                imageViews[row][col].setEffect(new Glow(1));
            }
            else {
                imageViews[row][col].setEffect(glow);

            }
            glowedImageViews.add(imageViews[row][col]);
        }
    }

    /**
     * clear the effect of all imageViews
     */
    public void clearGlowSquares(){
        for(ImageView imageView:glowedImageViews){
            imageView.setEffect(null);
        }
        glowedImageViews.clear();
    }

    /**
     * judge whether the board group is hinted
     *
     * @return true if the group is hinted, false otherwise
     */
    public boolean isHinted() { return isHinted; }
}

