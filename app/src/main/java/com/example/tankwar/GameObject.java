package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public abstract class GameObject {
    protected Context context;
    protected float positionX;
    protected float positionY;
    protected float degrees;
    protected Matrix matrix;
    protected Bitmap bitmap;
    private Paint paint;


    public GameObject(Context context, float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.matrix = new Matrix();
        this.context = context;
    }

    // Update logic must be implemented for each GameObject
    public abstract void update();

    //    Every GameObject must be drawn in TankWarView
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    // This is required
    public void setBitmap(int id) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / 2), (int) (bitmap.getHeight() / 2), false);
    }

    //    Helper methods
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

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public float getCenterX() {
        return (getPositionX() + (getWidth() / 2));
    }

    public float getCenterY() {
        return getPositionY() + (getHeight() / 2);
    }

    public Rect getRect() {
        int left = (int) getPositionX();
        int top = (int) getPositionY();
        int right = (int) getPositionX() + getWidth();
        int bottom = (int) getPositionY() + getHeight();

        return new Rect(left, top, right, bottom);
    }

    // Calculations between objects

    public boolean collidesWith(GameObject obj) {
        return Rect.intersects(getRect(), obj.getRect());
    }

    public float getDistanceFrom(GameObject obj) {
        return (float) Math.hypot((getCenterX() - obj.getCenterX()), (getCenterY() - obj.getCenterY()));
    }

    public float getDegreesFrom(GameObject obj) {
        return (float) ((float) Math.atan2(getPositionY() - obj.getPositionY(), obj.getPositionX() - getPositionX()) * 180 / Math.PI);
    }

    // Draw updates
    public void updatePosition() {
        matrix.postTranslate(positionX, positionY);
    }

    public void updateDegrees() {
        matrix.setRotate(-degrees, (float) getWidth() / 2, (float) getHeight() / 2);
    }

}
