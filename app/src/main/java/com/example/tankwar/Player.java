package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import static com.example.tankwar.TankWarView.fps;

public class Player extends GameObject {


    private float MAX_SPEED = 60f;
    private Joystick joystick;


    public Player(Context context, Joystick joystick, int imageId, int width, int height, float positionX, float positionY) {
        super(context, imageId, width, height, positionX, positionY);
        this.joystick = joystick;
    }

    public void update() {
        setRotation(joystick.getDegrees());

        int joystickX = joystick.getPositionX();
        int joystickY = joystick.getPositionY();

        if (joystickX > 0) {
            setPositionX(positionX + MAX_SPEED / fps);
        } else if (joystickX < 0) {
            setPositionX(positionX - MAX_SPEED / fps);
        }

        if (joystickY > 0) {
            setPositionY(positionY + MAX_SPEED / fps);
        } else if (joystickY < 0) {
            setPositionY(positionY - MAX_SPEED / fps);
        }

        updateRotation();
        updatePosition();
    }


    public void fire() {

    }
}
