package fr.mediapi.arkama.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSoin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("heal")) {
            if (args.length == 0) {
                if (p.getHealth() < 20) {
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setSaturation(10);
                    p.sendMessage("§e§lArka§c§lHeal §6§l>> §a vous avez été soigné");
                } else {
                    p.sendMessage("§e§lArka§c§lHeal §6§l>> §ctu a deja toutes ta vie");
                }
            }
            if (args.length == 1) {
                Player cible = Bukkit.getPlayer(args[0]);
                if (Bukkit.getOnlinePlayers().contains(cible)) {
                    if (cible.getHealth() < 20) {
                        cible.setHealth(20);
                        cible.setFoodLevel(20);
                        cible.setSaturation(10);
                        cible.sendMessage("§e§lArka§c§lHeal §6§l>> §a vous avez été soigné par " + p.getName());
                        p.sendMessage("§e§lArka§c§lHeal §6§l>> §a" + cible.getName() + " a bien été soigné");
                    } else {
                        p.sendMessage("§e§lArka§c§lHeal §6§l>> §c" + cible.getName() + " a toutes sa vie");
                    }
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (p.getFoodLevel() < 20) {
                p.setFoodLevel(20);
                p.setSaturation(10);
                p.sendMessage("§e§lArka§1§lFeed §6§l>> §a vous avez été nourris");
            } else {
                p.sendMessage("§e§lArka§1§lFeed §6§l>>  §ctu es déja rassasié");
            }
        }
        return false;
    }

}
