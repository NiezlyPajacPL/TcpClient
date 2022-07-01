import network.Client;
import network.TcpClient;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[]args){
        Scanner scan = new Scanner(System.in);

        TcpClient tcpClient = new TcpClient();
        tcpClient.startConnection("127.0.0.1", 5000);
        System.out.println("client started");
 //       String response = tcpClient.sendMessage("hello server");

        while (true) {
            String message = scan.nextLine();
            tcpClient.sendMessage(message);
        }

    }

    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        TcpClient tcpClient = new TcpClient();
        tcpClient.startConnection("127.0.0.1", 6666);
     //   String response = tcpClient.sendMessage("hello server");

    //    if(Objects.equals(response, "hello client")){
   //         System.out.println("koks");
    //    }
    }
}
