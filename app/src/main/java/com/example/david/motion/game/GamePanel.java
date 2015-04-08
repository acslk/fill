package com.example.david.motion.game;

import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.david.motion.R;
import com.example.david.motion.activities.GameActivity;
import com.example.david.motion.collectable.Collectable;
import com.example.david.motion.collectable.ColorObj;
import com.example.david.motion.collidable.DoomBlock;
import com.example.david.motion.collidable.FragileBlock;
import com.example.david.motion.collidable.Collidable;
import com.example.david.motion.collidable.SlideBlock;
import com.example.david.motion.collidable.SolidBlock;
import com.example.david.motion.collidable.SwitchBlock;
import com.example.david.motion.field.DirectionField;
import com.example.david.motion.field.Field;
import com.example.david.motion.field.NoGravityField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by David on 2015-03-14.
 */
public class GamePanel  implements SensorEventListener, View.OnTouchListener {

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
                updateDisplay();
            }
            Log.i("Motion", "Display loop finished executing");
        }
    }

    private class GameRunnable implements Runnable {
        @Override
        public void run() {
            Log.i("Motion", "Starting game loop");
            while (gameRunning) {
                updateMap();
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

    GameActivity parentActivity;
    SurfaceHolder surfaceHolder;

    ExecutorService pool;
    GameMap gameMap;

    public GamePanel (GameActivity parentActivity) {
        this.parentActivity = parentActivity;
        pool = Executors.newFixedThreadPool(2);
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void loadMap () throws IOException {
        List<Collidable> collidables = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        List<Collectable> collectables = new ArrayList<>();

        collidables.add(new SolidBlock(50, 50, 100, 100));
        collidables.add(new SlideBlock(50, 400, 400, 100, 1));
        collidables.add(new FragileBlock(50, 600, 200, 200));
        collidables.add(new DoomBlock(400, 500, 100, 100));
        collidables.add(new SwitchBlock(200, 50, 100, 100, 1));
        fields.add(new DirectionField(400, 50, 150, 150, GameObj.Direction.BOTTOM));
        fields.add(new NoGravityField(400, 250, 100, 100));
        collectables.add(new ColorObj(100, 200, 50, 0, 0));
        collectables.add(new ColorObj(20, 200, 0, 50, 0));

        gameMap = new GameMap(parentActivity.getResources().getDimension(R.dimen.ballDiameter), 800, 800);
        gameMap.loadStuff(collidables, fields, collectables);
    }

    public void updateMap () {
        gameMap.updateStatus();
    }

    public void updateDisplay () {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            gameMap.updateDisplay(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
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
        parentActivity.quit(returnIntent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("Motion", event.toString() + " " + v.getId());
        switch (v.getId()) {
            case R.id.gameSurfaceView :
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                }
                break;
            case R.id.statusBar:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    pauseGame();
                    parentActivity.showPauseDialog();
                    Log.i("Motion", "show dialog");
                }
                break;
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        gameMap.ball.updateAcceleration(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void setScreen(float width, float height) {
        gameMap.loadScreen(width, height);
    }

}
