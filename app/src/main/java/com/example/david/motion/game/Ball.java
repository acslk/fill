package com.example.david.motion.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.david.motion.R;

/**
 * Created by David on 2015-03-04.
 */
public class Ball extends GameObj {

    public static final float ACCELERATION_DIV_FACTOR = 10;
    public static final float ACCELERATION_Y_OFFSET = 0.5f;
    public static final float MAXA = 0.3f;
    public static final float MINA = 0.1f;
    public static final float MAXVX = 7;
    public static final float MAXVY = 7;
    public static final float BOUNCE = 0.8f;
    public static final float FRICTION = 0.05f;

    private static Drawable ballImage;

    public float vx = 0, vy = 0;
    public float ax = 0, ay = 0;
    public float size = 0; // in game unit

    public Ball (float x, float y, float size) {
        super(x, y, GameMap.unit(size), GameMap.unit(size));
        this.size = GameMap.unit(size);
        Log.i("Ball", x + " " + y + " " + size);
    }

    public Ball (Ball ball) {
        super(ball.x, ball.y, ball.size, ball.size);
        this.size = ball.size;
    }

    public void updateVelocity() {
        vx += ax;
        vy += ay;

        // friction
        if (vx <= FRICTION && vx >= -FRICTION) {
            vx = 0;
        } else if (vx > FRICTION) {
            vx -= FRICTION;
        } else if (vx < -FRICTION) {
            vx += FRICTION;
        }
        if (vy <= FRICTION && vy >= -FRICTION) {
            vy = 0;
        } else if (vy > FRICTION) {
            vy -= FRICTION;
        } else if (vy < -FRICTION) {
            vy += FRICTION;
        }

        // max velocity adjustment
        if (vx > MAXVX) {
            vx = MAXVX;
        } else if (vx < -MAXVX) {
            vx = -MAXVX;
        }
        if (vy > MAXVY) {
            vy = MAXVY;
        } else if (vy < -MAXVY) {
            vy = -MAXVY;
        }
    }

    public void updateDisplacement() {
        x += vx;
        y += vy;
    }

    public void interactBound (float width, float height) {
        if (x < 0) {
            x = 0;
            vx = -vx;
        } else if (x > width - size) {
            x = width - size;
            vx = -vx;
        }
        if (y < 0) {
            y = 0;
            vy = -vy;
        } else if (y > height - size) {
            y = height - size;
            vy = -vy;
        }
    }

    // let this copy value FROM ball
    public void getCopy (Ball ball) {
        x = ball.x;
        y = ball.y;
    }

    public void onDraw(Canvas canvas, float mapX, float mapY) {
        ballImage.setBounds(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + size + mapX), GameMap.px(y + size + mapY));
        ballImage.draw(canvas);
    }

    public static void loadResrouce (Context context) {
        ballImage = context.getResources().getDrawable(R.drawable.circle);
    }

}
