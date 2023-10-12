package fr.mediapi.arkama.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static fr.guigui205.arkama.ArkamaCore.getItem;

public class main {


    public static Inventory main() {
        Inventory inv = Bukkit.createInventory(null, 54, "§e§lArka§c§lm§e§la");
        for (int i = 0; i <= 53; i++) {
            ItemStack it1 = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta itM = it1.getItemMeta();
            itM.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
            itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            it1.setItemMeta(itM);
            inv.setItem(i, it1);
        }
        inv.setItem(2, getItem(Material.BRICK, "§eVille", "§4Allez faire un tour en ville"));
        inv.setItem(4, getItem(Material.NETHER_STAR, "§eHub", "§4Retourner au centre du hub"));
        inv.setItem(6, getItem(Material.DIAMOND_SWORD, "§eFaction", "§4Allez au faction !"));
        inv.setItem(10, getItem(Material.BLAZE_ROD, "§eCombat de magie", "§4Ne fonctionne pas encore"));
        inv.setItem(12, getItem(Material.POPPY, "§ePousse-Pousse", "§4Ne fonctionne pas encore"));
        inv.setItem(14, getItem(Material.GRASS, "§eSkyBlock", "§4Dev suspendu"));
        inv.setItem(16, getItem(Material.ARROW, "§eSkyWars", "§4Dev suspendu"));
        inv.setItem(19, getItem(Material.BLUE_BANNER, "§eCapture The Flag", "§4En dev"));
        inv.setItem(21, getItem(Material.IRON_SWORD, "§ePvP", "§4Dev suspendu"));
        inv.setItem(23, getItem(Material.EGG, "§eSplegg", "§4Dev suspendu"));
        inv.setItem(25, getItem(Material.RED_BED, "§eBedWars", "§4Dev suspendu"));
        inv.setItem(28, getItem(Material.WATER_BUCKET, "§eDé A Coudre", "§4Dev suspendu"));
        inv.setItem(30, getItem(Material.LADDER, "§eJump", "§4Build en cours"));
        inv.setItem(47, getItem(Material.LEATHER_HELMET, "§eSite", "§4Dev suspendu"));
        inv.setItem(49, getItem(Material.IRON_HELMET, "§eDiscord", "§4Prochainement"));
        inv.setItem(51, getItem(Material.APPLE, "§eSurvie", "§4Un survie entre pote, c'est cool !"));
        inv.setItem(53, getItem(Material.DARK_OAK_DOOR, "§eFermer le Menu", ""));
        return inv;
    }
}
