package src.network;

import game.Game;
import game.player.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private final List<PrintWriter> clientWriters = Collections.synchronizedList(new ArrayList<>());
    private final Map<PrintWriter, String> playerNames = Collections.synchronizedMap(new HashMap<>());
    private final Map<PrintWriter, Integer> playerIndexes = Collections.synchronizedMap(new HashMap<>());

    private final Map<Integer, Integer> dayVotes = new HashMap<>();
    private final Map<Integer, Integer> mafiaVotes = new HashMap<>();

    private int expectedPlayerCount = 4;
    private Game game;

    public void setExpectedPlayerCount(int count) {
        this.expectedPlayerCount = count;
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("✅ Server started on port " + port);
            while (true) {
                Socket client = serverSocket.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                clientWriters.add(writer);

                writer.println("ROOM_ACTIVE");
                writer.println("CONNECTED_PLAYERS: " + clientWriters.size());

                new Thread(new ClientHandler(client, writer)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;

        ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {

                    if (inputLine.startsWith("USERNAME:")) {
                        String username = inputLine.substring("USERNAME:".length()).trim();
                        int index = clientWriters.indexOf(out);
                        playerNames.put(out, username);
                        playerIndexes.put(out, index);
                        broadcast("NEW_PLAYER:" + username);

                        if (playerNames.size() == expectedPlayerCount) {
                            startGame();
                        }
                    }

                    else if (inputLine.startsWith("CHAT:")) {
                        String username = playerNames.get(out);
                        String message = inputLine.substring("CHAT:".length()).trim();
                        broadcast("CHAT:" + username + ": " + message);
                    }

                    else if (inputLine.startsWith("VOTE:")) {
                        int target = Integer.parseInt(inputLine.substring("VOTE:".length()).trim());
                        int voter = playerIndexes.get(out);
                        synchronized (dayVotes) {
                            dayVotes.put(voter, target);
                            if (dayVotes.size() == expectedPlayerCount) {
                                countVotesAndEliminate();
                            }
                        }
                    }

                    else if (inputLine.startsWith("MAFIA_VOTE:")) {
                        int target = Integer.parseInt(inputLine.substring("MAFIA_VOTE:".length()).trim());
                        int mafia = playerIndexes.get(out);
                        synchronized (mafiaVotes) {
                            mafiaVotes.put(mafia, target);
                            if (mafiaVotes.size() == game.getMafiaPlayers().size()) {
                                checkUnanimousMafiaVote();
                            }
                        }
                    }

                    else if (inputLine.startsWith("SHERIF_CHECK:")) {
                        int target = Integer.parseInt(inputLine.substring("SHERIF_CHECK:".length()).trim());
                        int sender = playerIndexes.get(out);

                        Player[] players = game.getPlayers();
                        if (players[sender] instanceof Sherif) {
                            String roleName = game.checkRole(target);
                            out.println("SHERIF_RESULT:" + target + ":" + roleName);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clientWriters.remove(out);
                playerNames.remove(out);
                playerIndexes.remove(out);
                broadcast("CONNECTED_PLAYERS: " + clientWriters.size());
            }
        }
    }

    private void startGame() {
        game = new Game(expectedPlayerCount, expectedPlayerCount / 3);
        Player[] players = new Player[expectedPlayerCount];
        for (int i = 0; i < expectedPlayerCount; i++) players[i] = new Player(i);
        game.setPlayers(players);
        game.distributeRoles();

        String[] roleList = new String[expectedPlayerCount];
        for (int i = 0; i < expectedPlayerCount; i++) roleList[i] = game.checkRole(i);

        for (PrintWriter writer : clientWriters) {
            int index = playerIndexes.get(writer);
            writer.println(index + "|" + roleList[index] + "|" + String.join(",", roleList));
        }

        startNightPhase();
    }

    private void startNightPhase() {
        game.startNightPhase();
        mafiaVotes.clear();

        int i = 0;
        for (PrintWriter writer : clientWriters) {
            if (game.getMafiaPlayers().contains(i)) {
                writer.println("NIGHT_CHAT:true");
            } else {
                writer.println("NIGHT_CHAT:false");
            }
            i++;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                checkUnanimousMafiaVote();
                broadcast("CLEAR_CHAT"); // ✅ clear chat for everyone after night ends
            }
        }, 30_000); // 30 seconds
    }

    private void checkUnanimousMafiaVote() {
        Map<Integer, Integer> voteCounts = new HashMap<>();
        for (int vote : mafiaVotes.values()) {
            voteCounts.put(vote, voteCounts.getOrDefault(vote, 0) + 1);
        }

        if (voteCounts.size() == 1) {
            int victim = mafiaVotes.values().iterator().next();
            game.getPlayers()[victim].setIsAlive(false);
            broadcast("ELIMINATED:" + victim);
            checkWinCondition();
        }

        startDayPhase(); // Move to day after vote check
    }

    private void startDayPhase() {
        game.startDayPhase();
        dayVotes.clear();

        for (PrintWriter writer : clientWriters) writer.println("DAY_START");
    }

    private void countVotesAndEliminate() {
        Map<Integer, Integer> voteCount = new HashMap<>();
        for (int votedFor : dayVotes.values()) {
            voteCount.put(votedFor, voteCount.getOrDefault(votedFor, 0) + 1);
        }

        int maxVotes = 0, eliminated = -1;
        for (Map.Entry<Integer, Integer> entry : voteCount.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                eliminated = entry.getKey();
            }
        }

        if (eliminated != -1) {
            game.getPlayers()[eliminated].setIsAlive(false);
            broadcast("ELIMINATED:" + eliminated);
            checkWinCondition();
        }

        dayVotes.clear();
        startNightPhase();
    }

    private void checkWinCondition() {
        int red = 0, black = 0;
        for (Player p : game.getPlayers()) {
            if (!p.isAlive()) continue;
            if (p instanceof Don || p instanceof Black) black++;
            else red++;
        }

        if (black == 0) broadcast("GAME_OVER:Red");
        else if (red <= black) broadcast("GAME_OVER:Black");
    }

    private void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }
}
