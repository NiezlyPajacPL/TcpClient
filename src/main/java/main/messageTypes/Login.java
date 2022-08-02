package main.messageTypes;

public class Login extends MessageType {
    private final String message;

    public Login(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
