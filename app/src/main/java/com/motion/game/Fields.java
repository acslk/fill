package com.motion.game;

import android.graphics.Canvas;

import com.motion.fields.Field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Fields {

    private List<Field> fields;

    public Fields (ColorMap map) {
        fields = new ArrayList<>();
    }

    public void add (Field f) {
        fields.add(f);
    }

    public void update(Game game, Ball ball) {
        for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
            Field f = iterator.next();
            if (f.containsBall(ball))
                f.applyForce(ball);
            f.update(game);
            if (!f.exist)
                iterator.remove();
        }
    }

    public void draw(Canvas canvas, float startX, float startY, float interpolation) {
        for (Field field : fields)
            field.draw(canvas, startX, startY, interpolation);
    }

}
