package com.example.david.motion.collectables;

import com.example.david.motion.game.GameMap;
import com.example.david.motion.game.GameObj;

/**
 * Created by David on 2015-03-27.
 */
public abstract class Collectable extends GameObj {

    public Collectable (float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    public abstract void collect (GameMap map);
}