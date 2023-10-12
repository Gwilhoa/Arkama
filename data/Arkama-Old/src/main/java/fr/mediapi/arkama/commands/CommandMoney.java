package fr.mediapi.arkama.commands;

import fr.mediapi.arkama.Arkama;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (Arkama.money.get(p.getUniqueId()) == null || Arkama.money.get(p.getUniqueId()) == 0) {
                p.sendMessage("ArkaMoney >> tu n'as pas d'argent");
            } else {
                p.sendMessage("ArkaMoney >> tu as " + Arkama.money.get(p.getUniqueId()) + " Arkagold");
            }
        }
        return false;
    }
}
