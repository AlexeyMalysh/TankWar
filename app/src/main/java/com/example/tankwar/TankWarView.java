package com.example.tankwar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
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

    private ArrayList<Enemy> enemyList;

    public TankWarView(Context context, int screenX, int screenY, Joystick joystick, FireButton fireButton) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        // Initialize joystick
        this.joystick = joystick;

        float centerX = (float) screenX / 2;
        float centerY = (float) screenY / 2;

        player = new Player(context, joystick, centerX, centerY);

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

        enemyList = new ArrayList<>();

        if (enemyList.size() <= 1) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (enemyList.size() <= 1) {
                        enemyList.add(new Enemy(getContext(), player, 500, 500));
                    }
                }
            }, 0, 10000);
        }


    }

    private void update() {

        // Player update logic
        player.update();

        CopyOnWriteArrayList<Bullet> activeBullets = new CopyOnWriteArrayList<>();

        for (Bullet bullet : player.getBullets()) {
            bullet.update();

            // Detect if bullet collides with any enemies
            for (Enemy enemy : enemyList) {
                // If bullet collides with player it will explode and be set false
                if (bullet.collidesWith(enemy) && bullet.isActive()) {
                    bullet.explode(enemy);
                }
            }

            // Any bullet that has not exploded or gone out of bounds with be looped through again
            if (!bullet.isDisposed()) {
                activeBullets.add(bullet);
            }
        }

        player.setBullets(activeBullets);


        // Enemies update logic
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

            enemy.setBullets(enemyActiveBullets);
        }


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

            debugOverlay();


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

    private void debugOverlay() {

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(40);
        canvas.drawText("FPS:" + fps, 400, 50, paint);
        canvas.drawText("Degrees: " + joystick.getDegrees(), 700, 50, paint);
        canvas.drawText("Strength: " + joystick.getStrength(), 700, 100, paint);
        canvas.drawText("Joystick X: " + joystick.getPositionX(), 1000, 50, paint);
        canvas.drawText("Joystick Y: " + joystick.getPositionY(), 1000, 100, paint);
        canvas.drawText("Player X: " + player.getPositionX(), 1500, 50, paint);
        canvas.drawText("Player Y: " + (player.getPositionY()), 1500, 100, paint);
        canvas.drawText("Active player bullets: " + player.getBullets().size(), 1500, 300, paint);

        if (player.isOutOfBounds()) {
            canvas.drawText("Player is out of bounds ", 1500, 350, paint);
        } else {
            canvas.drawText("Player is in bounds ", 1500, 350, paint);
        }

//        drawHitBox(player.getRect());

//        for (Bullet bullet : player.getBullets()) {
//            drawHitBox(bullet.getRect());
//        }
////
//        for (Enemy enemy : enemyList) {
//            drawHitBox(enemy.getRect());
//            for (Bullet bullet : enemy.getBullets()) {
//                drawHitBox(bullet.getRect());
//            }
//        }
    }

    private void drawHitBox(Rect rect) {
        paint.setColor(Color.argb(255, 255, 0, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        canvas.drawRect(rect, paint);
    }

}
