package main.messageTypes;

public class Login extends MessageType {
    private final boolean isLoginSuccessful;

    public Login(boolean isLoginSuccessful) {
        this.isLoginSuccessful = isLoginSuccessful;
    }

    public boolean isLoginSuccessful() {
        return isLoginSuccessful;
    }
}
