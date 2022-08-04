package main.managers.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConnectionFileHandler {

    String filePath;
    File settings;
    Scanner scanner;
    public ConnectionFileHandler(String filePath) {
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
