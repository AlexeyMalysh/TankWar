package com.example.tankwar;

import android.content.Context;

import static com.example.tankwar.TankWarView.fps;

public class Bullet extends GameObject {

    private static final float MAX_SPEED = 1000f;

    private float radianX;
    private float radianY;


    public Bullet(Context context, float positionX, float positionY, int playerWidth, int playerHeight, float degrees, Joystick joystick) {

        // TODO: This may need refactoring to allow for different colour bullets
        super(context, R.drawable.bullet_blue, positionX, positionY);

        this.radianX = joystick.getPositionX();
        this.radianY = joystick.getPositionY();

        // Calculates center of tank
        float centerX = getPositionX() + (playerWidth / 2) - (getWidth() / 2);
        float centerY = getPositionY() + (playerHeight / 2) - (getHeight() / 2);

        // Places bullet in front of tanks gun
        setPositionX(centerX + (playerWidth * (radianX / 2)));
        setPositionY(centerY - (playerHeight * (radianY / 2)));

        // Set initial degrees, this will stay the same through bullets life
        setDegrees(degrees);

        updateRotation();
        updatePosition();
    }

    public void update() {

        // Get x and y based on degrees of joystick
        setPositionX(getPositionX() + (radianX * MAX_SPEED / fps));
        setPositionY(getPositionY() - (radianY * MAX_SPEED / fps));

        updateRotation();
        updatePosition();
    }


}
