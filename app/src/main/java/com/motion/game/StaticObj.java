package com.motion.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class StaticObj {

    private int x, y, width, height;
    public boolean exist = true;

    public StaticObj(int x, int y, int width, int height) {
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

    public MovingObj.Direction bounceOff (Ball ball, Ball lastBall) {
        if (lastBall.x + lastBall.size <= x) {
            ball.vx = -ball.vx * ball.bounce;
            ball.x = x - ball.size;
            return MovingObj.Direction.LEFT;
        } else if (lastBall.x >= x + width) {
            ball.vx = -ball.vx * ball.bounce;
            ball.x = x + width;
            return MovingObj.Direction.RIGHT;
        } else if (lastBall.y + lastBall.size <= y) {
            ball.vy = -ball.vy * ball.bounce;
            ball.y = y - lastBall.size;
            return MovingObj.Direction.TOP;
        } else if (lastBall.y >= y + height) {
            ball.vy = -ball.vy * ball.bounce;
            ball.y = y + height;
            return MovingObj.Direction.BOTTOM;
        }
        return MovingObj.Direction.NULL;
    }

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
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
