package org.dlds.mobbattle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Criteria;
public class TimerHandler {
    int updateTimeMin = 30;
    private int localTime = updateTimeMin * 60;
    final String name_string = "mobBattle";
    static final Criteria criteria = Criteria.DUMMY;
    final String display_name_string = "Mob Battle";
//
    void setupScoreboardDisplay() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        final Scoreboard scoreboard = manager.getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective(name_string, criteria, display_name_string);
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
