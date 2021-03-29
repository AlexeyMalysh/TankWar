package com.example.tankwar;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.function.Function;

public class FireButton {

    private ImageButton fireButton;
    public boolean isPressed = false;

    @SuppressLint("ClickableViewAccessibility")
    public FireButton(ImageButton fireButtonView) {
        this.fireButton = fireButtonView;

        if (fireButton.getParent() != null) {
            ((ViewGroup) fireButton.getParent()).removeView(fireButton);
        }

        fireButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.isPressed = true;
                    toggleButton();
                    return true;
                case  MotionEvent.ACTION_UP:
                    this.isPressed = false;
                    toggleButton();
                    return true;
            }

            return true;
        });
    }

    private void toggleButton() {
        if(this.isPressed) {
            fireButton.setImageResource(R.drawable.fire_button_inactive);
            return;
        }

        fireButton.setImageResource(R.drawable.fire_button_active);
    }

    public boolean isPressed() {
        return this.isPressed;
    }
}
