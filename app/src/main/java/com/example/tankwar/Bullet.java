package com.example.tankwar;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.tankwar.TankWarView.fps;

public class Bullet extends GameObject {

    private static final float MAX_SPEED = 1000f;
    private static final int EXPLOSION_DELAY_MILLISECONDS = 100;
    private float radianX;
    private float radianY;
    private boolean active = true;
    private boolean disposed = false;


    public Bullet(Context context, Tank tank) {

        super(context, tank.getPositionX(), tank.getPositionY());

        setBitmap(getBulletBitmapId(tank.getType()));

        this.radianX = (float) Math.cos(tank.getDegrees() * Math.PI / 180);
        this.radianY = (float) Math.sin(tank.getDegrees() * Math.PI / 180);

        // Centers bullet within tank
        float centerX = getPositionX() + tank.getCenterX() - getCenterX();
        float centerY = getPositionY() + tank.getCenterY() - getCenterY();

        // Places bullet in front of tanks gun
        setPositionX(centerX + tank.getWidth() * (radianX / 2));
        setPositionY(centerY - tank.getHeight() * (radianY / 2));

        // This will stay the same throughout bullets life
        setDegrees(tank.getDegrees());

        // Initial update is required to draw Bullet on canvas
        updateDegrees();
        updatePosition();
    }

    public void update() {

        if (isOutOfBounds()) {
            dispose();
            return;
        }

        if (!active) {
            stop();
            return;
        }

        setPositionX(getPositionX() + (radianX * MAX_SPEED / fps));
        setPositionY(getPositionY() - (radianY * MAX_SPEED / fps));
        updateDegrees();
        updatePosition();
    }


    // Deals with updating of bullets position and if collisions should occur
    public boolean isActive() {
        return active;
    }

    private void dispose() {
        disposed = true;
        active = false;
    }

    // Deals with removing of bullet after collision or out of bounds
    public boolean isDisposed() {
        return disposed;
    }

    public void explode() {
        // Set active to false preventing updates to bullet
        active = false;

        // Change bullet sprite to explosion sprite
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setBitmap(R.drawable.explosion2);
            }
        }, 0);

        // Delay the removing of an active bullet so the explosion takes place first
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                disposed = true;
            }
        }, EXPLOSION_DELAY_MILLISECONDS);

    }

    private int getBulletBitmapId(TankType type) {
        switch (type) {
            case BLUE:
                return R.drawable.bullet_blue;
            case BLACK:
                return R.drawable.bullet_dark;
        }
        return 0;
    }

    private void stop() {
        setPositionX(getPositionX());
        setPositionY(getPositionY());
        setDegrees(getDegrees());
        updateDegrees();
        updatePosition();
    }

}
