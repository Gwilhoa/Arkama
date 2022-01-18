package fr.mediapi.arkama.commands;

import fr.mediapi.arkama.Arkama;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMsg implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player p = (Player) sender;
        Player cible = Bukkit.getPlayer(args[0]);
        StringBuilder message = new StringBuilder();
        if (cmd.getName().equalsIgnoreCase("msg")) {
            if (!p.getName().equals(cible.getName())) {
                if (Bukkit.getOnlinePlayers().contains(cible)) {
                    args[0] = "[" + p.getName() + " -> " + cible.getName() + "]";
                    for (String m : args) {
                        message.append(" ").append(m);
                    }
                    p.sendMessage(String.valueOf(message));
                    cible.sendMessage(String.valueOf(message));
                    Arkama.response.put(p.getUniqueId(), cible.getUniqueId());
                    Arkama.response.put(cible.getUniqueId(), p.getUniqueId());
                } else {
                    p.sendMessage(args[0] + " n'est pas connecté ou inexistant");
                }
            } else {
                p.sendMessage("Tu ne peux pas envoyer un message a toi meme");
            }
        }
        if (cmd.getName().equalsIgnoreCase("r")) {
            if (Bukkit.getOnlinePlayers().contains(cible)) {
                args[0] = "[" + p.getName() + " -> " + cible.getName() + "]";
                for (String m : args) {
                    message.append(" ").append(m);
                }
                p.sendMessage(String.valueOf(message));
                cible.sendMessage(String.valueOf(message));
                Arkama.response.put(p.getUniqueId(), cible.getUniqueId());
                Arkama.response.put(cible.getUniqueId(), p.getUniqueId());
            } else {
                p.sendMessage(args[0] + " n'est pas connecté ou inexistant");
            }
        }
        return false;
    }
}
