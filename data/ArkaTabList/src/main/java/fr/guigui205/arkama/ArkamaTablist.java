package fr.guigui205.arkama;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


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
        Player p = e.getPlayer();
        e.joinMessage(Component.text("§7[§a+§7] " + e.getPlayer().getName()));
        e.getPlayer().playerListName(Component.text("§r§e§l" + e.getPlayer().getName() + "§r"));
        p.sendActionBar(Component.text("§eBienvenue sur Bitumemc"));

    }
    public void onQuit(PlayerQuitEvent e) {
        e.quitMessage(Component.text("§7[§c-§7] " + e.getPlayer().getName()));
    }
}
