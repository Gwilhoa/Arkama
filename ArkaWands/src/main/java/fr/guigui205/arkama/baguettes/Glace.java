package fr.guigui205.arkama.baguettes;

import fr.guigui205.arkama.ArkamaCore;
import fr.guigui205.arkama.RayTrace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class Glace extends Baguette {
    public ArrayList<String> frozen = new ArrayList<>();

    public Glace() {
        super("§4§kgg§8[§6baguette de §bglace§8]§4§kgg", "§4-§b[§cCHARGEMENT§b]§4-", 90, Material.BLAZE_ROD, Material.ICE, "Baguette magique", "Pointe et gèle !");
    }

    @Override
    public void activate(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Entity en = RayTrace.getNearestEntityInSight(p, 5);
        if (!(en instanceof Player)) {
            return;
        }

        frozen.add(en.getName());
        Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
            frozen.remove(en.getName());
        }, 60);

        delay(p);
        reload(p);
    }
}
