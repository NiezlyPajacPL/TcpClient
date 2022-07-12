import managers.SubtitlesPrinter;
import network.Client;
import network.InputReader;
import network.TcpClient;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[]args){
        final String CONNECTION_IP = "127.0.0.1";
        final int CONNECTION_PORT = 4445;

        Scanner scan = new Scanner(System.in);
        Client client = new TcpClient(CONNECTION_IP,CONNECTION_PORT);
        Thread clientThread = new Thread(client);
        clientThread.start();
        SubtitlesPrinter.printRegistrationRequest();
        SubtitlesPrinter.printIsHelpNeeded();

        InputReader inputReader = new InputReader(scan,client);
        inputReader.start();
    }
}
