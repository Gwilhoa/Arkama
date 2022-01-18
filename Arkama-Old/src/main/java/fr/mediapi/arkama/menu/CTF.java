package fr.mediapi.arkama.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CTF {
    public static ItemStack getItem(Material material, String customname, String... Lore) {
        ItemStack it1 = new ItemStack(material, 1);
        ItemMeta itM = it1.getItemMeta();
        if (customname != null) itM.setDisplayName(customname);
        itM.setLore(Arrays.asList(Lore));
        itM.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        it1.setItemMeta(itM);
        return it1;
    }

    public static Inventory menuctf() {
        Inventory inv = Bukkit.createInventory(null, 54, "§e§lArka§c§lC§e§lTF");
        getItem(Material.DIAMOND_PICKAXE, "§4Coming Soon", "en construction");
        return inv;
    }
}
