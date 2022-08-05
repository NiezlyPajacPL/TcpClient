package main.managers.settings;

public class SettingsTemp {

    public boolean disableSounds;
    public boolean something;

    public SettingsTemp(boolean disableSounds, boolean something){
        this.disableSounds = disableSounds;
        this.something = something;
    }

    public SettingsTemp(){
        disableSounds = false;
        something = true;
    }
}
