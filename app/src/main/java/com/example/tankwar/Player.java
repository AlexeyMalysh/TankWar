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
    public float x;
    private float y;
    private float deg;

    private float velocityX;
    private int MAX_SPEED = 5;
    private boolean initialised = false;



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

        matrix.setRotate(-deg, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        matrix.postTranslate(this.x, this.y);





        canvas.drawBitmap(bitmap, matrix, paint);
    }

    public void update(int deg, int strength, int x, int y) {


        // Prevents accidental presses and prevents player resetting when letting go
        if (strength > 0) {

            if(x > 10) {
                this.x += 1;
            } else if (x < -10) {
                this.x -= 1;
            }


            if(y > 10) {
                this.y += 1;
            } else if(y < -10) {
                this.y -= 1;
            }

            setRotation(deg);

        }





//            if(x > 50) {
//                matrix.postTranslate((this.x += strength) / 60, y);
//            } else if(x < 50) {
//                matrix.postTranslate((this.x -= strength) / 60, y);
//            }
//
//
//            if(y > 50) {
//                matrix.postTranslate(x, (this.y += strength) / 60);
//            } else if (y < 50) {
//                matrix.postTranslate(x, (this.y -= strength) / 60);
//            }
//


    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRotation(int deg) {
        this.deg = deg;
    }

    public float getX () {
        return this.x;
    }
}
