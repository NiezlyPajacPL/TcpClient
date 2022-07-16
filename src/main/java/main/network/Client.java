package main.network;

public interface Client extends Runnable{

    void sendMessage(String message);

    void receiveMessage();

    void stopConnection();

    boolean isClientLoggedIn();
}
