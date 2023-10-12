package fr.guigui205.arkama.baguettes;

import fr.guigui205.arkama.ArkamaCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public abstract class Baguette {

    public Material item;
    public Material type;
    protected String name;
    protected String rel;
    protected int delay;
    protected String[] lore;

    public Baguette(String name, String rel, int delay, Material type, Material item, String... lore) {
        this.name = name;
        this.lore = lore;
        this.rel = rel;
        this.delay = delay;
        this.type = type;
        this.item = item;
        Handler.addBaguette(this);
    }

    public void delay(UUID uuid) {
        Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
            for (ItemStack is : Bukkit.getPlayer(uuid).getInventory().getStorageContents()) {
                if (is == null || is.getItemMeta() == null || is.getItemMeta().getDisplayName() == null) {
                    continue;
                }
                if (is.getItemMeta().getDisplayName().equals(rel)) {
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(name);
                    is.setItemMeta(im);
                }
            }

        }, delay);
    }

    public void delay(Player p) {
        delay(p.getUniqueId());
    }

    public void reload(Player p) {
        for (ItemStack is : p.getInventory().getStorageContents()) {
            if (is == null || is.getItemMeta() == null || is.getItemMeta().getDisplayName() == null) {
                continue;
            }
            if (is.getItemMeta().getDisplayName().equals(name)) {
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(rel);
                is.setItemMeta(im);
            }
        }
    }

    public ItemStack getItem() {
        ItemStack is = new ItemStack(type);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return is;
    }

    public abstract void activate(PlayerInteractEvent e);
}