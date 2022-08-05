package main.managers.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.*;

public class TEMPSettingsHandler {

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    String filePath;
    FileWriter file;
    FileReader settings;
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
        SettingsTemp settingsTemp = new SettingsTemp();
        System.out.println(gson.toJson(settingsTemp));
        try {
            file.write(gson.toJson(settingsTemp));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static SettingsTemp getSettings(){
        return null;
    }
}
