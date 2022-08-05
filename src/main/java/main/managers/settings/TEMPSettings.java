package main.managers.settings;

public class TEMPSettings {

    public boolean disableSounds;
    public boolean something;

    public TEMPSettings(boolean disableSounds, boolean something){
        this.disableSounds = disableSounds;
        this.something = something;
    }

    public TEMPSettings(){
        disableSounds = false;
        something = true;
    }
}
