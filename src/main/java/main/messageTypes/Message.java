package main.messageTypes;

public class Message extends MessageType {
    public Types type;
    public String sender;
    public String message;

    public Message(Types type, String message, String sender) {
        this.type = type;
        this.message = message;
        this.message = message;
    }
}
