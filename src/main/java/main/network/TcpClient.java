package main.network;

import javafx.application.Platform;
import main.managers.SubtitlesPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class TcpClient implements Client {
    private Socket clientSocket;
    private PrintWriter serverPrintWriter;
    private BufferedReader serverReceivedInput;
    private final String ip;
    private final int port;
    private boolean clientLoggedIn;
    private boolean clientConnected = false;
    private final ArrayList<String> usersList = new ArrayList<>();
    private MessageListener messageListener;

    public TcpClient(String ip, int port, MessageListener messageListener) {
        this.ip = ip;
        this.port = port;
        this.messageListener = messageListener;
    }

    @Override
    public void run() {
        startConnection(ip, port);
        while (true) {
            if (!clientSocket.isClosed()) {
                receiveMessage();
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
    public void receiveMessage() {
        try {
            serverReceivedInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = serverReceivedInput.readLine();
            updateClientList(message);
            clientIsLogged(message);
            if(isMessage(message)){
                messageListener.onMessageReceived(message);
            }
            SubtitlesPrinter.printReceivedMessage(message);
        } catch (IOException e) {
            SubtitlesPrinter.printLostConnection();
            clientLoggedIn = false;
            tryReconnecting();
        }
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

    private void clientIsLogged(String receivedData) {
        if ((Objects.equals(receivedData, "Registered Successfully!") || receivedData.contains("Hello again"))) {
            clientLoggedIn = true;
        } else if ((Objects.equals(receivedData, "Successfully logged out. See you soon!"))) {
            clientLoggedIn = false;
        }
    }

    private void updateClientList(String receivedData) {
        if (receivedData.contains("Online users list:")) {
            usersList.clear();
            String[] words = receivedData.split(" ");
            for (int i = 3; i < words.length; i++) {
                if (!usersList.contains(words[i])) {
                    usersList.add(words[i]);
                }
            }
        }
    }

    private boolean isMessage(String message) {
        if ((Objects.equals(message, "Registered Successfully!") || message.contains("Hello again")) ||
                message.equals("Successfully logged out. See you soon!") || message.contains("Online users list:")) {
            return false;
        }
        return true;
    }

    public ArrayList<String> getUsersList() {
        return usersList;
    }
}
