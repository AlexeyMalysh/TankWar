package com.example.tankwar;

import android.content.Context;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.tankwar.TankWarView.fps;

public class Player extends GameObject {

    private final float MAX_SPEED = 60f;
    private Joystick joystick;
    private Context context;
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();

    public Player(Context context, Joystick joystick, int imageId, float positionX, float positionY) {
        super(context, imageId, positionX, positionY);

        this.joystick = joystick;
        this.context = context;

        // Initial update is required to draw player on canvas
        updateDegrees();
        updatePosition();
    }

    public void update() {

        // Only update player if user is touching joystick
        if(joystick.getStrength() == 0) return;

        setDegrees(joystick.getDegrees());

        setPositionX(getPositionX() + (joystick.getPositionX() * MAX_SPEED / fps));
        setPositionY(getPositionY() - (joystick.getPositionY() * MAX_SPEED / fps));

        updateDegrees();
        updatePosition();
    }

    public void fire() {
        Bullet bullet = new Bullet(context,this, joystick);
        bullets.add(bullet);
    }

    public CopyOnWriteArrayList<Bullet> getBullets() { return bullets; }

}
