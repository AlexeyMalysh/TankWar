package com.example.tankwar;

import android.content.Context;

import static com.example.tankwar.TankWarView.fps;

public class Bullet extends GameObject {

    private static final float MAX_SPEED = 1000f;
    private float radianX;
    private float radianY;


    public Bullet(Context context, Player player) {

        // TODO: This may need refactoring to allow for different colour bullets
        super(context, R.drawable.bullet_blue, player.getPositionX(), player.getPositionY());

        this.radianX = (float) Math.cos(player.getDegrees() * Math.PI / 180);
        this.radianY = (float) Math.sin(player.getDegrees() * Math.PI / 180);

        // Centers bullet within tank
        float centerX = getPositionX() + player.getCenterX() - getCenterX();
        float centerY = getPositionY() + player.getCenterY() - getCenterY();

        // Places bullet in front of tanks gun
        setPositionX(centerX + player.getWidth() * (radianX / 2));
        setPositionY(centerY - player.getHeight() * (radianY / 2));

        // This will stay the same throughout bullets life
        setDegrees(player.getDegrees());

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

}
