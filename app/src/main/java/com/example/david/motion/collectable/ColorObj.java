package com.example.david.motion.collectable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.david.motion.R;
import com.example.david.motion.game.Ball;
import com.example.david.motion.game.GameMap;

/**
 * Created by David on 2015-03-27.
 */
public class ColorObj extends Collectable {

    private static int Height;
    private static int Width;
//    private static Drawable baseImage;

    private int red, green, blue;
    private Paint colorPaint;

    public ColorObj (float x, float y, int red, int green, int blue) {
        super(x,y,Width, Height);
        this.red = red;
        this.green = green;
        this.blue = blue;
        colorPaint = new Paint();
        colorPaint.setARGB(255, red + 150, green + 150, blue + 150);
    }

    @Override
    public void collect(Ball ball, GameMap map) {
        if (containsBall(ball)) {
            Paint p = map.getBackgroundPaint();
            int r = Color.red(p.getColor()) + red;
            int g = Color.green(p.getColor()) + green;
            int b = Color.blue(p.getColor()) + blue;
            if (r > 255)
                r = 255;
            if (g > 255)
                g = 255;
            if (b > 255)
                b = 255;
            p.setARGB(p.getAlpha(), r, g, b);
            exist = false;
        }
    }

    @Override
    public void onDraw(Canvas canvas, float mapX, float mapY) {
        canvas.drawRect(getOffsetRect(mapX, mapY), colorPaint);
    }

    public static void loadResource (Context context) {
        Width = context.getResources().getInteger(R.integer.colorObjSize);
        Height = Width;
    }
}
