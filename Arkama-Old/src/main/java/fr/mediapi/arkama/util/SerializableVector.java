package fr.mediapi.arkama.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

import java.io.Serializable;

public class SerializableVector implements Serializable {
    private static final long serialVersionUID = 2657651106777219170L;
    public double x;
    public double y;
    public double z;

    public SerializableVector(Location l) {
        this.x = l.getX();
        this.y = l.getY();
        this.z = l.getZ();
    }

    public SerializableVector() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public SerializableVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SerializableVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SerializableVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation(World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    public double getX() {
        return this.x;
    }

    public SerializableVector setX(int x) {
        this.x = (double) x;
        return this;
    }

    public SerializableVector setX(double x) {
        this.x = x;
        return this;
    }

    public SerializableVector setX(float x) {
        this.x = (double) x;
        return this;
    }

    public int getBlockX() {
        return NumberConversions.floor(this.x);
    }

    public double getY() {
        return this.y;
    }

    public SerializableVector setY(int y) {
        this.y = (double) y;
        return this;
    }

    public SerializableVector setY(double y) {
        this.y = y;
        return this;
    }

    public SerializableVector setY(float y) {
        this.y = (double) y;
        return this;
    }

    public int getBlockY() {
        return NumberConversions.floor(this.y);
    }

    public double getZ() {
        return this.z;
    }

    public SerializableVector setZ(int z) {
        this.z = (double) z;
        return this;
    }

    public SerializableVector setZ(double z) {
        this.z = z;
        return this;
    }

    public SerializableVector setZ(float z) {
        this.z = (double) z;
        return this;
    }

    public int getBlockZ() {
        return NumberConversions.floor(this.z);
    }

}
