package game;

import game.player.*;

import java.util.*;

public class Game implements Cloneable {
    private int nights;
    private int redsNumber;
    private int blacksNumber;
    private Player[] players;
    private int talkingTurn;
    private int gameCode;

    public boolean isNight = true;
    public boolean isFirstNight = true;

    private Set<Integer> mafiaPlayers = new HashSet<>();
    private Map<Integer, Integer> mafiaVotes = new HashMap<>();

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
            players[i] = null;
        }
        talkingTurn = 0;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setTalkingTurn(int talkingTurn) {
        this.talkingTurn = talkingTurn;
    }

    public int getTalkingTurn() {
        return talkingTurn;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    public int getGameCode() {
        return gameCode;
    }

    public void distributeRoles() {
        ArrayList<Player> activePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p != null) activePlayers.add(p);
        }

        Collections.shuffle(activePlayers, new Random());

        int total = activePlayers.size();
        int blackCount = Math.max(1, total / 3);
        int redCount = total - blackCount - 1;

        for (int i = 0; i < total; i++) {
            int num = activePlayers.get(i).getNumber();

            if (i == 0) {
                players[num] = new Sherif(num);
            } else if (i <= redCount) {
                players[num] = new Player(num);
            } else if (i < total - 1) {
                players[num] = new Black(num);
                mafiaPlayers.add(num);
            } else {
                players[num] = new Don(num);
                mafiaPlayers.add(num);
            }
        }
    }

    public String checkRole(int index) {
        if (players[index] instanceof Sherif) return "Sherif";
        if (players[index] instanceof Don) return "Don";
        if (players[index] instanceof Black) return "Black";
        return "Player";
    }

    public boolean isNight() {
        return isNight;
    }

    public boolean isFirstNight() {
        return isFirstNight;
    }

    public void startDayPhase() {
        isNight = false;
        isFirstNight = false;
        mafiaVotes.clear();
    }

    public void startNightPhase() {
        isNight = true;
        mafiaVotes.clear();
    }

    public boolean isMafia(int playerIndex) {
        return mafiaPlayers.contains(playerIndex);
    }

    public boolean registerMafiaVote(int mafiaPlayer, int target) {
        if (!isMafia(mafiaPlayer)) return false;
        if (mafiaVotes.containsKey(mafiaPlayer)) return false;
        mafiaVotes.put(mafiaPlayer, target);
        return true;
    }

    public Integer checkUnanimousVote() {
        if (mafiaVotes.size() < mafiaPlayers.size()) return null;

        int vote = -1;
        for (int v : mafiaVotes.values()) {
            if (vote == -1) vote = v;
            else if (vote != v) return null;
        }
        return vote;
    }

    public Set<Integer> getMafiaPlayers() {
        return mafiaPlayers;
    }

    public Map<Integer, Integer> getMafiaVotes() {
        return mafiaVotes;
    }
}
