package com.example.tankwar;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FireButton {

    private final int DELAY_MILLISECONDS = 1000;
    public ImageButton view;



    @SuppressLint("ClickableViewAccessibility")
    public FireButton(ImageButton fireButtonView) {
        this.view = fireButtonView;

        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void toggle() {
        view.setEnabled(false);
        updateImage(false);

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                () -> {
                    view.setEnabled(true);
                    updateImage(true);
                },
                DELAY_MILLISECONDS);
    }

    private void updateImage(boolean isActive) {
        int imageId = isActive ? R.drawable.fire_button_active : R.drawable.fire_button_inactive;

        view.setImageResource(imageId);
    }

}

