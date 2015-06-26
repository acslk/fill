package com.motion.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.motion.game.GameObj;

public class Particle extends GameObj{

    Paint paint;
    float vx, vy;

    public Particle(float x, float y, float size, float vx, float vy) {
        super(x, y, size, size);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        this.vx = vx;
        this.vy = vy;
    }

    public void update() {
        x += vx;
        y += vy;
        paint.setAlpha(paint.getAlpha() - 4);
    }

    public void draw(Canvas canvas, float mapX, float mapY, float interpolation) {
        canvas.drawOval(getOffsetRectF(mapX, mapY), paint);
    }
}
