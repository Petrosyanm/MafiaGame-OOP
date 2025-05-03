package game.player;

import game.Game;

public class Sherif extends Player{

    //Instance variables
    private boolean messageVisibility;
    private Game game;

    //Constructors
    public Sherif(Game game){
        super();
        messageVisibility = false;
        this.game = game;
    }

    //Setters and Getters
    public void setMessageVisibility(){
        messageVisibility = !messageVisibility;
    }

    private boolean getMessageVisibility(){
        return messageVisibility;
    }

    // Functionality
    public boolean findDon(int playerNumber){
        return game.getPlayers()[playerNumber] instanceof Don;
    }
}