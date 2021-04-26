package com.example.tankwar;

import android.content.Context;

import static com.example.tankwar.TankWarView.fps;

public class Enemy extends GameObject {

    private Player player;

    public Enemy(Context context, Player player, int imageId, float positionX, float positionY) {
        super(context, imageId, positionX, positionY);
        this.player = player;

        updateDegrees();
        updatePosition();
    }

    public void update() {

        turnTowardsPlayer();

        if (!collidesWith(player)) {
            moveTowardsPlayer();
        } else {
            stop();
        }

    }

    // TODO: Fire method, refactor player and enemy into abstract Tank class

    private void turnTowardsPlayer() {
        setDegrees(getDegreesFrom(player));
        updateDegrees();
    }

    private void moveTowardsPlayer() {
        float degrees = getDegreesFrom(player);

        float radianX = (float) Math.cos(degrees * Math.PI / 180);
        float radianY = (float) Math.sin(degrees * Math.PI / 180);

        setPositionX(getPositionX() + (radianX * 60f / fps));
        setPositionY(getPositionY() - (radianY * 60f / fps));

        updatePosition();
    }

    private void stop() {
        setPositionX(getPositionX());
        setPositionY(getPositionY());
        updatePosition();
    }

}

