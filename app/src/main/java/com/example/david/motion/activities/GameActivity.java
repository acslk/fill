package com.example.david.motion.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.david.motion.R;
import com.example.david.motion.collectable.Collectable;
import com.example.david.motion.collectable.PaintObj;
import com.example.david.motion.collidable.Collidable;
import com.example.david.motion.collidable.DoomBlock;
import com.example.david.motion.collidable.FragileBlock;
import com.example.david.motion.collidable.SlideBlock;
import com.example.david.motion.collidable.SolidBlock;
import com.example.david.motion.collidable.SwitchBlock;
import com.example.david.motion.field.DirectionField;
import com.example.david.motion.field.Field;
import com.example.david.motion.field.NoGravityField;
import com.example.david.motion.fragments.PauseDialogFragment;
import com.example.david.motion.game.GameMap;
import com.example.david.motion.game.GameObj;
import com.example.david.motion.region.ColorBlock;
import com.example.david.motion.region.GameColor;
import com.example.david.motion.region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameActivity extends FullScreenActivity implements SurfaceHolder.Callback, SensorEventListener, View.OnTouchListener {

    public static final int SOLIDWALL = 1;
    public static final int BREAKABLE = 2;
    public static final int MOVABLE = 3;
    public static final int DIRECTION_FIELD = 10;
    public static final int GRAVITY_FIELD = 11;
    public static final int COLOROBJ = 21;

    public volatile boolean gameRunning;

    private class DisplayRunnable implements Runnable {
        @Override
        public void run() {
            Log.i("Motion", "Starting display loop");
            while (gameRunning) {
                Canvas canvas = surfaceView.getHolder().lockCanvas();
                if (canvas != null) {
                    gameMap.updateDisplay(canvas);
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            Log.i("Motion", "Display loop finished executing");
        }
    }

    private class GameRunnable implements Runnable {
        @Override
        public void run() {
            Log.i("Motion", "Starting game loop");
            while (gameRunning) {
                gameMap.updateStatus();
                if (gameMap.isGameFinished()) {
                    endGame(false);
                }
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    break;
                }
            }
            Log.i("Motion", "Game loop finished executing");
        }
    }

    ExecutorService pool = Executors.newFixedThreadPool(2);
    GameMap gameMap;

    SurfaceView surfaceView;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.i("Motion", "after setContent");

        try {
            loadMap();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
        Log.i("Motion", "map loaded");

        surfaceView = (SurfaceView)findViewById(R.id.gameSurfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onBackPressed() {
        pauseGame();
        showPauseDialog();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameMap.loadScreen(surfaceView.getWidth(), surfaceView.getHeight());
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            Log.i("sensor", "null");
        } else {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
            Log.i("sensor", "exists");
        }

        resumeGame();
        Log.i("Motion", "gamepanel resumeGame finished");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        sensorManager.unregisterListener(this);
        pauseGame();
    }

    public void showPauseDialog () {
        FragmentManager fm = getFragmentManager();
        PauseDialogFragment dialog = new PauseDialogFragment();
        dialog.show(fm, "pause");
    }

    public void loadMap () throws IOException {
        List<ColorBlock> blocks = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        List<Collidable> collidables = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        List<Collectable> collectables = new ArrayList<>();

        float mapWidth = 800, mapHeight = 800;
        GameColor defaultColor = new GameColor(2, 2, 2);
        Region backRegion = new Region(defaultColor);
        backRegion.childBlocks.add(new ColorBlock(0, 0, mapWidth, mapHeight, 2, 2, 2));

        blocks.add(new ColorBlock(0, 0, 100, 100, 0, 0, 1));
        blocks.add(new ColorBlock(700, 0, 100, 100, 1, 0, 0));
        Region.setRegions(regions, blocks);
        backRegion.neighborRegions.addAll(regions);

        Log.i("MotionRegion", regions.size() + " ");
        for (Region region : regions) {
            for (ColorBlock block : region.childBlocks) {
                Log.i("MotionRegion", block.gameColor.toString());
            }
        }

        collidables.add(new SolidBlock(50, 50, 100, 100));
        collidables.add(new SlideBlock(50, 400, 400, 100, 1));
        collidables.add(new FragileBlock(50, 600, 200, 200));
        collidables.add(new DoomBlock(400, 500, 100, 100));
        collidables.add(new SwitchBlock(200, 50, 100, 100, 1));
        fields.add(new DirectionField(400, 50, 150, 150, GameObj.Direction.BOTTOM));
        fields.add(new NoGravityField(400, 250, 100, 100));
        collectables.add(new PaintObj(100, 200, 0, 0, 1));
        collectables.add(new PaintObj(20, 200, 0, 1, 0));
        collectables.add(new PaintObj(680, 0, 1, 0, 0));

        gameMap = new GameMap(getResources().getDimension(R.dimen.ballDiameter), 800, 800);
        gameMap.loadStuff(backRegion, regions, collidables, fields, collectables);
    }

    public void resumeGame() {
        if (!gameRunning) {
            gameRunning = true;
            pool.execute(new DisplayRunnable());
            pool.execute(new GameRunnable());
        }
    }

    public void pauseGame () {
        if (gameRunning) {
            gameRunning = false;
        }
    }

    public void endGame(boolean quit) {
        pauseGame();
        Intent returnIntent = gameMap.makeEndGameInfo();
        if (quit) {
            returnIntent.putExtra("gameStatus", false);
            returnIntent.putExtra("failMessage", "You have quitted the game");
        }
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("Motion", event.toString() + " " + v.getId());
//        switch (v.getId()) {
//            case R.id.gameSurfaceView :
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    pauseGame();
                    showPauseDialog();
                    Log.i("Motion", "show dialog");
                }
//                break;
//        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        gameMap.updateBallAcceleration(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
