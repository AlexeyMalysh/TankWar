package com.example.tankwar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.concurrent.CopyOnWriteArrayList;
import static com.example.tankwar.TankWarView.enemiesToSpawnPerWave;
import static com.example.tankwar.TankWarView.fps;

public class DebugOverlay {

    private final int PADDING_X = 170;
    private final int PADDING_Y = 60;
    private Paint paint;
    private Joystick joystick;
    private Player player;
    private CopyOnWriteArrayList<Enemy> enemies;

    DebugOverlay(Joystick joystick, Player player, CopyOnWriteArrayList<Enemy> enemies) {
        this.joystick = joystick;
        this.player = player;
        this.enemies = enemies;
        paint = new Paint();

    }

    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(40);

        gameStateOverlay(canvas, 100, PADDING_Y);

        joystickOverlay(canvas, PADDING_X * 2, PADDING_Y);

        playerOverlay(canvas, PADDING_X * 4, PADDING_Y);

        enemyOverlay(canvas, PADDING_X * 6, PADDING_Y);

        drawHitBoxes(canvas);
    }

    public void setEnemies(CopyOnWriteArrayList enemies) {
        this.enemies = enemies;
    }

    private void gameStateOverlay(Canvas canvas, int paddingX, int paddingY) {
        setBoldText();
        canvas.drawText("FPS:" + fps, paddingX, paddingY, paint);
    }

    private void joystickOverlay(Canvas canvas, int paddingX, int paddingY) {
        setBoldText();
        canvas.drawText("Joystick", paddingX, paddingY, paint);

        setNormalText();
        canvas.drawText("X: " + joystick.getPositionX(), paddingX, paddingY * 2, paint);
        canvas.drawText("Y: " + joystick.getPositionY(), paddingX, paddingY * 3, paint);
        canvas.drawText("Degrees: " + joystick.getDegrees(), paddingX, paddingY * 4, paint);
        canvas.drawText("Strength: " + joystick.getStrength(), paddingX, paddingY * 5, paint);
    }

    private void playerOverlay(Canvas canvas, int paddingX, int paddingY) {
        setBoldText();
        canvas.drawText("Player", paddingX, paddingY, paint);

        setNormalText();
        canvas.drawText("X: " + player.getPositionX(), paddingX, paddingY * 2, paint);
        canvas.drawText("Y: " + player.getPositionY(), paddingX, paddingY * 3, paint);
        canvas.drawText("Active Bullets: " + player.getBullets().size(), paddingX, paddingY * 4, paint);
        canvas.drawText("Score: " + player.getScore(), paddingX, paddingY * 5, paint);
    }

    private void enemyOverlay(Canvas canvas, int paddingX, int paddingY) {
        setBoldText();
        canvas.drawText("Enemies", paddingX, paddingY, paint);

        setNormalText();
        canvas.drawText("Spawned: " + enemies.size(), paddingX, paddingY * 2, paint);
        canvas.drawText("Spawn per wave: " + enemiesToSpawnPerWave, paddingX, paddingY * 3, paint);
    }


    private void drawHitBoxes(Canvas canvas) {
        drawHitBox(canvas, player.getRect());

        for (Bullet bullet : player.getBullets()) {
            drawHitBox(canvas, bullet.getRect());
        }

        for (Enemy enemy : enemies) {
            drawHitBox(canvas, enemy.getRect());
            for (Bullet bullet : enemy.getBullets()) {
                drawHitBox(canvas, bullet.getRect());
            }
        }
    }

    private void drawHitBox(Canvas canvas, Rect rect) {
        paint.setColor(Color.argb(255, 255, 0, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        canvas.drawRect(rect, paint);
    }

    private void setBoldText() {
        paint.setFakeBoldText(true);
    }

    private void setNormalText() {
        paint.setFakeBoldText(false);
    }

}
