package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;
import com.example.tankwar.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.tankwar.TankWarView.fps;

public class Bullet extends GameObject {

    private final float speed;
    private final float radianX;
    private final float radianY;
    private boolean active = true;
    private boolean disposed = false;
    private final int scaledWidth = 54;

    public Bullet(Context context, Tank tank) {
        super(context, tank.getPositionX(), tank.getPositionY(), false);

        this.speed = tank.getSpeed() * 5;

        setBitmap(getBulletBitmapId(tank.getType()), scaledWidth);

        this.radianX = tank.calculateRadianX();
        this.radianY = tank.calculateRadianY();

        // Centers bullet within tank
        float centerX = getPositionX() + tank.getCenterX() - getCenterX();
        float centerY = getPositionY() + tank.getCenterY() - getCenterY();

        // Places bullet in front of tanks gun
        setPositionX(centerX + tank.getWidth() * (radianX / 2));
        setPositionY(centerY - tank.getHeight() * (radianY / 2));

        // This will stay the same throughout bullets life
        setDegrees(tank.getDegrees());

        // Initial update is required to draw Bullet on canvas
        update();
    }

    public void update() {
        if (!active) return;

        if (isOutOfBounds()) dispose();

        setPositionX(getPositionX() + (radianX * speed / fps));
        setPositionY(getPositionY() - (radianY * speed / fps));

        updateDegrees();
        updatePosition();
    }

    public void explode(GameObject objHit) {
        // Set active to false preventing updates to bullet
        active = false;

        // Initially set to final explosion to get width
        setBitmap(R.drawable.explosion2, scaledWidth / 4);

        // Place explosion in center of target that was hit
        float centerX = objHit.getCenterX() - (float) getWidth() / 2;
        float centerY = objHit.getCenterY() - (float) getHeight() / 2;

        setPositionX(centerX);
        setPositionY(centerY);
        setDegrees(0);

        // Final update
        updateDegrees();
        updatePosition();

        // Bullet explosion animation
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setBitmap(R.drawable.explosion1, scaledWidth / 4);
            }
        }, 0);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setBitmap(R.drawable.explosion2, scaledWidth / 4);
            }
        }, 100);

        // Delay the removing of an active bullet so the explosion takes place first
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                disposed = true;
            }
        }, 150);
    }

    // Deals with updating of bullets position and if collisions should occur
    public boolean isActive() {
        return active;
    }

    // Deals with removing of bullet after collision or out of bounds
    public boolean isDisposed() {
        return disposed;
    }

    private void dispose() {
        disposed = true;
        active = false;
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
}
