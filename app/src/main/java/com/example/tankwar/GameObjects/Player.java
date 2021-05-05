package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.UI.Joystick;
import com.example.tankwar.MainActivity;

import static com.example.tankwar.TankWarView.fps;

public class Player extends Tank {

    public final float MAX_SPEED = 250f;
    private Joystick joystick;
    private int score = 0;

    public Player(Context context, Joystick joystick, float positionX, float positionY) {
        super(context, TankType.BLUE, positionX, positionY);

        this.joystick = joystick;

        // Initial update is required to draw player on canvas
        updateDegrees();
        updatePosition();
    }

    public void update() {

        // Only update player if user is touching joystick
        if (joystick.getStrength() == 0) return;

        checkBounds();

        setDegrees(joystick.getDegrees());

        setPositionX(getPositionX() + (joystick.getPositionX() * MAX_SPEED / fps));
        setPositionY(getPositionY() - (joystick.getPositionY() * MAX_SPEED / fps));

        updateDegrees();
        updatePosition();
    }


    public void checkBounds() {

        if (isOutOfBoundsX()) {
            if (getPositionX() > 1) {
                setPositionX(0);
            } else {
                setPositionX(MainActivity.getScreenWidth() - getWidth());
            }
        }

        if (isOutOfBoundsY()) {
            if (getPositionY() > 1) {
                setPositionY(0);
            } else {
                setPositionY(MainActivity.getScreenHeight() - getHeight());
            }
        }
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

}
