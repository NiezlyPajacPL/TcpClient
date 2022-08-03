package main.messageTypes;

public class Register extends MessageType{
    private final boolean isLoginSuccessful;

    public Register(boolean isLoginSuccessful) {
        this.isLoginSuccessful = isLoginSuccessful;
    }

    public boolean isLoginSuccessful() {
        return isLoginSuccessful;
    }
}
