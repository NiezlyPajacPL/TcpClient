package main.messageTypes;

public class UsersListReceiver extends MessageType {

    public Types type;
    public String users;

    public UsersListReceiver(Types type, String users) {
        this.type = type;
        this.users = users;
    }
}
