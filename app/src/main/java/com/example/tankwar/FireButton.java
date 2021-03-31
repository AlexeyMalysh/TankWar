package com.example.tankwar;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FireButton {

    public ImageButton view;
    public boolean isActive = false;

    @SuppressLint("ClickableViewAccessibility")
    public FireButton(ImageButton fireButtonView) {
        this.view = fireButtonView;

        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void toggle() {

        this.isActive = !this.isActive;

        toggleImage();
    }

    private void toggleImage() {
        int imageId = this.isActive ? R.drawable.fire_button_inactive : R.drawable.fire_button_active;

        view.setImageResource(imageId);
    }

}

