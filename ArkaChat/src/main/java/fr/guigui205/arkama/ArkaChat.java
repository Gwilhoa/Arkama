package fr.guigui205.arkama;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.event.player.AsyncChatEvent;


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
    public void onChat(AsyncChatEvent e) {
        ArkaGrades.Grade.Grades g = grades.get(e.getPlayer().getUniqueId());
        TextComponent tc = Component.text(g.grd.prefix +" §e"+ e.getPlayer().getName()+" §9>> "+g.grd.suffix+e.originalMessage());
        e.message(tc);
    }

}
