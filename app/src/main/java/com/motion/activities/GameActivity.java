package com.motion.activities;

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

import com.motion.R;
import com.motion.collectables.PaintObj;
import com.motion.collidables.FragileBlock;
import com.motion.collidables.SolidBlock;
import com.motion.collidables.SwitchBlock;
import com.motion.fields.DirectionField;
import com.motion.fields.NoGravityField;
import com.motion.fragments.PauseDialogFragment;
import com.motion.game.Block;
import com.motion.game.Collectables;
import com.motion.game.Collidables;
import com.motion.game.ColorMap;
import com.motion.game.Fields;
import com.motion.game.Game;
import com.motion.game.MovingObj;
import com.motion.game.GameColor;

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
    Game game;
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
                    game.updateStatus();
                    if (game.isGameFinished()) {
                        endGame(false);
                    }
                }

                interpolation = tempTime / UPDATE_GAME_INTERVAL;

                Canvas canvas = surfaceView.getHolder().lockCanvas();
                if (canvas != null) {
                    runOnUiThread(updateView);
                    game.updateDisplay(canvas, interpolation);
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
        game.loadScreen(surfaceView.getWidth(), surfaceView.getHeight());
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

        Collidables collidables = new Collidables(null);
        Fields fields = new Fields(null);
        Collectables collectables = new Collectables(null);

        int mapWidth = 100, mapHeight = 100;
        GameColor defaultColor = new GameColor(2, 2, 2);
        ColorMap colorMap = new ColorMap(mapWidth, mapHeight, defaultColor);
        game = new Game(mapWidth, mapHeight, 40, 70);

        colorMap.addRect(40, 50, 10, 5, new GameColor(1, 2, 2));
        colorMap.addRect(30, 30, 5, 20, new GameColor(1, 1, 2));
        colorMap.addRect(30, 10, 20, 25, new GameColor(1, 1, 2));
        colorMap.addRect(45, 30, 5, 20, new GameColor(1, 1, 2));
        colorMap.addRect(0, 0, 100, 10, new GameColor(1, 1, 1));

        collidables.add(new SwitchBlock(20, 10, 10, 10, 1));
        collidables.add(new FragileBlock(0, 45, 30, 5));
        collidables.add(new SolidBlock(50, 25, 10, 10));
//        collidables.add(new SlideBlock(600, 250, 100, 100, 1));
        collidables.add(new SolidBlock(70, 25, 10, 10));

        fields.add(new NoGravityField(0, 20, 30, 35));
        fields.add(new DirectionField(50, 40, 30, 10, MovingObj.Direction.BOTTOM));

        collectables.add(new PaintObj(10, 15, 1, 2, 2));
        collectables.add(new PaintObj(40, 35, 2, 1, 2));
        collectables.add(new PaintObj(40, 15, 2, 2, 1));
        collectables.add(new PaintObj(60, 70, 2, 2, 1));

        game.loadStuff(colorMap, collidables, fields, collectables);
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
        Intent returnIntent = game.makeEndGameInfo();
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    game.setUserTouching(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    game.setUserTouching(false);
                }
                break;
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        game.updateBallAcceleration(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
