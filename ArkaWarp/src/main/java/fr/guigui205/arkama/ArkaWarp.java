package fr.guigui205.arkama;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;

public class ArkaWarp extends JavaPlugin {
    public static HashMap<String, SerializableLocation> warps = new HashMap<>();
    private final String prefix = "§bWarps §9§l>>>§e";
    @Override
    public void onEnable() {
        getCommand("setwarp").setExecutor(this);
        getCommand("warp").setExecutor(this);
        getCommand("warplist").setExecutor(this);
        getCommand("delwarp").setExecutor(this);
        getCommand("setspawn").setExecutor(this);
        getCommand("spawn").setExecutor(this);
        if (new File("Arkama/warps.json").exists()) {
            try {
                warps = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/warps.json"))), new TypeToken<HashMap<String, SerializableLocation>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/warps.json").createNewFile();
                getLogger().warning("[warps] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (warps == null) {
            warps = new HashMap<>();
        }
        getServer().getPluginManager().registerEvents(new WarpEvent(), this);
        getLogger().warning("[ArkaWarps] chargé");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("setspawn")) {
            p.getWorld().setSpawnLocation(p.getLocation());
            p.sendMessage(prefix + "tu as mis en place le spawn a ta position");
        }
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
                ArkamaTeleport.teleport(p, p.getWorld().getSpawnLocation(), true);
            } else {
                boolean f = ArkamaTeleport.teleport(p, p.getWorld().getSpawnLocation(), false);
                if (!f) {
                    return false;
                }
            }
            p.sendMessage(prefix + " tu as été téléporté au spawn de " + p.getWorld().getName());
        }
        if (command.getName().equalsIgnoreCase("warp")) {
            ArkamaTeleport.teleport(p, warps.get(args[0]).toLocation(), p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR));
        } else if (command.getName().equalsIgnoreCase("warplist")) {
            Inventory inv = Bukkit.createInventory(null, 54, "WarpList");
            for (int i = 0; i <= 53; i++) {
                if (i < 36) {
                    ItemStack it1 = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                    ItemMeta itM = it1.getItemMeta();
                    itM.setDisplayName("pas utilisé");
                    it1.setItemMeta(itM);
                    inv.setItem(i, it1);
                } else {
                    ItemStack it1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    ItemMeta itM = it1.getItemMeta();
                    itM.setDisplayName("Arkama");
                    it1.setItemMeta(itM);
                    inv.setItem(i, it1);
                }
            }
            inv.setItem(47, ArkamaCore.getItem(Material.LIME_STAINED_GLASS_PANE, "spawn", "§eArkaWarp"));
            int i = 0;
            for (String s : warps.keySet()) {
                if (s.equals("hub")) {
                    inv.setItem(51, ArkamaCore.getItem(Material.LIME_STAINED_GLASS_PANE, s, "§eArkaWarp"));
                } else {
                    inv.setItem(i, ArkamaCore.getItem(Material.LIME_STAINED_GLASS_PANE, s, "§eArkaWarp"));
                    i += 1;
                }
            }
            p.openInventory(inv);
        } else if (command.getName().equalsIgnoreCase("setwarp") || command.getName().equalsIgnoreCase("delwarp")) {
            if (command.getName().equalsIgnoreCase("setwarp")) {
                if (args[0].equals("spawn")) {
                    return false;
                }
                warps.put(args[0], new SerializableLocation(p.getLocation()));
                p.sendMessage(prefix + " warp " + args[0] + " posé avec succès");
            } else {
                Inventory inv = Bukkit.createInventory(null, 54, "DelWarp");
                for (int i = 0; i <= 53; i++) {
                    if (i < 36) {
                        ItemStack it1 = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                        ItemMeta itM = it1.getItemMeta();
                        itM.setDisplayName("pas utilisé");
                        it1.setItemMeta(itM);
                        inv.setItem(i, it1);
                    } else {
                        ItemStack it1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                        ItemMeta itM = it1.getItemMeta();
                        itM.setDisplayName("Arkama");
                        it1.setItemMeta(itM);
                        inv.setItem(i, it1);
                    }
                }
                int i = 0;
                for (String s : warps.keySet()) {
                    if (!(s.equals("spawn")) && !(s.equals("hub"))) {
                        inv.setItem(i, ArkamaCore.getItem(Material.LIME_STAINED_GLASS_PANE, s, "§eArkaWarp"));
                        i += 1;
                    }
                }
                p.openInventory(inv);
            }
            if (new File("Arkama/warps.json").exists()) {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/warps.json")));
                    ArkamaCore.gson.toJson(warps, new TypeToken<HashMap<String, SerializableLocation>>() {
                    }.getType(), bw);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Fichier inexistant !");
                try {
                    new File("Arkama/warps.json").createNewFile();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/warps.json")));
                    ArkamaCore.gson.toJson(warps, new TypeToken<HashMap<String, SerializableLocation>>() {
                    }.getType(), bw);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}

class WarpEvent implements Listener {
    private final String prefix = "§bWarps §9§l>>>§e";
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("WarpList") || e.getView().getTitle().equals("DelWarp")) {
            ItemStack current = e.getCurrentItem();
            if (current == null) return;
            e.setCancelled(true);
            if (current.getType().equals(Material.LIME_STAINED_GLASS_PANE) && e.getView().getTitle().equals("WarpList")) {
                Location loc = e.getWhoClicked().getWorld().getSpawnLocation();
                if (!current.getItemMeta().getDisplayName().equals("spawn")) {
                    loc = ArkaWarp.warps.get(current.getItemMeta().getDisplayName()).toLocation();
                }
                if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
                    ArkamaTeleport.teleport(p, loc, true);
                } else {
                    boolean f = ArkamaTeleport.teleport(p, loc, false);
                    if (!f) {
                        return;
                    }
                    p.sendMessage(prefix + " tu as été téléporté au spawn");
                }
                p.sendMessage(prefix + " tu as été téléporté à " + current.getItemMeta().getDisplayName());
            } else if (current.getType().equals(Material.LIME_STAINED_GLASS_PANE) && e.getView().getTitle().equals("DelWarp")) {
                ArkaWarp.warps.remove(current.getItemMeta().getDisplayName());
                p.sendMessage(prefix + " warps " + current.getItemMeta().getDisplayName() + " supprimé");
                p.closeInventory();
            }
        }
    }
}
