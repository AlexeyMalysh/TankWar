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
                new Prop(context, 220, 300, R.drawable.tree_brown, true),
                new Prop(context, 540, 260, R.drawable.tree_brown, true)
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
