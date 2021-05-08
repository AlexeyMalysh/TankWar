package com.example.tankwar.UI;

import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.tankwar.R;

public class FireButton {

    public ImageButton view;

    public FireButton(ImageButton fireButtonView) {
        this.view = fireButtonView;

        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void toggle() {
        view.setEnabled(false);
        updateImage(false);

        int delayMilliseconds = 1000;

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                () -> {
                    view.setEnabled(true);
                    updateImage(true);
                },
                delayMilliseconds);
    }

    private void updateImage(boolean isActive) {
        int imageId = isActive ? R.drawable.fire_button_active : R.drawable.fire_button_inactive;
        view.setImageResource(imageId);
    }

}

