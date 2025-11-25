

/**
 * the core class for this game, board, card, island, fire, all have it, it can be used in many class in this game
 */
public class Square {
    private char stateChar;

    /*
     * inner location, just represent the position in the card. not whole position
     * when place the card on the whole board, we just set the square[][] to refer the place square
     */
    private final Location location;
    private final Card card;
    private final Color color;                  //color, maybe NONE of wile(w) or fire(f)
    private Card.CardType cardType;       //may not have type, like the islandBoard
    private boolean isHasCat;             //flag of cat
    private boolean isHasFire;            //flag Of fire
    private boolean isWildForRaft;        //for raft card, the wild area of the raftCard, give the flag
    private boolean isObjectiveRaft;      //for raft card, the center do not have color, just o

    /**
     * constructor for the normal card, should set the card and color
     *
     * @param location: square inner location
     * @param card: the instance of a card - there is raft card
     * @param color: If has color,it does. if the square is o or w, the color is NONE
     */
    public Square(Location location, Card card, Color color){
        this.location = location;
        this.card = card;
        this.color = color;
        if(this.card != null){
            this.cardType = card.getCardType();
        }
        else{
            this.cardType = Card.CardType.NONE;
        }
        this.isHasCat = false;
        this.isHasFire = false;
        this.isObjectiveRaft = false;
        this.isWildForRaft = false;
        this.stateChar = color.toChar(false);
    }

    /**
     * constructor for empty square Board, challenge initialization
     * it is not have anything when initialized, just location
     *
     * @param location: square inner location
     */
    public Square(Location location){
        this.stateChar = 'n';
        this.location = location;
        this.card = null;
        this.color = Color.NONE;
        this.cardType = Card.CardType.NONE;
        this.isHasCat = false;
        this.isHasFire = false;
        this.isWildForRaft = false;
        this.isObjectiveRaft = false;
    }

    /**
     * constructor for whole square Board, For string initialization
     * any information is created from the char, can know the color,
     * fire, wild or objective part of the square
     *
     * @param location square inner location
     * @param squareState the state char of this square
     */
    public Square(Location location, char squareState){
        this.stateChar = squareState;
        this.location = location;
        this.card = null;
        this.color = Color.charToColor(squareState);
        this.cardType = Card.CardType.NONE;
        switch (squareState){
            case 'B','R','Y','P','G','W'-> {
                this.isHasCat = true;
            }
            default -> {
                this.isHasCat = false;
            }
        }
        this.isHasFire = (squareState == 'f');
        this.isWildForRaft = (Character.toLowerCase(squareState) == 'w');
        this.isObjectiveRaft = (squareState=='o');
    }

    /**
     * action for removing cat from the square, just set the square be the lowercase letter
     */
    public void removeCat(){
        isHasCat = false;
        stateChar = Character.toLowerCase(stateChar);
    }

    /**
     * action for add cat to the square, just set the square be the uppercase letter
     */
    public void addCat(){
        isHasCat = true;
        stateChar =Character.toUpperCase(stateChar);
    }

    /**
     * get the location of the square, it is the inner property
     *
     * @return inner square location
     */
    public Location getLocation() { return location; }

    /**
     * get the card that the square belongs to
     *
     * @return the card that the square belongs to
     */
    public Card getCard() { return card; }

    /**
     * get the color of the square
     *
     * @return the color of the square
     */
    public Color getColor() { return color; }

    /**
     * get the cardType of the square
     *
     * @return the cardType of the square
     */
    public Card.CardType getCardType() { return cardType; }

    /**
     * get the stateChar of the square
     *
     * @return the stateChar of the square
     */
    public char getStateChar() { return stateChar; }

    /**
     * judge whether the square has a cat
     *
     * @return true if the square has a cat, false otherwise
     */
    public boolean isHasCat() { return isHasCat; }
    /**
     * judge whether the square has a cat
     *
     * @return true if the square has a cat, false otherwise
     */
    public boolean isHasFire(){return isHasFire;}
    /**
     * judge whether the square is the wild part of the raft
     *
     * @return true if the square is the wild part of the raft, false otherwise
     */
    public boolean isWildForRaft() { return isWildForRaft; }
    /**
     * judge whether the square is the objective part of the raft
     *
     * @return true if the square is the objective part of the raft, false otherwise
     */
    public boolean isObjectiveRaft() { return isObjectiveRaft; }

    /**
     * set the square has a cat or not
     *
     * @param hasCat the flag of hasCat,
     *               if true set the square stateChar to upperCase letter
     *               if false set the square stateChar to lowercase letter
     */
    public void setHasCat(boolean hasCat) {
        this.isHasCat = hasCat;
        this.stateChar = hasCat? Character.toUpperCase(this.stateChar):Character.toLowerCase(this.stateChar);
    }

    /**
     * set the square has fire or not
     *
     * @param hasFire the flag of hasFire
     *               if true set the square stateChar to 'f'
     */
    public void setHasFire(boolean hasFire){
        this.isHasFire = hasFire;
        if(hasFire){
            this.stateChar = 'f';
        }
    }

    /**
     * set the square is wild part or not
     *
     * @param isWildForRaft the flag of isWildForRaft
     *               if true set the square stateChar to 'w'
     */
    public void setIsWildForRaft(boolean isWildForRaft){
        this.isWildForRaft = isWildForRaft;
        if(isWildForRaft){
            this.stateChar = 'w';
        }
    }

    /**
     * set the square is objective part or not
     *
     * @param isObjectiveRaft the flag of isObjectiveRaft
     *               if true set the square stateChar to 'o'
     */
    public void setIsObjectiveRaft(boolean isObjectiveRaft){
        this.isObjectiveRaft = isObjectiveRaft;
        if(isObjectiveRaft){
            this.stateChar = 'o';
        }
    }

    /**
     * set the card type of the square belong to
     *
     * @param type the card type of the square belong to
     */
    public void setCardType(Card.CardType type){card.cardType = type;}

    /**
     * set the state char that the square is
     *
     * @param state the char of the state of the square
     */
    public void setState(char state) { this.stateChar = state;}

    /**
     * overwrite the toString method to output the square information
     * (stateChar, location, cardType, color, isHasFire, is objectiveRaft, isWild)
     *
     * @return the square state string
     */
    @Override
    public String toString() {
        return stateChar+""+location+ " " +cardType + " " +color+ " " + "fire:"+isHasFire + " " + " o:"+isObjectiveRaft+ " w:"+isWildForRaft;
    }
}
