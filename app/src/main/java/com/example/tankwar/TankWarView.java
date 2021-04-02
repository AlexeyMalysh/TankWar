package com.example.tankwar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

    }

    private void update() {

        for (Bullet bullet : player.getBullets()) {
            bullet.update();
        }

        player.update();
    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 26, 255, 128));

            debugOverlay();

            for (Bullet bullet : player.getBullets()) {
                bullet.draw(canvas);
            }

            player.draw(canvas);

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
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(40);
        canvas.drawText("FPS:" + fps, 400, 50, paint);
        canvas.drawText("Degrees: " + joystick.getDegrees(), 700, 50, paint);
        canvas.drawText("Strength: " + joystick.getStrength(), 700, 100, paint);
        canvas.drawText("Joystick X: " + joystick.getPositionX(), 1000, 50, paint);
        canvas.drawText("Joystick Y: " + joystick.getPositionY(), 1000, 100, paint);
        canvas.drawText("Player X: " + player.getPositionX(), 1500, 50, paint);
        canvas.drawText("Player Y: " + player.getPositionY(), 1500, 100, paint);
        canvas.drawText("Bullets On Screen: " + player.getBullets().size(), 700, 150, paint);
    }

}
