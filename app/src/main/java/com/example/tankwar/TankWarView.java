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
import com.example.tankwar.UI.DebugOverlay;
import com.example.tankwar.UI.FireButton;
import com.example.tankwar.UI.Joystick;

import java.util.Timer;
import java.util.TimerTask;
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
    private Joystick joystick;
    private FireButton fireButton;
    private Bitmap levelBg;
    private CopyOnWriteArrayList<Enemy> enemyList;
    public static int enemiesToSpawnPerWave = 1;
    private DebugOverlay debugOverlay;
    public static final boolean DEV_MODE = true;

    public TankWarView(Context context, Joystick joystick, FireButton fireButton) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        // Initialize joystick
        this.joystick = joystick;

        float centerX = (float) MainActivity.getScreenWidth() / 2;
        float centerY = (float) MainActivity.getScreenHeight() / 2;

        player = new Player(context, joystick, centerX, centerY);

        if (DEV_MODE) {
            debugOverlay = new DebugOverlay(joystick, player, enemyList);
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
        levelBg = BitmapFactory.decodeResource(getResources(), R.drawable.level_1);
        levelBg = Bitmap.createScaledBitmap(levelBg, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), false);

        enemyList = new CopyOnWriteArrayList<>();

        spawnEnemies();

    }

    private void spawnEnemies() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < enemiesToSpawnPerWave; i++) {
                    if (enemyList.size() < 10) {
                        spawnEnemy();
                    }
                }
            }
        }, 0, 5000);


    }

    private void spawnEnemy() {
        enemyList.add(new Enemy(getContext(), player));
    }

    private void update() {
        updatePlayer();
        updateEnemies();

        if(DEV_MODE) {
            debugOverlay.setEnemies(enemyList);
        }

    }

    private void updatePlayer() {
        player.update();

        CopyOnWriteArrayList<Bullet> activeBullets = new CopyOnWriteArrayList<>();

        for (Bullet bullet : player.getBullets()) {
            bullet.update();

            // Detect if bullet collides with any enemies
            for (Enemy enemy : enemyList) {
                // If bullet collides with player it will explode and be set false
                if (bullet.collidesWith(enemy) && bullet.isActive()) {
                    bullet.explode(enemy);
                    enemy.destroy();
                    player.incrementScore(10);

                    // If player has killed 10 enemies, increase enemy spawn per wave
                    if (player.getScore() != 0 && player.getScore() % 100 == 0) {
                        enemiesToSpawnPerWave += 1;
                    }

                    // Add an enemy so there is always at least one on screen at all times
                    if (enemyList.size() <= 1) {
                        spawnEnemy();
                    }
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

        for (Enemy enemy : enemyList) {
            enemy.update();

            // Create list that stores active bullets
            CopyOnWriteArrayList<Bullet> enemyActiveBullets = new CopyOnWriteArrayList<>();

            for (Bullet bullet : enemy.getBullets()) {
                bullet.update();

                // If bullet collides with player it will explode and be set false
                if (bullet.collidesWith(player) && bullet.isActive()) {
                    bullet.explode(player);
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

        enemyList = activeEnemies;
    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawBitmap(levelBg, 0, 0, paint);

            player.draw(canvas);

            for (Enemy enemy : enemyList) {
                enemy.draw(canvas);
                for (Bullet bullet : enemy.getBullets()) {
                    bullet.draw(canvas);
                }
            }

            for (Bullet bullet : player.getBullets()) {
                bullet.draw(canvas);
            }

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
