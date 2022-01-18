package fr.mediapi.arkama;

import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.menu.CTF;
import fr.mediapi.arkama.menu.main;
import fr.mediapi.arkama.murder.Game;
import fr.mediapi.arkama.murder.Murder;
import fr.mediapi.arkama.objects.Grade;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

import static fr.mediapi.arkama.Arkama.grade;



public class Evenement implements Listener {



 /*
    public static ItemStack getItem(Material material, String customname, short data, String... Lore) {

        ItemStack it1 = new ItemStack(material, 1, data);
        ItemMeta itM = it1.getItemMeta();
        if (customname != null) itM.setDisplayName(customname);
        itM.setLore(Arrays.asList(Lore));
        itM.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        it1.setItemMeta(itM);
        return it1;
    }*/

    private static Location makeCorpse(Player p) {
        Location spawn = p.getLocation().add(0, 0, 0);
        Block b = spawn.getBlock();
        while (b.getType() != Material.AIR) {
            spawn = spawn.add(0, 1, 0);
            b = spawn.getBlock();
        }
        b.setType(Material.PLAYER_HEAD);
        Skull s = (Skull) b.getState();
        //s.setRotation(ArmorStandUtil.yawToFace(p.getLocation().getYaw()));  FUCK THAT THING
        s.setOwningPlayer(p);
        s.update(true);
        return spawn;
    }

