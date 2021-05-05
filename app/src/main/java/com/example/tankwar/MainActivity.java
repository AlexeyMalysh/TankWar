package com.example.tankwar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.tankwar.UI.FireButton;
import com.example.tankwar.UI.Joystick;

import io.github.controlwear.virtual.joystick.android.JoystickView;


@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    TankWarView tankWarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Remove top bar
        this.getSupportActionBar().hide();

        // Set Initial layout
        setContentView(R.layout.activity_main);

        // Allows two views on same layout
        RelativeLayout layout = new RelativeLayout(getApplicationContext());

        // Init joystick
        JoystickView joystickView = findViewById(R.id.joystickView_left);
        Joystick joystick = new Joystick(joystickView);

        // Init fire button
        ImageButton fireButtonView = findViewById(R.id.fireButton);
        FireButton fireButton = new FireButton(fireButtonView);

        // Init game
        tankWarView = new TankWarView(getApplicationContext(), joystick, fireButton);

        // Add views
        layout.addView(tankWarView);
        layout.addView(joystickView);
        layout.addView(fireButtonView);

        // Update layout
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

    public static int getScreenWidth() {
        return Math.round(Resources.getSystem().getDisplayMetrics().widthPixels);
    }

    public static int getScreenHeight() {
        return Math.round(Resources.getSystem().getDisplayMetrics().heightPixels);
    }


    public static int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

}