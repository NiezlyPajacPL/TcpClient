package network;

import managers.MessageHelper;
import managers.SubtitlesPrinter;

import java.util.Scanner;

public class InputReader extends Thread {
    Scanner scan;
    Client client;
    SubtitlesPrinter subtitlesPrinter;
    final String HELP = "/help";
    final String LOGOUT = "/logout";

    public InputReader(Scanner scan, Client client, SubtitlesPrinter subtitlesPrinter) {
        this.scan = scan;
        this.client = client;
        this.subtitlesPrinter = subtitlesPrinter;
    }

    @Override
    public void run() {
        MessageHelper messageHelper = new MessageHelper(subtitlesPrinter);
        while (true) {
            String message = scan.nextLine();
            if (message.contains(HELP)) {
                SubtitlesPrinter.printHelper();
            } else if (message.contains(LOGOUT)) {
                client.sendMessage(message);
                break;
            }else if(messageHelper.messageCanBeSent(message,client.isClientLoggedIn())){
                client.sendMessage(message);
            }
        }

        SubtitlesPrinter.printBye();
        client.stopConnection();
    }
}
