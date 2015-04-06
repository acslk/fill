package com.example.david.motion.game;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.david.motion.R;

/**
 * Created by David on 2015-03-04.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GamePanel gamePanel;

    public GameSurfaceView(Context context, GamePanel gamePanel) {
        super(context);
        this.gamePanel = gamePanel;
        this.gamePanel.setSurfaceHolder(getHolder());
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("Motion", "surfaceCreated");
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

}
