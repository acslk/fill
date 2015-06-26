package com.motion.collectables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.motion.R;
import com.motion.game.GameMap;
import com.motion.region.GameColor;

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
        map.onChangeColor(paintColor);
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