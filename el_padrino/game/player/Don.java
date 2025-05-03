package game.player;

import game.Game;

public class Don extends Black{
    private Game game;

    //Constructors
    public Don(Game game){
        super();
        this.game = game;
    }

    //Functionality
    public boolean findSherif(int playerNumber){
        return game.getPlayers()[playerNumber] instanceof Sherif;
    }
}