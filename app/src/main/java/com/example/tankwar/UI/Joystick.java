package com.example.tankwar.UI;

import android.view.ViewGroup;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Joystick {

    private static final int THRESHOLD = 0;
    private final int LOOP_INTERVAL = 8;
    private final float BACKGROUND_RATIO = 0.5f;
    private final float BUTTON_RATIO = 0.5f;
    private float positionX = 1;
    private float positionY = 0;
    private int degrees = 0;
    private int strength = 0;
    private JoystickView joystick;


    public Joystick(JoystickView joystickView) {

        joystick = joystickView;

        joystick.setOnMoveListener(this::update, LOOP_INTERVAL);

        if (joystick.getParent() != null) {
            ((ViewGroup) joystick.getParent()).removeView(joystick);
        }

        joystick.setBackgroundSizeRatio(BACKGROUND_RATIO);
        joystick.setButtonSizeRatio(BUTTON_RATIO);
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
        if (this.strength <= THRESHOLD) return;

        this.degrees = degrees;

        // Calculates X and Y based on degrees of joystick
        positionX = (float) Math.cos(degrees * Math.PI / 180);
        positionY = (float) Math.sin(degrees * Math.PI / 180);
    }
}

