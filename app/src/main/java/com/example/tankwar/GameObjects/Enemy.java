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

    private boolean firing = true;
    private final Player player;

    public Enemy(Context context, Player player, float positionX, float positionY) {
        super(context, TankType.BLACK, positionX, positionY);

        this.player = player;
        this.speed = 125f;

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

                if (object instanceof Prop && object.isRigid()) {
                    bullet.explode(object);
                    continue;
                }

                if (!(object instanceof Player)) continue;

                Player player = (Player) object;

                if (!player.isInvulnerable()) {
                    bullet.explode(player);
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

            if (object instanceof Enemy) continue;

            if (collidesWith(object) && object.isRigid()) {
                stop();

                if (object instanceof Prop) {
                    firing = false;
                }

            }
        }
    }

    private void turnTowardsPlayer() {
        setDegrees(getDegreesFrom(player));
        updateDegrees();
    }

    private void moveTowardsPlayer() {
        setPositionX(getPositionX() + (calculateRadianX() * speed / fps));
        setPositionY(getPositionY() - (calculateRadianY() * speed / fps));
    }

    private void initFiring() {
        Random rand = new Random();

        int max = 5000;
        int mix = 2500;

        int fireRate = rand.nextInt(max - mix + 1) + mix;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (firing) fire();
            }
        }, mix, fireRate);
    }


}

