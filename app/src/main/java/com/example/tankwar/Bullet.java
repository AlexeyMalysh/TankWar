package com.example.tankwar;

import android.content.Context;

import static com.example.tankwar.TankWarView.fps;

public class Bullet extends GameObject {


    private static final float MAX_SPEED = 500f;
    private float radianY;
    private float radianX;
    public boolean isActive = true;

    public Bullet(Context context, float positionX, float positionY, int playerWidth, int playerHeight, int rotation) {
        super(context, R.drawable.bullet_blue, positionX, positionY);


        this.radianY = (float) Math.sin(rotation * Math.PI / 180);
        this.radianX = (float) Math.cos(rotation * Math.PI / 180);

        float centerX = getPositionX() + (playerWidth / 2) - (getWidth() / 2);
        float centerY = getPositionY() + (playerHeight / 2) - (getHeight() / 2);

        // Places bullet outside of tanks shell
        setPositionX(centerX + (playerWidth * (radianX / 2)));
        setPositionY(centerY - (playerHeight * (radianY / 2)));

        setRotation(rotation);

        // Initialises bullet position and rotation
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
