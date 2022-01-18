package fr.mediapi.arkama.murder.commandes;

import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.Arkama;
import fr.mediapi.arkama.murder.Arena;
import fr.mediapi.arkama.murder.Game;
import fr.mediapi.arkama.murder.Murder;
import fr.mediapi.arkama.util.SerializableVector;
import fr.mediapi.arkama.util.XpStart;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandMain implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if (command.getName().equals(Murder.subCommand)) {
            if (args.length >= 1) {
                String name = args[0];
                final String nsend = sender.getName();
                switch (name) {
                    case "menu":
                        if (sender instanceof Player) {
                            Inventory inv = Bukkit.createInventory(null, 54, "§lMurder !");
                            inv.setItem(20, ArkamaCore.getItem(Material.IRON_SWORD, "Trouver une partie", "§4Chercher une partie de murder"));
                            inv.setItem(24, ArkamaCore.getItem(Material.IRON_PICKAXE, "Créer une partie", "§4Créer une partie de murder !"));
                            ((Player) sender).openInventory(inv);
                        }
                        break;
                    case "start":
                        String game = Murder.findGame(nsend);
                        if (game.equals("")) {
                            sender.sendMessage("§b§lArka§4Murder §6>> §cvous n'êtes pas dans une partie !");
                            return false;
                        }
                        Game party = Murder.games.get(game);
                        if (!sender.isOp()) {
                            sender.sendMessage("§b§lArka§4Murder §6>> Vous n'avez pas le droit de faire ca !");
                        } else if (party.size() >= 2) {
                            Bukkit.getScheduler().cancelTask(party.task);
                            Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, () -> {
                                boolean success = party.start();
                                if (!success) {
                                    sender.sendMessage("§b§lArka§4Murder §6>> Impossible de démarrer la map ! il n'y a pas assez de spawns");
                                }
                            }, 200);
                            XpStart.xpStart(party.copyPlayers(), 10);
                        } else {
                            sender.sendMessage("§b§lArka§4Murder §6>> §cil faut au moins deux joueurs pour commencer la partie !");
                        }
                        break;
                    case "join":
                        if (args.length == 2) {
                            Game games = Murder.games.get(args[1]);
                            if (sender instanceof Player && games != null) {
                                boolean success = games.join(nsend);
                                if (success) {
                                    sender.sendMessage("§b§lArka§4Murder §6>> §aVous venez de rejoindre la partie !");
                                } else {
                                    sender.sendMessage("§b§lArka§4Murder §6>> §cTu est déja dans cette partie !");
                                }
                            } else {
                                sender.sendMessage("§b§lArka§4Murder §6>> §cLa partie que vous essayez de joindre n'existe pas !");
                            }
                        }
                        break;
                    case "quit":

                        if (sender instanceof Player && !Murder.findGame(nsend).equals("")) {
                            Murder.games.get(Murder.findGame(nsend)).quit(nsend);
                            sender.sendMessage("Tu as été enlevé de ta partie !");
                        } else {
                            sender.sendMessage("tu n'est pas dans une partie !");
                        }
                        break;
                    case "create":
                        if (args.length == 3) {
                            if (Murder.arenas.get(args[2]) == null) {
                                sender.sendMessage("§b§lArka§4Murder §6>> §ccette arène n'existe pas");
                                return false;
                            } else {
                                Game g = new Game(args[1], args[2]);
                                Murder.games.put(args[1], g);
                                sender.sendMessage("§b§lArka§4Murder §6>> §apartie crée avec succès !");
                            }
                        }
                        break;
                    case "list":
                        if (!Murder.findGame(nsend).equals("")) {
                            Game g = Murder.games.get(Murder.findGame(nsend));
                            sender.sendMessage("§b§lArka§4Murder §6>> §2partie " + g.name + " :");
                            for (String p : g.copyPlayers()) {
                                sender.sendMessage("   §e§l" + p);
                            }
                        } else {
                            sender.sendMessage("§b§lArka§4Murder §6>> &2Voici la liste des parties actuellement :");
                            for (String g : Murder.games.keySet()) {
                                sender.sendMessage("   " + g);
                            }
                        }
                        break;
                    case "restart":
                        if (Murder.recent.containsKey(nsend)) {
                            if (!Murder.findGame(Murder.recent.get(nsend).get(0)).equals("")) {
                                Murder.games.get(Murder.recent.get(nsend).get(0)).join(nsend);
                            } else {
                                Game g = new Game(Murder.recent.get(nsend).get(0), Murder.recent.get(nsend).get(1));
                                Murder.games.put(Murder.recent.get(nsend).get(0), g);
                                g.join(nsend);
                            }
                            sender.sendMessage("§b§lArka§4Murder §6>> §aVous venez de rejoindre la partie !");
                            Murder.recent.remove(nsend);
                        } else {
                            sender.sendMessage("Vous n'avez pas de partie récente !");
                        }
                        break;
                    case "arena":
                        if (args.length >= 2) {
                            String sub = args[1];
                            switch (sub) {
                                case "create":
                                    if (args.length == 3 && sender instanceof Player) {
                                        Arena arena = new Arena(args[2], ((Player) sender).getWorld().getName());
                                        Murder.arenas.put(args[2], arena);
                                        sender.sendMessage("§b§lArka§4Murder §6>> &aarène crée avec succès !");
                                    } else {
                                        sender.sendMessage("§b§lArka§4Murder §6>> &cmettez le nom de l'arène !");
                                    }
                                    break;
                                case "list":
                                    for (Arena arena : Murder.arenas.values()) {
                                        sender.sendMessage(arena.name);
                                    }
                                    break;
                                case "scrap":
                                    if (args.length == 3 && sender instanceof Player) {
                                        Arena arena = Murder.arenas.get(args[2]);
                                        SerializableVector v = new SerializableVector(((Player) sender).getLocation());
                                        arena.scrapSpawns.add(v);
                                        sender.sendMessage("§b§lArka§4Murder §6>> §apoint de scrap ajouté en  x :" + v.getX() + " y : " + v.getY() + " z :" + v.getZ());
                                    }
                                    break;
                                case "spawn":
                                    if (args.length == 3 && sender instanceof Player) {
                                        Arena arena = Murder.arenas.get(args[2]);
                                        SerializableVector v = new SerializableVector(((Player) sender).getLocation());
                                        arena.playerSpawns.add(v);
                                        sender.sendMessage("§b§lArka§4Murder §6>> §apoint de spawn de joueur ajouté en  x :" + v.getX() + " y : " + v.getY() + " z :" + v.getZ());
                                    }

                                    break;
                                case "listspawn":
                                    if (args.length == 3 && sender instanceof Player) {
                                        Arena arena = Murder.arenas.get(args[2]);
                                        for (SerializableVector v : arena.playerSpawns) {
                                            sender.sendMessage("§6spawn en x :" + v.getX() + " y: " + v.getY() + " z: " + v.getZ());
                                        }
                                    }
                                    break;
                                case "help":
                                    sender.sendMessage("/murder arena list : liste les arènes\n" +
                                            "/murder arena create <nom> : crée une arène\n" +
                                            "/murder arena spawn <arène> : ajoute un spawn a une arène\n" +
                                            "/murder arena listspawn <arène> : liste les spawns d'une arène\n" +
                                            "/murder arena scrap <arène> : ajoute un point de spawn de scrap (non implémenté)");
                                    break;
                            }
                        }
                        break;
                    case "help":
                        sender.sendMessage("/murder menu : acceder au menu du murder\n" +
                                "/murder arena : creer une nouvelle arène (voir \"/murder arena help\" pour plus d'informations)");
                        break;
                }
            } else {
                sender.sendMessage("§b§lArka§4Murder §6>> §csyntaxe invalide");
                sender.sendMessage("§3/murder menu : acceder au menu du murder");
                sender.sendMessage("§3/murder join <nom> rejoindre une arene");
                sender.sendMessage("§3/murder create <nom> <arène>: creer une partie");
                sender.sendMessage("§3/murder arena : creer une nouvelle arène");
            }
        }
        return false;
    }

}
