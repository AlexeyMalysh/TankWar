package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Tank extends GameObject {

    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    private Context context;
    private TankType type;
    private boolean disposed = false;

    public Tank(Context context, TankType type, float positionX, float positionY) {
        super(context, positionX, positionY);
        this.context = context;
        this.type = type;

        setBitmap(getTankBitmapId(type));
    }

    public void fire() {
        Bullet bullet = new Bullet(context, this);
        bullets.add(bullet);
    }

    public void destroy() {
        // Delay the removing of a tank so the explosion takes place first
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                disposed = true;
            }
        }, 150);
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void setBullets(CopyOnWriteArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public CopyOnWriteArrayList<Bullet> getBullets() {
        return bullets;
    }

    public TankType getType() {
        return type;
    }

    private int getTankBitmapId(TankType type) {
        switch (type) {
            case BLUE:
                return R.drawable.tank_blue;
            case BLACK:
                return R.drawable.tank_dark;
        }
        return 0;
    }

}
