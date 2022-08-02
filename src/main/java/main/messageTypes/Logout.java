package main.messageTypes;

public class Logout extends MessageType {
    private final String message;

    public Logout(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
