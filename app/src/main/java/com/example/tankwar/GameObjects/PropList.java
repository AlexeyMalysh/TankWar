package com.example.tankwar.GameObjects;

import android.content.Context;

import com.example.tankwar.MainActivity;
import com.example.tankwar.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PropList {

    private ArrayList<Prop> props;

    // Add props here to avoid polluting the TankWarView
    // Right now everything is on 16x16 grid due to scaling issues, if there was more time this would be refactored as it's very messy looking
    public PropList(Context context) {
        props = new ArrayList<>(Arrays.asList(
                new Prop(context, 12, (float) 4.5, (float) 3.5, R.drawable.tree_brown, true),
                new Prop(context, 30, 6, (float) 4.5, R.drawable.twigs_brown, false),
                new Prop(context, 12, (float) 3.5, (float) 11.5, R.drawable.tree_brown, true),
                new Prop(context, 30, 5, (float) 12.5, R.drawable.twigs_brown, false),
                new Prop(context, 12, (float) 9.5, 8, R.drawable.tree_brown, true),
                new Prop(context, 56, (float) 11.5, (float) 3.2, R.drawable.fence_red_vertical, true)
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
