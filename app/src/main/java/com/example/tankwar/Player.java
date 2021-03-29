package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Player {


    private float MAX_SPEED = 1.5f;
    private int RELATIVE_WIDTH = 20;
    private int SENSITIVITY_Y = 10;
    private int SENSITIVITY_X = 10;

    private Bitmap bitmap;
    private Paint paint;
    private float width;
    private float height;
    private float positionX;
    private float positionY;
    private float degrees;
    private boolean initialised = false;
    private Matrix matrix;


    public Player(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank_blue);

        width =  screenX / RELATIVE_WIDTH;

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) width, false);

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
        if(x > SENSITIVITY_X) {
            this.positionX += MAX_SPEED;
        } else if (x < - SENSITIVITY_X) {
            this.positionX -= MAX_SPEED;
        }

        if(y > SENSITIVITY_Y) {
            this.positionY += MAX_SPEED;
        } else if(y < -SENSITIVITY_Y) {
            this.positionY -= MAX_SPEED;
        }

        matrix.postTranslate(this.positionX, this.positionY);
    }


}
