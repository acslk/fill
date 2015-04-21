package com.example.david.motion.collectables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.david.motion.R;
import com.example.david.motion.game.GameMap;
import com.example.david.motion.region.GameColor;

/**
 * Created by David on 2015-03-27.
 */
public class PaintObj extends Collectable {

    private static int Height;
    private static int Width;
//    private static Drawable baseImage;

    private GameColor paintColor;
    private Paint paint;

    // TODO subtractive coloring has different primary colors
    public PaintObj(float x, float y, int r, int g, int b) {
        super(x,y,Width, Height);
        paintColor = new GameColor(r, g, b);
        paint = new Paint();
        paint.setColor(paintColor.getPaintColor());
    }

    @Override
    public void collect(GameMap map) {
        map.currentRegion.gameColor.addPaint(paintColor);
        map.currentRegion.onColorChange(map.regions);
        exist = false;
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
        canvas.drawRect(getOffsetRect(mapX, mapY), paint);
    }

    public static void loadResource (Context context) {
        Width = Height = context.getResources().getInteger(R.integer.colorObjSize);
    }
}
