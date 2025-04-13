package user;

public class User{
    //Instance variables
    public static int USERS_NUMBER;
    public final String ID;
    private String password;
    private String name;
    private int points;

    //Constructors
    public User(){
        USERS_NUMBER++;

        ID = USERS_NUMBER + "";
        password = "12345678";
        name = "User-" + ID;
        points = 0;
    }
    public User(String name, String password){
        USERS_NUMBER++;

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
    public void setName(){

    }

    public String getName(){

    }

    public void setPoints(){

    }

    public int getPoints(){

    }

    public void setPassword(){

    }

    private int getPassword(){

    }
}