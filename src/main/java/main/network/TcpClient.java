package main.network;

import main.helpers.Listeners.ServerResponseListener;
import main.managers.JsonMapperImpl;
import main.managers.console.ConsolePrinter;
import main.messageTypes.*;
import main.helpers.Listeners.LoginStatusListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class TcpClient implements Client {
    //CONNECTION
    private final String ip;
    private final int port;
    private Socket clientSocket;
    private boolean isClientLoggedIn;
    private boolean clientConnected = false;
    //RECEIVING AND SENDING
    private PrintWriter serverPrintWriter;
    private BufferedReader serverReceivedJSON;
    //LISTENERS
    private final ArrayList<ServerResponseListener> responseListeners = new ArrayList<>();
    private final ArrayList<LoginStatusListener> loginStatusListeners = new ArrayList<>();
    //OTHERS
    private ArrayList<String> onlineUsers;
    private final JsonMapperImpl jsonMapper = new JsonMapperImpl();

    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        connect(ip, port);
        while (true) {
            if (!clientSocket.isClosed()) {
                MessageType messageType = receiveMessage();

                if (messageType instanceof Login) {
                    ConsolePrinter.printReceivedLoginStatus();
                    for(LoginStatusListener loginStatusListener : loginStatusListeners){
                        loginStatusListener.onLoginStatusReceived(((Login) messageType).isLoginSuccessful());
                    }
                } else if (messageType instanceof Message message) {
                    for (ServerResponseListener responseListener : responseListeners) {
                        responseListener.onMessageReceived(new Message(message.getSender(), message.getMessage()));
                    }
                } else if (messageType instanceof UsersListReceiver) {
                    onlineUsers = ((UsersListReceiver) messageType).getUsers();
                    for (ServerResponseListener responseListener : responseListeners) {
                        responseListener.onUsersListReceived();
                    }
                } else if (messageType instanceof Logout) {
                    ConsolePrinter.printReceivedMessage((((Logout) messageType).getMessage()));
                    break;
                } else if (messageType instanceof Register) {
                    ConsolePrinter.printReceivedRegistrationStatus();
                    for(LoginStatusListener loginStatusListener : loginStatusListeners){
                        loginStatusListener.onLoginStatusReceived(((Register) messageType).isLoginSuccessful());
                    }
                }
            } else {
                break;
            }
        }
    }

    private void connect(String ip, int port) {
        try {
            clientSocket = new Socket(InetAddress.getByName(ip), port);
            ConsolePrinter.printConnectionEstablished();
            clientConnected = true;
        } catch (IOException e) {
            ConsolePrinter.printConnectionProblems();
            ConsolePrinter.printReconnectingUnsuccessful();
            try {
                sleep(5000);
                connect(ip, port);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public MessageType receiveMessage() {
        try {
            //Waiting for server input
            serverReceivedJSON = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return jsonMapper.mapJson(serverReceivedJSON.readLine());
        } catch (IOException e) {
            ConsolePrinter.printLostConnection();
            isClientLoggedIn = false;
            clientConnected = false;
            connect(ip, port);
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
            serverReceivedJSON.close();
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

    public void addMessageListener(ServerResponseListener serverResponseListener){
        responseListeners.add(serverResponseListener);
    }

    public void setLoginStatusListener(LoginStatusListener loginStatusListener) {
        loginStatusListeners.add(loginStatusListener);
    }

}
