package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Player {


    private Bitmap bitmap;
    private Paint paint;
    private float width;
    private float height;
    private float x;
    private float y;
    private float deg;


    public Player(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank_blue);

        x = screenX / 2;
        y = screenY / 2;

        width =  screenX / 20;
        height =  screenX / 20;

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
    }

    public void draw(Canvas canvas) {

        Matrix matrix = new Matrix();

        matrix.postTranslate(x, y);

        matrix.setRotate(-deg, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public void update() {

    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRotation(int deg) {
        this.deg = deg;
    }
}
