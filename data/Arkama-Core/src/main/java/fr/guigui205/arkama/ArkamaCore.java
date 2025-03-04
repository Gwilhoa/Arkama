package fr.guigui205.arkama;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fi.iki.elonen.NanoHTTPD;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ArkamaCore extends JavaPlugin {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static ArkamaCore instance;
    private HttpServer server;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().warning("[ArkaCore] chargé");

        // Démarrer le serveur HTTP
        this.server = new HttpServer(5001);
        try {
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            getLogger().info("Serveur HTTP démarré sur le port 5001");
        } catch (Exception e) {
            getLogger().severe("Impossible de démarrer le serveur HTTP");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (server != null) {
            server.stop();
            getLogger().info("Serveur HTTP arrêté");
        }
    }

    public static void Broadcast(String msg) {
        Collection<? extends Player> p = Bukkit.getOnlinePlayers();
        for (Player player : p) {
            player.sendMessage(msg);
        }
    }

    public static <K, V> List<K> getKeysFromValue(Map<K, V> hm, V value) {
        List<K> list = new ArrayList<>();
        for (K o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                list.add(o);
            }
        }
        return list;
    }

    public static ItemStack getItem(Material material, String customname, String... Lore) {
        ItemStack it1 = new ItemStack(material, 1);
        ItemMeta itM = it1.getItemMeta();
        if (customname != null) itM.setDisplayName(customname);
        itM.setLore(Arrays.asList(Lore));
        itM.addEnchant(Enchantment.LOOTING, 1, true);
        itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        it1.setItemMeta(itM);
        return it1;
    }

    private static class HttpServer extends NanoHTTPD {
        public HttpServer(int port) {
            super(port);
        }

        @Override
        public Response serve(IHTTPSession session) {
            try {
                if (Method.POST.equals(session.getMethod()) && "/chat".equals(session.getUri())) {
                    Map<String, String> body = new HashMap<>();
                    session.parseBody(body);

                    String requestBody = body.get("postData");
                    JsonObject json = gson.fromJson(requestBody, JsonObject.class);

                    String message = json.has("message") ? json.get("message").getAsString() : "Aucun message";
                    String author = json.has("author") ? json.get("author").getAsString() : "Anonyme";

                    ArkamaCore.Broadcast("§9" + author + " \uD83D\uDCAC " + message);

                    return newFixedLengthResponse("Message reçu : " + message);
                }

                if (Method.GET.equals(session.getMethod()) && "/players".equals(session.getUri())) {
                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();

                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("players", players.size());

                    return newFixedLengthResponse(Response.Status.OK, "application/json", jsonResponse.toString());
                }

                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Route non trouvée");
            } catch (JsonSyntaxException e) {
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "Format JSON invalide");
            } catch (Exception e) {
                e.printStackTrace();
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Erreur interne");
            }
        }

    }

    public static void sendMessageToDiscord(String message, final String channel1) {
        final String msg = cleanString(message);
        final String channel = channel1 == null ? "1342417055386960004" : channel1;
        System.out.println("sendMessageToDiscord appelée avec : " + msg);

        CompletableFuture.runAsync(() -> {
            String apiUrl = "https://api.bitume2000.fr/api/discord/send_message";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JsonObject payload = new JsonObject();
                payload.addProperty("content", msg);
                payload.addProperty("channel_id", channel);
                String jsonPayload = gson.toJson(payload);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static String cleanString(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("§.", "").replaceAll("\\s+", " ").trim();
    }
}

class GeneralEvent implements Listener {
}
