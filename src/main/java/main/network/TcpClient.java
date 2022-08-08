package main.network;

import main.managers.JsonMapperImpl;
import main.managers.SubtitlesPrinter;
import main.messageTypes.*;
import main.scenes.LoginStatusListener;

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
   // private ObservableList<String> onlineUsers = FXCollections.observableArrayList();
    private ArrayList<String> onlineUsers;
    private MessageListener messageListener;
    private LoginStatusListener loginStatusListener;

    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        connect(ip, port);
        JsonMapperImpl jsonMapper = new JsonMapperImpl();
        MessageType messageType;
        while (true) {
            if (!clientSocket.isClosed()) {
                String json = receiveMessage();
                messageType = jsonMapper.mapJson(json);

                if (messageType instanceof Login) {
                    isClientLoggedIn = ((Login) messageType).isLoginSuccessful();
                    System.out.println("Received login status");
                    loginStatusListener.onLoginStatusReceived();
                } else if (messageType instanceof Message message) {
                    messageListener.onMessageReceived(new Message(message.getSender(), message.getMessage()));
                } else if (messageType instanceof UsersListReceiver) {
                    onlineUsers = ((UsersListReceiver) messageType).getUsers();
                    messageListener.onUsersListReceived();
                } else if (messageType instanceof Logout) {
                    SubtitlesPrinter.printReceivedMessage((((Logout) messageType).getMessage()));
                    break;
                } else if (messageType instanceof Register) {
                    System.out.println("Received login status");
                    loginStatusListener.onLoginStatusReceived();
                    isClientLoggedIn = ((Register) messageType).isLoginSuccessful();
                }
            } else {
                break;
            }
        }
    }

    private void connect(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            SubtitlesPrinter.printConnectionEstablished();
            clientConnected = true;
        } catch (IOException e) {
            SubtitlesPrinter.printConnectionProblems();
            SubtitlesPrinter.printReconnectingUnsuccessful();
            try {
                sleep(5000);
                connect(ip,port);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
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
            connect(ip,port);
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

    @Override
    public boolean isClientLoggedIn() {
        return isClientLoggedIn;
    }

    @Override
    public boolean isClientConnected() {
        return clientConnected;
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setMessageListener(MessageListener messageListener){
        this.messageListener = messageListener;
    }

    public void setLoginStatusListener(LoginStatusListener loginStatusListener) {
        this.loginStatusListener = loginStatusListener;
    }
}
