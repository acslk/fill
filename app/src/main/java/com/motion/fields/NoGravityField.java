package com.motion.fields;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.motion.R;
import com.motion.game.Ball;
import com.motion.game.GameMap;

public class NoGravityField extends Field {
    private static Drawable baseImage;

    public NoGravityField (float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void applyForce (Ball ball) {
        ball.ax = 0;
        ball.ay = 0;
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImage.setBounds(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.nogravity_field);
    }
}
