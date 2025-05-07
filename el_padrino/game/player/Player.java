package game.player;

import java.io.Serializable;

public class Player implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    public static int PLAYERS_NUMBER;
    //Instance variables
    private int number;
    private int remarksNumber;
    private boolean canSpeak = true;
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
        number = PLAYERS_NUMBER - 1;
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

    // Getters and Setters

    //Setting the player's number
    public void setNumber(int number) {
        this.number = number;
    }

    //Getting the player's number
    public int getNumber(){
        return number;

    }

    //Setting the number of remarks
    public void setRemarksNumber(int remarksNumber){
        this.remarksNumber = remarksNumber;

        if(remarksNumber == 2) setCanSpeak(false);
        else if(remarksNumber >= 3) setIsAlive(false);
    }

    //Getting the number of remarks
    public int getRemarksNumber(){
        return remarksNumber;
    }

    //Setting the number of votes
    public void setVoteNumber(int voteNumber){
        this.voteNumber = voteNumber;
    }

    //Getting the number of votes
    public int getVoteNumber(){
        return voteNumber;
    }

    //Setting the status of the player(dead or alive)
    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    //Getting the status of the player(dead or alive)
    public boolean isAlive() {
        return isAlive;
    }

    // Setting if the player can speak
    public void setCanSpeak(boolean canSpeak){
        this.canSpeak = canSpeak;
    }


    // Getting if the player can speak, e.g. whether the number of remarks reach 3
    public boolean canSpeak(){
        return canSpeak;
    }

    @Override
    public String toString(){
        return "Player #" + number +
                " {Alive: " + isAlive +
                ", Can Speak: " + canSpeak +
                ", Remarks: " + remarksNumber +
                ", Votes: " + voteNumber +
                ", Message: " + (message != null ? message : "None") + "}";
    }
}