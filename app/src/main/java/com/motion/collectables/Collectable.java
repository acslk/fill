package com.motion.collectables;

import com.motion.game.Game;
import com.motion.game.MovingObj;
import com.motion.game.StaticObj;

public abstract class Collectable extends StaticObj {

    public Collectable (int x, int y, int width, int height) {
        super(x,y,width,height);
    }

    public abstract void collect (Game map);
}
