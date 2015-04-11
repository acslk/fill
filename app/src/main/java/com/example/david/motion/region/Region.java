package com.example.david.motion.region;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 09/04/15.
 */
public class Region {

    public GameColor gameColor;
    public List<ColorBlock> childBlocks = new ArrayList<>();
    public List<Region> neighborRegions = new ArrayList<>();

    public Region (GameColor gameColor) {
        this.gameColor = gameColor;
    }

    public void onDraw(Canvas canvas, float mapX, float mapY) {
        for (ColorBlock colorBlock : childBlocks) {
            colorBlock.onDraw(canvas, mapX, mapY);
        }
    }

    public static void setRegions (List<Region> regionList,
                                   List<ColorBlock> blockList) {

        for (int i = 0; i < blockList.size(); i++) {
            for (int j = i + 1; j < blockList.size(); j++) {
                if (blockList.get(i).isNeighbor(blockList.get(j))) {
                    blockList.get(i).neighborBlocks.add(blockList.get(j));
                    blockList.get(j).neighborBlocks.add(blockList.get(i));
                }
            }
        }

        // form the regions form the blocks
        for (ColorBlock block : blockList) {
            if (!block.added) {
                Region region = new Region(block.gameColor);
                region.childBlocks.add(block);
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

    public boolean isNeighbor (Region region) {
        for (ColorBlock block : childBlocks) {
            for (ColorBlock neighbor : block.neighborBlocks) {
                if (region.childBlocks.contains(neighbor))
                    return true;
            }
        }
        return false;
    }

}