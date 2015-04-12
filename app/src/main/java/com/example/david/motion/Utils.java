package com.example.david.motion;

/**
 * Created by dave on 07/04/15.
 */
public class Utils {

    public static boolean isBetween (int val, int max, int min) {
        return((val > min) && (val < max));
    }

    public static boolean isBetween (float val, float max, float min) {
        return((val > min) && (val < max));
    }

    public static int setBetween (int val, int max, int min) {
        if (val > max)
            return max;
        else if (val < min)
            return min;
        return val;
    }

    public static float setBetween (float val, float max, float min) {
        if (val > max)
            return max;
        else if (val < min)
            return min;
        return val;
    }

    public static double setBetween (double val, double max, double min) {
        if (val > max)
            return max;
        else if (val < min)
            return min;
        return val;
    }
}
