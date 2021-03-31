package com.example.tankwar;

import android.view.ViewGroup;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static com.example.tankwar.TankWarView.fps;

public class Joystick {

    private static final int THRESHOLD = 0;
    private static final int SENSITIVITY_X = 12;
    private static final int SENSITIVITY_Y = 12;
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
            this.update(degrees, strength);
        }, LOOP_INTERVAL);

        if (joystick.getParent() != null) {
            ((ViewGroup) joystick.getParent()).removeView(joystick);
        }

        joystick.setBackgroundSizeRatio((float) 0.5);
        joystick.setButtonSizeRatio((float) 0.5);
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


    private void update(int degrees, int strength) {

        this.strength = strength;

        if (this.strength <= this.THRESHOLD) {
            this.positionX = 0;
            this.positionY = 0;
            return;
        }

        int x = joystick.getNormalizedX() - CENTER_POSITION;
        int y = joystick.getNormalizedY() - CENTER_POSITION;


        if((x >= -SENSITIVITY_X) && (x <= SENSITIVITY_X)) {
            this.positionX = 0;
        } else {
            this.positionX = x;
        }

        if((y >= -SENSITIVITY_Y) && (y <= SENSITIVITY_Y)) {
            this.positionY = 0;
        } else {
            this.positionY = y;
        }

        this.degrees = degrees;
    }
}
