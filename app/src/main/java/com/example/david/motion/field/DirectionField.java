package com.example.david.motion.field;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

/**
* Created by David on 2015-03-08.
*/
public class DirectionField extends Field {

    private static Drawable baseImage;
    Direction direction;

    public DirectionField (float x, float y, float width, float height, Direction direction) {
        super(x, y, width, height);
        this.direction = direction;
    }

    @Override
    public void applyForce (Ball ball) {
        switch (direction) {
            case LEFT:
                ball.vx -= 0.5;
                break;
            case RIGHT:
                ball.vx += 0.5;
                break;
            case TOP:
                ball.vy -= 0.5;
                break;
            case BOTTOM:
                ball.vy += 0.5;
                break;
        }
    }

    @Override
    public void onDraw(Canvas canvas, float mapX, float mapY) {
        baseImage.setBounds(GameMap.px(x + mapX), GameMap.px(y + mapY),
                GameMap.px(x + width + mapX), GameMap.px(y + height + mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.direction_field);
    }
}
