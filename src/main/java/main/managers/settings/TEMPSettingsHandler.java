package main.managers.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.*;

public class TEMPSettingsHandler {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private String filePath;
    private FileWriter file;
    private FileReader settings;

    public TEMPSettingsHandler(String filePath) {
        this.filePath = filePath;
        try {
            FileWriter file = new FileWriter(filePath);
            Object object = JsonParser.parseReader(new FileReader(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToDefault() {
        TEMPSettings TEMPSettings = new TEMPSettings();
        System.out.println(gson.toJson(TEMPSettings));
        try {
            file.write(gson.toJson(TEMPSettings));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static TEMPSettings getSettings() {
        return null;
    }
}
