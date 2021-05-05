package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.tankwar.TankWarView.fps;

public class Enemy extends Tank {

    private final int MAX_STOPPING_DISTANCE = MainActivity.getScreenWidth() / 2;
    private final int MAX_FIRE_RATE = 5000;
    private final int MIN_FIRE_RATE = 2500;
    private final int MIN_STOPPING_DISTANCE = 200;
    private final float SPEED = 100f;
    private Player player;
    private float stoppingDistance;

    public Enemy(Context context, Player player ) {
        super(context, TankType.BLACK, 0, 0);
        this.player = player;

        setSpawnPoint();
        updateDegrees();
        updatePosition();
        initFiring();

        // Random distance enemy stopping distance for variation
        Random rand = new Random();
        stoppingDistance = rand.nextInt(MAX_STOPPING_DISTANCE - MIN_STOPPING_DISTANCE + 1) + MIN_STOPPING_DISTANCE;
    }

    public void update() {
        turnTowardsPlayer();

        // Regardless of collisions and distance move towards player if out of bounds
        if (isOutOfBounds()) {
            moveTowardsPlayer();
            return;
        }

        // stop movement if colliding or within stopped distance of player
        if (!collidesWith(player) && getDistanceFrom(player) > stoppingDistance) {
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

        Random rand = new Random();

        int fireRate = rand.nextInt(MAX_FIRE_RATE - MIN_FIRE_RATE + 1) + MIN_FIRE_RATE;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isOutOfBounds() && getDistanceFrom(player) <= stoppingDistance) {
                    fire();
                }
            }
        }, MIN_FIRE_RATE, fireRate);
    }

    private void setSpawnPoint() {
        Random rand = new Random();

        // Obtain a number between 0 - 3;
        int n = rand.nextInt(4);

        // Determine if any should spawn on top, bottom, left or right of screen
        switch (n) {
            case 0:
                //Enemy will spawn at top
                setPositionX(rand.nextInt(MainActivity.getScreenWidth()));
                setPositionY(MainActivity.getScreenHeight() + 100);
                break;
            case 1:
                // Enemy will spawn at bottom
                setPositionX(rand.nextInt(MainActivity.getScreenWidth()));
                setPositionY(-100);
                break;
            case 2:
                // Enemy will spawn at left
                setPositionX(-100);
                setPositionY(rand.nextInt(MainActivity.getScreenHeight()));
                break;
            case 3:
                // Enemy will spawn at right
                setPositionX(MainActivity.getScreenWidth() + 100);
                setPositionY(rand.nextInt(MainActivity.getScreenHeight()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + n);
        }

    }


}

