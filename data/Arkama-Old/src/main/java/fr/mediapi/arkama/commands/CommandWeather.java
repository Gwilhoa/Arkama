package fr.mediapi.arkama.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWeather implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("sun")) {
            if (args.length == 0) {
                for (Player s : Bukkit.getOnlinePlayers()) {
                    if (s.getWorld().equals(p.getWorld())) {
                        s.sendMessage(p.getName() + " a changé le temps il fait beau");
                    }
                }
                p.getWorld().setThundering(false);
                p.getWorld().setStorm(false);
            }
        }
        if (command.getName().equalsIgnoreCase("rain")) {
            if (args.length == 0) {
                for (Player s : Bukkit.getOnlinePlayers()) {
                    if (s.getWorld().equals(p.getWorld())) {
                        s.sendMessage(p.getName() + " a changé le temps il pleut");
                    }
                }
                p.getWorld().setThundering(false);
                p.getWorld().setStorm(true);
            }
        }
        if (command.getName().equalsIgnoreCase("thunder")) {
            if (args.length == 0) {
                Bukkit.broadcastMessage("changé le temps, l'orage peux tomber");
                p.getWorld().setThundering(true);
            }
        }
        if (command.getName().equalsIgnoreCase("weather")) {
            if (args.length == 0) {
                if (p.getWorld().hasStorm() || p.getWorld().isThundering()) {
                    p.getWorld().setThundering(false);
                    p.getWorld().setStorm(false);
                    for (Player s : Bukkit.getOnlinePlayers()) {
                        if (s.getWorld().equals(p.getWorld())) {
                            s.sendMessage(p.getName() + " a changé le temps il fait beau");
                        }
                    }
                } else {
                    p.getWorld().setStorm(true);
                    for (Player s : Bukkit.getOnlinePlayers()) {
                        if (s.getWorld().equals(p.getWorld())) {
                            s.sendMessage(p.getName() + " a changé le temps il pleut");
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    p.getWorld().setThundering(false);
                    p.getWorld().setStorm(false);
                    for (Player s : Bukkit.getOnlinePlayers()) {
                        if (s.getWorld().equals(p.getWorld())) {
                            s.sendMessage(p.getName() + " a changé le temps il fait beau");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rain")) {
                    p.getWorld().setStorm(true);
                    for (Player s : Bukkit.getOnlinePlayers()) {
                        if (s.getWorld().equals(p.getWorld())) {
                            s.sendMessage(p.getName() + " a changé le temps il pleut");
                        }
                    }
                } else if (args[0].equalsIgnoreCase(("thunder"))) {
                    p.getWorld().setStorm(true);
                    p.getWorld().setThundering(true);
                    for (Player s : Bukkit.getOnlinePlayers()) {
                        if (s.getWorld().equals(p.getWorld())) {
                            s.sendMessage(p.getName() + " a changé le temps l'orage va éclater");
                        }
                    }
                }
            }
        }
        return false;
    }
}
