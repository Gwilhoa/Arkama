package fr.mediapi.arkama.murder;

import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.Arkama;
import fr.mediapi.arkama.util.ItemBuilder;
import fr.mediapi.arkama.util.SerializableVector;
import fr.mediapi.arkama.util.XpStart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Game {
    public static final ItemStack GUN = new ItemBuilder(Material.BOW).addName("§b§kolo§bPistolet§kolo").addLore("§bTu es le détective", "§bTue le meurtrier !").addEnchant(Enchantment.ARROW_DAMAGE, 999).addEnchant(Enchantment.ARROW_INFINITE, 1).build();
    public static final ItemStack COUTEAU = new ItemBuilder(Material.DIAMOND_SWORD).addName("§c§kolo§cCouteau§kolo").addLore("§cTu es le meurtrier", "§cTue tout le monde !").addEnchant(Enchantment.DAMAGE_ALL, 999).build();
    public String name;
    public HashMap<Location, String> dead = new HashMap<>();
    public String arena;
    public String murder;
    public int task;
    public ArrayList<String> detect = new ArrayList<>();
    private ArrayList<String> players = new ArrayList<>();

    public Game(String name, String arena) {
        this.name = name;
        this.arena = arena;
    }

    private static <T> List<T> getRandomElement(List<T> list,
                                                int totalItems) {
        Random rand = new Random();
        List<T> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(list.size());
            newList.add(list.get(randomIndex));
            list.remove(randomIndex);
        }
        return newList;
    }

    private static <T> List<String> cloneList(List<T> list) {
        List<String> clone = new ArrayList<>(list.size());
        for (T item : list) {
            if (item instanceof String) {
                String item2 = (String) item;
                clone.add(item2);
            }
        }
        return clone;
    }

    public boolean join(String name) {
        if (!players.contains(name)) {
            players.add(name);
            if (size() == 3) {
                task = Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                    boolean sc = start();
                    if (!sc) {
                        Bukkit.broadcastMessage("Impossible de démarrer la map ! il n'y a pas assez de spawns");
                    }
                }, 1200).getTaskId();
                XpStart.xpStart(copyPlayers(), 60);
            }
            return true;
        }
        return false;
    }

    public void quit(String name) {
        players.remove(name);
    }

    public boolean contains(String name) {
        boolean ret = false;
        if (players != null) {
            if (players.size() >= 1) {
                ret = players.contains(name);
            }
        }
        if (murder != null) {
            ret = ret || murder.equals(name);
        }
        if (detect != null) {
            if (detect.size() >= 1) {
                ret = ret || detect.contains(name);
            }
        }
        return ret;
    }

    public int size() {
        return players.size();
    }

    public String[] copyPlayers() {
        String[] ret = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            ret[i] = players.get(i);
        }
        return ret;
    }

    public void stop() {
        //cleanup !
        final String message = "Faites \"/murder restart\" pour recommencer la partie !";
        ArrayList<String> par = new ArrayList<>();
        par.add(name);
        par.add(arena);

        for (Location l : dead.keySet()) {
            l.getBlock().setType(Material.AIR);
        }
        for (String s : dead.values()) {
            Murder.recent.put(s, par);
            Bukkit.getPlayer(s).setGameMode(GameMode.SURVIVAL);
            Bukkit.getPlayer(s).sendMessage(message);
        }
        for (String p : players) {
            Murder.recent.put(p, par);
            Bukkit.getPlayer(p).setFoodLevel(20);
            Bukkit.getPlayer(p).getInventory().clear();
            Bukkit.getPlayer(p).setGameMode(GameMode.SURVIVAL);
            Bukkit.getPlayer(p).sendMessage(message);
        }
        for (String d : detect) {
            Murder.recent.put(d, par);
            Bukkit.getPlayer(d).setFoodLevel(20);
            Bukkit.getPlayer(d).getInventory().clear();
            Bukkit.getPlayer(d).setGameMode(GameMode.SURVIVAL);
            Bukkit.getPlayer(d).sendMessage(message);
        }
        Murder.recent.put(murder, par);
        Bukkit.getPlayer(murder).setFoodLevel(20);
        Bukkit.getPlayer(murder).getInventory().clear();
        Bukkit.getPlayer(murder).sendMessage(message);

        Murder.games.remove(name);
    }

    public boolean start() {
        Arena arena1 = Murder.arenas.get(arena);
        if (arena1.playerSpawns.size() < players.size()) {
            return false;
        }
        //remove all recent players with that game name
        for (Map.Entry<String, ArrayList<String>> e : Murder.recent.entrySet()) {
            if (e.getValue().get(0).equals(name)) {
                Murder.recent.remove(e.getKey());
            }
        }
        List<SerializableVector> sv = getRandomElement((List<SerializableVector>) arena1.playerSpawns.clone(), players.size());
        Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("invis");
        if (t == null) t = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("invis");
        t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        int i = 0;
        for (String p : players) {
            Bukkit.getPlayer(p).getInventory().clear();
            t.addEntry(p);
            Bukkit.getPlayer(p).teleport(sv.get(i).toLocation(Bukkit.getWorld(arena1.world)), PlayerTeleportEvent.TeleportCause.PLUGIN);
            i++;
        }
        List<String> mr;
        if (players.size() >= 2) {
            mr = getRandomElement(cloneList(players), 2);
        } else if (players.size() == 1) {
            mr = getRandomElement(cloneList(players), 1);
        } else {
            return false;
        }
        murder = mr.get(0);
        if (mr.size() != 1) {
            detect.add(mr.get(1));
        }
        for (String p : players) {
            Bukkit.getPlayer(p).setHealth(20);
            Bukkit.getPlayer(p).setGameMode(GameMode.ADVENTURE);
            if (p.equals(murder)) {
                Player pl = Bukkit.getPlayer(murder);
                pl.sendTitle("§4§lTu es le Meurtrier !", "§cTue tout le monde !", 3, 40, 6);
                pl.getInventory().setHeldItemSlot(0);
                pl.getInventory().setItem(1, COUTEAU);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 2, true, false));
                pl.updateInventory();

            } else if (p.equals(detect.get(0))) {
                Player pl = Bukkit.getPlayer(detect.get(0));
                pl.sendTitle("§1§lTu es le Détective !", "§bTue le meurtrier !", 3, 40, 6);
                pl.getInventory().setHeldItemSlot(0);
                pl.getInventory().setItem(1, GUN);
                pl.getInventory().setItem(15, new ItemStack(Material.ARROW));
                pl.setFoodLevel(6);
                pl.setSaturation(20);
                pl.updateInventory();

            } else {
                Bukkit.getPlayer(p).setFoodLevel(6);
                Bukkit.getPlayer(p).setSaturation(20);
                Bukkit.getPlayer(p).sendTitle("Tu es un innocent !", "§cS§ru§cr§rv§ci§rs§c j§ru§cs§rq§cu§r'§ca §rl§ca §rf§ci§rn !", 3, 40, 6);
            }
        }
        players.remove(murder);
        players.remove(detect.get(0));
        return true;
    }
}
