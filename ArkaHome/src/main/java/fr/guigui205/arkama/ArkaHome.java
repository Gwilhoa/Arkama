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

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArkaHome extends JavaPlugin {
    private final String prefix = "§bHomes §9§l>>>§e";
    public static HashMap<UUID, List<Home>> homes = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("sethome").setExecutor(this);
        getCommand("home").setExecutor(this);
        getCommand("homelist").setExecutor(this);
        getCommand("delhome").setExecutor(this);
        getServer().getPluginManager().registerEvents(new HomeEvent(), this);


        if (new File("Arkama/home.json").exists()) {
            try {
                homes = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/home.json"))), new TypeToken<HashMap<UUID, List<Home>>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/home.json").createNewFile();
                getLogger().warning("[home] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (homes == null) {
            homes = new HashMap<>();
        }
        getLogger().warning("[ArkaHome] chargé");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true; //player only
        String s;
        if (args.length == 0) {
            s = "home";
        } else if (args.length == 1) {
            s = args[0];
        } else {
            sender.sendMessage("correcte usage : " + command.getName() + " <nom(optionnel)>");
            return false;
        }
        if (command.getName().equalsIgnoreCase("home")) {
            if (command.getName().equalsIgnoreCase("home")) {
                Player p = (Player) sender;
                if (!homes.isEmpty() && homes.containsKey(p.getUniqueId())) {
                    for (Home h : homes.get(p.getUniqueId())) {
                        if (h.name.equalsIgnoreCase(s)) {
                            if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
                                ArkamaTeleport.teleport(p, h.getpos(), true);
                            }
                            else{
                                boolean f = ArkamaTeleport.teleport(p,h.getpos(),false);
                                if (!f){
                                    return false;
                                }
                            }
                            p.sendMessage(prefix + " tu as été téléporté à " + s);
                            return false;
                        }
                    }
                    p.sendMessage(prefix + " " + s + " n'existe pas");
                } else {
                    p.sendMessage(prefix + " tu n'as pas d'home enregistré");
                }
            }
        } else if (command.getName().equalsIgnoreCase("sethome")) {
            if (homes == null || homes.get(((Player) sender).getUniqueId()) == null) {
                ArrayList<Home> set = new ArrayList<>();
                set.add(new Home(s, ((Player) sender).getLocation()));
                homes.put(((Player) sender).getUniqueId(), set);
            } else {
                ArrayList<Home> set = new ArrayList<>(homes.get(((Player) sender).getUniqueId()));
                set.removeIf(h -> h.name.equalsIgnoreCase(s));
                if (homes.get(((Player) sender).getUniqueId()).size() > 4) {
                    sender.sendMessage(prefix + " vous possedez trop de home");
                    return false;
                }
                set.add(new Home(s, ((Player) sender).getLocation()));
                homes.put(((Player) sender).getUniqueId(), set);
            }
            sender.sendMessage(prefix + " home posé avec succès");
        } else {
            if (homes.containsKey(((Player) sender).getUniqueId())){
                Inventory inv = Bukkit.createInventory(null, 27, command.getName());
            for (int i = 0; i <= 26; i++) {
                ItemStack it1 = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                ItemMeta itM = it1.getItemMeta();
                it1.setItemMeta(itM);
                inv.setItem(i, it1);
            }
            List<Home> hList = homes.get(((Player) sender).getUniqueId());
            ArrayList<Integer> l = new ArrayList<Integer>();
            l.add(16);
            l.add(10);
            l.add(15);
            l.add(11);
            int i = l.size() - 1;
            for (Home h : hList) {
                if (h.name.equals("home")) {
                    inv.setItem(13, ArkamaCore.getItem(Material.LIME_STAINED_GLASS_PANE, h.name, "§eArkaHome"));
                } else {
                    inv.setItem(l.get(i), ArkamaCore.getItem(Material.LIME_STAINED_GLASS_PANE, h.name, "§eArkaHome"));
                    i -= 1;
                }
            }
            while (i != -1) {
                inv.setItem(l.get(i), ArkamaCore.getItem(Material.RED_STAINED_GLASS_PANE, "Home non posé", "§eArkaHome"));
                i -= 1;
            }
            ((Player) sender).openInventory(inv);
        } else {
                sender.sendMessage(prefix + " tu as aucun home enregistré");
            }
        }
        if (new File("Arkama/home.json").exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/home.json")));
                ArkamaCore.gson.toJson(homes, new TypeToken<HashMap<UUID, List<Home>>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fichier inexistant !");
            try {
                new File("Arkama/home.json").createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/home.json")));
                ArkamaCore.gson.toJson(homes, new TypeToken<HashMap<UUID, List<Home>>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

class HomeEvent implements Listener {
    private final String prefix = "§bHomes §9§l>>>§e";
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("homelist")) {
            ItemStack current = e.getCurrentItem();
            if (current == null) return;
            e.setCancelled(true);
            if (current.getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                for (Home h : ArkaHome.homes.get(p.getUniqueId())) {
                    if (h.name.equals(current.getItemMeta().getDisplayName())) {
                        if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
                            ArkamaTeleport.teleport(p, h.getpos(), true);
                        } else {
                            boolean f = ArkamaTeleport.teleport(p, h.getpos(), false);
                            if (!f) {
                                return;
                            }
                        }
                        p.sendMessage(prefix + " tu as été téléporté à " + h.name);
                    }
                }
            }
        } else if (e.getView().getTitle().equals("delhome")) {
            ItemStack current = e.getCurrentItem();
            if (current == null) return;
            e.setCancelled(true);
            if (current.getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                for (Home h : ArkaHome.homes.get(p.getUniqueId())) {
                    if (h.name.equals(current.getItemMeta().getDisplayName())) {
                        ArkaHome.homes.remove(p.getUniqueId(), h);
                        p.sendMessage(prefix + " " + h.name + " supprimé avec succès");
                        p.closeInventory();
                    }
                }
            }
        }

    }
}

class Home {
    public String name;
    public String world;
    public Double x;
    public Double y;
    public Double z;

    public Home(String name, Location pos) {
        this.name = name;
        this.world = pos.getWorld().getName();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }
    public Location getpos(){
        return new Location(Bukkit.getWorld(this.world),this.x,this.y,this.z);

    }
    @Override
    public String toString() {
        return "Home{" +
                "name='" + name + '\'' +
                ", world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}