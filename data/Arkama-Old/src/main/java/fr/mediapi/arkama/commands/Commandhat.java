package fr.mediapi.arkama.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandhat implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player p = (Player) sender;
        if (p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getType().equals(Material.AIR)) {
            if (p.getInventory().getItemInMainHand() == null && p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                p.sendMessage("tu ne pas mettre de l'air");
            } else {
                p.getInventory().setHelmet(p.getInventory().getItemInMainHand());
                p.getInventory().setItemInMainHand(null);
                p.sendMessage("profite de ton nouveau chapeau");
            }
        } else p.sendMessage("tu as déja un chapeau");
        return false;
    }
}

