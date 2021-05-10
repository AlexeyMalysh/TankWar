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
import com.example.tankwar.GameObjects.GameObject;
import com.example.tankwar.GameObjects.Player;
import com.example.tankwar.GameObjects.Prop;
import com.example.tankwar.GameObjects.PropList;
import com.example.tankwar.UI.DebugOverlay;
import com.example.tankwar.UI.FireButton;
import com.example.tankwar.UI.HealthBar;
import com.example.tankwar.UI.Joystick;
import com.example.tankwar.UI.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class TankWarView extends SurfaceView implements Runnable {

    private final Context context;
    private Thread gameThread = null;
    private final SurfaceHolder ourHolder;
    private final Paint paint;
    private volatile boolean playing;
    private boolean paused = false;
    public static long fps;

    private final Joystick joystick;
    private final FireButton fireButton;
    private DebugOverlay debugOverlay;
    private Bitmap levelBg;
    private HealthBar healthBar;
    private Score score;
    private Player player;
    private EnemySpawner enemySpawner;
    private PropList propList;

    public static final boolean DEV_MODE = false;

    public TankWarView(Context context, Joystick joystick, FireButton fireButton) {
        super(context);
        ourHolder = getHolder();
        paint = new Paint();
        this.context = context;
        this.joystick = joystick;
        this.fireButton = fireButton;

        prepareLevel();
    }

    private void prepareLevel() {
        levelBg = BitmapFactory.decodeResource(getResources(), R.drawable.level_bg);
        levelBg = Bitmap.createScaledBitmap(levelBg, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), false);

        float centerX = (float) MainActivity.getScreenWidth() / 2;
        float centerY = (float) MainActivity.getScreenHeight() / 2;

        player = new Player(context, joystick, centerX, centerY);

        healthBar = new HealthBar(context, player);

        score = new Score(context, player);

        enemySpawner = new EnemySpawner(context, player);

        propList = new PropList(context);

        if (DEV_MODE) {
            debugOverlay = new DebugOverlay(joystick, player, enemySpawner, propList);
        }

        initFireButton();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initFireButton() {

        fireButton.view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                player.fire();
                fireButton.toggle();
            }

            return true;
        });
    }

    @Override
    public void run() {
        while (playing) {

            long startFrameTime = System.currentTimeMillis();

            if (!paused) {
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    private void update() {
        ArrayList<GameObject> gameObjects = new ArrayList();

        gameObjects.addAll(enemySpawner.getEnemies());
        gameObjects.addAll(propList.getProps());
        gameObjects.add(player);

        player.update(gameObjects);
        enemySpawner.update(gameObjects);
        propList.update();
    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            Canvas canvas = ourHolder.lockCanvas();

            canvas.drawBitmap(levelBg, 0, 0, paint);

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
            score.draw(canvas);

            if (DEV_MODE) debugOverlay.draw(canvas);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

}
