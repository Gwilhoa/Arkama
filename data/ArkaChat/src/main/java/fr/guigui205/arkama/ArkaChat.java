package fr.guigui205.arkama;

import com.google.common.reflect.TypeToken;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static fr.guigui205.arkama.ArkamaCore.gson;
import static fr.guigui205.arkama.ArkamaCore.sendMessageToDiscord;


public class ArkaChat extends JavaPlugin {
    private static final String PREFIX = "§fArkaChat §9§l>> §e";
    static final HashMap<UUID, MessageState> chats = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("setpseudo").setExecutor(this);
        getCommand("setchat").setExecutor(this);


        if (new File("Arkama/chat.json").exists()) {
            try {
                ChatConfig.chatConfig = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("Arkama/chat.json"))), new TypeToken<HashMap<UUID, ChatConfig>>() {
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
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s).append(" ");
        }
        if (command.getName().equalsIgnoreCase("setpseudo")) {
            ChatConfig config = ChatConfig.chatConfig.computeIfAbsent(p.getUniqueId(), k -> new ChatConfig("&7" + p.getName(), " &r"));
            config.prefix = sb.toString().trim();
            config.save();
            sender.sendMessage(PREFIX + " Pseudonyme set to " + sb);
            p.playerListName(Component.text(config.prefix.replace("&", "§")));
        }
        if (command.getName().equalsIgnoreCase("setchat")) {
            ChatConfig config = ChatConfig.chatConfig.computeIfAbsent(p.getUniqueId(), k -> new ChatConfig(" &7" + p.getName(), " &r"));
            config.suffix = sb.toString().replace("&", "§").trim();
            config.save();
            sender.sendMessage(PREFIX + " chat set to " + sb);
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
        String messageContent = PlainTextComponentSerializer.plainText().serialize(message);

        if (config == null) {
            config = new ChatConfig("&7" + source.getName(), "&r");
            ChatConfig.chatConfig.put(source.getPlayer().getUniqueId(), config);
            config.save();
        }

        String content = config.prefix.replace("&", "§") + " " + config.suffix.replace("&", "§") + " " + messageContent.replace("&", "§");
        System.out.println("messageContent: " + content);

        if (!ArkaChat.chats.containsKey(source.getUniqueId())) {
            ArkaChat.chats.put(source.getUniqueId(), new MessageState(false, ""));
        }

        MessageState state = ArkaChat.chats.get(source.getUniqueId());

        // Vérification si le message est nouveau ou a changé
        if (!state.sent && !state.message.equals(content)) {
            sendMessageToDiscord(content, null);
            ArkaChat.chats.put(source.getUniqueId(), new MessageState(true, content));

            // Réinitialiser après 1 seconde
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1000);  // Attendre 1 seconde
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                // Réinitialiser le flag après le délai
                ArkaChat.chats.put(source.getUniqueId(), new MessageState(false, ""));
            });
        } else if (state.message.equals(content)) {
            // Message déjà envoyé récemment et identique, afficher un message mais ne pas envoyer
            System.out.println("Message déjà envoyé récemment et identique.");
        } else {
            // Message différent, rien à faire
            System.out.println("Message envoyé avec succès.");
        }

        return Component.text(content);
    }
}

// Classe pour garder l'état du message et son envoi
class MessageState {
    boolean sent;
    String message;

    public MessageState(boolean sent, String message) {
        this.sent = sent;
        this.message = message;
    }
}