package com.motion.game;

import android.graphics.Canvas;

import com.motion.collectables.Collectable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collectables {

    private List<Collectable> collectables;

    public Collectables (ColorMap map) {
        collectables = new ArrayList<>();
    }

    public void add (Collectable c) {
        collectables.add(c);
    }

    public void update (Game game, Ball ball) {
        for (Iterator<Collectable> iterator = collectables.iterator(); iterator.hasNext();) {
            Collectable c = iterator.next();
            if (c.containsBall(ball))
                c.collect(game);
            if (!c.exist)
                iterator.remove();
        }
    }

    public void draw (Canvas canvas, float startX, float startY, float interpolation) {
        for (Collectable collectable : collectables)
            collectable.draw(canvas, startX, startY, interpolation);
    }

}
