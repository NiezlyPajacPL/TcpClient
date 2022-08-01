package main.messageTypes;

public class Logout extends MessageType {
    public Types type;
    public String message;

    public Logout(Types type, String message) {
        this.type = type;
        this.message = message;
    }
}
