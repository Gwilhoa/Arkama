package fr.mediapi.arkama.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ArmorStandUtil {
    public static final BlockFace[] RADIAL = {BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST};


    public static ItemStack makeSkull(Player p) {
        ItemStack skull = new ItemBuilder(Material.PLAYER_HEAD).addName(p.getName() + "'s skull.").build();
        ItemMeta meta = skull.getItemMeta();
        ((SkullMeta) meta).setOwningPlayer(p);
        skull.setItemMeta(meta);
        return skull;
    }

    public static ArmorStand spawnArmorStand(Location loc) {
        return (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
    }

    public static BlockFace yawToFace(float yaw) {
        return RADIAL[Math.round(yaw / 45f) & 0x7];
    }
}
