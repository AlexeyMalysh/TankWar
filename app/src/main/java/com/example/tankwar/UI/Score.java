package com.example.tankwar.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.tankwar.GameObjects.Player;
import com.example.tankwar.MainActivity;
import com.example.tankwar.R;


public class Score {

    private final Player player;
    private final Paint paint;

    public Score(Context context, Player player) {
        this.player = player;
        this.paint = new Paint();
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.rubik_mono_one));
        paint.setTextSize(MainActivity.dpToPx(17));
    }

    public void draw(Canvas canvas) {

        String score = "Score:" + player.getScore();

        float scoreWidth = paint.measureText(score);

        // Place in center
        float x = ((float) MainActivity.getScreenWidth() / 2) - (scoreWidth / 2);
        float y = MainActivity.dpToPx(40);

        setStroke();
        canvas.drawText(score, x, y, paint);

        setFill();
        canvas.drawText(score, x, y, paint);

    }

    private void setFill() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
    }

    private void setStroke() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);
    }
}
