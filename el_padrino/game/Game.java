package game;

import game.player.*;

import java.util.ArrayList;
import java.util.Random;

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
        for(int i = 0; i < players.length; i++){
            players[i] = new Player(i);
        }
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
        for(int i = 0; i < players.length; i++){
            players[i] = new Player(i);
        }
        talkingTurn = 0;
    }

    //Getters and Setters
    public void setNights(int nights) {
        this.nights = nights;
    }

    public int getNights(){
        return nights;
    }

    public void setRedsNumber(int redsNumber){
        this.redsNumber = redsNumber;
    }

    public int getRedsNumber(){
        return redsNumber;
    }

    public void setBlacksNumber(int blacksNumber){
        this.blacksNumber = blacksNumber;
    }

    public int getBlacksNumber(){
        return blacksNumber;
    }

    public void setPlayers(Player[] players){
        for (int i = 0; i < this.players.length; i++) {
            this.players[i] = players[i];
        }
    }

    public Player[] getPlayers() {
        Player[] playersCopy = new Player[players.length];
        for(int i = 0; i < players.length; i++){
            playersCopy[i] = players[i].clone();
        }

        return playersCopy;
    }


    // For saying last words after being killed
    public void setTalkingTurn(int talkingTurn){
        this.talkingTurn = talkingTurn;
    }

    public int getTalkingTurn(){
        return talkingTurn;
    }
    
    //Functionality
    public boolean checkWinner(){

    }

    public boolean isGameOver(){

    }

    public void distributeRoles(){
        ArrayList<Player> shuffledPlayers = new ArrayList<>();

        for(int i = 0; i < players.length; i++){
            shuffledPlayers.add(players[i]);
        }

        Random random = new Random();
        for(int i = shuffledPlayers.size() - 1; i > 0; i++){
            int j = random.nextInt(i + 1);

            Player tempPlayer = shuffledPlayers.get(i);
            shuffledPlayers.set(i, shuffledPlayers.get(j));
            shuffledPlayers.set(j, tempPlayer);
        }

        for(int i = 0; i < shuffledPlayers.size(); i++){
            Player player = shuffledPlayers.get(i);
            int number = player.getNumber();

            if(i == 0) players[i] = new Sherif(number);
            else if(i < redsNumber) players[i] = new Player(number);
            else if (i < shuffledPlayers.size() - 1) players[i] = new Black(number);
            else players[i] = new Don(number);
        }
    }

    public String checkRole(int playerNumber){
        //TODO: add exceptions??
        if(playerNumber >= 0 && playerNumber < players.length){
            if(players[playerNumber] instanceof Sherif) return "Sherif";
            else if(players[playerNumber] instanceof Don) return "Don";
            else if(players[playerNumber] instanceof Black) return "Black";
            else return "Player";
        }

        return null;
    }

    public void changeTurn(){
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

    public boolean checkTurn(int playerNumber){
        return getTalkingTurn() == playerNumber;
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("-Game State:\n");
        sb.append("Nights: ").append(nights).append("\n");
        sb.append("Reds: ").append(redsNumber).append(", Blacks: ").append(blacksNumber).append("\n");
        sb.append("Talking Turn: Player #").append(talkingTurn).append("\n");

        sb.append("-Players:\n");
        for (Player player : players) {
            sb.append(player).append("\n");
        }

        return sb.toString();
    }
}