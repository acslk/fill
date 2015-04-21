package com.example.david.motion.collidables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

/**
 * Created by David on 2015-03-05.
 */
public class DoomBlock extends Collidable {

    private static Drawable baseImage;
    private boolean collided = false;

    public DoomBlock(float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    @Override
    public void collide(Ball ball, Ball lastBall) {
        bounceOff(ball, lastBall);
        collided = true;
    }

    @Override
    public void update(GameMap gameMap) {
        if (collided)
            gameMap.failGame("The ball caught on fire");
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImage.setAlpha(255);
        baseImage.setBounds(getOffsetRect(mapX, mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.bouncing);
    }
}
