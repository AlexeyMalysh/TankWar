package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;

// Used for trees, barricades, etc
public class Prop extends GameObject {

    private boolean rigid;

    public Prop(Context context, float positionX, float positionY, int bitmapId, boolean rigid) {
        super(context, MainActivity.dpToPx(positionX), MainActivity.dpToPx(positionY));
        this.rigid = rigid;
        setBitmap(bitmapId);
        update();
    }

    public Prop(Context context, float positionX, float positionY, int degrees, int bitmapId, boolean rigid) {
        super(context, MainActivity.dpToPx(positionX), MainActivity.dpToPx(positionY));
        this.rigid = rigid;
        setBitmap(bitmapId);
        setDegrees(degrees);
        update();
    }

    public void update() {
        updateDegrees();
        updatePosition();
    }

    // Rigid determines if the prop can be clipped through
    public boolean isRigid() {
        return rigid;
    }
}
