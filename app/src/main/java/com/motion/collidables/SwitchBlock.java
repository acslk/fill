package com.motion.collidables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.motion.R;
import com.motion.game.Ball;
import com.motion.game.Game;

public class SwitchBlock extends Collidable {

    private static Drawable baseImageSolid, baseImageDoom;
    private boolean collided = false;
    private boolean doomMode = false;
    private boolean switching = false;
    private int switchInterval;
    private int switchCounter = 0;
    private int solidAlpha = 255;

    public SwitchBlock(int x, int y, int width, int height, int switchInterval) {
        super(x,y,width,height);
        this.switchInterval = switchInterval;
    }

    @Override
    public void collide(Ball ball, Ball lastBall) {
        bounceOff(ball, lastBall);
        if (doomMode)
            collided = true;
    }

    @Override
    public void update(Game game) {
        if (collided)
            game.failGame("The ball touched the red block");
        if (switching) {
            if (!doomMode)
                solidAlpha -= 5;
            else
                solidAlpha += 5;
            if (solidAlpha == 0 || solidAlpha == 255) {
                doomMode = !doomMode;
                switching = false;
            }
        } else {
            switchCounter++;
            if (switchCounter >= switchInterval * 60) {
                switching = true;
                switchCounter = 0;
            }
        }
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        baseImageSolid.setAlpha(solidAlpha);
        baseImageSolid.setBounds(getOffsetRect(mapX, mapY));
        baseImageSolid.draw(canvas);
        baseImageDoom.setAlpha(255- solidAlpha);
        baseImageDoom.setBounds(getOffsetRect(mapX, mapY));
        baseImageDoom.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImageSolid = context.getResources().getDrawable(R.drawable.solidblock);
        baseImageDoom = context.getResources().getDrawable(R.drawable.bouncing);
    }

}
