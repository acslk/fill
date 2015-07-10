package com.motion.fields;

import com.motion.game.Ball;
import com.motion.game.Game;
import com.motion.game.GameObj;

public abstract class Field extends GameObj {

    public Field (float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public abstract void applyForce (Ball ball);

    public void update (Game game) {}
}
