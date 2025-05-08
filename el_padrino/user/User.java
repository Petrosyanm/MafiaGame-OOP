package user;
import java.io.*;

public class User{
    //Instance variables
    public static int USERS_NUMBER = getLastUserId();
    public final String ID;
    private String password;
    private String name;
    private int points;

    //Constructors
    public User(){
        USERS_NUMBER++;
        ID = USERS_NUMBER + "";
        setLastUserId(USERS_NUMBER);
        password = "12345678";
        name = "User-" + ID;
        points = 0;
    }
    public User(String name, String password, boolean creatingAccount){
        if(creatingAccount){
            USERS_NUMBER++;
            setLastUserId(USERS_NUMBER);
        }
        ID = USERS_NUMBER + "";
        this.password = password;
        this.name = name;
        points = 0;
    }
    public User(User that){
        this.ID = that.ID;
        this.password = that.password;
        this.name = that.name;
        this.points = that.points;
    }
    //Setters and Getters
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public int getPoints() {
        return points;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    private String getPassword() {
        return password;
    }

    public String toString() {
        return ID + "|" + name + "|" + password + "|" + points;
    }


    private static int getLastUserId() {
        try (BufferedReader reader = new BufferedReader(new FileReader("last_id.txt"))) {
            String line = reader.readLine();
            if(line != null)
                return Integer.parseInt(line);
            else
                return 0;
        } catch (IOException e) {
            return 0;
        }
    }

    private static void setLastUserId(int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("last_id.txt"))) {
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}