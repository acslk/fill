package com.example.david.motion.collidable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

/**
 * Created by David on 2015-04-02.
 */
public class SwitchBlock extends Collidable {

    private static Drawable baseImageSolid, baseImageDoom;
    private boolean collided = false;
    private boolean doomMode = false;
    private boolean switching = false;
    private int switchInterval;
    private int switchCounter = 0;
    private int solidAlpha = 255;

    public SwitchBlock(float x, float y, float width, float height, int switchInterval) {
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
    public void update(GameMap gameMap) {
        if (collided)
            gameMap.failGame("The ball caught on fire");
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
    public void onDraw(Canvas canvas, float mapX, float mapY) {
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
