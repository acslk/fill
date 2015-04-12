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

    public abstract void collide (Ball ball, Ball lastBall);

    public void update (GameMap gameMap) {};

}
