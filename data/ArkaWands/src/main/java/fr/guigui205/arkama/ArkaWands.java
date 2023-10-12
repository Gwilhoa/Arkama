package fr.guigui205.arkama;

import fr.guigui205.arkama.baguettes.*;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;


public class ArkaWands extends JavaPlugin {
    public static final Feu f = new Feu();
    public static final Foudre fo = new Foudre();
    public static final Glace gl = new Glace();

    @Override
    public void onEnable() {
        getCommand("magie").setExecutor(this);
        for (Baguette b : Handler.getList()) {
            ItemStack fin = b.getItem();
            ShapedRecipe r = new ShapedRecipe(new NamespacedKey(this, "baguette_" + b.item.name().toLowerCase()), fin);
            r.shape(" e ", "ebe", " e ");
            r.setIngredient('b', b.type);
            r.setIngredient('e', b.item);
            getServer().addRecipe(r);
        }
        getServer().getPluginManager().registerEvents(new WandsEvent(), this);
        getLogger().warning("[ArkaWands] chargé");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("magie") && sender instanceof Player) {
            for (Baguette b : Handler.getList()) {
                ((Player) sender).getInventory().addItem(b.getItem());
            }
        }
        return false;
    }
}

class WandsEvent implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (ArkaWands.gl.frozen.contains(e.getPlayer().getName())) {
            e.getPlayer().teleport(e.getFrom());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Handler.handle(e);
    }
}
