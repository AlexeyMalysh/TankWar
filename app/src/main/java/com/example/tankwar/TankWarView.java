package com.example.tankwar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import io.github.controlwear.virtual.joystick.android.JoystickView;


public class TankWarView extends SurfaceView implements Runnable {


    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = false;
    private Canvas canvas;
    private Paint paint;
    public static long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    private Player player;
    private Joystick joystick;
    private FireButton fireButton;
    private int score = 0;
    private int lives = 5;



    public TankWarView(Context context, int playerX, int playerY, Joystick joystick, FireButton fireButton) {

        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = playerX;
        screenY = playerY;

        // Initialize player
        player = new Player(context, screenX, screenY);

        // Initialize joystick
        this.joystick = joystick;

        // Initialize fire button
        this.fireButton = fireButton;

        prepareLevel();
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
        // Only updates when joystick is being pressed
        if(joystick.getStrength() > 0) {
            player.update(joystick.getDegrees(), joystick.getPositionX(), joystick.getPositionY());
        }

        if(fireButton.isPressed()) {
            player.fire();
            fireButton.isPressed = false;
        }
    }


    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 26, 255, 128));

            debugOverlay();

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

    private void debugOverlay() {

        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(40);
        canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);
        canvas.drawText("FPS:" + fps, 400, 50, paint);
        canvas.drawText("Degrees: " + joystick.getDegrees(), 700, 50, paint);
        canvas.drawText("Strength: " + joystick.getStrength(), 700, 100, paint);
        canvas.drawText("Joystick X: " + joystick.getPositionX(), 1000, 50, paint);
        canvas.drawText("Joystick Y: " + joystick.getPositionY(), 1000, 100, paint);
        canvas.drawText("Player X: " + player.getPositionX(), 1300, 50, paint);
        canvas.drawText("Player Y: " + player.getPositionY(), 1300, 100, paint);
        canvas.drawText("Player Fired: " + player.DEBUG_FIRE_COUNT, 1700, 50, paint);

    }

}
