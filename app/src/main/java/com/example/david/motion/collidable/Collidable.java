package com.example.david.motion.collidable;

import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;
import com.example.david.motion.game.GameObj;

/**
 * Created by David on 2015-03-04.
 */
public abstract class Collidable extends GameObj {

    public Collidable (float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    public Direction bounceOff (Ball ball, Ball lastBall) {
        if (lastBall.x + lastBall.size <= x) {
            ball.vx = -ball.vx;
            ball.x = x - ball.size;
            return Direction.LEFT;
        } else if (lastBall.x >= x + width) {
            ball.vx = -ball.vx;
            ball.x = x + width;
            return Direction.RIGHT;
        } else if (lastBall.y + lastBall.size <= y) {
            ball.vy = -ball.vy;
            ball.y = y - lastBall.size;
            return Direction.TOP;
        } else if (lastBall.y >= y + height) {
            ball.vy = -ball.vy;
            ball.y = y + height;
            return Direction.BOTTOM;
        }
        return Direction.NULL;
    }

    public abstract void collide (Ball ball, Ball lastBall);

    public void update (GameMap gameMap) {};

}
