package main.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SettingsFileHandler {

    String filePath;
    File settings;
    Scanner scanner;
    public SettingsFileHandler(String filePath) {
        this.filePath = filePath;
        settings = new File(filePath);
        try {
            scanner = new Scanner(settings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getConnectionIP(){
        return scanner.nextLine();
    }

    public int getConnectionPort(){
        return scanner.nextInt();
    }
}
