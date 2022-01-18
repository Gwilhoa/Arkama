package fr.mediapi.arkama.util;

import org.bukkit.Location;
import org.bukkit.World;

public class NamedLocation extends Location {
    public String name;

    public NamedLocation(String name, World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.name = name;
    }

    public NamedLocation(String name, Location l) {
        super(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        this.name = name;
    }

    public String toString() {
        return name + "," + this.getWorld().getName() + "," + this.getX() + "," + this.getY() + "," + this.getZ() + "," + this.getYaw() + "," + this.getPitch();
    }
}
