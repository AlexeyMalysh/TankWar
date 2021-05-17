package com.example.tankwar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Remove top bar
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_game_over);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String finalScore = "Final Score: " + intent.getIntExtra("SCORE", 0);

        // Capture the layout's TextView and set the string as its text
        TextView finalScoreView = findViewById(R.id.finalScore);
        finalScoreView.setText(finalScore);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void quitGame(View view) {
        Intent intent = new Intent(this, TitleScreenActivity.class);
        startActivity(intent);
    }

}