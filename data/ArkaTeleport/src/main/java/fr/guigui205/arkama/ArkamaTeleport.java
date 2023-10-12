package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public class ArkamaTeleport extends JavaPlugin {

    public static ArkamaTeleport instance;

    public ArkamaTeleport() {
        instance = this;
    }

    private static final HashMap<UUID, Location> oldpos = new HashMap<UUID, Location>();
    public static HashMap<UUID, Integer> tp = new HashMap<UUID, Integer>();

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new TeleportEvent(), this);
        getLogger().warning("[ArkaTeleport] chargé");
    }

    public static boolean teleport(Player p, Location l, boolean f) {
        if (f) {
            oldpos.put(p.getUniqueId(), p.getLocation());
            return p.teleport(l);
        } else {
            Integer task = 3;
            p.sendTitle("téléportation dans ...", Integer.toString(task));
            tp.put(p.getUniqueId(), task);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
                if (tp.get(p.getUniqueId()) == 0) {
                    teleport(p, l, true);
                    ArkamaTeleport.tp.remove(p.getUniqueId());
                    return;
                }
                if (tp.get(p.getUniqueId()) == -1) {
                    p.sendTitle("teleportation annulée", "");
                    tp.remove(p.getUniqueId());
                    return;
                }
                tp.put(p.getUniqueId(), tp.get(p.getUniqueId()) - 1);
                p.sendTitle("téléportation dans ...", tp.get(p.getUniqueId()).toString());
            }, 20L, 20L);

        }
        return false;
    }
    public static Location oldPos(Player p){
            return oldpos.get(p.getUniqueId());
        }


}
class TeleportEvent implements Listener{
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if (ArkamaTeleport.tp != null && ArkamaTeleport.tp.get(e.getPlayer().getUniqueId()) != null) {
            ArkamaTeleport.tp.put(e.getPlayer().getUniqueId(), -1);
        }
    }
}
