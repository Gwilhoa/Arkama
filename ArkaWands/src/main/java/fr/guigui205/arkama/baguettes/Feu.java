package fr.guigui205.arkama.baguettes;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Feu extends Baguette {
    public Feu() {
        super("§4§kgg§8[§6baguette de §4feu§8]§4§kgg", "§4-[§cCHARGEMENT§4]-", 15, Material.BLAZE_ROD, Material.MAGMA_CREAM, "§a§lBaguette magique", "§4§lFEU!");
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

        reload(p);
        LargeFireball lfb = p.launchProjectile(LargeFireball.class);
        lfb.setCustomName("Boule de feu");
        lfb.setBounce(false);
        lfb.setIsIncendiary(true);
        lfb.setYield(3);
        delay(p);
    }
}
