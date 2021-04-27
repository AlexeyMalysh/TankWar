package com.example.tankwar;

import android.content.Context;
import static com.example.tankwar.TankWarView.fps;

public class Bullet extends GameObject {

    private static final float MAX_SPEED = 1000f;
    private float radianX;
    private float radianY;


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
        setPositionX(getPositionX() + (radianX * MAX_SPEED / fps));
        setPositionY(getPositionY() - (radianY * MAX_SPEED / fps));

        updateDegrees();
        updatePosition();
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
