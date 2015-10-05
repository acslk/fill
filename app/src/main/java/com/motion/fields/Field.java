package com.motion.fields;

import com.motion.game.Ball;
import com.motion.game.Game;
import com.motion.game.MovingObj;
import com.motion.game.StaticObj;

public abstract class Field extends StaticObj {

    public Field (int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract void applyForce (Ball ball);

    public void update (Game game) {}
}
