package fr.mediapi.arkama.murder;

import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.SubPlugin;
import fr.mediapi.arkama.menu.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Murder implements SubPlugin {
    public final static String subCommand = "murder";
    public static HashMap<String, Game> games = new HashMap<>();
    public static HashMap<String, Game> create = new HashMap<>();
    public static HashMap<String, Arena> arenas = new HashMap<>();
    // name -> game (name)
    public static HashMap<String, ArrayList<String>> recent = new HashMap<>();

    public static ItemStack getItem(Material material, String customname, String... Lore) {

        ItemStack it1 = new ItemStack(material, 1);
        ItemMeta itM = it1.getItemMeta();
        if (customname != null) itM.setDisplayName(customname);
        itM.setLore(Arrays.asList(Lore));
        itM.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        it1.setItemMeta(itM);
        return it1;
    }

    public static String findGame(String name) {
        for (Game game : games.values()) {
            if (game.contains(name)) {
                return game.name;
            }
        }
        return "";
    }

    public static String findDead(String name) {
        for (Game game : games.values()) {
            if (game.dead.containsValue(name)) {
                return game.name;
            }
        }
        return "";
    }

    public static void saveArenas() {
        for (Arena arena : arenas.values()) {
            arena.save();
        }
    }

    public static void loadArenas() {
        try (Stream<Path> walk = Files.walk(Paths.get(""))) {
            List<String> results = walk.map(Path::toString)
                    .filter(f -> f.endsWith("_arena.ser"))
                    .collect(Collectors.toList());
            for (String result : results) {
                Arena a = Arena.load(new File(result));
                if (a != null) {
                    arenas.put(a.name, a);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openCreateInv(Player p) {

        Inventory in = Bukkit.createInventory(null, 54, "§cMurder !");
        in.setItem(20, ArkamaCore.getItem(Material.OAK_SIGN,
                "Nom de la partie :",
                create.get(p.getName()).name.equals("DEFAULT") ?
                        "Entrez le nom de la partie" :
                        "Remplacez le nom de la partie"));
        in.setItem(24, ArkamaCore.getItem(Material.RED_BED,
                "Arène :",
                create.get(p.getName()).arena.equals("DEFAULT") ?
                        "Choisissez l'arène" :
                        "Remplacez l'arène"));
        if (create.get(p.getName()).name.equals("DEFAULT")) {
            in.setItem(40, getItem(Material.RED_TERRACOTTA,
                    "Créer la partie",
                    "§4§lIl manque le nom de la partie !"));
        } else if (create.get(p.getName()).arena.equals("DEFAULT")) {
            in.setItem(40, getItem(Material.RED_TERRACOTTA,
                    "Créer la partie",
                    "§4§lIl manque l'arène !"));
        } else {
            in.setItem(40, getItem(Material.GREEN_TERRACOTTA,
                    "Créer la partie",
                    "§1Créer la partie de murder !"));
        }
        p.openInventory(in);
    }
}
