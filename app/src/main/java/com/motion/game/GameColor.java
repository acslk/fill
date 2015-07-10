package com.motion.game;

import android.graphics.Color;

import com.motion.Utils;

/**
 * Object representing colors used in the game. RGB in this class are not the actual
 * RGB for colors
 */
public class GameColor {

    private final static int[] backR = {155,190, 220};
    private final static int[] backG = {145, 195, 230};
    private final static int[] backB = {140, 175, 215};
    private final static int[] paintVal = {80, 110, 140};
    private final static int[] lightVal = {200, 230, 255};
    int r, g, b;

    public GameColor (int r, int g, int b) {
        this.r = bound(r);
        this.g = bound(g);
        this.b = bound(b);
    }

    public int getBackgroundColor () {
        return Color.rgb(backR[r], backG[g], backB[b]);
    }

    public int getBackgroundColor2 () { return Color.rgb(backR[r] - 40, backG[g] - 40, backB[b] - 40); }

    public int getPaintColor () {
        return Color.rgb(paintVal[r], paintVal[g], paintVal[b]);
    }

    public int getLightColor () {
        return Color.rgb(lightVal[r], lightVal[g], lightVal[b]);
    }

    /**
     * Apply subtractive coloring
     * @param paintColor
     */
    public void addPaint (GameColor paintColor) {
        r = bound(r + paintColor.r - 2);
        g = bound(g + paintColor.g - 2);
        b = bound(b + paintColor.b - 2);
    }

    /**
     * Apply addictive coloring
     * @param lightColor
     */
    public void addLight (GameColor lightColor) {
        r = bound(r + lightColor.r);
        g = bound(g + lightColor.g);
        b = bound(b + lightColor.b);
    }

    public boolean match(int r, int g, int b) {
        return this.r == r && this.g == g && this.b == b;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof GameColor))
            return false;
        GameColor gameColor = (GameColor)o;
        return this.r == gameColor.r && this.g == gameColor.g && this.b == gameColor.b;
    }

    public String toString () {
        return "r" + r + " g" + g + " b" + b;
    }

    public static int bound (int c) {
        return Utils.setBetween(c, 2, 0);
    }

}
