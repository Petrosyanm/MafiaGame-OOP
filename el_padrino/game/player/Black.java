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
    public void killPlayer(int playerNumber, Player[] players){
//      TODO: Prevented self-voting / self-killing
        if(playerNumber >= 0 && playerNumber < players.length && players[playerNumber].isAlive() && playerNumber != this.getNumber()){
            players[playerNumber].setVoteNumber(players[playerNumber].getVoteNumber() + 1);
        }
    }

    @Override
    public String toString(){
        return "Black: " + super.toString();
    }
}