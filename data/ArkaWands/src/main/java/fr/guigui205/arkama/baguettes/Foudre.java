package fr.guigui205.arkama.baguettes;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class Foudre extends Baguette {
    public Foudre() {
        super("§4§kgg§8[§6baguette §eéléctrique§8]§4§kgg", "§4-§e[§cCHARGEMENT§e]§4-", 70, Material.BLAZE_ROD, Material.GOLD_BLOCK, "§a§lBaguette magique", "§e§lFOUDRE!");
    }

    @Override
    public void activate(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getLevel() < 1 && !p.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (p.getLevel() >= 1) {
            p.setLevel(p.getLevel() - 1);
        }
        delay(p);
        reload(p);
        Block block = p.getTargetBlock((Set<Material>) null, 30);
        p.getWorld().strikeLightning(block.getLocation());
    }
}
