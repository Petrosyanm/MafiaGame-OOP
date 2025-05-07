package game;

import java.io.*;
import java.util.ArrayList;

public class GameStorage {
    private static final String FILE_NAME = "games.ser";

    public static void saveGames(ArrayList<Game> games) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(games);
        } catch (IOException e) {
            System.err.println("Error saving games: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Game> loadGames() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ArrayList<Game>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading games: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}






/*
package game;

import java.util.ArrayList;

public class GameStorage{
    private static ArrayList<Game> activeGames = new ArrayList<>();

    public static void addGame(Game game) {
        activeGames.add(game);
    }

    public static ArrayList<Game> getGames() {
        return activeGames;
    }

    public static Game findGameByCode(int code) {
        for (Game game : activeGames) {
            if (game.getGameCode() == code) {
                return game;
            }
        }
        return null;
    }
}
*/
