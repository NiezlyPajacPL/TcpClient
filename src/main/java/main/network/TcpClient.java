package main.network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.managers.JsonMapperImpl;
import main.managers.SubtitlesPrinter;
import main.messageTypes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class TcpClient implements Client {
    private Socket clientSocket;
    private PrintWriter serverPrintWriter;
    private BufferedReader serverReceivedInput;
    private final String ip;
    private final int port;
    private boolean isClientLoggedIn;
    private boolean clientConnected = false;
    public boolean messageArrived = false;
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
                    System.out.println("Received login status");
                    messageArrived = true;
                    isClientLoggedIn = ((Login) messageType).isLoginSuccessful();
                } else if (messageType instanceof Message message) {
                    messageListener.onMessageReceived(new Message(message.getSender(), message.getMessage()));
                } else if (messageType instanceof UsersListReceiver) {
                    ArrayList<String> usersList = ((UsersListReceiver) messageType).getUsers();
                    System.out.println(usersList.get(0));
                    //updateOnlineUsers(((UsersListReceiver) messageType).getUsers());
                } else if (messageType instanceof Logout) {
                    SubtitlesPrinter.printReceivedMessage((((Logout) messageType).getMessage()));
                    break;
                } else if (messageType instanceof Register) {
                    System.out.println("Received login status");
                    messageArrived = true;
                    isClientLoggedIn = ((Register) messageType).isLoginSuccessful();
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
            isClientLoggedIn = false;
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
        return isClientLoggedIn;
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
