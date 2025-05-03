package user;

import java.io.*;
import java.util.*;

public class UserDataManaging {
    private static final String PATH_TO_DATA = "../../user/usersInfo.txt"; 

    public static void saveUser(User user) throws LogInRegisterException {
        if (userExists(user.getName()))
            throw new LogInRegisterException("User already exists");
        try{
            BufferedWriter w = new BufferedWriter(new FileWriter(PATH_TO_DATA, true));
            w.write(user.toString());
            w.newLine();
            w.close();
        } catch (IOException e) {
            System.out.println("no data found. Sorry...");
            System.exit(0);
        }
    }

    public static void logIn(String name, String password) throws LogInRegisterException {
        try {
            BufferedReader r = new BufferedReader(new FileReader(PATH_TO_DATA));
            String line;
            while ((line = r.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 4) {
                    if (data[1].equals(name)) 
                        if(data[2].equals(password))
                            return;
                        else 
                            throw new LogInRegisterException("Wrong password");
                }
            }
            throw new LogInRegisterException("User not found. Wrong username");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new LogInRegisterException("Data not found");
        }
    }

    public static boolean userExists(String name) {
        try {
            BufferedReader r = new BufferedReader(new FileReader(PATH_TO_DATA));
            String line;
            while ((line = r.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 4 && data[1].equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
