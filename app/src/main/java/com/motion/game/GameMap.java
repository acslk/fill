package com.motion.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.Log;

import com.motion.R;
import com.motion.collectables.Collectable;
import com.motion.collidables.Collidable;
import com.motion.fields.Field;
import com.motion.region.GameColor;
import com.motion.region.Region;

import java.util.Iterator;
import java.util.List;

public class GameMap {

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
    public Region currentRegion;
    public Region backRegion;
    public List<Region> regions;

    public List<Collidable> collidables;
    public List<Field> fields;
    public List<Collectable> collectables;
    public Ball ball, lastBall;

    //  user status
    boolean userTouching = false;

    // finish game information
    boolean gameFinished = false;
    boolean levelPassed = false;
    String failMessage = "uninitialized";

    public GameMap (float width, float height, float ballStartX, float ballStartY) {

        ball = new Ball(ballStartX, ballStartY);
        lastBall = new Ball(ball);
        this.width = width;
        this.height = height;

        startX = startY = 0;
        foundationPaint = new Paint();
        foundationPaint.setColor(foundationColor);
    }

    public void loadStuff (Region backRegion, List<Region> regions, List<Collidable> collidables,
                           List<Field> fields, List<Collectable> collectables) {
        this.backRegion = backRegion;
        this.regions = regions;
        this.collidables = collidables;
        this.fields = fields;
        this.collectables = collectables;

        currentRegion = backRegion;
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

        for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
            Field f = iterator.next();
            if (f.containsBall(ball))
                f.applyForce(ball);
            f.update(this);
            if (!f.exist)
                iterator.remove();
        }

        ball.updateVelocity();
        ball.updateDisplacement();

        for (Iterator<Collidable> iterator = collidables.iterator(); iterator.hasNext();) {
            Collidable c = iterator.next();
            if (c.containsBall(ball))
                c.collide(ball, lastBall);
            c.update(this);
            if (!c.exist)
                iterator.remove();
        }

        for (Region region : regions) {
            if (region.containsBall(ball))
                region.collide(ball, lastBall);
        }

        ball.interactBound(width, height);
        lastBall.getCopy(ball);

        for (Iterator<Collectable> iterator = collectables.iterator(); iterator.hasNext();) {
            Collectable c = iterator.next();
            if (c.containsBall(ball))
                c.collect(this);
            if (!c.exist)
                iterator.remove();
        }

        setDisplayPosition();

        long diff = System.nanoTime() - time;
        Log.i("MotionStatus", " " + diff);
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

        // draw contents
        backRegion.onDraw(canvas, startX, startY, interpolation);
        for (Region region : regions)
            region.onDraw(canvas, startX, startY, interpolation);
        for (Field field : fields)
            field.draw(canvas, startX, startY, interpolation);
        ball.draw(canvas, startX, startY, interpolation);
        for (Collectable collectable : collectables)
            collectable.draw(canvas, startX, startY, interpolation);
        for (Collidable collidable : collidables)
            collidable.draw(canvas, startX, startY, interpolation);

        long diff = System.nanoTime() - time;
        Log.i("MotionDisplay", " " + diff);
    }

    public synchronized void onChangeColor(GameColor paintColor) {
        currentRegion.gameColor.addPaint(paintColor);
        currentRegion.onColorChange(regions);
        if (regions.isEmpty()) {
            gameFinished = true;
            levelPassed = true;
        }
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
