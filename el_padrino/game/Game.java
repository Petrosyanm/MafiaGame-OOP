package game;

import game.player.Player;

import java.util.ArrayList;

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
        int turn = getTalkingTurn();
        int nextTurn = (turn + 1) % players.length;

        while(nextTurn != turn){
            if(players[nextTurn].isAlive() && players[nextTurn].canSpeak()){
                talkingTurn = nextTurn;
                return;
            }

            nextTurn = (nextTurn + 1) % players.length;
        }
    }

    // For saying last words after being killed
    public void setTalkingTurn(int talkingTurn){
        this.talkingTurn = talkingTurn;
    }

    public int getTalkingTurn(){

    }
    
    //Functionality
    public boolean checkWinner(){

    }

    public boolean isGameOver(){

    }

    public void distributeRoles(){

    }

    public String checkRole(int playerNumber){

    }

    public int checkTurn(int playerNumber){

    }

//    TODO: Maybe we should return the index of the player and not the player itself
    public Player decideVictim(){
        int maxVotes = 0;
        ArrayList<Player> candidates = new ArrayList<>();

        for(Player player : players){
            int votes = player.getVoteNumber();

            if(votes > maxVotes){
                maxVotes = votes;
                candidates.clear();
                candidates.add(player);
            }
            else if(votes == maxVotes){
                candidates.add(player);
            }
        }

        clearVotes();

        if(candidates.size() == 1) return candidates.get(0);

        // If more than one player has the highest votes or everyone has 0 vote, no one is killed; voting / killing should be repeated elsewhere based on this null return
        else return null;
    }

    public void clearVotes(){
        for(Player player : players){
            player.setVoteNumber(0);
        }
    }
}