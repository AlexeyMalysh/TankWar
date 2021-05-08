package com.example.tankwar.UI;

import android.view.ViewGroup;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Joystick {

    private static final int threshold = 0;
    private float positionX = 1;
    private float positionY = 0;
    private int degrees = 0;
    private int strength = 0;

    public Joystick(JoystickView joystickView) {
        int loopInterval = 8;

        joystickView.setOnMoveListener(this::update, loopInterval);

        if (joystickView.getParent() != null) {
            ((ViewGroup) joystickView.getParent()).removeView(joystickView);
        }

        joystickView.setBackgroundSizeRatio(0.5f);
        joystickView.setButtonSizeRatio(0.5f);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public int getDegrees() {
        return degrees;
    }

    public int getStrength() {
        return strength;
    }

    private void update(int degrees, int strength) {

        this.strength = strength;

        // Prevents updates if player is not touching joystick
        if (this.strength <= threshold) return;

        this.degrees = degrees;

        // Calculates X and Y based on degrees of joystick
        positionX = (float) Math.cos(degrees * Math.PI / 180);
        positionY = (float) Math.sin(degrees * Math.PI / 180);
    }
}

