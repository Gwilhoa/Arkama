package fr.mediapi.arkama.commands;


import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.util.SelectorHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class CommandSimple implements CommandExecutor {

    private static int calc = 5;

    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("craft") && sender instanceof Player) {
            ((Player) sender).openWorkbench(null, true);
            sender.sendMessage("§e§lArka§7§lCraft §6§l >>§avous venez d'ouvrir une table de craft");

        }
        if (cmd.getName().equalsIgnoreCase("nick") && sender instanceof Player) {
            if (args.length != 0) {
                String rename = "§e§l" + args[0].replace('&', '§');
                ((Player) sender).setCustomName(rename);
                ((Player) sender).setCustomNameVisible(true);
                ((Player) sender).setPlayerListName(rename);
                sender.sendMessage("§e§lArka§a§lRename §6§l >> §aton nouveau nom est " + args[0].replace('&', '§') + " §r");
            } else {
                ((Player) sender).setCustomName("§e§l" + sender.getName() + "§r");
                ((Player) sender).setPlayerListName("§e§l" + sender.getName() + "§r");
                sender.sendMessage("§e§lArka§a§lRename §6§l >> §ctu as reset ton pseudo");
            }
        }
        if (cmd.getName().equalsIgnoreCase("invsee") && sender instanceof Player) {
            if (args.length == 1) {
                Player cible = Bukkit.getPlayer(args[0]);
                if (Bukkit.getOnlinePlayers().contains(cible)) {
                    ((Player) sender).openInventory(cible.getInventory());
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("rl")) {
            Bukkit.broadcastMessage("§e§lArka§dInfo §6§l>> reload dans");
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.broadcastMessage("§a" + calc);
                calc--;
            }, 20);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.broadcastMessage("§2" + calc);
                calc--;
            }, 40);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.broadcastMessage("§6" + calc);
                calc--;
            }, 60);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.broadcastMessage("§c" + calc);
                calc--;
            }, 80);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.broadcastMessage("§4" + calc);
                calc--;
            }, 100);
            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                Bukkit.reload();
                Bukkit.broadcastMessage("§ala mise a jour a été réalisée avec succès");
            }, 105);
        }
        if (cmd.getName().equalsIgnoreCase("kick") && args.length >= 1) {
            if (!args[0].equals("Guigui205") && !args[0].equals("Tenshi_Gaki")) {
                Entity[] vics = SelectorHelper.getTargets(sender, args[0]);
                if (vics == null) {
                    vics = new Entity[]{};
                }
                if (vics.length == 0) {
                    sender.sendMessage("Aucune personne trouvée");
                }
                String kickm;
                if (args.length >= 2) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]);
                        sb.append(" ");
                    }
                    kickm = "§cArkaKick §4§l>> §c" + args[0] + " as été kick pour raison : " + sb.toString();
                    Bukkit.broadcastMessage("§cArkaKick §4§l>> §ctu as été kick pour raison : " + sb.toString());
                } else {
                    kickm = "§cArkaKick §4§l>> §ctu as été kick sans raison apparente";
                }
                for (Entity vic : vics) {
                    if (vic instanceof Player) {
                        ((Player) vic).kickPlayer(kickm);
                    } else {
                        sender.sendMessage("tu est sur que " + (vic == null ? "null" : vic.getName()) + " est bien un joueur ?");
                    }
                }
            }

        }


        return false;
    }

}


