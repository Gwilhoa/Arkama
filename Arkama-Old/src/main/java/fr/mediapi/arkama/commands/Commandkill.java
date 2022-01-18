package fr.mediapi.arkama.commands;

import fr.mediapi.arkama.util.SelectorHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Commandkill implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        Player p = (Player) sender;
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("Guigui205") || !args[0].equalsIgnoreCase(p.getName())) {
                Entity[] vics = SelectorHelper.getTargets(sender, args[0]);
                if (vics.length == 0) {
                    sender.sendMessage("rien trouvé");
                }
                for (Entity vic : vics) {
                    if (vic instanceof Damageable) {
                        ((Damageable) vic).setHealth(0);
                        Bukkit.broadcastMessage("§e§lArka§0§lDeath §4>> §cau désir de §e§l" + p.getName() + "§4§l " + vic.getName() + "§c a perdu la vie ");
                    }

                }
            } else {
                p.sendMessage("erreur");
            }
        }
        return false;
    }
}
