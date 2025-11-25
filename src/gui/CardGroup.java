package gui;
import Card;
import Orientation;
import Square;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Inherits from the base class group.
 * represent a card group in this game.
 * the card group has the group and the card instance, and can link them together
 * it can easily draw the card squares to the group. and these function can be the method
 * of this class.
 * the card can be fireTile or pathwayCard.
 */
public class CardGroup extends Group {
    private Card card;
    private double originalX;
    private double originalY;
    private Group imageViewGroup;
    private ImageView imageViews[][];
    private Rectangle rect;
    private double offsetX;
    private double offsetY;
    boolean isShowed;

    /**
     * constructor of this class, first we should not show the card
     *
     * @param card the relevant card object
     */
    public CardGroup(Card card) {
        this.card = card;
        this.imageViewGroup = new Group();
        this.getChildren().add(imageViewGroup);
        int sizeRow = card.getSizeRow();
        int sizeCol = card.getSizeCol();
        double cardWidth = sizeCol*Game.GRID_CELL_WIDTH;
        double cardHeight = sizeRow*Game.GRID_CELL_HEIGHT;
        this.rect = new Rectangle(cardWidth,cardHeight);
        this.rect.setFill(Color.GRAY);
        this.rect.setOpacity(0);
        this.rect.setVisible(false);

        offsetX=0;
        offsetY=0;
        isShowed = false;

        //set the shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setColor(Color.GRAY);
        this.setEffect(dropShadow);
    }

    /**
     * hide the card, let the rect be added to the group and be visible
     * set the imageViews become invisible
     */
    public void hideCard(){
        this.rect.setVisible(true);
        //set the animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(Game.ANIMATION_TIME_MS),rect);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();


        this.getChildren().add(rect);

        if(imageViews!=null) {
            rect.setWidth(imageViews[0].length*Game.GRID_CELL_WIDTH);
            rect.setHeight(imageViews.length*Game.GRID_CELL_HEIGHT);

            for (int row = 0; row < imageViews.length; row++) {
                for (int col = 0; col < imageViews[0].length; col++) {
                    if (imageViews[row][col] != null) {
                        //set imageViews become invisible
                        imageViews[row][col].setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * show the imageViews and hide the rect, the card will represent the squares
     */
    public void showCard(){
        if(!isShowed){
            isShowed = true;
            //animation for hide the rect
            if(rect.isVisible()){
                FadeTransition fRect = new FadeTransition(Duration.millis(Game.ANIMATION_TIME_MS),rect);
                fRect.setFromValue(rect.getOpacity());
                fRect.setToValue(0);
                fRect.play();
            }
            //animation for appear the imageViews
            FadeTransition fImageView = new FadeTransition(Duration.millis(Game.ANIMATION_TIME_MS),imageViewGroup);
            fImageView.setFromValue(0);
            fImageView.setToValue(1.0);
            fImageView.play();
            fImageView.setOnFinished(event -> {
                //after finish the animation, let the rect become invisible
                this.rect.setVisible(false);
                this.getChildren().remove(rect);
            });
        }
        //this.rect.setVisible(false);
        Orientation orientation = card.getOrientation();
        boolean isFlipped = card.isFlipped();
        Square[][] squares;

        //get the 2D array squares, we should consider the situations of fireTile or pathwayCard
        if(card.getCardType() == Card.CardType.FIRETILE) {
            squares = card.getCardSquare(orientation, isFlipped);
        }
        else{
            squares = card.getCardSquare(orientation);
        }

        Image image;
        char state;

        int newSizeRow = squares.length;
        int newSizeCol = squares[0].length;

        //set imageViews and the image
        if(imageViews == null){
            imageViews = new ImageView[newSizeRow][newSizeCol];
            for (int row = 0; row < newSizeRow; row++) {
                for (int col = 0; col < newSizeCol; col++) {
                    imageViews[row][col] = new ImageView();
                    this.imageViewGroup.getChildren().add(imageViews[row][col]);
                    imageViews[row][col].setFitWidth(Game.GRID_CELL_WIDTH);
                    imageViews[row][col].setFitHeight(Game.GRID_CELL_HEIGHT);

                    imageViews[row][col].setX(col * Game.GRID_CELL_WIDTH);
                    imageViews[row][col].setY(row * Game.GRID_CELL_HEIGHT);
                }
            }
        }
        else {
            //create the imageView first
            int oldSizeRow = imageViews.length;
            int oldSizeCol = imageViews[0].length;

            if(newSizeRow != oldSizeRow || newSizeCol != oldSizeCol){
                this.imageViewGroup.getChildren().clear();
                imageViews = new ImageView[newSizeRow][newSizeCol];
                for (int row = 0; row < newSizeRow; row++) {
                    for (int col = 0; col < newSizeCol; col++) {
                        imageViews[row][col] = new ImageView();
                        this.imageViewGroup.getChildren().add(imageViews[row][col]);
                        imageViews[row][col].setFitWidth(Game.GRID_CELL_WIDTH);
                        imageViews[row][col].setFitHeight(Game.GRID_CELL_HEIGHT);

                        imageViews[row][col].setX(col * Game.GRID_CELL_WIDTH);
                        imageViews[row][col].setY(row * Game.GRID_CELL_HEIGHT);
                    }
                }
            }
        }

        //this truly draw the image
        for (int row = 0; row < newSizeRow; row++) {
            for (int col = 0; col < newSizeCol; col++) {
                imageViews[row][col].setVisible(true);
                imageViews[row][col].setImage(null);
                state = 0;
                if (squares[row][col] != null) {
                    state = squares[row][col].getStateChar();
                    image = Picture.getPicture(state);

                    //set each image view
                    if (image != null && imageViews[row][col] != null) {
                        FadeTransition fImage = new FadeTransition(Duration.millis(Game.ANIMATION_TIME_MS),imageViews[row][col]);
                        fImage.setFromValue(1.0);
                        fImage.setToValue(0);
                        imageViews[row][col].setImage(image);
                    }
                }
            }
        }
    }

    /**
     * set the offest X and Y, they are the offset distance between mouse pos and
     * this group layout position
     *
     * @return the offest X
     */
    public void setOffset(double offsetX,double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * set the linked card to this group
     *
     * @param card the linked card to this group
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * set the original pos, that the group can correct find the original pos and go back
     *
     * @param x the original pos x
     * @param y the original pos y
     */
    public void setOriginalPos(double x,double y){
        this.originalX = x;
        this.originalY = y;
    }

    /**
     * get the relevant card
     *
     */
    public Card getCard() {
        return card;
    }

    /**
     * get the original pos X
     *
     * @return the original pos X
     */
    public double getOriginalX() {
        return originalX;
    }

    /**
     * get the original pos Y
     *
     * @return the original pos Y
     */
    public double getOriginalY() {
        return originalY;
    }

    /**
     * get the offest X
     *
     * @return the offest X
     */
    public double getOffsetX() { return offsetX; }

    /**
     * get the offest Y
     *
     * @return the offest Y
     */
    public double getOffsetY() { return offsetY; }

    /**
     * directly return to the original layout position, just
     * set the original X and Y
     */
    public void returnToOriginalPos(){
        this.setLayoutX(originalX);
        this.setLayoutY(originalY);
    }
}
