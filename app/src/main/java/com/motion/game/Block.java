package com.motion.game;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class Block{

    private static final float OFFSET = 0.1f;
    private static final float MIN_OVERLAP = 2f;

    private GameColor color;
    LinearGradient linearGradient;

    public Block(int x, int y, GameColor color) {
        this.color = color;
    }

    public GameColor getColor () {
        return color;
    }

    public void setColor (GameColor color) {
        this.color = new GameColor(color);
    }

    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
//        Rect r = getOffsetRect(mapX, mapY);
//        linearGradient = new LinearGradient(r.left, r.top, r.right, r.bottom,
//                gameColor.getBackgroundColor(), gameColor.getBackgroundColor2(), Shader.TileMode.CLAMP);
//        paint.setShader(linearGradient);
//        canvas.drawRect(getOffsetRect(mapX, mapY), p);
    }

}
