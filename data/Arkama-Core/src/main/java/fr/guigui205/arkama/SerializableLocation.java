package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializableLocation {
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    public SerializableLocation(String world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public SerializableLocation(Location l) {
        this(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
    }


    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "SerializableLocation{" +
                "world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                '}';
    }
}
