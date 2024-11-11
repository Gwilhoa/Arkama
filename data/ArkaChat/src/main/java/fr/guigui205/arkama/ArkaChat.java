package fr.guigui205.arkama;

import com.google.gson.reflect.TypeToken;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;


public class ArkaChat extends JavaPlugin {
    private static final String PREFIX = "§fArkaChat §9§l>> §e";

    @Override
    public void onEnable(){
        getCommand("setprefix").setExecutor(this);
        getCommand("setsuffix").setExecutor(this);


        if (new File("Arkama/chat.json").exists()) {
            try {
                ChatConfig.chatConfig = ArkamaCore.gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/chat.json"))), new TypeToken<HashMap<UUID, ChatConfig>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new File("Arkama/chat.json").createNewFile();
                getLogger().warning("[chat] creation du fichier");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (ChatConfig.chatConfig == null) {
            ChatConfig.chatConfig = new HashMap<>();
        }

        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getLogger().warning("[ArkaChat] chargé");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("setprefix")) {
            if (args.length == 1) {
                ChatConfig config = ChatConfig.chatConfig.computeIfAbsent(p.getUniqueId(), k -> new ChatConfig(" §7", " §r"));
                config.prefix = args[0].replace("&", "§");
                config.save();
                sender.sendMessage(PREFIX + " Prefix set to " + args[0]);
            }
        }
        if (command.getName().equalsIgnoreCase("setsuffix")) {
            if (args.length == 1) {
                ChatConfig config = ChatConfig.chatConfig.computeIfAbsent(p.getUniqueId(), k -> new ChatConfig(" §7", " §r"));
                config.suffix = args[0].replace("&", "§");
                config.save();
                sender.sendMessage(PREFIX + " Suffix set to " + args[0]);
            }
        }
        return true;
    }
}

class ChatEvent implements Listener, ChatRenderer {


    @EventHandler
    public void onChat(AsyncChatEvent e) {
        e.renderer(this);
    }


    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        ChatConfig config = ChatConfig.chatConfig.get(source.getPlayer().getUniqueId());
        if (config == null) {
            config = new ChatConfig("§7", "§r");
            ChatConfig.chatConfig.put(source.getPlayer().getUniqueId(), config);
            config.save();
        }
        return Component.text(config.prefix + source.getName()+" §9>> "+config.suffix).append(message);
    }
}


class ChatConfig {
    public String prefix;
    public String suffix;
    public static HashMap<UUID, ChatConfig> chatConfig = new HashMap<>();

    public ChatConfig(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void save()
    {
        if (new File("Arkama/chat.json").exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/chat.json")));
                ArkamaCore.gson.toJson(chatConfig, new TypeToken<HashMap<UUID, ChatConfig>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fichier inexistant !");
            try {
                new File("Arkama/chat.json").createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/chat.json")));
                ArkamaCore.gson.toJson(chatConfig, new TypeToken<HashMap<UUID, ChatConfig>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

