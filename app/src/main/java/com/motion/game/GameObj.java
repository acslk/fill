package com.motion.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class GameObj {

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM, NULL
    }

    public float x, y, width, height;
    public boolean exist = true;

    public GameObj (float x, float y, float width, float height) {
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

    public boolean containsRectObj (GameObj gameObj) {
        if (gameObj.x + gameObj.width <= x) {
            return false;
        } else if (gameObj.x >= x + width) {
            return false;
        } else if (gameObj.y + gameObj.height <= y) {
            return false;
        } else if (gameObj.y >= y + height) {
            return false;
        }
        return true;
    }

    public Direction bounceOff (Ball ball, Ball lastBall) {
        if (lastBall.x + lastBall.size <= x) {
            ball.vx = -ball.vx * ball.bounce;
            ball.x = x - ball.size;
            return Direction.LEFT;
        } else if (lastBall.x >= x + width) {
            ball.vx = -ball.vx * ball.bounce;
            ball.x = x + width;
            return Direction.RIGHT;
        } else if (lastBall.y + lastBall.size <= y) {
            ball.vy = -ball.vy * ball.bounce;
            ball.y = y - lastBall.size;
            return Direction.TOP;
        } else if (lastBall.y >= y + height) {
            ball.vy = -ball.vy * ball.bounce;
            ball.y = y + height;
            return Direction.BOTTOM;
        }
        return Direction.NULL;
    }

    protected Rect getOffsetRect (float mapX, float mapY) {
        return new Rect(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
    }

    protected RectF getOffsetRectF (float mapX, float mapY) {
        return new RectF(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
    }

    public abstract void draw(Canvas canvas, float mapX, float mapY, float interpoation);

}
