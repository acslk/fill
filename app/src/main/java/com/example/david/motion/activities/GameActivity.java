package com.example.david.motion.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.motion.R;
import com.example.david.motion.collectables.Collectable;
import com.example.david.motion.collectables.PaintObj;
import com.example.david.motion.collidables.Collidable;
import com.example.david.motion.collidables.DoomBlock;
import com.example.david.motion.collidables.FragileBlock;
import com.example.david.motion.collidables.SlideBlock;
import com.example.david.motion.collidables.SolidBlock;
import com.example.david.motion.collidables.SwitchBlock;
import com.example.david.motion.fields.DirectionField;
import com.example.david.motion.fields.Field;
import com.example.david.motion.fields.NoGravityField;
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

    public static final int UPDATE_GAME_INTERVAL = 20;

    public volatile boolean gameRunning;

    ExecutorService pool = Executors.newFixedThreadPool(1);
    GameMap gameMap;
    SensorManager sensorManager;
//    GestureDetector gestureDetector;

    SurfaceView surfaceView;
    TextView itemView;
    TextView timeView;
    ImageView pauseView;
    public long totalTime = 0, tempTime = 0;
    public long lastTime;
    float interpolation;

    private class GameRunnable implements Runnable {
        @Override
        public void run() {
            Log.i("Motion", "Starting game loop");
            while (gameRunning) {

                long currTime = System.currentTimeMillis();
                totalTime += (currTime - lastTime);
                tempTime += (currTime - lastTime);
                lastTime = currTime;

                while (tempTime > UPDATE_GAME_INTERVAL) {
                    tempTime -= UPDATE_GAME_INTERVAL;
                    gameMap.updateStatus();
                    if (gameMap.isGameFinished()) {
                        endGame(false);
                    }
                    runOnUiThread(updateView);
                }

                interpolation = tempTime / UPDATE_GAME_INTERVAL;

                Canvas canvas = surfaceView.getHolder().lockCanvas();
                if (canvas != null) {
                    gameMap.updateDisplay(canvas, interpolation);
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            Log.i("Motion", "Game loop finished executing");
        }
    }

    private Runnable updateView = new Runnable() {
        @Override
        public void run() {
            timeView.setText(totalTime + "");
        }
    };

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

//        gestureDetector = new GestureDetector(this, gestureListener);

        surfaceView = (SurfaceView)findViewById(R.id.gameSurfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);

        itemView = (TextView)findViewById(R.id.itemView);
        timeView = (TextView)findViewById(R.id.gameTime);
        pauseView = (ImageView)findViewById(R.id.pauseGame);
        pauseView.setOnTouchListener(this);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onBackPressed() {
        pauseGame();
        showPauseDialog();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("Window", "onattachedwindow");
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameMap.loadScreen(surfaceView.getWidth(), surfaceView.getHeight());
        holder.setFormat(PixelFormat.RGBA_8888);
        resumeGame();
        Log.i("Motion", "gamepanel resumeGame finished");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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

        gameMap = new GameMap(800, 800, 0, 0);
        gameMap.loadStuff(backRegion, regions, collidables, fields, collectables);
    }

    public void resumeGame() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            Log.i("sensor", "null");
        } else {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
            Log.i("sensor", "exists");
        }
        if (!gameRunning) {
            gameRunning = true;
            lastTime = System.currentTimeMillis();
            pool.execute(new GameRunnable());
        }
    }

    public void pauseGame () {
        sensorManager.unregisterListener(this);
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
        switch (v.getId()) {
            case R.id.pauseGame :
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pauseGame();
                    showPauseDialog();
                    Log.i("Motion", "show dialog");
                }
                break;
            case R.id.gameSurfaceView :
                if (event.getAction() == MotionEvent.ACTION_DOWN && !gameMap.isUserTouching()) {
                    gameMap.setUserTouching(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP && gameMap.isUserTouching()) {
                    gameMap.setUserTouching(false);
                }
                break;
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        gameMap.updateBallAcceleration(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
