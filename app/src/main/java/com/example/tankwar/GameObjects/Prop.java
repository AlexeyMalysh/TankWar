package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;

// Used for trees, barricades, etc
public class Prop extends GameObject {

    public Prop(Context context, float positionX, float positionY, int bitmapId, boolean rigid) {
        super(context, positionX, positionY, rigid);
        this.rigid = rigid;
        setBitmap(bitmapId, 12);
        update();
    }

    public void update() {
        updateDegrees();
        updatePosition();
    }

}
