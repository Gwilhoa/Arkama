package fr.guigui205.arkama;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

import static fr.guigui205.arkama.ArkamaCore.gson;

public class ArkaGrades extends JavaPlugin {
    public static HashMap<UUID, Grade.Grades> grades = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("setgrade").setExecutor(this);
        //getCommand("upgrade").setExecutor(this);
        //getCommand("downgrade").setExecutor(this);


        if (new File("Arkama/grades.json").exists()) {
            try {
                grades = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/grades.json"))), new TypeToken<HashMap<UUID, Grade.Grades>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/grades.json").createNewFile();
                getLogger().warning("[ArkaGrade] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (grades == null) {
            grades = new HashMap<>();
        }
        getServer().getPluginManager().registerEvents(new GradeEvent(), this);
        getLogger().warning("[ArkaGrade] chargé");
    }
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
                        grades.put(cible.getUniqueId(), found);
                        cible.setPlayerListName(grades.get(p.getUniqueId()).grd.prefix + "§r§e§l" + cible.getName() + "§r");
                        Grade.saveGrade();
                        if (grades.get(cible.getUniqueId()).smaller(found)) {
                            Bukkit.broadcastMessage("§4Grade §9§l>>> §eFélicitation à " + cible.getName() + " qui vient de passer " + found.grd.prefix);
                        } else {
                            Bukkit.broadcastMessage("demote de " + cible.getName() + " qui vient de passer " + found.grd.prefix);
                        }
                    } else {
                        p.sendMessage("grade dispo :" + l);
                    }
                }
            } else {
                p.sendMessage("usage correcte : /setgrade <pseudo> <grade>");
            }
        }
        return false;
    }

    public static class Grade {

        public String name;
        public String suffix;
        public String prefix;

        public HashMap<String, Boolean> perms;

        public Grade(String name, String suffix, String prefix, HashMap<String, Boolean> perms) {
            this.name = name;
            this.suffix = suffix;
            this.prefix = prefix;
            this.perms = perms;
        }

        public static void saveGrade() {
            if (new File("Arkama/grades.json").exists()) {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/grades.json")));
                    gson.toJson(grades, new TypeToken<HashMap<UUID, Grades>>() {
                    }.getType(), bw);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Fichier inexistant !");
                try {
                    new File("Arkama/grades.json").createNewFile();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/grades.json")));
                    gson.toJson(grades, new TypeToken<HashMap<UUID, Grade.Grades>>() {
                    }.getType(), bw);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public enum Grades {
            JOUEUR("joueur", ChatColor.GRAY + "", "§8[§7joueur§8] ", asMap(Arrays.asList(Collections.singletonMap("mv.bypass.gamemode.*", true), Collections.singletonMap("multiverse.core.create", false)))),
            BUILDER("builder", ChatColor.AQUA + "", "§8[§bBuildeur§8] "),
            ARKAMIEN("arkamien", ChatColor.LIGHT_PURPLE + "", "§8[§eArka§cm§dien§8] "),
            MODO("modo", ChatColor.GREEN + "", "§8[§aModérateur§8] "),
            ADMIN("admin", "§c", "§8§l[§c§ladministrateur§8§l] "),
            RESPONSABLE("resp", "§9", "§8§l[§9§lresponsable§8§l]"),
            FONDATEUR("fondateur", ChatColor.GOLD + "", "§8§l[§6§lFondateur§8§l] "),
            DEVELOPPEUR("Developpeur", ChatColor.DARK_RED + "", "§8§l[§5§lDeveloppeur§8§l] ");

            public Grade grd;

            Grades(String name, String suffix, String prefix, HashMap<String, Boolean> perms) {
                this.grd = new Grade(name, suffix, prefix, perms);
            }

            Grades(String name, String suffix, String prefix) {
                this(name, suffix, prefix, new HashMap<>());
            }

            private static HashMap<String, Boolean> asMap(List<Map<String, Boolean>> a) {
                HashMap<String, Boolean> hm = new HashMap<>();
                for (Map<String, Boolean> e : a) {
                    for (Map.Entry<String, Boolean> en : e.entrySet()) {
                        hm.put(en.getKey(), en.getValue());
                    }
                }
                return hm;
            }

            public static Optional<Grades> byName(String name) {
                for (Grades g : values()) {
                    if (g.grd.name.equals(name)) {
                        return Optional.of(g);
                    }
                }
                return Optional.empty();
            }

            public static Grades getPrevious(Grades g) {
                int j = 0;
                for (int i = 0; i < values().length; i++) {
                    if (values()[i] == g) {
                        j = i;
                        break;
                    }
                }
                try {
                    return values()[j - 1];
                } catch (IndexOutOfBoundsException e) {
                    return g;
                }
            }

            public static Grades getNext(Grades g) {
                boolean next = false;
                for (Grades gr : values()) {
                    if (gr == g) {
                        next = true;
                    }
                    if (next) {
                        return gr;
                    }
                }
                return g;
            }

            public static boolean hasNext(Grades g) {
                return g != getNext(g);
            }

            public static boolean hasPrevious(Grades g) {
                return g != getPrevious(g);
            }

            public HashMap<String, Boolean> getPerms() {
                if (!hasPrevious(this)) {
                    return this.grd.perms;
                }
                Grades g = getPrevious(this);
                HashMap<String, Boolean> hm = concatPerms(this.grd.perms, g.grd.perms);
                while (hasPrevious(g)) {
                    g = getPrevious(g);
                    hm = concatPerms(hm, g.grd.perms);
                }
                return hm;
            }

            private HashMap<String, Boolean> concatPerms(HashMap<String, Boolean> a, HashMap<String, Boolean> b) {
                HashMap<String, Boolean> ls = new HashMap<>(a);

                for (Map.Entry<String, Boolean> en : b.entrySet()) {
                    if (ls.containsKey(en.getKey())) {
                        ls.put(en.getKey(), en.getValue() || b.get(en.getKey()));
                    } else {
                        ls.put(en.getKey(), en.getValue());
                    }
                }
                return ls;
            }

            public boolean smaller(Grades g) {
                for (Grades gr : values()) {
                    if (this == gr) {
                        return true;
                    }
                    if (gr == g) {
                        return false;
                    }
                }
                return false;
            }
        }
    }
    class GradeEvent implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent e){
            if (!grades.containsKey(e.getPlayer().getUniqueId())){
                Bukkit.broadcastMessage("bienvenue à "+e.getPlayer().getName());
                grades.put(e.getPlayer().getUniqueId(), Grade.Grades.JOUEUR);
                Grade.saveGrade();
            }
        }
    }
}