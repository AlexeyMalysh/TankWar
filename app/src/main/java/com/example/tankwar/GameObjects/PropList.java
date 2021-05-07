package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PropList {

    private ArrayList<Prop> props;

    // Add props here to avoid polluting the TankWarView
    public PropList(Context context) {
        props = new ArrayList<>(Arrays.asList(
                new Prop(context, 266, 80, R.drawable.tree_brown, true),
                new Prop(context, 287, 322, R.drawable.twigs_brown, false),
                new Prop(context, 220, 300, R.drawable.tree_brown, true),
                new Prop(context, 540, 260, 90, R.drawable.sandbang_brown, true),
                new Prop(context, 545, 300, -45, R.drawable.sandbang_brown, true),
                new Prop(context, 840, 85, R.drawable.fence_red_vertical, false)
        ));
    }

    public ArrayList<Prop> getProps() {
        return props;
    }

    public void update() {
        for (Prop prop : props) {
            prop.update();
        }
    }
}
