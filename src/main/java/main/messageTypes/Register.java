package main.messageTypes;

public class Register extends MessageType{
    private final String message;

    public Register(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
