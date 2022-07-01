import network.Client;
import network.TcpClient;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[]args){
        Scanner scan = new Scanner(System.in);

        TcpClient tcpClient = new TcpClient("127.0.0.1",5000);
        System.out.println("client started");
        Thread thread = new Thread(tcpClient);
        thread.start();
 //       String response = tcpClient.sendMessage("hello server");
        while (true) {
            String message = scan.nextLine();
            tcpClient.sendMessage(message);
        }

    }
}
