package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient implements Client{
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;

    @Override
    public void run() {
        while (true) {
            try {
                String receivedMessage = input.readLine();
                System.out.println(receivedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        output.println(msg);
    }

    public void stopConnection() {
        try {
            input.close();
            output.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
