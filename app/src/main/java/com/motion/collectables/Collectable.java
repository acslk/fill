package com.motion.collectables;

import com.motion.game.GameMap;
import com.motion.game.GameObj;

public abstract class Collectable extends GameObj {

    public Collectable (float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    public abstract void collect (GameMap map);
}