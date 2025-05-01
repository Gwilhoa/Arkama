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
        ArkamaCore.sendMessageToDiscord("Démarrage du serveur", null);
        getServer().getPluginManager().registerEvents(new TablistEvent(), this);
        getLogger().warning("[ArkaTablist] chargé");
    }


}

class TablistEvent implements Listener {

}
