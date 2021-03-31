package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class GameObject {
    protected float positionX;
    protected float positionY;
    protected int rotation;
    protected Matrix matrix;
    protected Bitmap bitmap;
    private Paint paint;


    public GameObject(Context context, int imageId, float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.matrix = new Matrix();

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / 2), (int) (bitmap.getHeight() / 2), false);
    }

    public abstract void update();

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float x) {
        this.positionX = x;
    }

    public void setPositionY(float y) {
        this.positionY = y;
    }

    public void setRotation(int degrees) {
        this.rotation = degrees;
    }

    public void updatePosition() {
        matrix.postTranslate(positionX, positionY);
    }

    public void updateRotation() {
        matrix.setRotate(-rotation, getWidth() / 2, getHeight() / 2);
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }


}
