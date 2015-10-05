package com.motion.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.Log;

import com.motion.R;

/**
 * Contains all related objects and logic for one level of game
 */
public class Game {

    private static float scale; // conversion factor from map unit to pixel
    private static int foundationColor;
    private static int fillColor;

    private Paint foundationPaint;
    private float startX, startY;

    /**
     * size of screen used to display the map, in game unit
     */
    private float screenWidth, screenHeight;
    private boolean fullWidth = true, fullHeight = true;

    // map objects
    public final float width, height; // in map unit
    public ColorMap colorMap;

    private Collidables collidables;
    private Fields fields;
    private Collectables collectables;
    public Ball ball, lastBall;

    //  user status
    boolean userTouching = false;

    // finish game information
    boolean gameFinished = false;
    boolean levelPassed = false;
    String failMessage = "uninitialized";

    public Game(float width, float height, float ballStartX, float ballStartY) {

        ball = new Ball(ballStartX, ballStartY);
        lastBall = new Ball(ball);
        this.width = width;
        this.height = height;

        startX = startY = 0;
        foundationPaint = new Paint();
        foundationPaint.setColor(foundationColor);
    }

    public void loadStuff (ColorMap colorMap, Collidables collidables,
                           Fields fields, Collectables collectables) {
        this.colorMap = colorMap;
        colorMap.setBallPos(ball);
        this.collidables = collidables;
        this.fields = fields;
        this.collectables = collectables;
    }

    public void loadScreen(float screenWidth, float screenHeight) {
        this.screenWidth = unit(screenWidth);
        this.screenHeight = unit(screenHeight);
        if (width < this.screenWidth) {
            startX = (this.screenWidth - width)/2;
            fullWidth = false;
        }
        if (height < this.screenHeight) {
            startY = (this.screenHeight - height)/2;
            fullHeight = false;
        }
    }

    // this might seem redundant, but it's currently used for synchronization
    public synchronized void updateBallAcceleration (SensorEvent sensorEvent) {
        ball.updateAcceleration(sensorEvent);
    }

    public synchronized void updateStatus () {

        long time = System.nanoTime();

        fields.update(this, ball);
        ball.updateVelocity();
        ball.updateDisplacement();
        collidables.update(this, ball, lastBall);
        colorMap.update(ball, lastBall);

        ball.interactBound(width, height);
        lastBall.getCopy(ball);
        collectables.update(this, ball);

        setDisplayPosition();

        long diff = System.nanoTime() - time;
    }

    public synchronized void setDisplayPosition () {

        if (fullWidth) {
            if (ball.x > screenWidth / 2 && ball.x < width - screenWidth / 2)
                startX = screenWidth / 2 - ball.x;
            else if (ball.x <= screenWidth / 2)
                startX = 0;
            else
                startX = screenWidth - width;
        }

        if (fullHeight) {
            if (ball.y > screenHeight / 2 && ball.y < height - screenHeight / 2)
                startY = screenHeight / 2 - ball.y;
            else if (ball.y <= screenHeight / 2)
                startY = 0;
            else
                startY = screenHeight - height;
        }
    }

    public synchronized void updateDisplay (Canvas canvas, float interpolation) {

        long time = System.nanoTime();

        // draw border if not fullscreen
        if (!fullWidth || !fullHeight) {
            canvas.drawColor(fillColor);
            canvas.drawRect(px(startX - 1), px(startY - 1), px(startX + width + 1), px(startY + height + 1),
                    foundationPaint);
        }

        canvas.drawColor(Color.WHITE);

        // draw contents
        colorMap.onDraw(canvas, startX, startY, screenWidth, screenHeight, interpolation);
        fields.draw(canvas, startX, startY, interpolation);
        ball.draw(canvas, startX, startY, interpolation);
        collectables.draw(canvas, startX, startY, interpolation);
        collidables.draw(canvas, startX, startY, interpolation);

        long diff = System.nanoTime() - time;
    }

    public synchronized void onChangeColor(int x, int y, GameColor paintColor) {
//        currentRegion.gameColor.addPaint(paintColor);
//        currentRegion.changeColor(regions);
//        if (regions.isEmpty()) {
//            gameFinished = true;
//            levelPassed = true;
//        }
        colorMap.changeColor(x, y, paintColor);
    }

    public boolean isUserTouching () {
        return userTouching;
    }

    public synchronized void setUserTouching (boolean userTouching) {
        // from touch to not touch
        if (this.userTouching && !userTouching) {
            this.userTouching = userTouching;
            ball.setBoost(false);
        }
        // from not touch to touch
        else if (!this.userTouching && userTouching) {
            this.userTouching = userTouching;
            ball.setBoost(true);
        }
    }

    public void failGame(String message) {
        gameFinished = true;
        levelPassed = false;
        failMessage = message;
    }

    public boolean inCurrentScreen (StaticObj obj) {
        return true;
    }

    public boolean isGameFinished () {
        return gameFinished;
    }

    public Intent makeEndGameInfo () {
        Intent infoIntent = new Intent();
        infoIntent.putExtra("gameStatus", levelPassed);
        if (!levelPassed)
            infoIntent.putExtra("failMessage", failMessage);
        return infoIntent;
    }

    public static void loadResource (Context context) {
        scale = context.getResources().getDimension(R.dimen.gameUnit);
        foundationColor = Color.WHITE;
        fillColor = context.getResources().getColor(R.color.fill);
    }

    public static int px (float unit) {
        return (int)(unit * scale + 0.5f);
    }

    public static float unit (float px) {
        return px / scale;
    }

}
