package fr.guigui205.arkama;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static fr.guigui205.arkama.ArkaDaC.current;
import static fr.guigui205.arkama.ArkaDaC.onjump;
import static fr.guigui205.arkama.ArkamaCore.getKeysFromValue;
import static fr.guigui205.arkama.ArkamaCore.gson;


public class ArkaDaC extends JavaPlugin {
    public static HashMap<String, SerializableLocation> arena = new HashMap<>();
    public static HashMap<String, ArrayList<Player>> current = new HashMap<>();
    public static HashMap<String, Player> onjump = new HashMap<>();

    public static void game(ArrayList<Player> l, String s) {
        Player p = l.remove(0);
        onjump.put(s, p);
        p.teleport(arena.get(s).toLocation());
    }

    @Override
    public void onEnable() {
        new File("Arkama/dac").mkdir();
        if (new File("Arkama/dac/arena.json").exists()) {
            try {
                arena = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/dac/arena.json"))), new TypeToken<HashMap<String, SerializableLocation>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/dac/arena.json").createNewFile();
                getLogger().warning("[ArkaDaC] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (arena == null) {
            arena = new HashMap<>();
        }
        getCommand("dac").setExecutor(this);
        getServer().getPluginManager().registerEvents(new DACEVENT(), this);
        getLogger().warning("[ArkaDaC] chargé");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args[0].equalsIgnoreCase("create")) {
            arena.put(args[1], new SerializableLocation(p.getLocation()));
            if (new File("Arkama/dac/arena.json").exists()) {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/dac/arena.json")));
                    gson.toJson(arena, new TypeToken<HashMap<String, SerializableLocation>>() {
                    }.getType(), bw);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Fichier inexistant !");
                try {
                    new File("Arkama/dac/arena.json").createNewFile();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/dac/arena.json")));
                    gson.toJson(arena, new TypeToken<HashMap<String, SerializableLocation>>() {
                    }.getType(), bw);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (args[0].equalsIgnoreCase("join")) {
            if (arena.containsKey(args[1])) {
                if (!current.containsKey(args[1])) {
                    current.put(args[1], new ArrayList<>());
                }
                current.get(args[1]).add(p);
                p.teleport(arena.get(args[1]).toLocation());
                if (current.get(args[1]).size() >= 4) {
                    game(current.get(args[1]), args[1]);
                }
            }
        }
        return false;
    }
}

class DACEVENT implements Listener {
    @EventHandler
    public void OnDamage(EntityDamageEvent e) {
        EntityType t = e.getEntityType();
        if (t.equals(EntityType.PLAYER)) {
            Player p = Bukkit.getPlayer(e.getEntity().getUniqueId());
            if (onjump.containsValue(p)) {
                p.sendMessage("éliminé");
                current.get(getKeysFromValue(onjump, p).get(0)).remove(p);
                ArkaDaC.game(current.get(getKeysFromValue(onjump, p).get(0)), getKeysFromValue(onjump, p).get(0));
            }
        }
    }

    public void onSwim(EntityToggleSwimEvent e) {
        EntityType t = e.getEntityType();
        if (t.equals(EntityType.PLAYER)) {
            Player p = Bukkit.getPlayer(e.getEntity().getUniqueId());
            if (onjump.containsValue(p)) {
                p.sendMessage("éliminé");
                current.get(getKeysFromValue(onjump, p).get(0)).remove(p);
                current.get(getKeysFromValue(onjump, p).get(0)).add(p);
                ArkaDaC.game(current.get(getKeysFromValue(onjump, p).get(0)), getKeysFromValue(onjump, p).get(0));
            }
        }
    }
}

