package com.motion.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.motion.R;
import com.motion.collectables.PaintObj;
import com.motion.collidables.DoomBlock;
import com.motion.collidables.FragileBlock;
import com.motion.collidables.SolidBlock;
import com.motion.collidables.SwitchBlock;
import com.motion.fields.DirectionField;
import com.motion.fields.NoGravityField;
import com.motion.game.Ball;
import com.motion.game.Game;

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

        Game.loadResource(context);
        Ball.loadResrouce(context);

        SolidBlock.loadResource(context);
        FragileBlock.loadResource(context);
//        SlideBlock.loadResource(context);
        DoomBlock.loadResource(context);
        SwitchBlock.loadResource(context);

        DirectionField.loadResource(context);
        NoGravityField.loadResource(context);

        PaintObj.loadResource(context);
    }
}
