package com.example.david.motion.fields;

import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;
import com.example.david.motion.game.GameObj;

/**
 * Created by David on 2015-03-04.
 */
public abstract class Field extends GameObj {

    public Field (float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public abstract void applyForce (Ball ball);

    public void update (GameMap gameMap) {}
}
