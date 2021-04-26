package com.example.tankwar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

    private Enemy enemy;

    public TankWarView(Context context, int screenX, int screenY, Joystick joystick, FireButton fireButton) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        // Initialize joystick
        this.joystick = joystick;

        float centerX = (float) screenX / 2;
        float centerY = (float) screenY / 2;

        player = new Player(context, joystick, R.drawable.tank_blue, centerX, centerY);

        this.fireButton = fireButton;
        initFireButton();

        prepareLevel();


        // Temporary enemy
        enemy = new Enemy(context, player, R.drawable.tank_dark, 500, 750);


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


    }

    private void update() {

        for (Bullet bullet : player.getBullets()) {
            bullet.update();
        }

        player.update();
        enemy.update();
    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            canvas.drawBitmap(levelBg, 0, 0, paint);

            for (Bullet bullet : player.getBullets()) {
                bullet.draw(canvas);
            }

            player.draw(canvas);

            debugOverlay();


            // Temporary
            enemy.draw(canvas);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    // TODO: This linting needs addressed
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
        canvas.drawText("Bullets on screen: " + player.getBullets().size(), 1500, 300, paint);
        canvas.drawText("Enemy X: " + enemy.getPositionX(), 1500, 150, paint);
        canvas.drawText("Enemy Y: " + (enemy.getPositionY()), 1500, 200, paint);


        drawHitBox(player.getRect());
        drawHitBox(enemy.getRect());

        for (Bullet bullet : player.getBullets()) {
            drawHitBox(bullet.getRect());
        }

    }

    private void drawHitBox(Rect rect) {
        paint.setColor(Color.argb(255, 255, 0, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        canvas.drawRect(rect, paint);
    }

}
