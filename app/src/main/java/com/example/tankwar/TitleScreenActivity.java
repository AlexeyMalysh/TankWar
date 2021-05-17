package com.example.tankwar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class TitleScreenActivity extends AppCompatActivity {

    private GlobalState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Remove top bar
        this.getSupportActionBar().hide();

        appState = ((GlobalState) getApplicationContext());

        Log.d("MUSIC", String.valueOf(appState.musicEnabled()));

        initMusicButton();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toggleMusic(View view) {
        if (!appState.musicEnabled()) {
            ((ImageButton) view).setImageResource(R.drawable.music_on);
            startService(new Intent(this, MusicService.class));
        } else {
            ((ImageButton) view).setImageResource(R.drawable.music_off);
            stopService(new Intent(this, MusicService.class));
        }
        appState.toggleMusic();
    }

    private void initMusicButton() {
        ImageButton musicButton = findViewById(R.id.musicButton);

        if (!appState.musicEnabled()) {
            musicButton.setImageResource(R.drawable.music_off);
            return;
        }

        musicButton.setImageResource(R.drawable.music_on);

        if (!MusicService.isActive()) {
            startService(new Intent(this, MusicService.class));
        }
    }
}