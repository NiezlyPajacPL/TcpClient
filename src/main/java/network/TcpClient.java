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
    private final String ip;
    private final int port;

    public TcpClient(String ip, int port){
        this.ip  = ip;
        this.port = port;
        startConnection(ip,port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                receiveMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void receiveMessage() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = input.readLine();
        System.out.println(message);
        //     output.println(message);
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
