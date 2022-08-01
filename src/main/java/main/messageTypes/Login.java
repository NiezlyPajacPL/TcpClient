package main.messageTypes;

public class Login extends MessageType {
    public Types type;
    public String message;

    public Login(Types type, String message) {
        this.type = type;
        this.message = message;
    }
}
