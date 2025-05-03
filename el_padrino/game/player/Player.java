package game.player;

public class Player implements Cloneable{
    public static int PLAYERS_NUMBER;
    //Instance variables
    private int number;
    private int remarksNumber;
    private boolean isAlive;
    private int voteNumber;
    private String message;

    //Constructors
    public Player(int number){
        PLAYERS_NUMBER++;
        this.number = number;
        isAlive = true;
        remarksNumber = 0;
        voteNumber = 0;
        message = null;
    }
    public Player(){
        PLAYERS_NUMBER++;
        number = PLAYERS_NUMBER;
        isAlive = true;
        remarksNumber = 0;
        voteNumber = 0;
        message = null;
    }
    public Player(Player that){
        PLAYERS_NUMBER++;
        this.number = that.number;
        this.isAlive = that.isAlive;
        this.remarksNumber = that.remarksNumber;
        this.voteNumber = that.voteNumber;
        this.message = that.message;
    }

    @Override
    public Player clone(){
        try{
            return (Player) super.clone();
        } catch (CloneNotSupportedException e){
            return null; //never reached
        }
    }

    //Setters
    //Setting the player's number
    public void setNumber(int number) {
        this.number = number;
    }

    //Setting the number of remarks
    public void setRemarksNumber(int remarksNumber){
        this.remarksNumber = remarksNumber;
    }

    //Setting the number of votes
    public void setVoteNumber(int voteNumber){
        this.voteNumber = voteNumber;
    }

    //Setting the status of the player(dead or alive)
    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    //Getters
    //Getting the player's number
    public int getNumber(){
        return number;

    }
    //Getting the number of remarks
    public int getRemarksNumber(){
        return remarksNumber;
    }

    //Getting the number of votes
    public int getVoteNumber(){
        return voteNumber;
    }

    //Getting the status of the player(dead or alive)
    public boolean isAlive() {
        return isAlive;
    }
}