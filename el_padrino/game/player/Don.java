package game.player;

public class Don extends Black{

    //Constructors
    public Don(){
        super();
    }

    public Don(int number){
        super(number);
    }

    //Functionality
    public boolean findSherif(int playerNumber, Player[] players){
        return players[playerNumber] instanceof Sherif;
    }

    @Override
    public String toString(){
        return "Don: " + super.toString();
    }
}