package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

import static fr.guigui205.arkama.ArkaGrades.grades;


public class ArkaChat extends JavaPlugin {

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getLogger().warning("[ArkaChat] chargé");
    }
}

class ChatEvent implements Listener {


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        ArkaGrades.Grade.Grades g = grades.get(e.getPlayer().getUniqueId());
        e.setFormat(g.grd.prefix +" §e"+ e.getPlayer().getName()+" §9>> "+g.grd.suffix+e.getMessage().replace("&","§"));
    }

}
