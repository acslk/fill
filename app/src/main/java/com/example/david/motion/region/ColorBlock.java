package com.example.david.motion.region;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.david.motion.game.GameObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 09/04/15.
 */
public class ColorBlock extends GameObj {

    private static final float OFFSET = 0.1f;
    private static final float MIN_OVERLAP = 2f;

    public boolean added = false;
    boolean back = true;
    public GameColor gameColor;
    Paint paint;

    public List<ColorBlock> neighborBlocks = new ArrayList<>();

    public ColorBlock (float x, float y, float width, float height, int r, int g, int b) {
        super(x, y, width, height);
        gameColor = new GameColor(r, g, b);
        paint = new Paint();
        paint.setColor(gameColor.getBackgroundColor());
    }

    // TODO implement this
    public boolean isNeighbor (ColorBlock colorBlock) {
        return false;
    }

    public void resetColor (GameColor gameColor) {
        paint.setColor(gameColor.getBackgroundColor());
    }

    @Override
    public void onDraw(Canvas canvas, float mapX, float mapY) {
        canvas.drawRect(getOffsetRect(mapX, mapY), paint);
    }

}
