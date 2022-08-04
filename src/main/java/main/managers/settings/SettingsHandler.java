package main.managers.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class SettingsHandler {

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    String filePath;
    FileWriter file;
    FileReader settings;
    public SettingsHandler(String filePath) {
        this.filePath = filePath;
        try {
            FileWriter file = new FileWriter(filePath);
            Object object = JsonParser.parseReader(new FileReader(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToDefault() {
        Settings settings = new Settings();
        System.out.println(gson.toJson(settings));
        try {
            file.write(gson.toJson(settings));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Settings getSettings(){
        return null;
    }
}
