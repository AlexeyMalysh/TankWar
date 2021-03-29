package com.example.tankwar;

import android.view.ViewGroup;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Joystick {

    private final int CENTER_POSITION = 50;
    private final int LOOP_INTERVAL = 8;
    private int positionX;
    private int positionY;
    private int degrees;
    private int strength;
    private JoystickView joystick;


    public Joystick(JoystickView joystickView) {

        joystick = joystickView;

        joystick.setOnMoveListener((degrees, strength) -> {
           update(degrees, strength);
        }, LOOP_INTERVAL);

        if (joystick.getParent() != null) {
            ((ViewGroup) joystick.getParent()).removeView(joystick);
        }

        normalizeButton();
    }

    public void update(int degrees, int strength) {
        this.positionX = joystick.getNormalizedX() - CENTER_POSITION;
        this.positionY = joystick.getNormalizedY() - CENTER_POSITION;
        this.degrees = degrees;
        this.strength = strength;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getDegrees() {
        return degrees;
    }

    public int getStrength() {
        return strength;
    }

    private void normalizeButton() {
        joystick.setBackgroundSizeRatio((float) 0.5);
        joystick.setButtonSizeRatio((float) 0.5);
    }

}
