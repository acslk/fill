package com.example.david.motion.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.david.motion.R;
import com.example.david.motion.collectable.ColorObj;
import com.example.david.motion.collidable.DoomBlock;
import com.example.david.motion.collidable.FragileBlock;
import com.example.david.motion.collidable.SlideBlock;
import com.example.david.motion.collidable.SolidBlock;
import com.example.david.motion.collidable.SwitchBlock;
import com.example.david.motion.field.DirectionField;
import com.example.david.motion.field.HighVField;
import com.example.david.motion.field.LowVField;
import com.example.david.motion.field.NoGravityField;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

public class TitleActivity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        loadResources(this);
    }

    public void openLevelSelection (View view) {
        startActivity(new Intent(this, LevelActivity.class));
        overridePendingTransition(0,0);
    }

    public void addPreference (View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Stage1_completed", true);
        editor.putInt("Stage2_nextLevel", 5);
        editor.putInt("stagesUnlocked", 5);
        editor.apply();
    }

    public static void loadResources (Context context) {

        GameMap.loadResource(context);
        Ball.loadResrouce(context);

        SolidBlock.loadResource(context);
        FragileBlock.loadResource(context);
        SlideBlock.loadResource(context);
        DoomBlock.loadResource(context);
        SwitchBlock.loadResource(context);

        DirectionField.loadResource(context);
        NoGravityField.loadResource(context);
        LowVField.loadResource(context);
        HighVField.loadResource(context);

        ColorObj.loadResource(context);
    }
}
