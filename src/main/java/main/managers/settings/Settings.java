package main.managers.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Settings {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private String filePath;
    private File file;
    private FileWriter fileWriter;
    private FileReader settings;
    private SettingsObj settingsObj;

    public Settings(String filePath) {
        this.filePath = filePath;
        file = new File(filePath);
        if (isSettingsEmpty()) {
            System.out.println("Settings file was empty. Settings has been set to default.");
            setToDefault();
        }
    }
    public String getConnectionIP() {
        settingsObj = getSettings();
        return settingsObj.connectionIP;
    }

    public int getConnectionPort(){
        settingsObj = getSettings();
        return settingsObj.port;
    }

    public boolean soundsMuted() {
        settingsObj = getSettings();
        return settingsObj.disableSounds;
    }

    public void setToDefault() {
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SettingsObj SettingsObj = new SettingsObj();
        System.out.println(gson.toJson(SettingsObj));
        try {
            fileWriter.write(gson.toJson(SettingsObj));
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isSettingsEmpty() {
        return file.length() == 0;
    }

    private SettingsObj getSettings() {
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            settingsObj = gson.fromJson(reader, SettingsObj.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settingsObj;
    }
}
