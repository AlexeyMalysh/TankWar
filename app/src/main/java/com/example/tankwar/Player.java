package com.example.tankwar;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import static com.example.tankwar.TankWarView.fps;

public class Player extends GameObject {

    private final float MAX_SPEED = 60f;
    private Joystick joystick;
    private Context context;
    private List<Bullet> bullets = new ArrayList<>();

    public Player(Context context, Joystick joystick, int imageId, float positionX, float positionY) {
        super(context, imageId, positionX, positionY);
        this.joystick = joystick;
        this.context = context;
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
        Bullet bullet = new Bullet(context, getPositionX(), getPositionY(), getWidth(), getHeight(), rotation);

        bullets.add(bullet);
    }

    public List<Bullet> getBullets() {
        return this.bullets;
    }
}
