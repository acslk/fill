package com.example.david.motion.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.Log;

import com.example.david.motion.region.GameColor;
import com.example.david.motion.R;
import com.example.david.motion.collectable.Collectable;
import com.example.david.motion.collidable.Collidable;
import com.example.david.motion.field.Field;
import com.example.david.motion.region.Region;

import java.util.Iterator;
import java.util.List;

/**
 * Created by David on 2015-03-14.
 */
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

    public final float width, height; // in map unit
    public Region currentRegion;
    public Region backRegion;
    public List<Region> regions;

    public List<Collidable> collidables;
    public List<Field> fields;
    public List<Collectable> collectables;
    public Ball ball, lastBall;

    boolean gameFinished = false;
    boolean levelPassed = false;
    String failMessage = "uninitialized";

    public GameMap (float ballSize, float width, float height) {

        ball = new Ball(100, 100, ballSize);
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

    public synchronized void updateDisplay (Canvas canvas) {

        long time = System.nanoTime();

        // draw border if not fullscreen
        if (!fullWidth || !fullHeight) {
            canvas.drawColor(fillColor);
            canvas.drawRect(px(startX - 1), px(startY - 1), px(startX + width + 1), px(startY + height + 1),
                    foundationPaint);
        }

        // draw contents
        backRegion.onDraw(canvas, startX, startY);
        for (Region region : regions)
            region.onDraw(canvas, startX, startY);
        ball.onDraw(canvas, startX, startY);
        for (Field field : fields)
            field.onDraw(canvas, startX, startY);
        for (Collectable collectable : collectables)
            collectable.onDraw(canvas, startX, startY);
        for (Collidable collidable : collidables)
            collidable.onDraw(canvas, startX, startY);

        long diff = System.nanoTime() - time;
        Log.i("MotionDisplay", " " + diff);
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
