package fr.mediapi.arkama.commands;


import fr.mediapi.arkama.Arkama;
import fr.mediapi.arkama.objects.Grade;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandGrade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("setgrade")) {
            if (args.length == 2) {
                Player cible = Bukkit.getPlayer(args[0]);
                if (Bukkit.getOnlinePlayers().contains(cible) && cible != null) {
                    Grade.Grades found = null;
                    ArrayList<String> l = new ArrayList<>();
                    for (Grade.Grades g : Grade.Grades.values()) {
                        if (g.grd.name.equalsIgnoreCase(args[1])) {
                            found = g;
                            l.add(g.grd.name);
                            break;
                        }
                    }
                    if (found != null) {
                        Arkama.setGrade(cible, found);
                        if (Arkama.grade.get(cible.getUniqueId()).smaller(found)) {
                            Bukkit.broadcastMessage("Félicitation à " + cible.getName() + " qui vient de passer " + found.grd.prefix);
                        } else {
                            Bukkit.broadcastMessage("demote de " + cible.getName() + " qui vient de passer " + found.grd.prefix);
                        }
                    } else {
                        p.sendMessage("grade dispo :" + l.toString());
                    }
                }
            } else {
                p.sendMessage("usage correcte : /setgrade <pseudo> <grade>");
            }
        }
        return false;
    }
}
