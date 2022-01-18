package fr.mediapi.arkama;

import com.google.gson.reflect.TypeToken;
import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.commands.*;
import fr.mediapi.arkama.murder.Murder;
import fr.mediapi.arkama.murder.commandes.CommandMain;
import fr.mediapi.arkama.objects.Grade;
import fr.mediapi.arkama.objects.Kit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static fr.mediapi.arkama.objects.Grade.saveGrade;

public class Arkama extends JavaPlugin implements CommandExecutor {


    public static HashMap<UUID, Grade.Grades> grade = new HashMap<>();
    public static HashMap<String, Kit> skit = new HashMap<>();
    public static HashMap<UUID, UUID> response = new HashMap<>();
    public static ArrayList<String> hidden = new ArrayList<>();
    public static HashMap<UUID, Integer> money = new HashMap<>();


    public static void setGrade(Player p, Grade.Grades t) {
        grade.put(p.getUniqueId(), t);
        p.setPlayerListName(grade.get(p.getUniqueId()).grd.prefix + "§r§e§l" + p.getName() + "§r");
        saveGrade();
    }



    public static void saveKit() {
        if (new File("Arkama/kit.json").exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/kit.json")));
                ArkamaCore.gson.toJson(skit, new TypeToken<HashMap<String, Kit>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fichier inexistant !");
            try {
                new File("Arkama/kit.json").createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/kit.json")));
                ArkamaCore.gson.toJson(skit, new TypeToken<HashMap<String, Kit>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDisable() {
        getLogger().info("saving arenas...");
        Murder.saveArenas();
        getLogger().info("saved !");
        getLogger().info("saving grades...");
        getLogger().info("saved !");
    }


    @Override
    public void onEnable() {


        Murder.loadArenas();



        if (new File("Arkama/money.json").exists()) {
            try {
                money = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/money.json"))), new TypeToken<HashMap<UUID, Integer>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/money.json").createNewFile();
                getLogger().warning("[money] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (grade == null) {
            grade = new HashMap<>();
        }

        if (money == null) {
            money = new HashMap<>();
        }
        if (skit == null) {
            skit = new HashMap<>();
        }


        getCommand("time").setExecutor(new CommandTime());
        getCommand("sun").setExecutor(new CommandWeather());
        getCommand("rain").setExecutor(new CommandWeather());
        getCommand("thunder").setExecutor(new CommandWeather());
        getCommand("weather").setExecutor(new CommandWeather());
        getCommand("craft").setExecutor(new CommandSimple());
        getCommand("magie").setExecutor(new CommandSimple());
        getCommand("nick").setExecutor(new CommandSimple());
        getCommand("invsee").setExecutor(new CommandSimple());
        getCommand("heal").setExecutor(new CommandSoin());
        getCommand("feed").setExecutor(new CommandSoin());
        getCommand("rl").setExecutor(new CommandSimple());
        getCommand("kick").setExecutor(new CommandSimple());
        getCommand("enchant").setExecutor(new CommandEnch());
        getCommand("vanish").setExecutor(new CommandVanish());
        getCommand("msg").setExecutor(new CommandMsg());
        getCommand("kill").setExecutor(new Commandkill());
        getCommand("hat").setExecutor(new Commandhat());
        getCommand("speed").setExecutor(new CommandSpeed());
        getCommand("rename").setExecutor(new CommandRename());
        getCommand("murder").setExecutor(new CommandMain());
        getCommand("setgrade").setExecutor(new CommandGrade());
        getCommand("r").setExecutor(new CommandMsg());
        getCommand("money").setExecutor(new CommandMoney());
        getCommand("kit").setExecutor(new CommandKit());
        getServer().getPluginManager().registerEvents(new Evenement(), this);

        getLogger().warning("LOL, XD");
    }
}

