package main.messageTypes;

public class Register extends MessageType{
    public Types type;
    public String message;

    public Register(Types type, String message) {
        this.type = type;
        this.message = message;
    }
}
