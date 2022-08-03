package main.network;

public interface Client extends Runnable {

    void sendMessage(String message);

    String receiveMessage();

    void stopConnection();

    boolean isClientLoggedIn();

    boolean isClientConnected();
}

