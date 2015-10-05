package com.motion.collidables;

import com.motion.game.Ball;
import com.motion.game.Game;
import com.motion.game.MovingObj;
import com.motion.game.StaticObj;

public abstract class Collidable extends StaticObj {

    public Collidable (int x, int y, int width, int height) {
        super(x,y,width,height);
    }

    public abstract void collide (Ball ball, Ball lastBall);

    public void update (Game game) {};

}
