package com.example.david.motion.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.example.david.motion.fragments.PauseDialogFragment;
import com.example.david.motion.game.GamePanel;
import com.example.david.motion.game.GameSurfaceView;

import java.io.IOException;


public class GameActivity extends FullScreenActivity {

    SensorManager sensorManager;
    GameSurfaceView gameView;
    public GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamePanel = new GamePanel(this);


        try {
            gamePanel.loadMap();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
        Log.i("Motion", "map loaded");
        gameView = new GameSurfaceView(this,gamePanel);
        Log.i("Motion", "gameSurfaceView created");
        gameView.setClickable(true);
        gameView.setOnTouchListener(gamePanel);
        Log.i("Motion", "before setContent");
        setContentView(gameView);
        Log.i("Motion", "after setContent");

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume () {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            Log.i("sensor", "null");
        } else {
            sensorManager.registerListener(gamePanel, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
            Log.i("sensor", "exists");
        }
//        gamePanel.resumeGame();
    }

    protected void onPause () {
        super.onPause();
        sensorManager.unregisterListener(gamePanel);
//        gamePanel.pauseGame();
    }

    @Override
    public void onBackPressed() {
        gamePanel.pauseGame();
        showPauseDialog();
    }

    public void showPauseDialog () {
        FragmentManager fm = getFragmentManager();
        PauseDialogFragment dialog = new PauseDialogFragment();
        dialog.show(fm, "pause");
    }

    public void quit(Intent returnIntent) {
        onPause();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
