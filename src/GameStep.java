

import gui.Game;
/**
 * this class is store the game process and decide the next step and the state of the game(win or loss)
 * also store the every flag that we should need
 */
public class GameStep {
    private GameRun gameRun;
    private boolean isWin;
    private boolean isLoss;
    private boolean needDrawPathwayCard;    //must follow the NONE,or RESET
    private boolean needPlacePathwayCard;   //user choose
    private boolean needDiscardPathwayCard; //must follow the moveCat

    private boolean needDrawFireTile;       //must follow draw pathWayCard
    private boolean needPlaceFireTile;      //must follow the drawFireTile
    private boolean needMoveCat;            //user choose
    private StateType stateType;
    private StateType preStateType;
    private boolean stateChanged;
    private String endString;

    /**
     * at beginning, the state must be REST(create from challenge) or NONE(create from state String)
     * at REST                  --->DRAW_PATH
     * at DRAW_PATH             --->PLAY_PATH_OR_MOVE_CAT
     * at PLAY_PATH_OR_MOVE_CAT --->DRAW_FIRE, DISCARD_PATH
     * at DRAW_FIRE             --->PLAY_FIRE
     * at PLAY_FIRE             --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH---go DRAW_PATH means rest
     * at DISCARD_PATH          --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH---go DRAW_PATH means rest
     * at any                   ---> can go to WINN or LOSS
     */
    public enum StateType{
        DRAW_PATH, PLAY_PATH_OR_MOVE_CAT, DRAW_FIRE, PLAY_FIRE, DISCARD_PATH , RESET , WIN, LOSS, NONE;

        /**
         * overwrite toString method to output the string of every step
         *
         * @return the state string of different phase
         */
        public String toString(){
            switch(this){
                case DRAW_PATH -> {
                    return "Draw Pathway Card";
                }
                case PLAY_PATH_OR_MOVE_CAT -> {
                    return "Place Pathway Card or Move Cat";
                }
                case DRAW_FIRE -> {
                    return "Draw Fire Tile";
                }
                case PLAY_FIRE -> {
                    return "Place Fire Tile";
                }
                case DISCARD_PATH -> {
                    return "Discard Pathway Card";
                }
                case WIN -> {
                    return "WIN!\nGame End";
                }
                case LOSS -> {
                    return "LOSS!\nGame End";
                }
                default -> {
                    return "NONE";
                }
            }
        }
    }

    /**
     * it is the constructor of game step, use the state of game to create the state of current step of the game
     *
     * @param gameRun the gameRun value that store all values, objects and method about the game
     */
    public GameStep(GameRun gameRun){
        this.gameRun = gameRun;
        isWin=false;
        isLoss=false;
        if(gameRun.getChallenge()!=null){
            stateType = StateType.RESET;
        }
        else {
            stateType = StateType.NONE;
        }
        stateChanged = false;
        endString = "";
        updateStateType();
    }

    /**
     * get the current state type of the game, can know what phase the game is
     *
     * @return stateType of the game
     */
    public StateType getStateType() {
        return stateType;
    }

    /**
     * get the endString of the game, if the game is over, we can read this string
     * it is about the information of the end game
     *
     * @return endString of the game
     */
    public String getEndString(){
        return endString + "\nPlease click the RESET button to restart the game again.";
    }

    /**
     * judgement about whether the state has been changed
     *
     * @return true if the state has been changed, false otherwise
     */
    public boolean isStateChanged() {  return stateChanged; }

    /**
     * judgement about whether the game is won
     *
     * @return true if the game is won
     */
    public boolean isWin() { return isWin; }

    /**
     * judgement about whether the game is loss
     *
     * @return true if the game is loss
     */
    public boolean isLoss(){ return isLoss; }

    /**
     * set the flag about need of placing a pathwayCard
     *
     * @param needPlacePathwayCard give true if we needto placePathwayCard, false means needn't
     */
    public void setNeedPlacePathwayCard(boolean needPlacePathwayCard) {   this.needPlacePathwayCard = needPlacePathwayCard;  }

    /**
     * set the flag about need  of moving a cat
     *
     * @param needMoveCat give true if we need to move a cat, false means needn't
     */
    public void setNeedMoveCat(boolean needMoveCat) { this.needMoveCat = needMoveCat; }

    /**
     * set the flag about need of placing a fireTile
     *
     * @param needPlaceFireTile give true if we need to place a fireTile, false means needn't
     */
    public void setNeedPlaceFireTile(boolean needPlaceFireTile) { this.needPlaceFireTile = needPlaceFireTile; }

    /**
     * set the flag about need of drawing a pathwayCard
     *
     * @param needDrawPathwayCard give true if we need to draw a pathwayCard, false means needn't
     */
    public void setNeedDrawPathwayCard(boolean needDrawPathwayCard) { this.needDrawPathwayCard = needDrawPathwayCard; }

    /**
     * set the state type of the game, let the game be the state that we set.
     */
    public void setStateType(StateType stateType) { this.stateType = stateType; }

