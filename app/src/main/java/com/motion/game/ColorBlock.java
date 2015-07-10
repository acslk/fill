package com.motion.game;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class ColorBlock extends GameObj {

    private static final float OFFSET = 0.1f;
    private static final float MIN_OVERLAP = 2f;

    public boolean added = false;
    boolean back = true;
    public GameColor gameColor;
    Paint paint;
    LinearGradient linearGradient;

    public List<ColorBlock> neighborBlocks = new ArrayList<>();

    public ColorBlock (float x, float y, float width, float height, int r, int g, int b) {
        this(x, y, width, height, new GameColor(r, g, b));
    }

    public ColorBlock (float x, float y, float width, float height, GameColor gameColor) {
        super(x, y, width, height);
        this.gameColor = gameColor;
        paint = new Paint();
        paint.setDither(true);
        paint.setColor(gameColor.getBackgroundColor());
    }

    // TODO implement this
    public boolean isNeighbor (ColorBlock colorBlock) {
        return false;
    }

    public void resetColor (GameColor gameColor) {
        this.gameColor = gameColor;
    }

    @Override
    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
//        Rect r = getOffsetRect(mapX, mapY);
//        linearGradient = new LinearGradient(r.left, r.top, r.right, r.bottom,
//                gameColor.getBackgroundColor(), gameColor.getBackgroundColor2(), Shader.TileMode.CLAMP);
//        paint.setShader(linearGradient);
        paint.setColor(gameColor.getBackgroundColor());
        canvas.drawRect(getOffsetRect(mapX, mapY), paint);
    }

}
