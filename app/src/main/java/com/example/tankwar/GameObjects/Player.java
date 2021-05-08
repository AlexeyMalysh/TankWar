package com.example.tankwar.GameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tankwar.UI.Joystick;
import com.example.tankwar.MainActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.tankwar.TankWarView.fps;

public class Player extends Tank {

    private final Joystick joystick;
    private final Paint paint;
    private int score = 0;
    private int health = 3;
    public final int invulnerabilityTime = 1500;
    private boolean invulnerable = false;

    public Player(Context context, Joystick joystick, float positionX, float positionY) {
        super(context, TankType.BLUE, positionX, positionY);
        this.joystick = joystick;
        this.speed = 200f;
        paint = new Paint();

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
            setPositionX(getPositionX() + (calculateRadianX() * getSpeed() / fps));
            setPositionY(getPositionY() - (calculateRadianY() * getSpeed() / fps));
        }

        checkBounds();
        checkCollisions(objects);

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

                if (object.isRigid()) bullet.explode(object);

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
            if (!(object instanceof Player) && collidesWith(object) && object.isRigid()) {
                stop();
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public int getHealth() {
        return health;
    }

    private void decrementHealth(int amount) {
        health -= amount;
    }

    public void takeDamage() {
        decrementHealth(1);
        toggleInvulnerability();
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    private void toggleInvulnerability() {
        invulnerable = true;

        blinkAnimation();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                invulnerable = false;
            }
        }, invulnerabilityTime);
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