    /**
     *
     * every action of the game, we should run this method to update the values in the gameStep
     * use updateStateType can cover all situations, just use this method for all action, it is convenient
     * the following is about the process sequence of the game
     *
     * at beginning, the state must be REST(create from challenge) or NONE(create from state String)
     * at NONE                  --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH
     * at REST                  --->DRAW_PATH
     * at DRAW_PATH             --->DRAW_PATH or PLAY_PATH_OR_MOVE_CAT
     * at PLAY_PATH_OR_MOVE_CAT --->DRAW_FIRE, DISCARD_PATH
     * at DISCARD_PATH          --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH
     * at DRAW_FIRE             --->PLAY_FIRE
     * at PLAY_FIRE             --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH
     * at any                   ---> can go to WINN or LOSS
     */
    public void updateStateType(){
        preStateType = stateType;
        //should first judge win or loss. if win or loss, the following switch will not run
        switch (stateType){
            case NONE       ->  stateType = changeStateFromNONE();
            case RESET      ->  stateType = changeStateFromRESET();
            case DRAW_PATH  ->  stateType = changeStateFromDrawPath();
            case PLAY_PATH_OR_MOVE_CAT  ->  stateType = changeStateFromPlayPathOrMoveCat();
            case DISCARD_PATH -> stateType = changeStateFromDisCardPath();
            case DRAW_FIRE  ->  stateType = changeStateFromDrawFire();
            case PLAY_FIRE  ->  stateType = changeStateFromPlayFire();
            default         ->  {}
        }

        //if won, set the state become win
        if(judgeWin()) {
            stateType = StateType.WIN;
            isWin = true;
            isLoss = false;
        }
        //if loss, set the state become loss
        if(judgeLoss()){
            stateType = StateType.LOSS;
            isLoss = true;
            isWin = false;
        }

        //set the flag of whether the stateChanged
        if(preStateType != stateType){
            stateChanged = true;
        }
        else {
            stateChanged = false;
        }
    }

    /**
     * judgement about whether the game is won, all cats on the raft
     *
     * @return true if the game is won, false otherwise
     */
    public boolean judgeWin(){
        Location catLocation;
        for(Cat cat: gameRun.getCats()){
            catLocation = cat.getPlacedLocation();
            Square square = gameRun.getBoard().getSingleSquare(catLocation);

            //one is not on raft card
            if(square.getCardType() != Card.CardType.RAFT){
                return false;
            }
            else {
                if(square.getColor()!=Color.NONE && !square.getColor().equals(cat.getColor())){
                    return false;
                }
            }
        }
        //all on the raft card
        endString = "All cats are on the raftCard";
        return true;
    }

    /**
     * judgement about whether the game is loss,
     * need to place a fireTile, but no place to play
     * on cat has no way to the raft
     * need draw pathwayCard but all decks are empty
     * need draw fireTile but no fireTile in firTileBag
     *
     * @return true if the game is loss, false otherwise
     */
    public boolean judgeLoss(){
        //can not place a fire tile
        if(needPlaceFireTile) {
            if(gameRun.isNoPlaceForFireTile(gameRun.getDrawnFireTile())) {
                endString = "Need placeFireTile, but there is no place to place the fireTire.";
                return true;
            }
        }

        //no way for a cat to reach the raft,this should judge when preState is playFire,
        //because this method should take too long time
        //just save time, not every time judge it
        if(preStateType == StateType.PLAY_FIRE){
            if(gameRun.isNowayForOneCatToRaft(true)) {
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append("Cat: ");
                for(Cat cat:gameRun.getCats()){
                    if(!cat.isCanGetToRaft()){
                        stringBuffer.append(cat.getPlacedLocation()+", "+cat.getColor());
                    }
                }
                endString = stringBuffer.toString()+"\nhas no way to reach the raft.";
                return true;
            }
        }

        //no more pathwayCard remaining and the player required to draw one
        if(needDrawPathwayCard
                && gameRun.getDecks()[0].isEmpty()
                && gameRun.getDecks()[1].isEmpty()
                && gameRun.getDecks()[2].isEmpty()
                && gameRun.getDecks()[3].isEmpty())
        {
            endString = "Need draw pathWayCard, but all decks are empty.";
            return true;
        }

        //no fireTile in the bag and the player should draw a fireTile
        if(needDrawFireTile && gameRun.getFireTileBag().isEmpty()){
            endString = "Need draw fireTile, but fireTileBag is empty.";
            return true;
        }

        return false;
    }

