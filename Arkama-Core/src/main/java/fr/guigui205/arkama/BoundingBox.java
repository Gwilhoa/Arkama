package fr.guigui205.arkama;


import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class BoundingBox {

    //min and max points of hit box
    Vector max;
    Vector min;

    BoundingBox(Vector min, Vector max) {
        this.max = max;
        this.min = min;
    }

    BoundingBox(Block block) {
        org.bukkit.util.BoundingBox bb = block.getBoundingBox();
        min = bb.getMin();
        max = bb.getMax();
    }


    BoundingBox(org.bukkit.util.BoundingBox bb) {
        min = bb.getMin();
        max = bb.getMax();
    }

    public Vector midPoint() {
        return max.clone().add(min).multiply(0.5);
    }

}
