package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class ArkaUtils extends JavaPlugin {
    private static final String PREFIX = "§fArkaUtils §9§l>> §e";
    public static boolean canExplode = false;
    private static int calc = 5;

    @Override
    public void onEnable() {
        getCommand("explode").setExecutor(this);
        getCommand("ban").setExecutor(this);


        getServer().getPluginManager().registerEvents(new UtilEvent(), this);
        getLogger().warning("[ArkaUtils] chargé");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {

            if (command.getName().equalsIgnoreCase("explode")) {
                canExplode = !canExplode;
                if (canExplode) {
                    ArkamaCore.Broadcast("ALERTE §cLes explosions sont activé sur Ville1");
                } else {
                    ArkamaCore.Broadcast("§aSécurité remise en place, bonne journée");
                }
            }
        }
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("ban")) {
                if (args[0].equalsIgnoreCase("Gwilhoa")) {
                    String commandToExecute = "minecraft:ban" + " " +
                            p.getName() + " " + "§4 Contre uno";
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                } else {
                    StringBuilder commandToExecute = new StringBuilder("minecraft:ban");
                    for (String arg : args) {
                        commandToExecute.append(" ").append(arg);
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute.toString());
                }
                return true;
        }
        if (command.getName().equalsIgnoreCase("explode")) {
            if (p.getName().equalsIgnoreCase("Gwilhoa")) {
                canExplode = !canExplode;
                if (canExplode) {
                    ArkamaCore.Broadcast("§4§lALERTE §cLes explosions sont activé sur Ville1");
                } else {
                    ArkamaCore.Broadcast("§aSécurité remise en place, bonne journée");
                }
            }
        }
        if (command.getName().equalsIgnoreCase("invsee")) {
            if (args.length == 1) {
                Player cible = Bukkit.getPlayer(args[0]);
                if (cible == null) {
                    p.sendMessage("§cPlayer not found");
                } else if (Bukkit.getOnlinePlayers().contains(cible)) {
                    ((Player) sender).openInventory(cible.getInventory());
                }
            }
        }
        if (command.getName().equalsIgnoreCase("hat")) {
            if (p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getType().equals(Material.AIR)) {
                if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    p.sendMessage("Tu ne pas mettre de l'air");
                } else {
                    p.getInventory().setHelmet(p.getInventory().getItemInMainHand());
                    p.sendMessage("Profite de ton nouveau chapeau !");
                }
            } else p.sendMessage("Tu as déja un chapeau");
        }
        if (command.getName().equalsIgnoreCase("rl")) {
            ArkamaCore.Broadcast("§e§lArka§dInfo §6§l>> reload dans");
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                ArkamaCore.Broadcast("§a" + calc);
                calc--;
            }, 20);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                ArkamaCore.Broadcast("§2" + calc);
                calc--;
            }, 40);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                ArkamaCore.Broadcast("§6" + calc);
                calc--;
            }, 60);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                ArkamaCore.Broadcast("§c" + calc);
                calc--;
            }, 80);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                ArkamaCore.Broadcast("§4" + calc);
                calc--;
            }, 100);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.reload();
                ArkamaCore.Broadcast("§ala mise a jour a été réalisée avec succès");
            }, 105);
        }
        return true;
    }
}

class UtilEvent implements Listener {
    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity().getWorld().getName().equals("Ville1") && !ArkaUtils.canExplode) {
            e.setCancelled(true);
        }
    }


}

