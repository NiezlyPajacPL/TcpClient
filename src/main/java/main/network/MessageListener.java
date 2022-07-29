package main.network;

import main.helpers.MessageData;

public interface MessageListener {
    void onMessageReceived(MessageData messageData);
}
