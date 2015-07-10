package com.motion.game;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class ColorMap {

    public GameColor gameColor;
    public List<ColorBlock> childBlocks = new ArrayList<>();
    public List<ColorMap> neighborRegions = new ArrayList<>();
    public ColorBlock collideBlock;

    public ColorMap(GameColor gameColor) {
        this.gameColor = gameColor;
    }

    public boolean containsBall (Ball ball) {
        for (ColorBlock block : childBlocks) {
            if (block.containsBall(ball)) {
                collideBlock = block;
                return true;
            }
        }
        return false;
    }

    public void collide (Ball ball, Ball lastBall) {
        collideBlock.bounceOff(ball, lastBall);
    }

    public static void setRegions (List<ColorMap> regionList, List<ColorBlock> blockList) {

        // set all block neighbor relationships
        for (int i = 0; i < blockList.size(); i++) {
            for (int j = i + 1; j < blockList.size(); j++) {
                if (blockList.get(i).isNeighbor(blockList.get(j))) {
                    blockList.get(i).neighborBlocks.add(blockList.get(j));
                    blockList.get(j).neighborBlocks.add(blockList.get(i));
                }
            }
        }

        // form the regions from the blocks
        for (ColorBlock block : blockList) {
            if (!block.added) {
                ColorMap region = new ColorMap(block.gameColor);
                region.childBlocks.add(block);
                block.added = true;
                region.traverse(block);
                regionList.add(region);
            }
        }

        // resolve neighbors of all regions
        for (int i = 0; i < regionList.size(); i++) {
            for (int j = i + 1; j < regionList.size(); j++) {
                if (regionList.get(i).isNeighbor(regionList.get(j))) {
                    regionList.get(i).neighborRegions.add(regionList.get(j));
                    regionList.get(j).neighborRegions.add(regionList.get(i));
                }
            }
        }
    }

    public void traverse (ColorBlock block) {
        for (ColorBlock adjacent : block.neighborBlocks) {
            if (!adjacent.added && gameColor.equals(adjacent.gameColor)) {
                childBlocks.add(adjacent);
                adjacent.added = true;
                traverse(adjacent);
            }
        }
    }


    public void onColorChange (List<ColorMap> regionList) {

        for (ColorBlock block : childBlocks)
            block.resetColor(gameColor);

        for (int i = 0; i < neighborRegions.size(); i++) {
            ColorMap merged = neighborRegions.get(i);
            if (gameColor.equals(merged.gameColor)) {
                // remove from list
                neighborRegions.remove(merged);
                i--;
                regionList.remove(merged);

                // redirect all neighbor connection to the matching region
                // update neighbor regions of current region
                for (ColorMap neighbor : merged.neighborRegions) {
                    if (neighbor != this) {
                        neighbor.neighborRegions.remove(merged);
                        if (!neighborRegions.contains(neighbor)) {
                            neighborRegions.add(neighbor);
                            neighbor.neighborRegions.add(this);
                        }
                    }
                }

                // merge all blocks
                childBlocks.addAll(merged.childBlocks);
            }
        }
    }

    public boolean isNeighbor (ColorMap region) {
        for (ColorBlock block : childBlocks) {
            for (ColorBlock neighbor : block.neighborBlocks) {
                if (region.childBlocks.contains(neighbor))
                    return true;
            }
        }
        return false;
    }

    public void onDraw(Canvas canvas, float mapX, float mapY, float interpolation) {
        for (ColorBlock block : childBlocks)
            block.draw(canvas, mapX, mapY, interpolation);
    }

}
