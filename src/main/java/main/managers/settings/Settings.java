package main.managers.settings;

public class Settings {

    public boolean disableSounds;
    public boolean something;

    public Settings(boolean disableSounds,boolean something){
        this.disableSounds = disableSounds;
        this.something = something;
    }

    public Settings(){
        disableSounds = false;
        something = true;
    }
}
