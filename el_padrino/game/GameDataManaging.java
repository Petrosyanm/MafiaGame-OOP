package game;

import java.io.*;
import java.util.Random;

public class GameDataManaging {
    private static final int CODE_LENGTH = 8;
    private static final String GAME_DATA_PATH = "games.txt";
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static StringBuilder sb;

    public static String generateCode(){
        Random random = new Random();
        sb = new StringBuilder();
        int index;

        for(int i = 0; i < CODE_LENGTH; i++){
            index = random.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }

        return sb.toString();
    }

    public static void createGameCode(Game game) {
        String code;

        do {
            code = generateCode();
        } while (checkGameCode(code));

        try (BufferedWriter w = new BufferedWriter(new FileWriter(GAME_DATA_PATH, true))) {
            w.write(code);
            w.newLine();
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage()); //TODO: Change
        }
    }

    public static boolean checkGameCode(String code) {
        try (BufferedReader r = new BufferedReader(new FileReader(GAME_DATA_PATH))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.equals(code)) return true;
            }
        } catch (IOException e) {
            System.out.println("Error reading game file: " + e.getMessage()); //TODO: Change
        }
        return false;
    }
}
