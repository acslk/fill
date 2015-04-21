package com.example.david.motion.fields;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

/**
 * Created by David on 2015-04-02.
 */
public class LowVField extends Field {

    private static Drawable baseImage;

    public LowVField (float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void applyForce (Ball ball) {
        ball.vx += 0.5;
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImage.setBounds(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.direction_field);
    }

}
