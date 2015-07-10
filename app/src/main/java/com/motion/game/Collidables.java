package com.motion.game;

import android.graphics.Canvas;

import com.motion.collidables.Collidable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collidables {

    private List<Collidable> collidables;

    public Collidables(ColorMap map) {
        collidables = new ArrayList<>();
    }

    public void add (Collidable c) {
        collidables.add(c);
    }

    public void update (Game game, Ball ball, Ball lastBall) {
        for (Iterator<Collidable> iterator = collidables.iterator(); iterator.hasNext();) {
            Collidable c = iterator.next();
            if (c.containsBall(ball))
                c.collide(ball, lastBall);
            c.update(game);
            if (!c.exist)
                iterator.remove();
        }
    }

    public void draw (Canvas canvas, float startX, float startY, float interpolation) {
        for (Collidable collidable : collidables)
            collidable.draw(canvas, startX, startY, interpolation);
    }
}
