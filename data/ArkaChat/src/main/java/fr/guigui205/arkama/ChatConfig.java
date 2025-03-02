package fr.guigui205.arkama;


import com.google.common.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

import static fr.guigui205.arkama.ArkamaCore.gson;

public class ChatConfig {
    public static HashMap<UUID, ChatConfig> chatConfig = new HashMap<>();
    public String prefix;
    public String suffix;

    public ChatConfig(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void save() {
        if (new File("Arkama/chat.json").exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Arkama/chat.json")));
                gson.toJson(chatConfig, new TypeToken<HashMap<UUID, ChatConfig>>() {
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
                gson.toJson(chatConfig, new TypeToken<HashMap<UUID, ChatConfig>>() {
                }.getType(), bw);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
