package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;


public class ArkaTP extends JavaPlugin {
    private final String prefix = "§1Téléportation §9§l>>>";
    @Override
    public void onEnable(){
        getCommand("tp").setExecutor(this);
        getCommand("back").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        ArrayList<Player> Players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("back")){
            if (ArkamaTeleport.oldPos(p) != null){
                p.sendMessage(prefix + " tu as été téléporté à ton ancienne position");
                ArkamaTeleport.teleport(p, ArkamaTeleport.oldPos(p), p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR));
            } else {
                p.sendMessage(prefix + " tu n'as pas d'ancienne zone de téléportation");
            }
        } else {
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("@a"))
                {
                    for (Player cible : Players)
                    {
                        if (!cible.equals(p))
                            ArkamaTeleport.teleport(cible, p.getLocation(), true);
                    }
                }
                if (args[0].equalsIgnoreCase("@r"))
                {
                    int i = new Random().nextInt(Players.size());
                    ArkamaTeleport.teleport(Players.get(i), p.getLocation(), true);
                }
                else if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    ArkamaTeleport.teleport(p, Bukkit.getPlayer(args[0]).getLocation(), true);
                    p.sendMessage(prefix + " tu as été téléporté à " + Bukkit.getPlayer(args[0]).getName());
                } else {
                    p.sendMessage(prefix + " joueur déconnecté ou inexistant");
                }
            } else if (args.length == 2){
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0])) && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))){
                    ArkamaTeleport.teleport(Bukkit.getPlayer(args[0]), Bukkit.getPlayer(args[1]).getLocation(), true);
                    p.sendMessage(prefix + " tu as téléporté " + Bukkit.getPlayer(args[0]).getName() + " à " + Bukkit.getPlayer(args[1]).getName());
                } else {
                    p.sendMessage(prefix + " un des joueurs est déconnecté ou inexistant");
                }
            } else if (args.length == 3) {
                try {
                    int x = Integer.parseInt(args[0]);
                    int y = Integer.parseInt(args[1]);
                    int z = Integer.parseInt(args[2]);
                    ArkamaTeleport.teleport(p, new Location(p.getWorld(), x, y, z), true);
                    p.sendMessage(prefix + " tu as été téléporté aux coordonées " + x + " " + y + " " + z);
                } catch (NumberFormatException e) {
                    p.sendMessage(prefix + " les coordonnées ne sont pas des lettres");
                }
            } else if (args.length == 4) {
                Player c = Bukkit.getPlayer(args[0]);
                if (Bukkit.getOnlinePlayers().contains(c)) {
                    try {
                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        int z = Integer.parseInt(args[3]);
                        ArkamaTeleport.teleport(p, new Location(p.getWorld(), x, y, z), true);
                        p.sendMessage(prefix + " tu as été téléporté aux coordonées " + x + " " + y + " " + z);
                    } catch (NumberFormatException e) {
                        p.sendMessage(prefix + " les coordonnées ne sont pas des lettres");
                    }
                }
            }
        }
        return false;
    }
}
