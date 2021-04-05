package com.example.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class GameObject {
    protected float positionX;
    protected float positionY;
    protected float degrees;
    protected Matrix matrix;
    protected Bitmap bitmap;
    private Paint paint;


    public GameObject(Context context, int imageId, float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.matrix = new Matrix();

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);

        // TODO: Height and width should be scaled to device dimensions
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / 2), (int) (bitmap.getHeight() / 2), false);
    }

    // Update logic must be implemented for each GameObject
    public abstract void update();

    //    Every GameObject must be drawn in TankWarView
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
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

    public int getCenterX() {
        return getWidth() / 2;
    }

    public int getCenterY() {
        return getHeight() / 2;
    }

    public Rect getRect() {
        int left = (int) getPositionX();
        int top = (int) getPositionY();
        int right = (int) getPositionX() + getWidth();
        int bottom = (int) getPositionY() + getHeight();

        return new Rect(left, top, right, bottom);
    }

    public boolean collidesWith(GameObject obj) {
        return Rect.intersects(getRect(), obj.getRect());
    }

    // Draw updates
    public void updatePosition() {
        matrix.postTranslate(positionX, positionY);
    }

    public void updateDegrees() {
        matrix.setRotate(-degrees, (float) getWidth() / 2, (float) getHeight() / 2);
    }

}
