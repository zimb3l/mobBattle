package org.dlds.mobbattle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class TimerHandler {
    static final Criteria criteria = Criteria.DUMMY;
    final String name_string = "mobBattle";
    final String display_name_string = "Mob Battle";
    int updateTimeMin = 30;
    net.kyori.adventure.text.Component display_name = net.kyori.adventure.text.Component.text(display_name_string);
    private int localTime = updateTimeMin * 60;

    void setupScoreboardDisplay() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name_string, criteria, display_name);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final int totalTimeTilUpdate = 60 * updateTimeMin;
        localTime = totalTimeTilUpdate; // Initialize localTime

        new BukkitRunnable() {
            @Override
            public void run() {
                int minutes = localTime / 60;
                int seconds = localTime % 60;

                String timeString = String.format("Time: %02d:%02d", minutes, seconds);

                objective.getScore(timeString).setScore(1); // Update the scoreboard

                if (localTime <= 0) {
                    localTime = totalTimeTilUpdate; // Reset the timer
                    // TODO PLACEHOLDER -- HERE GOES WHAT HAPPENS WHEN TIMER REACHES ZERO
                } else {
                    localTime--; // Decrease the timer by 1 second
                }

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.setScoreboard(scoreboard);
                }
            }
        }.runTaskTimer((Plugin) this, 0, 20); // Update every second
    }

}
