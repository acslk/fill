package com.motion.collidables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.motion.R;
import com.motion.game.Ball;

public class SolidBlock extends Collidable {

    private static Drawable baseImage;

    public SolidBlock(float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    @Override
    public void collide(Ball ball, Ball lastBall) {
        bounceOff(ball, lastBall);
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImage.setAlpha(255);
        baseImage.setBounds(getOffsetRect(mapX, mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.solidblock);
    }
}
