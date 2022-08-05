package main.managers.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Settings {

    private String filePath;
    private File settings;
    private Scanner scanner;

    public Settings(String filePath) {
        this.filePath = filePath;
        settings = new File(filePath);
        try {
            scanner = new Scanner(settings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getConnectionIP() {
        return scanner.nextLine();
    }

    public int getConnectionPort() {
        return scanner.nextInt();
    }
}
