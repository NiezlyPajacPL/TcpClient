package main.helpers.Listeners;

import main.messageTypes.Message;

public interface ServerResponseListener {
    void onMessageReceived(Message messageData);

    void onUsersListReceived();

    void onLogout();
}
