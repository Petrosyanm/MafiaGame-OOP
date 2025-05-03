package game.player;

import game.Game;

public class Sherif extends Player{

    //Instance variables
    private boolean messageVisibility;

    //Constructors
    public Sherif(){
        super();
        messageVisibility = false;
    }

    //Setters and Getters
    public void setMessageVisibility(){
        messageVisibility = !messageVisibility;
    }

    private boolean getMessageVisibility(){
        return messageVisibility;
    }

    // Functionality
    public boolean findDon(int playerNumber, Player[] players){
        return players[playerNumber] instanceof Don;
    }
}