package com.example.tankwar.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.tankwar.EnemySpawner;
import com.example.tankwar.GameObjects.Bullet;
import com.example.tankwar.GameObjects.Enemy;
import com.example.tankwar.GameObjects.Player;

import static com.example.tankwar.TankWarView.fps;

public class DebugOverlay {

    private final Paint paint;
    private final Joystick joystick;
    private final Player player;
    private EnemySpawner enemySpawner;

    public DebugOverlay(Joystick joystick, Player player, EnemySpawner enemySpawner) {
        this.joystick = joystick;
        this.player = player;
        this.enemySpawner = enemySpawner;
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(40);

        final int PADDING_Y = 60;
        final int PADDING_X = 170;

        gameStateOverlay(canvas, 100, PADDING_Y);

        joystickOverlay(canvas, PADDING_X * 2, PADDING_Y);

        playerOverlay(canvas, PADDING_X * 4, PADDING_Y);

        enemyOverlay(canvas, PADDING_X * 6, PADDING_Y);

        drawHitBoxes(canvas);
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
        canvas.drawText("Health: " + player.getHealth(), paddingX, paddingY * 4, paint);
        canvas.drawText("Invincible: " + player.isInvulnerable(), paddingX, paddingY * 5, paint);
        canvas.drawText("Active Bullets: " + player.getBullets().size(), paddingX, paddingY * 6, paint);
        canvas.drawText("Score: " + player.getScore(), paddingX, paddingY * 7, paint);
    }

    private void enemyOverlay(Canvas canvas, int paddingX, int paddingY) {
        setBoldText();
        canvas.drawText("Enemies", paddingX, paddingY, paint);

        setNormalText();
        canvas.drawText("Max: " + enemySpawner.getMaxEnemies(), paddingX, paddingY * 2, paint);
        canvas.drawText("Spawned: " + enemySpawner.getEnemies().size(), paddingX, paddingY * 3, paint);
        canvas.drawText("Enemies per wave: " + enemySpawner.getEnemiesPerWave(), paddingX, paddingY * 4, paint);
    }


    private void drawHitBoxes(Canvas canvas) {
        drawHitBox(canvas, player.getRect());

        for (Bullet bullet : player.getBullets()) {
            drawHitBox(canvas, bullet.getRect());
        }

        for (Enemy enemy : enemySpawner.getEnemies()) {
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
