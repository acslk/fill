package com.example.david.motion.collidable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;
import com.example.david.motion.game.GamePanel;
import com.example.david.motion.game.GameSurfaceView;

/**
 * Created by David on 2015-03-05.
 */
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
    public void onDraw(Canvas canvas, float mapX, float mapY) {
        baseImage.setAlpha(255);
        baseImage.setBounds(getOffsetRect(mapX, mapY));
        baseImage.draw(canvas);
    }

    public static void loadResource (Context context) {
        baseImage = context.getResources().getDrawable(R.drawable.test);
    }
}