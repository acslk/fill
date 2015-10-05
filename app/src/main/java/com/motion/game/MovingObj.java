package com.motion.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class MovingObj {

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM, NULL
    }

    public float x, y, width, height;
    public float vx = 0, vy = 0;
    public boolean exist = true;

    public MovingObj(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean containsBall(Ball ball) {
        if (ball.x + ball.size <= x) {
            return false;
        } else if (ball.x >= x + width) {
            return false;
        } else if (ball.y + ball.size <= y) {
            return false;
        } else if (ball.y >= y + height) {
            return false;
        }
        return true;
    }

    public boolean containsRectObj (MovingObj movingObj) {
        if (movingObj.x + movingObj.width <= x) {
            return false;
        } else if (movingObj.x >= x + width) {
            return false;
        } else if (movingObj.y + movingObj.height <= y) {
            return false;
        } else if (movingObj.y >= y + height) {
            return false;
        }
        return true;
    }

    protected Rect getOffsetRect (float mapX, float mapY) {
        return new Rect(Game.px(x + mapX), Game.px(y + mapY),
                Game.px(x + width + mapX), Game.px(y + height + mapY));
    }

    protected RectF getOffsetRectF (float mapX, float mapY) {
        return new RectF(Game.px(x + mapX), Game.px(y + mapY),
                Game.px(x + width + mapX), Game.px(y + height + mapY));
    }

    public abstract void draw(Canvas canvas, float mapX, float mapY, float interpoation);

}
