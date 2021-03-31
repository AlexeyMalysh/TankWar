package com.example.tankwar;

import android.content.Context;
import android.graphics.Canvas;

public class Bullet extends GameObject {


    public Bullet(Context context, float positionX, float positionY) {
        super(context, R.drawable.tank_blue, 100, 100, positionX, positionY);
    }

    public void update() {

    }
}
