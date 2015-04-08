package com.example.david.motion;

import android.graphics.Color;

/**
 * Object representing colors used in the game. RGB in this class are not the actual
 * RGB for colors
 */
public class GameColor {

    private final static int[] backVal = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private final static int backAlpha = 200;
    private final static int[] objVal = {0, 1, 2, 3, 4 ,5, 6, 7, 8, 9};
    int r, g, b;

    public GameColor (int r, int g, int b) {
        this.r = bound(r);
        this.g = bound(g);
        this.b = bound(b);
    }

    public int getBackgroundColor (int r, int g, int b) {
        return Color.argb(backAlpha, backVal[r], backVal[g], backVal[b]);
    }

    public int getObjColor (int r, int g, int b) {
        return Color.argb(255, objVal[r], objVal[g], objVal[b]);
    }

    public boolean match(int r, int g, int b) {
        return this.r == r && this.g == g && this.b == b;
    }

    public static int bound (int c) {
        return Utils.setBetween(c, 9, 0);
    }

}
