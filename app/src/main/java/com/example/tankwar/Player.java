package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import static com.example.tankwar.TankWarView.fps;

public class Player {


    private float MAX_SPEED = 60f;
    private int RELATIVE_WIDTH = 20;
    private int SENSITIVITY_Y = 10;
    private int SENSITIVITY_X = 10;

    private Bitmap bitmap;
    private Paint paint;
    private float width;
    private float positionX;
    private float positionY;
    private float degrees;
    private boolean initialised = false;
    private Matrix matrix;

    public int DEBUG_FIRE_COUNT = 0;


    public Player(Context context, int screenX, int positionX, int positionY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank_blue);

        width =  screenX / RELATIVE_WIDTH;

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) width, false);

        this.positionX = positionX;
        this.positionY = positionY;

        matrix = new Matrix();


    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public float getPositionX () {
        return this.positionX;
    }

    public float getPositionY () {
        return this.positionY;
    }

    public void update(int degrees, int joystickX, int joystickY) {
            setRotation(degrees);

            setPosition(joystickX, joystickY);
    }

    private void setRotation(int degrees) {
        this.degrees = degrees;

        matrix.setRotate(-this.degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    }

    private void setPosition(int x, int y) {

        if(x > 0) {
            this.positionX += MAX_SPEED / fps;
        } else if (x < 0) {
            this.positionX -= MAX_SPEED / fps;
        }

        if(y > 0) {
            this.positionY += MAX_SPEED / fps;
        } else if(y < 0) {
            this.positionY -= MAX_SPEED / fps;
        }

        matrix.postTranslate(this.positionX, this.positionY);
    }

    private void updatePosition() {

    }


    public void fire() {
        this.DEBUG_FIRE_COUNT += 1;
    }
}
