package com.motion.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ColorMap {

    private Block map[][];
    private GameColor currentColor;

    public ColorMap(int width, int height, GameColor color) {
        map = new Block[width][height];
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                map[i][j] = new Block(i, j, new GameColor(color));
    }

    public void addRect(int x, int y, int width, int height, GameColor color) {
        for (int i = x; i < x+width; i++)
            for (int j = y; j < y+height; j++)
                map[i][j].setColor(color);
    }

    public void setBallPos(Ball ball) {
        currentColor = map[(int)ball.x][(int)ball.y].getColor();
    }

    public void update(Ball ball, Ball lastBall) {
    }


    public boolean changeColor(int x, int y, GameColor paintColor) {

        int original = map[x][y].getColor().getBackgroundColor();
        boolean[][] visited = new boolean[map.length][map[0].length];

        ArrayList<Point> queue = new ArrayList<>();
        queue.add(new Point(x,y));

        while (!queue.isEmpty()) {
            Point p = queue.remove(0);
            if (p.x < 0 || p.x >= map.length || p.y < 0 || p.y >= map[0].length)
                continue;
            if (visited[p.x][p.y])
                continue;

            visited[p.x][p.y] = true;
            if (map[p.x][p.y].getColor().getBackgroundColor() != original)
                continue;

            map[p.x][p.y].getColor().addPaint(paintColor);
            queue.add(new Point(p.x-1, p.y));
            queue.add(new Point(p.x+1, p.y));
            queue.add(new Point(p.x, p.y-1));
            queue.add(new Point(p.x, p.y+1));
        }

        GameColor c = map[0][0].getColor();
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (!map[i][j].getColor().equals(c))
                    return false;

        return true;
    }

    public void onDraw(Canvas canvas, float mapX, float mapY, float screenWidth,
                       float screenHeight, float interpolation) {
        int startX = (int)(-mapX);
        int startY = (int)(-mapY);
        int endX = (int)(-mapX + screenWidth);
        int endY = (int)(-mapY + screenHeight);

        if (endX >= map.length)
            endX = map.length - 1;
        if (endY >= map[0].length)
            endY = map[0].length - 1;

        GameColor c = map[startX][startY].getColor();
        Paint p = new Paint();
        p.setColor(c.getBackgroundColor());
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (!map[i][j].getColor().equals(c)) {
                    c = map[i][j].getColor();
                    p.setColor(c.getBackgroundColor());
                }
                canvas.drawRect(Game.px(i + mapX), Game.px(j + mapY),
                        Game.px(i + 1 + mapX), Game.px(j + 1 + mapY), p);
            }
        }
    }

}
