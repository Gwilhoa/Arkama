package fr.mediapi.arkama.util;

import fr.guigui205.arkama.ArkamaCore;
import fr.mediapi.arkama.Arkama;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class XpStart {

    public static HashMap<String, Integer> tid = new HashMap<>();
    public static HashMap<String, Integer> cid = new HashMap<>();

    public static void xpStart(String[] plays, int secs) {

        for (String pn : plays) {
            xpStart(Bukkit.getPlayer(pn), secs);
        }
    }

    public static void xpStart(ArrayList<String> plays, int secs) {
        for (String pn : plays) {
            xpStart(Bukkit.getPlayer(pn), secs);
        }
    }

    public static void xpStart(Player p, int secs) {
        if (tid.get(p.getName()) != null && tid.get(p.getName()) != 0) {
            Bukkit.getScheduler().cancelTask(tid.get(p.getName()));
            tid.remove(p.getName());
            Bukkit.getScheduler().cancelTask(cid.get(p.getName()));
            cid.remove(p.getName());
        }
        p.setExp(1);
        p.setLevel(secs);
        AtomicInteger i = new AtomicInteger();
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(ArkamaCore.instance, () -> {
            if (p.getExp() - i.get() / 180.0f <= 0) {
                p.setExp(0);
                if (p.getLevel() <= 1) {
                    p.setLevel(0);
                } else {
                    p.setLevel(p.getLevel() - 1);
                    p.setExp(1);
                    i.set(0);
                }
            } else {
                p.setExp(p.getExp() - i.get() / 180.0f);
                i.getAndIncrement();
            }
        }, 0, 1);
        tid.put(p.getName(), task.getTaskId());
        cid.put(p.getName(), Bukkit.getScheduler().runTaskLater(ArkamaCore.instance, task::cancel, secs * 20).getTaskId());
    }
}
