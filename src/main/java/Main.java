import network.Client;
import network.TcpClient;

import java.util.Objects;

public class Main {

    public static void main(String[]args){
        TcpClient tcpClient = new TcpClient();
        tcpClient.startConnection("127.0.0.1", 6666);
        System.out.println("Server started");
        String response = tcpClient.sendMessage("hello server");

        if(Objects.equals(response, "hello client")){
            System.out.println("koks");
        }
    }

    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        TcpClient tcpClient = new TcpClient();
        tcpClient.startConnection("127.0.0.1", 6666);
        String response = tcpClient.sendMessage("hello server");

        if(Objects.equals(response, "hello client")){
            System.out.println("koks");
        }
    }
}
