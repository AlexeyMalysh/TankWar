package com.example.tankwar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.tankwar.GameObjects.Bullet;
import com.example.tankwar.GameObjects.Enemy;
import com.example.tankwar.GameObjects.Player;
import com.example.tankwar.GameObjects.Prop;
import com.example.tankwar.GameObjects.PropList;
import com.example.tankwar.UI.DebugOverlay;
import com.example.tankwar.UI.FireButton;
import com.example.tankwar.UI.HealthBar;
import com.example.tankwar.UI.Joystick;

import java.util.concurrent.CopyOnWriteArrayList;

public class TankWarView extends SurfaceView implements Runnable {

    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = false;
    private Canvas canvas;
    private Paint paint;
    public static long fps;
    private long timeThisFrame;
    private Player player;
    private FireButton fireButton;
    private Bitmap levelBg;
    private DebugOverlay debugOverlay;
    public static final boolean DEV_MODE = true;
    private EnemySpawner enemySpawner;
    private HealthBar healthBar;
    private PropList propList;

    public TankWarView(Context context, Joystick joystick, FireButton fireButton) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        float centerX = (float) MainActivity.getScreenWidth() / 2;
        float centerY = (float) MainActivity.getScreenHeight() / 2;

        player = new Player(context, joystick, centerX, centerY);

        healthBar = new HealthBar(context, player);

        enemySpawner = new EnemySpawner(context, player);

        propList = new PropList(context);

        if (DEV_MODE) {
            debugOverlay = new DebugOverlay(joystick, player, enemySpawner, propList);
        }

        this.fireButton = fireButton;
        initFireButton();

        prepareLevel();
    }

    @Override
    public void run() {
        while (playing) {

            long startFrameTime = System.currentTimeMillis();

            if (!paused) {
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    private void prepareLevel() {
        levelBg = BitmapFactory.decodeResource(getResources(), R.drawable.level_bg);
        levelBg = Bitmap.createScaledBitmap(levelBg, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), false);
    }


    private void update() {
        updatePlayer();
        updateEnemies();
        propList.update();
    }

    private void updatePlayer() {
        player.update();


        for (Prop prop : propList.getProps()) {
            // If player collides with prop prevent movement in that direction
            if (player.collidesWith(prop) && prop.isRigid()) {
                player.stop();
            }
        }

        for (Enemy enemy : enemySpawner.getEnemies()) {
            if (player.collidesWith(enemy)) {
                player.stop();
            }
        }

        CopyOnWriteArrayList<Bullet> activeBullets = new CopyOnWriteArrayList<>();

        for (Bullet bullet : player.getBullets()) {
            bullet.update();

            // Detect if bullet collides with any enemies
            for (Enemy enemy : enemySpawner.getEnemies()) {
                // If bullet collides with player it will explode and be set false
                if (bullet.collidesWith(enemy) && bullet.isActive()) {
                    bullet.explode(enemy);
                    enemy.destroy();
                    player.incrementScore(10);

                    // each time player kills 10 enemies, increase enemy spawns
                    if (player.getScore() != 0 && player.getScore() % 100 == 0) {
                        enemySpawner.increaseIntensity();
                    }
                }
            }

            for (Prop prop : propList.getProps()) {
                // If bullet collides with prop it will explode and be removed
                if (bullet.collidesWith(prop) && bullet.isActive() && prop.isRigid()) {
                    bullet.explode(prop);
                }
            }

            // Any bullet that has not exploded or gone out of bounds with be looped through again
            if (!bullet.isDisposed()) {
                activeBullets.add(bullet);
            }
        }

        player.setBullets(activeBullets);
    }

    private void updateEnemies() {
        CopyOnWriteArrayList<Enemy> activeEnemies = new CopyOnWriteArrayList<>();


        for (Enemy enemy : enemySpawner.getEnemies()) {
            enemy.update();

            for (Prop prop : propList.getProps()) {
                // If player collides with prop prevent movement in that direction
                if (enemy.collidesWith(prop) && prop.isRigid()) {
                    enemy.stop();
                    enemy.setFiring(false);
                } else {
                    enemy.setFiring(true);
                }
            }

            // Create list that stores active bullets
            CopyOnWriteArrayList<Bullet> enemyActiveBullets = new CopyOnWriteArrayList<>();

            for (Bullet bullet : enemy.getBullets()) {
                bullet.update();

                for (Prop prop : propList.getProps()) {
                    // If bullet collides with prop it will explode and be removed
                    if (bullet.collidesWith(prop) && bullet.isActive() && prop.isRigid()) {
                        bullet.explode(prop);
                    }
                }

                // If bullet collides with player it will explode and be set false
                if (bullet.collidesWith(player) && bullet.isActive() && !player.isInvulnerable()) {
                    bullet.explode(player);
                    player.takeDamage();
                }

                // Any bullet that has not exploded or gone out of bounds with be looped through again
                if (!bullet.isDisposed()) {
                    enemyActiveBullets.add(bullet);
                }


            }


            // Update enemy bullets to bullets that are still active
            enemy.setBullets(enemyActiveBullets);

            // Any enemy that has not been destroyed will be looped through again
            if (!enemy.isDisposed()) {
                activeEnemies.add(enemy);
            }
        }

        enemySpawner.setEnemies(activeEnemies);
    }


    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawBitmap(levelBg, 0, 0, paint);

            // TODO: This needs ordered correctly
            for (Prop prop : propList.getProps()) {
                prop.draw(canvas);
            }

            player.draw(canvas);


            for (Enemy enemy : enemySpawner.getEnemies()) {
                enemy.draw(canvas);
                for (Bullet bullet : enemy.getBullets()) {
                    bullet.draw(canvas);
                }
            }

            for (Bullet bullet : player.getBullets()) {
                bullet.draw(canvas);
            }


            healthBar.draw(canvas);

            if (DEV_MODE) debugOverlay.draw(canvas);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initFireButton() {

        fireButton.view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    player.fire();
                    fireButton.toggle();
                    return true;
            }

            return true;
        });
    }

}
