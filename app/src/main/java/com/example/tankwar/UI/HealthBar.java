package com.example.tankwar.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

import com.example.tankwar.GameObjects.Player;
import com.example.tankwar.MainActivity;
import com.example.tankwar.R;

public class HealthBar {

    private Bitmap bitmap;
    private Paint paint;
    private Player player;

    // TODO: Needs refactored
    public HealthBar(Context context, Player player) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pixel_heart);
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) (MainActivity.getScreenWidth() / 30), (int) (MainActivity.getScreenWidth() / 30), false);
        this.player = player;
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        float dp = 40f;
        float paddingLeft = MainActivity.dpToPx(dp);
        float margin = 18f;

        for (int i = 0; i < player.getHealth(); i++) {
            if (i == 0) {
                canvas.drawBitmap(bitmap, paddingLeft, 75, paint);
            } else {
                canvas.drawBitmap(bitmap, (paddingLeft) + (bitmap.getWidth() * i) + (margin * i), 75, paint);
            }
        }

    }

}
