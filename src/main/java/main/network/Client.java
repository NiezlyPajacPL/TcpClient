package main.network;

import main.messageTypes.MessageType;

public interface Client extends Runnable {

    void sendMessage(String message);

    MessageType receiveMessage();

    void stopConnection();

    boolean isClientLoggedIn();

    boolean isClientConnected();
}

