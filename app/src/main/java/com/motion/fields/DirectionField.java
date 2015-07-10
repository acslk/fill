package com.motion.fields;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.motion.R;
import com.motion.game.Ball;
import com.motion.game.Game;

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
                ball.vx -= 0.2;
                break;
            case RIGHT:
                ball.vx += 0.2;
                break;
            case TOP:
                ball.vy -= 0.2;
                break;
            case BOTTOM:
                ball.vy += 0.2;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImage.setBounds(Game.px(x + mapX), Game.px(y + mapY),
                Game.px(x + width + mapX), Game.px(y + height + mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.direction_field);
    }
}
