package fr.mediapi.arkama.util;

import java.util.ArrayList;

public class ScoreboardUpdater implements Runnable {
    private static ArrayList<FakeScoreboard> sbs;

    public static void addScoreboard(FakeScoreboard s) {
        sbs.add(s);
    }

    @Override
    public void run() {
        for (FakeScoreboard s : sbs) {
            s.update();
        }
    }
}
