package com.example.david.motion.field;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

/**
 * Created by David on 2015-04-02.
 */
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
    public void onDraw(Canvas canvas, float mapX, float mapY) {
        baseImage.setBounds(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.nogravity_field);
    }
}
