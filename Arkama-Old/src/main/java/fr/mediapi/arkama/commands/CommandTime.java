package fr.mediapi.arkama.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTime implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        String prefix = "§e§lArka§c§lT§b§lime §6§l>> " + p.getCustomName();
        String Wprefix = "§e§lArka§c§lW§b§leather §6§l>> " + p.getCustomName() + " ";
        if (command.getName().equalsIgnoreCase("time")) {
            if (args.length == 0) {
                p.sendMessage("correct usgae /time day/night/set");
            } else {
                if (args[0].equalsIgnoreCase("day")) {
                    p.getWorld().setTime(1500);
                    Bukkit.broadcastMessage(prefix + " a mis le jour");
                }
                if (args[0].equalsIgnoreCase("night")) {
                    p.getWorld().setTime(18000);
                    Bukkit.broadcastMessage(prefix + " a mis la nuit");
                }
                if (args[0].equalsIgnoreCase("set")) {
                    try {
                        int x = Integer.parseInt(args[1]);
                        Bukkit.broadcastMessage(prefix + "a mis le jour a " + x + " ticks");
                        p.getWorld().setTime(x);
                    } catch (NumberFormatException e) {
                        p.sendMessage("erreur");
                    }
                }
            }

        }
        return false;
    }

}
