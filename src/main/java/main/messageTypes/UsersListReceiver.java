package main.messageTypes;

public class UsersListReceiver extends MessageType {
    private final String users;

    public UsersListReceiver(String users) {
        this.users = users;
    }

    public String getUsers() {
        return users;
    }
}
