package com.motion.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.SensorEvent;
import android.util.Log;

import com.motion.R;
import com.motion.Utils;
import com.motion.effects.ParticleGroup;

public class Ball extends MovingObj {

    // Constants
    public static final float Default_ACCELERATION_DIV_FACTOR = 1000;
    public static final float Default_ACCELERATION_Y_OFFSET = 1f;
    public static final float Default_MAXA = 0.05f;
    public static final float Default_MINA = 0.005f;
    public static final float Default_MAXV = 0.7f;
    public static final float Default_BOUNCE = 0.8f;
    public static final float Default_FRICTION = 0.02f;

    private static Drawable ballImage;
    private static float ballSize;

    // Movement varaibles
    public float ax = 0, ay = 0;
    public float dirx = 0, diry = 0;
    public float maxA = Default_MAXA, minA = Default_MINA;
    public float maxV = Default_MAXV;

    public boolean frictionEnabled;
    public float friction = Default_FRICTION;
    public float bounce = Default_BOUNCE;

    // Particle effect
    ParticleGroup particleGroup;

    // Ability and Effects variables
    public int ballType = 0;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_BOOST = 1;
    public static final int TYPE_SHIELD = 2;
    public static final int TYPE_SPIKE = 3;
    public boolean immune = false;
    public boolean boost = false;
    public boolean spike = false;
    
    public float size = 0; // in game unit

    public Ball (float x, float y) {
        super(x, y, Game.unit(ballSize), Game.unit(ballSize));
        this.size = Game.unit(ballSize);
        particleGroup = new ParticleGroup(5, 10, size/2, size/2);
        Log.i("Ball", x + " " + y + " " + size);
    }

    public Ball (Ball ball) {
        super(ball.x, ball.y, ball.size, ball.size);
        this.size = ball.size;
    }

    public void bounceX() {
        vx *= -bounce;
    }

    public void bounceY() {
        vy *= -bounce;
    }
    
    public void updateAcceleration (SensorEvent event) {

        ax = event.values[1];
        ay = event.values[0];

        long time = System.nanoTime();
        double anglex = Math.asin(Utils.setBetween(ax / 9.81, 1, -1));
        double angley = Math.asin(Utils.setBetween(ay / 9.81, 1, -1));

        anglex = Utils.setBetween(anglex * 3, Math.PI/2, -Math.PI/2);
        angley = Utils.setBetween(angley * 3, Math.PI/2, -Math.PI/2);

        long diff = System.nanoTime() - time;

        ax = (float)(Math.sin(anglex) * 9.81 / Default_ACCELERATION_DIV_FACTOR);
        ay = (float)(Math.sin(angley) * 9.81 / Default_ACCELERATION_DIV_FACTOR);

        double len = Math.sqrt(ax * ax + ay * ay);
        dirx = ax / (float)len;
        diry = ay / (float)len;

//        ax = event.values[1]/Ball.Default_ACCELERATION_DIV_FACTOR;
//        ay = event.values[0]/Ball.Default_ACCELERATION_DIV_FACTOR - Ball.Default_ACCELERATION_Y_OFFSET;
        
//        ax = Utils.setBetween(ax, maxA, -maxA);
//        if (ax > friction && ax < minA)
//            ax = minA;
//        else if (ax < -friction && ax > -minA)
//            ax = -minA;

//        ay = Utils.setBetween(ay, maxA, -maxA);
//        if (ay > friction && ay < minA)
//            ay = minA;
//        else if (ay < -friction && ay > -minA)
//            ay = -minA;
    }

    public void updateVelocity() {

        vx += ax;
        vy += ay;

        if (boost) {
            vx += dirx / 100;
            vy += diry / 100;
        }

        if (frictionEnabled) {
            if (vx <= friction && vx >= -friction) {
                vx = 0;
            } else if (vx > friction) {
                vx -= friction;
            } else if (vx < -friction) {
                vx += friction;
            }
            if (vy <= friction && vy >= -friction) {
                vy = 0;
            } else if (vy > friction) {
                vy -= friction;
            } else if (vy < -friction) {
                vy += friction;
            }
        }

//        if (vx > maxV) {
//
//        } else if (vx < -maxV) {
//
//        }
//        if (vy > maxV) {
//
//        } else if (vy < -maxV) {
//
//        }
    }

    public void updateDisplacement() {
        particleGroup.update(x, y);
        x += vx;
        y += vy;
    }

    public void interactBound (float width, float height) {
        if (x < 0) {
            x = 0;
            vx = -vx * bounce;
        } else if (x > width - size) {
            x = width - size;
            vx = -vx * bounce;
        }
        if (y < 0) {
            y = 0;
            vy = -vy * bounce;
        } else if (y > height - size) {
            y = height - size;
            vy = -vy * bounce;
        }
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
        if (boost) {
            particleGroup.changeSetting(20, 1);
        } else {
            particleGroup.changeSetting(5, 10);
        }
    }

    // let this copy value FROM ball
    public void getCopy (Ball ball) {
        x = ball.x;
        y = ball.y;
    }

    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        particleGroup.draw(canvas, mapX, mapY, interpoation);
        ballImage.setBounds(getOffsetRect(mapX, mapY));
        ballImage.draw(canvas);
    }

    public static void loadResrouce (Context context) {
        ballImage = context.getResources().getDrawable(R.drawable.ball);
        ballSize = context.getResources().getDimension(R.dimen.ballDiameter);
    }

}
