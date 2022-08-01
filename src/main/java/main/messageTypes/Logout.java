package main.messageTypes;

public class Logout extends MessageType {
    public String message;

    public Logout(String message) {
        this.message = message;
    }
}
