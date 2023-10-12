package fr.guigui205.arkama.baguettes;

import fr.guigui205.arkama.ArkamaCore;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class Handler {
    private static ArrayList<Baguette> baguettes = new ArrayList<>();

    public static void handle(PlayerInteractEvent e) {
        for (Baguette ba : baguettes) {
            if (e.getItem() != null && e.getItem().getType() == ba.type && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().equals(ba.name)) {
                ba.activate(e);
            }
        }
    }

    public static void addBaguette(Baguette b) {
        for (Baguette ba : baguettes) {
            if (ba.rel.equals(b.rel)) {
                ArkamaCore.instance.getLogger().severe(b.rel + " Est imcompatible avec " + ba.rel + " !!!!");
            }
        }
        baguettes.add(b);
    }

    public static ArrayList<Baguette> getList() {
        return baguettes;
    }
}
