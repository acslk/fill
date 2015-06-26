package com.motion.collidables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.motion.R;
import com.motion.game.Ball;
import com.motion.game.GameMap;

public class FragileBlock extends Collidable {

    private static final float THRESHOLD = Ball.Default_MAXV;
    private static Drawable baseImage;
    boolean fading = false;
    int alpha = 255;

    public FragileBlock(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void collide (Ball ball, Ball lastBall) {
        float ballvx = ball.vx;
        float ballvy = ball.vy;
        Direction direction = bounceOff(ball, lastBall);
        switch (direction) {
            case LEFT:
                if (ballvx >= THRESHOLD)
                    fading = true;
                break;
            case RIGHT:
                Log.i("Motion", lastBall.vx + " " + -THRESHOLD);
                if (ballvx <= -THRESHOLD)
                    fading = true;
                break;
            case TOP:
                if (ballvy >= THRESHOLD)
                    fading = true;
                break;
            case BOTTOM:
                if (ballvy <= -THRESHOLD)
                    fading = true;
                break;
        }
    }

    @Override
    public void update(GameMap gameMap) {
        if (fading) {
            alpha -= 5;
            if (alpha < 0)
                exist = false;
        }
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImage.setAlpha(alpha);
        baseImage.setBounds(getOffsetRect(mapX, mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.breakable);
    }
}