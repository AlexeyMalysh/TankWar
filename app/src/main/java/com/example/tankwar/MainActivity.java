package com.example.tankwar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import io.github.controlwear.virtual.joystick.android.JoystickView;


public class MainActivity extends AppCompatActivity {

    TankWarView tankWarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Remove top bar
        this.getSupportActionBar().hide();

        // Set Initial layout
        setContentView(R.layout.activity_main);

        // Allows two views on same layout
        RelativeLayout layout = new RelativeLayout(getApplicationContext());


        tankWarView = new TankWarView(getApplicationContext(), getScreenWidth(), getScreenHeight());

        JoystickView joystick = initJoystick();

        layout.addView(tankWarView);

        layout.addView(joystick);

        // Update to new layout
        setContentView(layout);
    }


    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        tankWarView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        tankWarView.pause();
    }


    private JoystickView initJoystick() {
        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView_left);

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                tankWarView.onJoystickEvent(angle, strength);
            }
        });

        if (joystick.getParent() != null) {
            ((ViewGroup) joystick.getParent()).removeView(joystick);
        }

        return joystick;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}