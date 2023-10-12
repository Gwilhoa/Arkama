package fr.mediapi.arkama.util;

import fr.guigui205.arkama.ArkamaCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class ScoreboardCreator {

    public static Scoreboard empty;

    static {
        empty = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    }


    public static BukkitTask startUpdate() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(ArkamaCore.instance, new ScoreboardUpdater(), 1L, 1L);
    }

    public static void setNewScoreboard(Player p, FakeScoreboard score) {
        p.setScoreboard(score.underScore);
        ScoreboardUpdater.addScoreboard(score);
    }

    public static void setScoreboard(Player p, FakeScoreboard score) {
        p.setScoreboard(score.underScore);
    }

    public static void removeScoreboard(Player p) {
        p.setScoreboard(empty);
    }

    public static ScoreboardBuilder createScoreboard() {
        return new ScoreboardBuilder();
    }

    public static ScoreboardBuilder createScoreboard(Scoreboard sc) {
        ScoreboardBuilder sb = new ScoreboardBuilder().setTitle(sc.getObjectives().iterator().next().getName());
        for (Team t : sc.getTeams()) {
            sb.addLine(t.getPrefix() + t.getSuffix());
        }
        return sb;
    }


    static class ScoreboardBuilder {
        private final ArrayList<String> objs = new ArrayList<>();
        private final Scoreboard sc;
        private String title;

        public ScoreboardBuilder() {
            sc = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        private static ArrayList<String> convertIntoPieces(String s, int allowed_line_size) {
            ArrayList<String> parts = new ArrayList<>();

            if (ChatColor.stripColor(s).length() > allowed_line_size) {
                parts.add(s.substring(0, allowed_line_size));

                String s2 = s.substring(allowed_line_size);
                if (s2.length() > allowed_line_size)
                    s2 = s2.substring(0, allowed_line_size);
                parts.add(s2);
            } else {
                parts.add(s);
                parts.add("");
            }

            return parts;
        }

        public FakeScoreboard create() {
            if (sc.getObjectives().size() == 0) {
                throw new IllegalArgumentException("You need to set a title");
            }
            int i = 0;
            int score = objs.size();
            for (String s : objs) {
                Team t = sc.registerNewTeam(i + ""); // Create the first team
                t.addEntry(ChatColor.values()[i] + ""); // Assign the team with a color
                ArrayList<String> ar = convertIntoPieces(s, 16);
                t.setPrefix(ar.get(0));
                t.setSuffix(ar.get(1));
                sc.getObjectives().iterator().next().getScore(ChatColor.values()[i] + "").setScore(score); // Set the socre number

                score--;
                i++;
            }
            return new FakeScoreboard(sc);
        }

        /**
         * Sets a line to a scoreboard at the index set (starts at 0)
         * (NOTE : empty lines are NOT added)
         *
         * @param line the line
         * @param s    Objective name
         * @return the builder object
         */
        public ScoreboardBuilder setLine(int line, String s) {
            if (s.length() >= 32) {
                throw new IllegalArgumentException("name is over the 32 char limit");
            }
            objs.set(line, s);
            return this;
        }

        /**
         * Adds a line to a scoreboard, the first one you put is on the bottom
         *
         * @param s Objective name
         * @return the builder object
         */
        public ScoreboardBuilder addLine(String s) {
            if (s.length() >= 32) {
                throw new IllegalArgumentException("name is over the 32 char limit");
            }
            objs.add(s);
            return this;
        }

        public ScoreboardBuilder setTitle(String title) {
            sc.getObjective(this.title).unregister();
            this.title = title;
            sc.registerNewObjective(title, "dummy", title).setDisplaySlot(DisplaySlot.SIDEBAR);
            return this;
        }
    }
}
