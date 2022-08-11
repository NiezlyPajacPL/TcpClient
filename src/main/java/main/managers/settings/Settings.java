package main.managers.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.managers.console.ConsolePrinter;

import java.io.*;

public class Settings {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final File file;
    private FileWriter fileWriter;
    private SettingsObj settingsObj;

    public Settings(String filePath) {
        file = new File(filePath);
        try {
            if (file.createNewFile()) {
                ConsolePrinter.createdSettingsFile();
            } else if (isSettingsEmpty()) {
                ConsolePrinter.emptySettings();
                setToDefault();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConnectionIP() {
        settingsObj = getSettings();
        return settingsObj.getConnectionIP();
    }

    public int getConnectionPort() {
        settingsObj = getSettings();
        return settingsObj.getPort();
    }

    //SOUNDS
    public boolean isSoundMuted() {
        settingsObj = getSettings();
        return settingsObj.isSoundsDisabled();
    }

    public void muteSounds() {
        settingsObj = getSettings();
        settingsObj.setDisableSounds(true);
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settingsObj));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unMuteSounds() {
        settingsObj = getSettings();
        settingsObj.setDisableSounds(false);
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settingsObj));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
