package fr.mediapi.arkama.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {
    private String name;
    private List<String> lore;
    private Material m;
    private HashMap<Enchantment, Integer> enchs;
    private ItemStack temp;


    public ItemBuilder(Material m) {
        this.m = m;
    }

    public ItemStack build() {
        ItemStack is = new ItemStack(m, 1);
        ItemMeta im = is.getItemMeta();
        if (lore != null) {
            im.setLore(lore);
        }
        if (name != null) {
            im.setDisplayName(name);
        }
        if (enchs != null) {
            for (Enchantment e : enchs.keySet()) {
                im.addEnchant(e, enchs.get(e), true);
            }
        }
        is.setItemMeta(im);
        return is;
    }


    public ItemBuilder addName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder addLore(String... lines) {
        if (lines != null && lines.length > 0) {
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.addAll(Arrays.asList(lines));
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment e, int level) {
        if (enchs == null) {
            enchs = new HashMap<>();
        }
        enchs.put(e, level);
        return this;
    }

}
