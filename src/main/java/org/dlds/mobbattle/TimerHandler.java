package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.dlds.mobbattle.objects.ClockInventory;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;

import java.util.*;

public class TimerHandler {
    static final Criteria criteria = Criteria.DUMMY;
    final String name_string = "mobBattle";
    final String display_name_string = "Mob Battle";
    private final JavaPlugin plugin;
    int updateTimeMin = 1;
    private int localTime = updateTimeMin * 60;
    private int elapsedTime = 0;
    private ClockInventoryRepository clockInventoryRepository;
    private Scoreboard scoreboard;
    private Objective objective;

    public TimerHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        setupScoreboard();
    }

    private void setupScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(name_string, criteria, Component.text(display_name_string));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        updateScoreboard();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    void updateScoreboard() {
        int hours = elapsedTime / 3600;
        int minutes = (elapsedTime % 3600) / 60;
        int seconds = elapsedTime % 60;
        String timeString = String.format("Time : %02d:%02d:%02d", hours, minutes, seconds);

        objective.displayName(Component.text(timeString));
    }

    void updateScoreboardWithRankings() {
        updateScoreboard();

        List<Map.Entry<UUID, Integer>> playerPoints = new ArrayList<>();
        for (Map.Entry<UUID, ClockInventory> entry : clockInventoryRepository.getInventoryMap().entrySet()) {
            UUID playerId = entry.getKey();
            ClockInventory inventory = entry.getValue();
            int points = inventory.getCurrentPoints();
            playerPoints.add(new AbstractMap.SimpleEntry<>(playerId, points));
        }
        playerPoints.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        scoreboard.getEntries().forEach(entry -> {
            if (entry.startsWith("Position ")) {
                scoreboard.resetScores(entry);
            }
        });

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : playerPoints) {
            UUID playerId = entry.getKey();
            String playerName = Bukkit.getOfflinePlayer(playerId).getName();
            String rankEntry = String.format("Position %d : %s", rank, playerName);
            Score score = objective.getScore(rankEntry);

            score.setScore(0);
            rank++;
        }
    }

    void setupScoreboardDisplay() {
        clockInventoryRepository = ClockInventoryRepository.getInstance();

        final int totalTimeTilUpdate = 60 * updateTimeMin;
        localTime = totalTimeTilUpdate;

        new BukkitRunnable() {
            @Override
            public void run() {
                localTime--;
                elapsedTime++;

                if (localTime <= 0) {
                    localTime = totalTimeTilUpdate;

                    updateScoreboardWithRankings();
                } else {
                    updateScoreboard();
                }

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.setScoreboard(scoreboard);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
