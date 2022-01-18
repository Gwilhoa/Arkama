package fr.mediapi.arkama.objects;

import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.*;

import static fr.guigui205.arkama.ArkamaCore.gson;
import static fr.mediapi.arkama.Arkama.grade;



public class Grade {

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
                gson.toJson(grade, new TypeToken<HashMap<UUID, Grades>>() {
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
                gson.toJson(grade, new TypeToken<HashMap<UUID, Grade.Grades>>() {
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
        ADMIN("admin", "§c" + "" + ChatColor.BOLD, "§8§l[§c§ladministrateur§8§l] "),
        RESPONSABLE("resp", "§9" + "" + ChatColor.BOLD, "§8§l[§9§lresponsable§8§l]"),
        FONDATEUR("fondateur", ChatColor.GOLD + "" + ChatColor.BOLD, "§8§l[§6§lFondateur§8§l] "),
        DEVELOPPEUR("Developpeur", ChatColor.DARK_RED + "" + ChatColor.BOLD, "§8§l[§5§lDeveloppeur§8§l] ");

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
