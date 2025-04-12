package game.player;

public class Player {
    //Instance variables
    private int number;
    private int remarksNumber;
    private boolean isAlive;
    private int voteNumber;
    private String message;

    //Constructors
    public Player(int number){
        
    }
    public Player(Player that){
        
    }
    
    //Setters
    //Setting the number of remarks
    public void setRemarksNumber(int remarksNumber){
        this.remarksNumber = remarksNumber;
    }
    //Setting the number of votes
    public void setVoteNumber(int voteNumber){
        this.voteNumber = voteNumber;
    }
    //Setting the status of the player(dead or alive)
    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    //Getters
    //Getting the number of remarks
    public int getRemarksNumber(){
        return remarksNumber;
    }
    //Getting the number of votes
    public int getVoteNumber(){
        return voteNumber;
    }
    //Getting the status of the player(dead or alive)

    public boolean isAlive() {
        return isAlive;
    }
}