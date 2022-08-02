package main.network;

import main.messageTypes.Message;

public interface MessageListener {
    void onMessageReceived(Message messageData);

//    void onLoginConfirmation();
}
