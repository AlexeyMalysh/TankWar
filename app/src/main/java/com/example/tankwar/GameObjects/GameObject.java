package com.example.tankwar.GameObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.tankwar.MainActivity;
import com.example.tankwar.R;

public abstract class GameObject {
    protected Context context;
    protected Bitmap bitmap;
    protected Matrix matrix;
    protected Paint paint;
    protected float positionX;
    protected float positionY;
    protected float degrees;
    protected boolean rigid;

    public GameObject(Context context, float positionX, float positionY, boolean rigid) {
        this.context = context;
        this.matrix = new Matrix();
        this.positionX = positionX;
        this.positionY = positionY;
        this.rigid = rigid;
    }

    public void setBitmap(int id, int scaledWidth) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        this.bitmap = scale(bitmap, MainActivity.getScreenWidth() / scaledWidth, 100000);
    }

    private Bitmap scale(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        }
        return image;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public void update() {
        updateDegrees();
        updatePosition();
    }

    public void updatePosition() {
        matrix.postTranslate(positionX, positionY);
    }

    public void updateDegrees() {
        matrix.setRotate(-degrees, (float) getWidth() / 2, (float) getHeight() / 2);
    }


    // Position Helpers -----------------------------------------

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
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


    // Dimension Helpers -----------------------------------------

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

    // Bounds Calculations  -----------------------------------------

    public boolean isOutOfBoundsX() {
        return (getPositionX() + getWidth() < 0) || getPositionX() > MainActivity.getScreenWidth();
    }

    public boolean isOutOfBoundsY() {
        return (getPositionY() + getHeight() < 0) || getPositionY() > MainActivity.getScreenHeight();
    }

    public boolean isOutOfBounds() {
        return isOutOfBoundsX() || isOutOfBoundsY();
    }


    // Calculations Between Objects -----------------------------------------

    public float getDegreesFrom(GameObject obj) {
        return (float) ((float) Math.atan2(getPositionY() - obj.getPositionY(), obj.getPositionX() - getPositionX()) * 180 / Math.PI);
    }

    // Collision Detection -----------------------------------------

    public boolean collidesWith(GameObject obj) {
        return Rect.intersects(getRect(), obj.getRect());
    }

    public boolean isRigid() {
        return this.rigid;
    }

}


