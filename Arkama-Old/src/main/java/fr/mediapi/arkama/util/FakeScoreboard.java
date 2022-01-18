package fr.mediapi.arkama.util;

import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class FakeScoreboard {

    public Scoreboard underScore;
    public String title;
    public ArrayList<String> lines;

    public FakeScoreboard(Scoreboard underScore) {
        this.underScore = underScore;
        this.title = underScore.getObjectives().iterator().next().getName();

    }

    public void update() {

    }
}
