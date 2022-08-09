package main.messageTypes;

import com.google.gson.JsonArray;

import java.util.ArrayList;

public class UsersListReceiver extends MessageType {
    private final JsonArray users;

    public UsersListReceiver(JsonArray users) {
        this.users = users;
    }

    public ArrayList<String> getUsers() {
        ArrayList<String> usersList = new ArrayList<>();
        if (users != null) {
            //Iterating JSON array
            for (int i = 0; i < users.size(); i++) {
                //Adding each element of JSON array into ArrayList
                usersList.add(users.get(i).getAsString());
            }
        }
        return usersList;
    }
}
