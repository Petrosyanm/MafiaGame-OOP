package game.player;

public class Don extends Black{

    //Constructors
    public Don(){
        super();
    }

    //Functionality
    public boolean findSherif(int playerNumber, Player[] players){
        return players[playerNumber] instanceof Don;
    }

    @Override
    public String toString(){
        return "Sherif: " + super.toString();
    }
}