    public static ItemStack menu() {
        ItemStack menu = new ItemStack(Material.COMPASS);
        ItemMeta custommenu = menu.getItemMeta();
        custommenu.setDisplayName("§6Menu");
        custommenu.setLore(Arrays.asList("§a§lOuvrir le menu", "§e§lArkama"));
        custommenu.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        custommenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        menu.setItemMeta(custommenu);
        return menu;
    }


    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (p.getScoreboardTags().contains("edit")) {
            event.setCancelled(true);
            Murder.create.get(p.getName()).name = event.getMessage();
            p.removeScoreboardTag("edit");
            Murder.openCreateInv(p);
            return;
        }
        if (!Murder.findGame(p.getName()).equals("") && Murder.games.get(Murder.findGame(p.getName())).dead.containsValue(p.getName())) {
            event.setCancelled(true);
            p.sendMessage("Vous n'avez pas le droit de parler quand vous êtes mort.");
        } else {
            Grade g = grade.get(p.getUniqueId()).grd;
            event.setFormat(g.prefix + p.getName() + " §c>> §6" + g.suffix + event.getMessage().replace('&', '§'));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!Murder.findGame(p.getName()).equals("")) {
            Location head = makeCorpse(p);
            e.setDeathMessage(null);
            p.sendTitle("Vous êtes mort !", "", 3, 60, 6);
            p.setGameMode(GameMode.SPECTATOR);
            Game g = Murder.games.get(Murder.findGame(p.getName()));
            if (g != null) {
                if (g.dead == null) {
                    g.dead = new HashMap<>();
                }
                g.quit(p.getName());
                e.setKeepInventory(false);
                boolean isdet = g.detect.remove(p.getName());
                if (p.getInventory().contains(Game.GUN)) {
                    List<ItemStack> drp = e.getDrops();
                    drp.clear();
                    drp.add(Game.GUN);
                } else {
                    e.getDrops().clear();
                }

                g.dead.put(head, p.getName());
                if (!isdet && !g.murder.equals(p.getName()) && g.detect.contains(p.getKiller().getName())) {
                    Player k = p.getKiller();
                    k.getInventory().clear();
                    Location l = k.getLocation();
                    l.getWorld().dropItem(l.add(0, 1, 0), Game.GUN);
                }
                if (g.murder.equals(p.getName())) {
                    for (String n : g.dead.values()) {
                        Bukkit.getPlayer(n).sendTitle("FIN DU JEU", "Les innocents ont gagné !", 3, 60, 6);
                    }
                    for (String n : g.copyPlayers()) {
                        Bukkit.getPlayer(n).sendTitle("FIN DU JEU", "Les innocents ont gagné !", 3, 60, 6);
                    }
                    Bukkit.getPlayer(g.murder).sendTitle("FIN DU JEU", "Les innocents ont gagné !", 3, 60, 6);
                    for (String n : g.detect) {
                        Bukkit.getPlayer(n).sendTitle("FIN DU JEU", "Les innocents ont gagné !", 3, 60, 6);
                    }

                    g.stop();
                } else if (g.copyPlayers() == null || g.copyPlayers().length == 0 && g.detect.size() == 0) {
                    for (String n : g.dead.values()) {
                        Bukkit.getPlayer(n).sendTitle("FIN DU JEU", "Le murder a gagné", 3, 60, 6);
                    }
                    Bukkit.getPlayer(g.murder).sendTitle("FIN DU JEU", "Tu as gagné", 3, 60, 6);
                    g.stop();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();

        if (!Murder.findDead(p.getName()).equals("")) {
            Game g = Murder.games.get(Murder.findDead(p.getName()));
            if (g != null) {
                if (g.dead == null) {
                    g.dead = new HashMap<>();
                }
                if (g.dead.containsValue(p.getName())) {
                    for (Map.Entry<Location, String> en : g.dead.entrySet()) {
                        if (en.getValue().equals(p.getName())) {
                            event.setRespawnLocation(en.getKey().add(0, 1, 0));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage("§8§l[§4§l-§8§l]§e§l " + p.getName());
        if (Arkama.hidden.contains(p.getName())) {
            Arkama.hidden.remove(p.getName());
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            for (Player h : Bukkit.getOnlinePlayers()) {
                if (h != null) {
                    h.showPlayer(ArkamaCore.instance, p);
                }
            }
        }
        for (String pa : Arkama.hidden) {
            p.showPlayer(ArkamaCore.instance, Bukkit.getPlayer(pa));
        }
        if (!Murder.findGame(p.getName()).equals("")) {
            Murder.games.get(Murder.findGame(p.getName())).quit(p.getName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!grade.containsKey(p.getUniqueId())) {
            Arkama.setGrade(p, Grade.Grades.JOUEUR);
        }
        e.setJoinMessage("§8§l[§a§l+§8§l]§e§l " + grade.get(p.getUniqueId()).grd.prefix + p.getName());
        p.setPlayerListName(grade.get(p.getUniqueId()).grd.prefix + "§r§e§l" + p.getName() + "§r");
        p.sendActionBar("§eBienvenue sur Arkama");
        //p.sendTitle("§eArkama", "§6amuse toi bien ! §e" + p.getDisplayName(), 50, 50, 50);
        p.getInventory().clear();
        p.getInventory().setItem(4, menu());
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.getWorld().getName().equals("hub")) {
            e.getWorld().setThundering(false);
            e.getWorld().setStorm(false);
            e.getWorld().setWeatherDuration(-1);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player p = event.getPlayer();
        Block b = event.getClickedBlock();
        if (b != null && b.getType() != Material.ENCHANTING_TABLE && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Inventory i = p.getOpenInventory().getTopInventory();
            if (i instanceof EnchantingInventory) {
                //nic
            }
        }
        ItemStack it = event.getItem();
        if (it == null || it.getType() == Material.AIR) return;
        if (it.getType() == Material.COMPASS && it.hasItemMeta() && it.getItemMeta().getDisplayName().equals("§6Menu")) {
            p.openInventory(main.main());
        }
        if (it.getType() == Material.BLUE_BANNER && it.hasItemMeta() && it.getItemMeta().getDisplayName().equals("§eCapture The Flag")) {
            p.openInventory(CTF.menuctf());
        }
        if (it.getType() == Material.NETHER_STAR && it.hasItemMeta() && it.getItemMeta().getLore().equals(Arrays.asList("§eretour hub", "§e§lArkama"))) {
            p.getInventory().clear();
            p.getInventory().setItem(4, menu());
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        if (current == null) return;
        if (event.getView().getTitle().equalsIgnoreCase("§e§lArka§c§lm§e§la")) {
            event.setCancelled(true);
            if (current.getType() == Material.BRICK) {
                player.closeInventory();
            }
            if (current.getType() == Material.NETHER_STAR) {
                player.closeInventory();
            }
            if (current.getType() == Material.DARK_OAK_DOOR) {
                player.closeInventory();
            }
            if (current.getType() == Material.DIAMOND_SWORD) {
            }
            if (current.getType() == Material.ENDER_CHEST) {
                player.closeInventory();
            }
            if (current.getType() == Material.APPLE) {
                Location survie = new Location(Bukkit.getWorld("survie"), -495, 67, 802, 180, 0);
                player.teleport(survie);
            }

        } else if (event.getView().getTitle().equalsIgnoreCase("§lMurder !")) {
            event.setCancelled(true);
            if (current.getType() == Material.IRON_SWORD) {
                Inventory in = Bukkit.createInventory(null, 54, "§eMurder !");
                int i = 10;
                for (Map.Entry<String, Game> en : Murder.games.entrySet()) {
                    in.setItem(i, ArkamaCore.getItem(Material.IRON_SWORD, "Game \"" + en.getKey() + "\"", "Joueurs : " + en.getValue().size() + "/6", "Map : " + en.getValue().arena));
                    i++;
                    if (i == 42) {
                        //TODO next page ?
                        break;
                    }
                    if (i + 1 % 9 == 0) {
                        i = i + 2;
                    }
                }
                player.openInventory(in);
            } else if (current.getType() == Material.IRON_PICKAXE) {
                Murder.create.put(player.getName(), new Game("DEFAULT", "DEFAULT"));
                Murder.openCreateInv(player);
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("§cMurder !")) {
            event.setCancelled(true);
            if (current.getType() == Material.OAK_SIGN) {
                player.closeInventory();
                player.sendMessage("Entrez le nom de la partie :");
                player.addScoreboardTag("edit");
            } else if (current.getType() == Material.RED_BED) {
                Inventory in = Bukkit.createInventory(null, 54, "§4Murder !");
                int i = 10;
                for (String en : Murder.arenas.keySet()) {
                    in.setItem(i, ArkamaCore.getItem(Material.IRON_SWORD, "Arène \"" + en + "\""));
                    i++;
                    if (i == 42) {
                        //TODO next page ?
                        break;
                    }
                    if (i + 1 % 9 == 0) {
                        i = i + 2;
                    }
                }
                player.openInventory(in);
            } else if (current.getType() == Material.LIME_TERRACOTTA) {
                //créer la partie !
                Game g = Murder.create.remove(player.getName());
                Murder.games.put(g.name, g);
                player.closeInventory();
                player.sendMessage("Partie crée avec succès !");
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("§eMurder !")) {
            event.setCancelled(true);
            if (current.getType() == Material.IRON_SWORD) {
                Game g = Murder.games.get(current.getItemMeta().getDisplayName().substring(6, current.getItemMeta().getDisplayName().length() - 1));
                if (g == null) {
                    player.sendMessage("Game : " + current.getItemMeta().getDisplayName().substring(6, current.getItemMeta().getDisplayName().length() - 1) +
                            " non trouvée !! veuillez reporter cette erreur a un administrateur");
                    return;
                }
                boolean success = g.join(player.getName());
                player.closeInventory();
                if (!success) {
                    player.sendMessage("Vous êtes déja dans cette partie !");
                } else {
                    player.sendMessage("Vous avez bien rejoint cette partie !");
                }
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("§4Murder !")) {
            event.setCancelled(true);
            if (current.getType() == Material.IRON_SWORD) {
                String arena = current.getItemMeta().getDisplayName().substring(7, current.getItemMeta().getDisplayName().length() - 1);
                if (Murder.arenas.get(arena) == null) {
                    System.out.println("LE JOUEUR " + player.getName() + " A TENTE DE METTRE LE NOM INVALIDE " + arena);
                    return;
                }
                Murder.create.get(player.getName()).arena = arena;
                Murder.openCreateInv(player);
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("§eArka§cC§eTF")) {

        }
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent e) {
        Entity en = e.getEntity();
        Item i = e.getItem();
        if (en instanceof Player) {
            Game g = Murder.games.get(Murder.findGame(en.getName()));
            if (g != null) {
                if ((g.murder.equals(en.getName()) || g.detect.contains(en.getName())) && i.getItemStack().isSimilar(Game.GUN)) {
                    e.setCancelled(true);
                } else if (i.getItemStack().isSimilar(Game.GUN)) {
                    g.detect.add(en.getName());
                    ((Player) en).getInventory().setItem(15, new ItemStack(Material.ARROW));
                    ((Player) en).updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material roto = block.getType();
        if (roto == Material.DIAMOND_ORE) {
            Bukkit.broadcastMessage(player.getDisplayName() + " a trouvé du diamant.");
        }

    }

    @EventHandler
    public void onHarm(EntityDamageEvent e) {
        EntityType t = e.getEntityType();
        if (t == EntityType.PLAYER && e.getEntity().getWorld().getName().equals("hub")) {
            e.setCancelled(true);
        }

    }


    @EventHandler
    public void onSign(SignChangeEvent e) {
        e.setLine(0, e.getLine(0).replace('&', '§'));
        e.setLine(1, e.getLine(1).replace('&', '§'));
        e.setLine(2, e.getLine(2).replace('&', '§'));
        e.setLine(3, e.getLine(3).replace('&', '§'));
    }

    @EventHandler
    public void onNonLivingDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LargeFireball && e.getEntity().getCustomName().equals("Boule de feu")) {
            double d = 1 + (3 - 1) * new Random().nextDouble();
            e.setDamage(d);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
         if (e.getPlayer().getLocation().getWorld().getName().equals("jump")) {
             Location l = e.getPlayer().getLocation();
             Block b = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ()).getBlock();
             if (b.getType() == Material.GREEN_GLAZED_TERRACOTTA) {
                 e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 6, true, false));
             }
             if (b.getType() == Material.LIGHT_BLUE_GLAZED_TERRACOTTA) {
                 Vector v = e.getPlayer().getVelocity();
                 e.getPlayer().setVelocity(new Vector(v.getX(), 2.5D, v.getZ()));
                 //e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,1,10,true,false));
             }
        }
    }
}


