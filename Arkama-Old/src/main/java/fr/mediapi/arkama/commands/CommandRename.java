package fr.mediapi.arkama.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CommandRename implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rename")) {
            Player p = (Player) sender;
            ItemStack rename = new ItemStack(p.getInventory().getItemInMainHand());
            ItemMeta metaname = rename.getItemMeta();
            metaname.setDisplayName(args[0].replace("&", "§"));
            rename.setItemMeta(metaname);
            p.getInventory().setItemInMainHand(rename);
        }
        return false;
    }
}

