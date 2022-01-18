package fr.guigui205.arkama;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/**
 * Hello world!
 */
public class ArkamaCore extends JavaPlugin {
    public static <K, V> List<K> getKeysFromValue(Map<K, V> hm, V value) {
        List<K> list = new ArrayList<>();
        for (K o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                list.add(o);
            }
        }
        return list;
    }

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static ArkamaCore instance;

    @Override
    public void onEnable() {
        new File("Arkama").mkdir();
        instance = this;
        getServer().getPluginManager().registerEvents(new GeneralEvent(), this);
        getLogger().warning("[ArkaCore] chargement des add-ons");
    }

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

    public static void Broadcast(String msg)
    {
        Collection<? extends Player> p = Bukkit.getOnlinePlayers();
        for (Player player : p)
        {
            player.sendMessage(msg);
        }
    }

}

class GeneralEvent implements Listener {
    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity().getWorld().getName().equals("Ville1")) {
            e.setCancelled(true);
        }
    }

}