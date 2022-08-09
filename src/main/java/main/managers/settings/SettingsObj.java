package main.managers.settings;

public class SettingsObj {

    public boolean disableSounds;
    public String connectionIP;
    public int port;

    public SettingsObj(){
        disableSounds = false;
        connectionIP = "109.207.149.50";
        port = 4446;
    }
    public boolean isSoundsDisabled() {
        return disableSounds;
    }

    public void setDisableSounds(boolean disableSounds) {
        this.disableSounds = disableSounds;
    }

    public String getConnectionIP() {
        return connectionIP;
    }

    public int getPort() {
        return port;
    }
}
