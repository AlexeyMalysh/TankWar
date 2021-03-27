package com.example.tankwar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.Console;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static android.content.ContentValues.TAG;


public class TankWarView extends SurfaceView implements Runnable {


    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    public static long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;

    private Player player;
    private JoystickView joystick;
    private int score = 0;
    private int lives = 5;

    private int deg;
    private int strength;
    private int x;
    private int y;


    public TankWarView(Context context, int x, int y) {

        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        // Initialize player
        player = new Player(context, screenX, screenY);

        prepareLevel();
    }


    public void onJoystickEvent(int deg, int strength, int x, int y) {

        player.update(deg, strength, x, y);

        this.deg = deg;
        this.strength = strength;
        this.x = x;
        this.y = y;
    }


    private void prepareLevel() {

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


    private void update() {

    }


    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 255, 128));
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);
            canvas.drawText("FPS:" + fps, 1250, 50, paint);
            canvas.drawText("Degree: " + deg, 1500, 50, paint);
            canvas.drawText("Strength " + strength, 1750, 50, paint);
            canvas.drawText("Joystick X: " + (x), 2000, 50, paint);
            canvas.drawText("Joystick Y " + y, 2250, 50, paint);


            canvas.drawText("Player X" + player.getX(), 2250, 500, paint);

            player.draw(canvas);

            ourHolder.unlockCanvasAndPost(canvas);
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

}
