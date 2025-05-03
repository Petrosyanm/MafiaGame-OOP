package game;

import game.player.Player;

public class Game implements Cloneable{
    //Instance Variables
    private int nights;
    private int redsNumber;
    private int blacksNumber;
    private Player[] players;
    private int talkingTurn;

    //Constructors
    public Game(){
        nights = 0;
        redsNumber = 7;
        blacksNumber = 3;
        players = new Player[10];
        talkingTurn = 0;
    }

    public Game(Game that){
        this.nights = that.nights;
        this.redsNumber = that.redsNumber;
        this.blacksNumber = that.blacksNumber;
        this.talkingTurn = that.talkingTurn;

        this.players = new Player[that.players.length];
        for(int i = 0; i < players.length; i++){
            players[i] = that.players[i].clone();
        }
    }

    public Game(int playersNumber, int blacksNumber){
        nights = 0;
        redsNumber = playersNumber - blacksNumber;
        this.blacksNumber = blacksNumber;
        players = new Player[playersNumber];
        talkingTurn = 0;
    }

    //Getters and Setters
    public void setNights(){

    }

    public int getNights(){

    }

    public void setRedsNumber(){

    }

    public int getRedsNumber(){

    }

    public void setBlacksNumber(){

    }

    public int getBlacksNumber(){

    }

    public void setPlayers(){

    }

    public Player[] getPlayers() {
        Player[] playersCopy = new Player[players.length];
        for(int i = 0; i < players.length; i++){
            playersCopy[i] = players[i].clone();
        }

        return playersCopy;
    }

    public void setTalkingTurn(){

    }

    public int getTalkingTurn(){

    }
    
    //Functionality
    public boolean checkWinner(){

    }

    public boolean isGameOver(){

    }

    public String checkRole(int playerNumber){

    }

    public int changeTurn(int playerNumber){

    }

    public int checkTurn(int playerNumber){

    }

    public void decideVictim(){

    }

    public void distributeRoles(){

    }
}