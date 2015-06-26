package com.motion.collidables;

import com.motion.game.Ball;
import com.motion.game.GameMap;
import com.motion.game.GameObj;

public abstract class Collidable extends GameObj {

    public Collidable (float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    public abstract void collide (Ball ball, Ball lastBall);

    public void update (GameMap gameMap) {};

}
