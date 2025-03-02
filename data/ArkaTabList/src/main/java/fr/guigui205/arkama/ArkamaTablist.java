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
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ChatConfig config = ChatConfig.chatConfig.get(e.getPlayer().getUniqueId());
        if (config == null) {
            config = new ChatConfig("&7" + e.getPlayer().getName(), "&r");
            ChatConfig.chatConfig.put(e.getPlayer().getUniqueId(), config);
            config.save();
        }
        e.joinMessage(Component.text("§7[§a+§7] " + config.prefix.replace("&", "§") + "§r"));
        ArkamaCore.sendMessageToDiscord("[+] "+p.getName(), null);
        e.getPlayer().playerListName(Component.text(config.prefix.replace("&", "§") + "§r"));
        p.sendActionBar(Component.text("§eBienvenue sur Bitumemc"));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ChatConfig config = ChatConfig.chatConfig.get(e.getPlayer().getUniqueId());
        if (config == null) {
            config = new ChatConfig("&7" + e.getPlayer().getName(), "&r");
            ChatConfig.chatConfig.put(e.getPlayer().getUniqueId(), config);
            config.save();
        }
        ArkamaCore.sendMessageToDiscord("[-] "+ e.getPlayer().getName(), null);
        e.quitMessage(Component.text("§7[§c-§7] " + config.prefix.replace("&", "§") + "§r"));
    }
}
