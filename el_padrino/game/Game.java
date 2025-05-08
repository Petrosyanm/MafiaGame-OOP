
package game;

import game.player.*;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Cloneable {
    private int nights;
    private int redsNumber;
    private int blacksNumber;
    private Player[] players;
    private int talkingTurn;
    private int gameCode;

    public Game() {
        nights = 0;
        redsNumber = 7;
        blacksNumber = 3;
        players = new Player[10];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i);
        }
        talkingTurn = 0;
    }

    public Game(int playersNumber, int blacksNumber) {
        nights = 0;
        redsNumber = playersNumber - blacksNumber;
        this.blacksNumber = blacksNumber;
        players = new Player[playersNumber];
        for (int i = 0; i < players.length; i++) {
            players[i] = null; // Initialize as null, players added on join
        }
        talkingTurn = 0;
    }

    public void setNights(int nights) { this.nights = nights; }
    public int getNights() { return nights; }

    public void setRedsNumber(int redsNumber) { this.redsNumber = redsNumber; }
    public int getRedsNumber() { return redsNumber; }

    public void setBlacksNumber(int blacksNumber) { this.blacksNumber = blacksNumber; }
    public int getBlacksNumber() { return blacksNumber; }

    public void setPlayers(Player[] players) {
        for (int i = 0; i < this.players.length; i++) {
            this.players[i] = players[i];
        }
    }

    public Player[] getPlayers() {
        Player[] playersCopy = new Player[players.length];
        for (int i = 0; i < players.length; i++) {
            playersCopy[i] = players[i] == null ? null : players[i].clone();
        }
        return playersCopy;
    }

    public void setTalkingTurn(int talkingTurn) { this.talkingTurn = talkingTurn; }
    public int getTalkingTurn() { return talkingTurn; }

    public void setGameCode(int gameCode) { this.gameCode = gameCode; }
    public int getGameCode() { return gameCode; }

    public void distributeRoles() {
        ArrayList<Player> activePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p != null) activePlayers.add(p);
        }

        Random random = new Random();
        for (int i = activePlayers.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Player temp = activePlayers.get(i);
            activePlayers.set(i, activePlayers.get(j));
            activePlayers.set(j, temp);
        }

        for (int i = 0; i < activePlayers.size(); i++) {
            int num = activePlayers.get(i).getNumber();
            if (i == 0) players[num] = new Sherif(num);
            else if (i < redsNumber) players[num] = new Player(num);
            else if (i < activePlayers.size() - 1) players[num] = new Black(num);
            else players[num] = new Don(num);
        }
    }

    public String checkRole(int playerNumber) {
        if (playerNumber >= 0 && playerNumber < players.length) {
            if (players[playerNumber] instanceof Sherif) return "Sherif";
            if (players[playerNumber] instanceof Don) return "Don";
            if (players[playerNumber] instanceof Black) return "Black";
            return "Player";
        }
        return null;
    }

    public void changeTurn() {
        int turn = getTalkingTurn();
        int nextTurn = (turn + 1) % players.length;
        while (nextTurn != turn) {
            if (players[nextTurn].isAlive() && players[nextTurn].canSpeak()) {
                talkingTurn = nextTurn;
                return;
            }
            nextTurn = (nextTurn + 1) % players.length;
        }
    }

    public boolean checkTurn(int playerNumber) {
        return getTalkingTurn() == playerNumber;
    }

    public Player decideVictim() {
        int maxVotes = 0;
        ArrayList<Player> candidates = new ArrayList<>();
        for (Player player : players) {
            if (player == null) continue;
            int votes = player.getVoteNumber();
            if (votes > maxVotes) {
                maxVotes = votes;
                candidates.clear();
                candidates.add(player);
            } else if (votes == maxVotes) {
                candidates.add(player);
            }
        }
        clearVotes();
        return candidates.size() == 1 ? candidates.get(0) : null;
    }

    public void clearVotes() {
        for (Player player : players) {
            if (player != null) player.setVoteNumber(0);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-Game Code: ").append(gameCode).append("\n");
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
