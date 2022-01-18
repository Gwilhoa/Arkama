package fr.guigui205.arkama;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ArkaGamemode extends JavaPlugin {
    private final String prefix = "§cGamemode §1§l>>>§e";
    @Override
    public void onEnable() {
        getCommand("gamemode").setExecutor(this);
        getCommand("gm").setExecutor(this);
        getLogger().warning("[ArkaGamemode] chargé");
    }
    private void setGamemode(Player p, Integer i,CommandSender exec) {
        if (0 <= i && i <= 3) {
            p.setGameMode(Objects.requireNonNull(GameMode.getByValue(i)));
            if (exec instanceof Player){
                if (exec.equals(p)){
                    p.sendMessage(prefix + " tu es passé en " + Objects.requireNonNull(GameMode.getByValue(i)));
                    getLogger().info(p.getName() + " est passé en " + Objects.requireNonNull(GameMode.getByValue(i)));
                } else {
                    exec.sendMessage(prefix + " " + p.getName() + " est passé en " + Objects.requireNonNull(GameMode.getByValue(i)));
                    p.sendMessage(prefix + " tu es passé en " + Objects.requireNonNull(GameMode.getByValue(i)));
                    getLogger().info(p.getName() + " est passé en " + Objects.requireNonNull(GameMode.getByValue(i)) + " par " + exec.getName());
                }
            } else {
                getLogger().info(p.getName() + " est passé en " + Objects.requireNonNull(GameMode.getByValue(i)));
            }
        } else {
            if (exec instanceof Player) {
                exec.sendMessage(i + " n'est pas un nombre valide");
            } else {
                getLogger().info(i + " n'est pas un nombre valide");
            }
        }
    }
    private void setGamemode(Player p, String s,CommandSender exec){
        if (s.equalsIgnoreCase("survival")){
            setGamemode(p,0,exec);
        }
        else if (s.equalsIgnoreCase("creative")){
            setGamemode(p,1,exec);
        }
        else if (s.equalsIgnoreCase("adventure") || s.equalsIgnoreCase("aventure")){
            setGamemode(p,2,exec);
        }
        else if (s.equalsIgnoreCase("spectator") || s.equalsIgnoreCase("spectateur")){
            setGamemode(p,3,exec);
        }
        else {
            if (exec instanceof Player) {
                exec.sendMessage(prefix + "mauvais format /gamemode <joueur> <mode> ou /gm <mode>");
            } else {
                getLogger().info("mauvais format /gamemode <joueur> <mode>");
            }
        }
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2 ) {
            Player cible = Bukkit.getPlayer(args[0]);
            if (Bukkit.getOnlinePlayers().contains(cible)) {
                try {
                    setGamemode(cible, Integer.parseInt(args[1]),sender);
                } catch (NumberFormatException e) {
                    setGamemode(cible, args[1],sender);
                }
            }
            return false;
        }
        if (sender instanceof Player) {
            if (args.length == 1) {
                try {
                    setGamemode((Player) sender, Integer.parseInt(args[0]),sender);
                } catch (NumberFormatException e) {
                    setGamemode((Player) sender, args[0],sender);
                }
            }
            if (args.length == 0){
                if (((Player)sender).getGameMode().equals(GameMode.CREATIVE)){
                    setGamemode(((Player)sender),0,sender);
                }
                else{
                    setGamemode(((Player)sender),1,sender);
                }
            }
        } else {
            getLogger().info("mauvais format /gamemode <joueur> <mode>");
        }
        return false;
    }
}
