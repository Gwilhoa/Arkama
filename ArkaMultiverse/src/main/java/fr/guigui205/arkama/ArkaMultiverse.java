package fr.guigui205.arkama;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ArkaMultiverse extends JavaPlugin {
    public static ArrayList<String> worlds = new ArrayList<>();

    public static void saveWorld() {
        if (new File("Arkama/worlds.json").exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/worlds.json")));
                ArkamaCore.gson.toJson(worlds, new TypeToken<ArrayList<String>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fichier inexistant !");
            try {
                new File("Arkama/worlds.json").createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/worlds.json")));
                ArkamaCore.gson.toJson(worlds, new TypeToken<ArrayList<String>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        if (new File("Arkama/worlds.json").exists()) {
            try {
                worlds = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/worlds.json"))), new TypeToken<ArrayList<String>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/worlds.json").createNewFile();
                getLogger().warning("[worlds] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (worlds == null) {
            worlds = new ArrayList<>();
        }
        getCommand("mv").setExecutor(new CommandWorld());
        getCommand("multiverse").setExecutor(new CommandWorld());
        getServer().getPluginManager().registerEvents(new WorldsEvent(), this);
        getLogger().warning("[ArkaMultiverse] chargé");
    }

    public static class WorldsHandler {

        public static World createWorld(String name) {
            return WorldCreator.name(name).createWorld();
        }

        public static World createWorld(String name, World.Environment environment) {
            return WorldCreator.name(name).environment(environment).createWorld();
        }

        public static World createWorld(String name, World.Environment environment, long seed) {
            return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
        }

        public static World createWorld(String name, World.Environment environment, String generator) {
            return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
        }

        public static World createWorld(String name, World.Environment environment, ChunkGenerator generator) {
            return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
        }

        public static World createWorld(String name, World.Environment environment, long seed, String generator) {
            return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
        }

        public static World createWorld(String name, World.Environment environment, long seed, ChunkGenerator generator) {
            return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
        }

        public static void saveWorld(String name) {
            World w = Bukkit.getWorld(name);
            if (w == null) {
                return;
            }
            if (!Bukkit.unloadWorld(name, true)) {
                return;
            }
            File world = w.getWorldFolder();
            Path sourcePath = world.toPath();
            File target = new File(world.getParentFile(), world.getName() + "_backup");
            target.mkdirs();
            Path targetPath = target.toPath();
            try {
                Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(final Path dir,
                                                             final BasicFileAttributes attrs) throws IOException {
                        Files.createDirectories(targetPath.resolve(sourcePath
                                .relativize(dir)));
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(final Path file,
                                                     final BasicFileAttributes attrs) throws IOException {
                        Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                });
                createWorld(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void restoreWorld(String name) {
            World w = Bukkit.getWorld(name);
            if (w == null) {
                return;
            }
            if (!Bukkit.unloadWorld(name, false)) {
                return;
            }
            File world = w.getWorldFolder();
            Path targetPath = world.toPath();
            File target = new File(world.getParentFile(), world.getName() + "_backup");
            Path sourcePath = target.toPath();
            try {
                Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(final Path dir,
                                                             final BasicFileAttributes attrs) throws IOException {
                        Files.createDirectories(targetPath.resolve(sourcePath
                                .relativize(dir)));
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(final Path file,
                                                     final BasicFileAttributes attrs) throws IOException {
                        Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                });
                createWorld(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public class CommandWorld implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String msg, String[] args) {
            if (command.getName().equals("mv") || command.getName().equals("multiverse")) {
                if (args.length >= 1) {
                    String subcom = args[0];
                    switch (subcom) {
                        case "load":
                            if (commandSender instanceof Player && args.length == 2) {
                                Bukkit.unloadWorld(args[1], true);
                            }
                        case "save":
                            if (commandSender instanceof Player) {
                                WorldsHandler.saveWorld(((Player) commandSender).getWorld().getName());
                            }
                            break;
                        case "tp":
                            if (commandSender instanceof Player && args.length == 2) {
                                ArkamaTeleport.teleport(((Player) commandSender), Bukkit.getWorld(args[1]).getSpawnLocation(), true);
                            }
                            break;
                        case "create":
                            if (args.length == 2) {
                                WorldsHandler.createWorld(args[1], World.Environment.NORMAL);
                                worlds.add(args[1]);
                                saveWorld();
                                commandSender.sendMessage("Monde crée");
                                return true;
                            } else if (args.length == 3) {
                                try {
                                    WorldsHandler.createWorld(args[1], World.Environment.valueOf(args[2].toUpperCase()));
                                    worlds.add(args[1]);
                                    saveWorld();
                                    commandSender.sendMessage("Monde crée");
                                    return true;
                                } catch (IllegalArgumentException ignored) {
                                    commandSender.sendMessage("Erreur ! environnement inconnu");
                                    return false;
                                }

                            } else if (args.length == 4) {
                                try {
                                    WorldsHandler.createWorld(args[1], World.Environment.valueOf(args[2].toUpperCase()), args[3]);
                                    worlds.add(args[1]);
                                    saveWorld();
                                    commandSender.sendMessage("Monde crée");
                                    return true;
                                } catch (IllegalArgumentException ignored) {
                                    commandSender.sendMessage("Erreur ! environnement/générateur inconnu");
                                    return false;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return false;
        }
    }

    class WorldsEvent implements Listener {
        @EventHandler
        public void onLoad(ServerLoadEvent event) {
            if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
                for (String name : worlds) {
                    WorldsHandler.createWorld(name);
                }
            }
        }
    }
}