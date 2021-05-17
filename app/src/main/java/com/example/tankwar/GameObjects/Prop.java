package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;

// Used for trees, barricades, etc
public class Prop extends GameObject {

    public Prop(Context context, int scaledWidth, float col, float row, int bitmapId, boolean rigid) {
        super(context, MainActivity.getScreenWidth() / 16 * col, MainActivity.getScreenHeight() / 16 * row, rigid);
        this.rigid = rigid;
        setBitmap(bitmapId, scaledWidth);
        update();
    }

    public void update() {
        updateDegrees();
        updatePosition();
    }

}
