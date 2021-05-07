package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.tankwar.TankWarView.fps;

public class Enemy extends Tank {

    private final int MAX_FIRE_RATE = 5000;
    private final int MIN_FIRE_RATE = 2500;
    private boolean firing = true;
    private Player player;

    public Enemy(Context context, Player player) {
        super(context, TankType.BLACK, 0, 0);
        this.player = player;
        this.speed = 125f;

        setSpawnPoint();
        updateDegrees();
        updatePosition();
        initFiring();
    }

    public void update(List<GameObject> objects) {
        firing = true;
        turnTowardsPlayer();
        moveTowardsPlayer();
        updateDegrees();
        updatePosition();
        checkCollisions(objects);
        updateBullets(objects);
    }

    private void updateBullets(List<GameObject> objects) {
        CopyOnWriteArrayList<Bullet> activeBullets = new CopyOnWriteArrayList<>();

        for (Bullet bullet : getBullets()) {

            bullet.update();

            for (GameObject object : objects) {

                if (object instanceof Enemy) continue;

                if (!bullet.isActive() || !bullet.collidesWith(object)) continue;

                bullet.explode(object);

                if (!(object instanceof Player)) continue;

                Player player = (Player) object;

                if (!player.isInvulnerable()) {
                    player.takeDamage();
                }
            }

            if (!bullet.isDisposed()) {
                activeBullets.add(bullet);
            }
        }

        setBullets(activeBullets);
    }

    public void checkCollisions(List<GameObject> objects) {
        for (GameObject object : objects) {
            if (collidesWith(object) && !(object instanceof Enemy)) {
                stop();
            }

            if (collidesWith(object) && object instanceof Prop) {
                firing = false;
            }
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

        setPositionX(getPositionX() + (radianX * speed / fps));
        setPositionY(getPositionY() - (radianY * speed / fps));

        updatePosition();
    }

    // TODO: Needs refactored
    public void stop() {

        float degrees = getDegreesFrom(player);

        float radianX = (float) Math.cos(degrees * Math.PI / 180);
        float radianY = (float) Math.sin(degrees * Math.PI / 180);

        setPositionX(getPositionX() - (radianX * speed / fps));
        setPositionY(getPositionY() + (radianY * speed / fps));

        updateDegrees();
        updatePosition();
    }

    private void initFiring() {
        Random rand = new Random();

        int fireRate = rand.nextInt(MAX_FIRE_RATE - MIN_FIRE_RATE + 1) + MIN_FIRE_RATE;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (firing) {
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

