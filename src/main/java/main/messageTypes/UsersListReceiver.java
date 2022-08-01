package main.messageTypes;

public class UsersListReceiver extends MessageType {
    public String users;

    public UsersListReceiver(String users) {
        this.users = users;
    }
}
