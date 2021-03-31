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
    protected int width;
    protected int height;
    protected int rotation;
    protected Matrix matrix;
    protected Bitmap bitmap;
    private Paint paint;


    public GameObject(Context context, int imageId, int width, int height, float positionX, float positionY) {
        this.width = width;
        this.height = height;
        this.positionX = positionX;
        this.positionY = positionY;
        this.matrix = new Matrix();

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);

        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public abstract void update();

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
        matrix.setRotate(-rotation, width / 2, height / 2);
    }

}
