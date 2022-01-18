package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.guigui205.arkama.ArkaGrades.grades;


public class ArkamaTablist extends JavaPlugin {


    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new TablistEvent(), this);
        getLogger().warning("[ArkaTablist] chargé");
    }


}

class TablistEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!grades.containsKey(e.getPlayer().getUniqueId())) {
            Bukkit.broadcastMessage("bienvenue à " + e.getPlayer().getName());
            grades.put(e.getPlayer().getUniqueId(), ArkaGrades.Grade.Grades.JOUEUR);
            ArkaGrades.Grade.saveGrade();
        }
        e.setJoinMessage("§7[§a+§7] " + grades.get(e.getPlayer().getUniqueId()).grd.suffix + e.getPlayer().getName());
        e.getPlayer().setPlayerListName(grades.get(e.getPlayer().getUniqueId()).grd.prefix + "§r§e§l" + e.getPlayer().getName() + "§r");
    }
}
