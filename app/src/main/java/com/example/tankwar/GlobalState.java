package com.example.tankwar;

import android.app.Application;

public class GlobalState extends Application {


    private boolean music = true;
    private boolean soundEffects = true;

    public boolean musicEnabled() {
        return music;
    }

    public boolean soundEffectsEnabled() {
        return soundEffects;
    }

    public void toggleMusic() {
        music = !music;
    }

    public void toggleSoundEffects() {
        soundEffects = !soundEffects;
    }

}
