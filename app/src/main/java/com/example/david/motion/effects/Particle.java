package com.example.david.motion.effects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.david.motion.game.GameObj;

/**
 * Created by dave on 21/04/15.
 */
public class Particle extends GameObj{

    Paint paint;
    float vx, vy;

    public Particle(float x, float y, float size, float vx, float vy, Paint paint) {
        super(x, y, size, size);
        this.paint = paint;
        this.vx = vx;
        this.vy = vy;
    }

    public void update() {
        x += vx;
        y += vy;
    }

    public void draw(Canvas canvas, float mapX, float mapY, float interpolation) {
        canvas.drawOval(getOffsetRectF(mapX, mapY), paint);
    }
}
