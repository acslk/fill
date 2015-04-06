package com.example.david.motion.game;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by David on 2015-03-05.
 */
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

    protected Rect getOffsetRect (float mapX, float mapY) {
        return new Rect(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
    }

    public abstract void onDraw (Canvas canvas, float mapX, float mapY);

}
