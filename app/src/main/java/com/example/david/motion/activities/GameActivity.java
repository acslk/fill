package com.example.david.motion.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.david.motion.R;
import com.example.david.motion.fragments.PauseDialogFragment;
import com.example.david.motion.game.GamePanel;

import java.io.IOException;


public class GameActivity extends FullScreenActivity implements SurfaceHolder.Callback {

    SurfaceView surfaceView;
    SensorManager sensorManager;
    public GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.i("Motion", "after setContent");

        gamePanel = new GamePanel(this);
        try {
            gamePanel.loadMap();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
        Log.i("Motion", "map loaded");

        surfaceView = (SurfaceView)findViewById(R.id.gameSurfaceView);
        gamePanel.setSurfaceHolder(surfaceView.getHolder());
        surfaceView.getHolder().addCallback(this);

        surfaceView.setOnTouchListener(gamePanel);

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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gamePanel.setScreen(surfaceView.getWidth(), surfaceView.getHeight());
        gamePanel.resumeGame();
        Log.i("Motion", "gamepanel resumeGame finished");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gamePanel.pauseGame();
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