    /**
     * determine the next stateType of the game, current stateType is NONE
     * at NONE                  --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromNONE(){
        needDrawPathwayCard = false;
        needPlacePathwayCard = false;
        needDiscardPathwayCard = false;
        needDrawFireTile = false;       //must follow draw pathWayCard
        needPlaceFireTile = false;      //must follow the drawFireTile
        needMoveCat = false;            //user choose

        //hand not empty--can PLAY_PATH_OR_MOVE_CAT
        if(!gameRun.getHand().isEmpty()){
            return StateType.PLAY_PATH_OR_MOVE_CAT;
        } else {
            //hand empty--need to draw
            needDrawPathwayCard = true;
            return StateType.DRAW_PATH;
        }
    }

    /**
     * determine the next stateType of the game, current stateType is DRAW_PATH
     * At DRAW_PATH --->PLAY_PATH_OR_MOVE_CAT
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromDrawPath(){
        //draw 6 card then go to action
        if(gameRun.getHand().getCardNum()==6) {
            needDrawPathwayCard = false;
            return StateType.PLAY_PATH_OR_MOVE_CAT;
        }
        return StateType.DRAW_PATH;
    }

    /**
     * determine the next stateType of the game, current stateType is PLAY_PATH_OR_MOVE_CAT
     * At PLAY_PATH_OR_MOVE_CAT --->DRAW_FIRE, DISCARD_PATH
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromPlayPathOrMoveCat(){
        //user choose which action
        if (needPlacePathwayCard){
            needPlacePathwayCard = false;
            needDrawFireTile = true;
            return StateType.DRAW_FIRE;
        }
        if (needMoveCat){
            needMoveCat = false;
            needDiscardPathwayCard = true;
            return StateType.DISCARD_PATH;
        }
        return StateType.PLAY_PATH_OR_MOVE_CAT;
    }

    /**
     * determine the next stateType of the game, current stateType is DISCARD_PATH
     * At DISCARD_PATH          --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromDisCardPath(){
        //if hand is empty, we should draw card
        if(gameRun.getHand().isEmpty()){
            needDiscardPathwayCard = false;
            needDrawPathwayCard = true;
            return StateType.DRAW_PATH;
        }else{
            //this is not empty
            //continue the play card or move cat
            //must judge whether we should continue discard the card
            if(!gameRun.needDisCard()){
                needDiscardPathwayCard = false;
                return StateType.PLAY_PATH_OR_MOVE_CAT;
            }
        }

        return StateType.DISCARD_PATH;
    }

    /**
     * determine the next stateType of the game, current stateType is DRAW_FIRE
     * At DRAW_FIRE             --->PLAY_FIRE
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromDrawFire(){
        needDrawFireTile = false;
        needPlaceFireTile = true;
        return StateType.PLAY_FIRE;
    }

    /**
     * determine the next stateType of the game, current stateType is PLAY_FIRE
     * At PLAY_FIRE             --->PLAY_PATH_OR_MOVE_CAT or DRAW_PATH
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromPlayFire(){
        needPlaceFireTile = false;
        //if the hand is empty, we should draw
        if(gameRun.getHand().isEmpty()) {
            needDrawPathwayCard = true;
            return StateType.DRAW_PATH;
        }
        return StateType.PLAY_PATH_OR_MOVE_CAT;
    }

    /**
     * determine the next stateType of the game, current stateType is REST
     * At REST                  --->DRAW_PATH
     *
     * @return the stateType that the next step should be
     */
    private StateType changeStateFromRESET(){
        needDrawPathwayCard = true;

        needPlacePathwayCard = false;   //user choose
        needDiscardPathwayCard = false; //must follow the moveCat

        needDrawFireTile = false;       //must follow draw pathWayCard
        needPlaceFireTile = false;      //must follow the drawFireTile
        needMoveCat = false;            //user choose
        return StateType.DRAW_PATH;
    }

    /**
     * get the hint string from the different stateType
     *
     * @return the hint string to hint the user what he should do next
     */
    public String getHint(){
        switch(stateType){
            case DRAW_PATH -> {
                return "Click on Decks to Draw 6 Cards";
            }
            case PLAY_PATH_OR_MOVE_CAT -> {
                return "Press and Drag the Cat or Pathway Card to move or place on the Board, Press 'R' to rotate card";
            }
            case DRAW_FIRE -> {
                return "Click on the Fire Tile Bag to Draw 1 Tile";
            }
            case PLAY_FIRE -> {
                return "Press and Drag Fire Tile to place on the Board. Press 'T' and 'R' to Flip and Rotate Fire Tile";
            }
            case DISCARD_PATH -> {
                return "Press and Drag Pathway Card to Bin or Press 'D' to Discard"+(gameRun.getNeedDisCardNum()==2?" 2 cards":" 1 card");
            }
            case WIN, LOSS -> {
                return "Please Click RESET to Restart, Choose Difficulty or State String";
            }
            default -> {
                return "NONE";
            }
        }
    }

    @Override
    /**
     * overwrite the toString method to output the value of gameStep, consider the stateType
     * and the flag of need of all action
     *
     * @return string of the gameStep
     */
    public String toString() {
        return stateType
                +"    needDrawPathwayCard: "+ needDrawPathwayCard
                +"    needPlacePathwayCard: "+ needPlacePathwayCard
                +"    needDiscardPathwayCard: "+ needDiscardPathwayCard
                +"    needDrawFireTile: "+ needDrawFireTile
                +"    needPlaceFireTile: "+ needPlaceFireTile
                +"    needMoveCat: "+ needMoveCat;

    }

    /**
     * inner main method to make the test, check whether this method is well-designed
     *
     * @param args
     */
    public static void main(String[] args) {
        GameRun gameRun= new GameRun(0);
        GameStep gameStep = new GameStep(gameRun);

        System.out.println(gameStep);
    }
}
