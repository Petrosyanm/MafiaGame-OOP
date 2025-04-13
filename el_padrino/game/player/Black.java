package game.player;

public class Black extends Player{

    //Instance variables
    private boolean messageVisibility;

    //Constructors
    public Black(){
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

    //Functionality
    public void killPlayer(int playerNumber){

    }
}