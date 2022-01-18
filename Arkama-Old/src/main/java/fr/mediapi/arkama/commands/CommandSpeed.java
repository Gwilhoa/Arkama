package fr.mediapi.arkama.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpeed implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speed")) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage("correcte usage : /speed fly/walk (nombre 1 à 10)");
                p.sendMessage("ta vitesse de marche est de " + 10 * p.getWalkSpeed());
                p.sendMessage("ta vitesse de vol est de " + 10 * p.getFlySpeed());

            } else if (args[0].equalsIgnoreCase("reset")) {
                if (args.length == 1) {
                    p.setWalkSpeed((float) 0.2);
                    p.setFlySpeed((float) 0.1);
                    p.sendMessage("tu as reset ta vitesse");
                } else if (args[1].equalsIgnoreCase("fly")) {
                    p.setFlySpeed((float) 0.1);
                } else if (args[1].equalsIgnoreCase("walk")) {
                    p.setWalkSpeed((float) 0.2);
                }
            } else if (args[0].equalsIgnoreCase("fly")) {
                if (args.length == 2) {
                    try {
                        float s = Float.parseFloat(args[1]);
                        s = s / 10;
                        if (s > 1) {
                            p.sendMessage("vitesse trop élevée !");
                        } else if (s < -1) {
                            p.sendMessage("vitesse trop basse !");
                        } else {
                            p.setFlySpeed(s);
                            p.sendMessage("tu as passé ta vitesse de vol a " + 10 * p.getFlySpeed());
                        }
                    } catch (NumberFormatException e) {
                        p.sendMessage("erreur");
                    }

                }
            } else if (args[0].equalsIgnoreCase("walk")) {
                if (args.length == 2) {
                    try {
                        float s = Float.parseFloat(args[1]);
                        s = s / 10;
                        if (s > 1) {
                            p.sendMessage("vitesse trop élevée !");
                        } else if (s < -1) {
                            p.sendMessage("vitesse trop basse");
                        } else {
                            p.setWalkSpeed(s);
                            p.sendMessage("tu as passé ta vitesse de marche a " + 10 * p.getWalkSpeed());
                        }
                    } catch (NumberFormatException e) {
                        p.sendMessage("erreur");
                    }
                }
            }
        }
        return false;
    }
}