package gui;

import Cat;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Inherits from the base class group.
 * represent a cat group in this game.
 * the cat group has the group and the cat instance, and can link them together
 * it can easily draw the cat to the group, and show the state. and these
 * function can be the method of this class.
 */

public class CatGroup extends Group {
    private Cat cat;
    private double lastX;
    private double lastY;
    private Text text;
    private Rectangle rect;
    private ImageView imageView;
    double offsetX;
    double offsetY;

    /**
     * constructor of this class, directly show the cat and label that represent
     * the exhausted information of the cat
     *
     * @param cat the relevant cat object
     */
    public CatGroup(Cat cat) {
        this.cat = cat;
        this.lastX = cat.getPlacedLocation().getColumn();
        this.lastY = cat.getPlacedLocation().getRow();
        offsetX=0;
        offsetY=0;

        //animation to show, appear
        this.setOpacity(0);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(Game.ANIMATION_TIME_MS),this);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        text = new Text("Rested");
        text.setFont(new Font(10));
        text.setLayoutX(3);
        text.setLayoutY(12);
        rect= new Rectangle();
        rect.setFill(Color.WHITE);
        rect.setWidth(35);
        rect.setHeight(15);

        //draw the cat
        this.imageView = new ImageView();
        this.imageView.setFitWidth(Game.GRID_CELL_WIDTH);
        this.imageView.setFitHeight(Game.GRID_CELL_HEIGHT);
        Image image = Picture.getPicture(cat.getColor().toChar(true));

        //set each image view
        if (image != null) {
            imageView.setImage(image);
        }

        this.getChildren().add(text);
        this.getChildren().add(rect);
        this.getChildren().add(imageView);

        rect.toFront();
        text.toFront();

        //set the shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setColor(Color.GRAY);
        this.setEffect(dropShadow);

        showState();
    }

    /**
     * show the state of the cat, a cat can have three status,
     * exhausted, not-exhausted, arrived(not consider the other two state),
     * just write in a method, can fit usage situation.
     */
    public void showState(){
        if(cat.isArrived()){
            text.setText("Arrive");
            text.setFill(Color.BLACK);
            rect.setFill(Color.WHITE);
            this.setOpacity(0.6);
            this.imageView.setEffect(null);
        }
        else if(cat.isExhausted()) {
            text.setText("Tired");
            text.setFill(Color.WHITE);
            rect.setFill(Color.BLACK);
            this.setEffect(new Glow(0.2));
        }
        else if(!cat.isExhausted()){
            text.setText("Rested");
            text.setFill(Color.BLACK);
            rect.setFill(Color.WHITE);
            this.setEffect(new Glow(0.4));
        }
    }

    /**
     * set and store the last position of the cat, if movement is not valid
     * the cat can return to this position
     *
     * @param x last position of x
     * @param y last position of y
     */
    public void setLastPos(double x,double y){
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * set and store the offset distance of between layout and mouse,
     * can let the group follow the mouse
     *
     * @param offsetX the offset distance X between layout and mouse
     * @param offsetY the offset distance Y between layout and mouse
     */
    public void setOffset(double offsetX,double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * get the last position X
     *
     * @return the last position X
     */
    public double getLastX() {
        return lastX;
    }

    /**
     * get the last position Y
     *
     * @return tht last position Y
     */
    public double getLastY() {
        return lastY;
    }

    /**
     * get the linked cat
     *
     * @return the linked cat
     */
    public Cat getCat() {
        return cat;
    }

    /**
     * get the offset of X, can let the cat follow the mouse
     *
     * @return the offset distance of X
     */
    public double getOffsetX() { return offsetX; }

    /**
     * get the offset of Y, can let the cat follow the mouse
     *
     * @return the offset distance of Y
     */
    public double getOffsetY() { return offsetY; }

    /**
     * if the movement is not valid, the group can return to the
     * last position
     */
    public void returnToLastPos(){
        this.setLayoutX(lastX);
        this.setLayoutY(lastY);
    }
}
