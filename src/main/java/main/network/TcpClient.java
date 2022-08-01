package main.network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.helpers.MessageData;
import main.managers.JsonMapperImpl;
import main.managers.SubtitlesPrinter;
import main.messageTypes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class TcpClient implements Client {
    private Socket clientSocket;
    private PrintWriter serverPrintWriter;
    private BufferedReader serverReceivedInput;
    private final String ip;
    private final int port;
    private boolean clientLoggedIn;
    private boolean clientConnected = false;
    private ObservableList<String> onlineUsers = FXCollections.observableArrayList();
    private MessageListener messageListener;

    public TcpClient(String ip, int port, MessageListener messageListener) {
        this.ip = ip;
        this.port = port;
        this.messageListener = messageListener;
    }

    @Override
    public void run() {
        startConnection(ip, port);
        JsonMapperImpl jsonMapper = new JsonMapperImpl();
        MessageType messageType;
        while (true) {
            if (!clientSocket.isClosed()) {
                String json = receiveMessage();
                messageType = jsonMapper.mapJson(json);

                if (messageType instanceof Login) {
                    SubtitlesPrinter.printReceivedMessage((((Login) messageType).message));
                    clientLoggedIn = true;
                } else if (messageType instanceof Message message) {
                    messageListener.onMessageReceived(new MessageData(message.sender, message.message)); //todo zamienic MessageData i Message w jedna klase
                } else if (messageType instanceof UsersListReceiver) {
                    updateOnlineUsers(((UsersListReceiver) messageType).users);
                } else if (messageType instanceof Logout) {
                    SubtitlesPrinter.printReceivedMessage((((Logout) messageType).message));
                    break;
                } else if (messageType instanceof Register) {
                    SubtitlesPrinter.printReceivedMessage((((Register) messageType).message));
                    clientLoggedIn = true;
                }
            } else {
                break;
            }
        }
    }

    private void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            clientConnected = true;
        } catch (IOException e) {
            SubtitlesPrinter.printConnectionProblems();
            tryReconnecting();
        }
    }

    @Override
    public String receiveMessage() {
        try {
            //Waiting for server input
            serverReceivedInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return serverReceivedInput.readLine();
        } catch (IOException e) {
            SubtitlesPrinter.printLostConnection();
            clientLoggedIn = false;
            clientConnected = false;
            tryReconnecting();
        }
        return null;
    }

    @Override
    public void sendMessage(String msg) {
        try {
            serverPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            serverPrintWriter.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopConnection() {
        try {
            serverReceivedInput.close();
            serverPrintWriter.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryReconnecting() {
        try {
            System.gc();
            clientSocket = new Socket(ip, port);
            SubtitlesPrinter.printConnectionEstablished();
            SubtitlesPrinter.printEnter(5);
            SubtitlesPrinter.printRegistrationRequest();
            SubtitlesPrinter.printIsHelpNeeded();
            clientConnected = true;
        } catch (IOException e) {
            SubtitlesPrinter.printReconnectingUnsuccessful();
            try {
                sleep(5000);
                tryReconnecting();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public boolean isClientLoggedIn() {
        return clientLoggedIn;
    }

    @Override
    public boolean isClientConnected() {
        return clientConnected;
    }


    private void updateOnlineUsers(String receivedData) {
        if (onlineUsers != null) {
            onlineUsers.clear();
        }

        String[] words = receivedData.split(" ");
        for (int i = 0; i < words.length; i++) {
            onlineUsers.add(words[i].replace("[", "").replace("]", "").replace(",", ""));
        }
    }

    public ObservableList<String> getOnlineUsers() {
        return onlineUsers;
    }

}
