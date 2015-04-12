package com.example.david.motion.collectable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;
import com.example.david.motion.region.GameColor;

/**
 * Created by David on 2015-03-27.
 */
public class LightObj extends Collectable {

    private static int Height;
    private static int Width;
//    private static Drawable baseImage;

    private GameColor lightColor;
    private Paint paint;

    public LightObj(float x, float y, int r, int g, int b) {
        super(x,y,Width, Height);
        lightColor = new GameColor(r, g, b);
        paint = new Paint();
        paint.setColor(lightColor.getLightColor());
    }

    @Override
    public void collect(GameMap map) {
        map.currentRegion.gameColor.addLight(lightColor);
        map.currentRegion.onColorChange(map.regions);
        exist = false;
    }

    @Override
    public void onDraw(Canvas canvas, float mapX, float mapY) {
        canvas.drawRect(getOffsetRect(mapX, mapY), paint);
    }

    public static void loadResource (Context context) {
        Width = Height = context.getResources().getInteger(R.integer.colorObjSize);
    }
}
