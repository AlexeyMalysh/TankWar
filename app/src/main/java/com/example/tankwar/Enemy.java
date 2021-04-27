package com.example.tankwar;

import android.content.Context;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.tankwar.TankWarView.fps;

public class Enemy extends Tank {

    private Player player;
    private final float SPEED = 50f;

    public Enemy(Context context, Player player, float positionX, float positionY) {
        super(context, TankType.BLACK, positionX, positionY);

        this.player = player;

        updateDegrees();
        updatePosition();
        initFiring();
    }

    public void update() {
        turnTowardsPlayer();

        if (!collidesWith(player)) {
            moveTowardsPlayer();
        } else {
            stop();
        }
    }

    private void turnTowardsPlayer() {
        setDegrees(getDegreesFrom(player));
        updateDegrees();
    }

    private void moveTowardsPlayer() {
        float degrees = getDegreesFrom(player);

        float radianX = (float) Math.cos(degrees * Math.PI / 180);
        float radianY = (float) Math.sin(degrees * Math.PI / 180);

        setPositionX(getPositionX() + (radianX * SPEED / fps));
        setPositionY(getPositionY() - (radianY * SPEED / fps));

        updatePosition();
    }

    private void stop() {
        setPositionX(getPositionX());
        setPositionY(getPositionY());
        updatePosition();
    }

    private void initFiring() {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                fire();
            }
        },0,5000);
    }

}

