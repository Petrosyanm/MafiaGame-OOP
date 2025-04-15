package user;

public class LogInRegisterException extends Exception {
    public LogInRegisterException() {
        super("Login or registeration problem(");
    }
    public LogInRegisterException(String message) {
        super(message);
    }
}