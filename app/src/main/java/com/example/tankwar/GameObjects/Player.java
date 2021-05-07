package com.example.tankwar.GameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tankwar.EnemySpawner;
import com.example.tankwar.UI.Joystick;
import com.example.tankwar.MainActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.tankwar.TankWarView.fps;

public class Player extends Tank {

    public final float MAX_SPEED = 250f;
    public final int INVULNERABILITY_TIME = 1500;
    private Joystick joystick;
    private int score = 0;
    private int health = 3;
    private boolean invulnerable = false;
    private Paint paint;


    public Player(Context context, Joystick joystick, float positionX, float positionY) {
        super(context, TankType.BLUE, positionX, positionY);
        this.joystick = joystick;
        this.speed = 200f;

        paint = new Paint();

        // Initial update is required to draw player on canvas
        updateDegrees();
        updatePosition();
    }

    // Overridden to show invulnerability frames
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public void update(List<GameObject> objects) {

        // Only update player if user is touching joystick
        if (joystick.getStrength() > 0) {
            setDegrees(joystick.getDegrees());

            setPositionX(getPositionX() + (joystick.getPositionX() * getSpeed() / fps));
            setPositionY(getPositionY() - (joystick.getPositionY() * getSpeed() / fps));

            checkBounds();
            checkCollisions(objects);
        }

        updateDegrees();
        updatePosition();

        updateBullets(objects);
    }


    private void updateBullets(List<GameObject> objects) {
        CopyOnWriteArrayList<Bullet> activeBullets = new CopyOnWriteArrayList<>();

        for (Bullet bullet : getBullets()) {

            bullet.update();

            for (GameObject object : objects) {

                if (object instanceof Player) continue;

                if (!bullet.isActive() || !bullet.collidesWith(object)) continue;

                bullet.explode(object);

                if (object instanceof Enemy) {
                    ((Enemy) object).destroy();
                    incrementScore(10);
                }

            }

            if (!bullet.isDisposed()) {
                activeBullets.add(bullet);
            }
        }

        setBullets(activeBullets);
    }


    public void checkBounds() {
        if (isOutOfBoundsX()) {
            if (getPositionX() > 1) {
                setPositionX(0);
            } else {
                setPositionX(MainActivity.getScreenWidth() - getWidth());
            }
        }

        if (isOutOfBoundsY()) {
            if (getPositionY() > 1) {
                setPositionY(0);
            } else {
                setPositionY(MainActivity.getScreenHeight() - getHeight());
            }
        }
    }

    public void checkCollisions(List<GameObject> objects) {
        for (GameObject object : objects) {
            if (collidesWith(object) && !(object instanceof Player)) {
                stop();
            }
        }
    }

    // Reverses direction user is trying to move which stops player
    public void stop() {
        setPositionX(getPositionX() - (joystick.getPositionX() * MAX_SPEED / fps));
        setPositionY(getPositionY() + (joystick.getPositionY() * MAX_SPEED / fps));
        updateDegrees();
        updatePosition();
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public void takeDamage() {
        decrementHealth(1);
        toggleInvulnerability();
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void decrementHealth(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }

    private void toggleInvulnerability() {
        invulnerable = true;

        blinkAnimation();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                invulnerable = false;
            }
        }, INVULNERABILITY_TIME);
    }

    private void blinkAnimation() {
        Timer t = new Timer();


        t.scheduleAtFixedRate(new TimerTask() {
            boolean transparent = false;

            @Override
            public void run() {

                if (!invulnerable) {
                    paint.setAlpha(255);
                    t.cancel();
                    return;
                }

                if (transparent) {
                    paint.setAlpha(255);
                    transparent = false;
                } else {
                    paint.setAlpha(50);
                    transparent = true;
                }

            }
        }, 0, 100);

    }


}
