package fr.mediapi.arkama.commands;

import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.Arkama;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandVanish implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("vanish") && commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (!Arkama.hidden.contains(p.getName())) {
                Arkama.hidden.add(p.getName());
                p.sendMessage("Tu es désormais invisible");
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 9, true, false));
                for (Player h : Bukkit.getOnlinePlayers()) {
                    if (h != null) {
                        h.hidePlayer(ArkamaCore.instance, p);

                    }
                }
                Bukkit.broadcastMessage("§8§l[§4§l-§8§l]§e§l " + p.getDisplayName());
            } else {
                Arkama.hidden.remove(p.getName());
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.sendMessage("Tu es désormais visible");
                for (Player h : Bukkit.getOnlinePlayers()) {
                    if (h != null) {
                        h.showPlayer(ArkamaCore.instance, p);

                    }
                }
                Bukkit.broadcastMessage("§8§l[§a§l+§8§l]§e§l " + p.getDisplayName());
            }
        }
        return false;
    }
}
