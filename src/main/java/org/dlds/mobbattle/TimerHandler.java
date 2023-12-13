package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.dlds.mobbattle.objects.ClockInventory;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;

import java.time.Duration;
import java.util.*;

public class TimerHandler {
    static final Criteria criteria = Criteria.DUMMY;
    final String name_string = "mobBattle";
    final String display_name_string = "Mob Battle";
    private final JavaPlugin plugin;
    int updateTimeMin = 30;
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

    void clearExistingTeams(int teamAmount) {
        for (int i = 1; i <= teamAmount; i++) {
            Team existingTeam = scoreboard.getTeam("rank" + i);
            if (existingTeam != null) {
                existingTeam.unregister();
            }
        }
    }

    void updateScoreboardWithRankings() {
        List<Map.Entry<UUID, Integer>> playerPoints = new ArrayList<>();
        for (Map.Entry<UUID, ClockInventory> entry : clockInventoryRepository.getInventoryMap().entrySet()) {
            UUID playerId = entry.getKey();
            ClockInventory inventory = entry.getValue();
            int points = inventory.getCurrentPoints();
            playerPoints.add(new AbstractMap.SimpleEntry<>(playerId, points));
        }
        playerPoints.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        clearExistingTeams(playerPoints.size());

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : playerPoints) {
            UUID playerId = entry.getKey();

            String playerName = Bukkit.getOfflinePlayer(playerId).getName();
            Team team = scoreboard.registerNewTeam("rank" + rank);
            NamedTextColor color = (rank == 1) ? NamedTextColor.GOLD : (rank == 2) ? NamedTextColor.GRAY : (rank == 3) ? NamedTextColor.DARK_RED : NamedTextColor.WHITE;

            team.prefix(Component.text("Position " + rank + " : ", color));
            team.color(color);
            if (playerName != null) {
                team.addEntry(playerName);
                Score score = objective.getScore(playerName);
                score.setScore(rank);
            }

            rank++;
        }
    }

    void setupScoreboardDisplay() {
        clockInventoryRepository = ClockInventoryRepository.getInstance();

        final int totalTimeTilUpdate = 60 * updateTimeMin;
        localTime = totalTimeTilUpdate;

        updateScoreboardWithRankings();

        new BukkitRunnable() {
            @Override
            public void run() {
                localTime--;
                elapsedTime++;

                if (localTime <= 5) {
                    showCountdownTimer();
                    localTime = totalTimeTilUpdate + 5;
                } else {
                    updateScoreboard();
                }

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.setScoreboard(scoreboard);
                }
            }
        }.runTaskTimer(plugin, 0, 20);

    }

    private void showCountdownTimer() {
        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown <= 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showTitle(Title.title(Component.text("Updating Scoreboard!", NamedTextColor.GREEN), Component.empty(), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(2), Duration.ofMillis(0))));
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                    }
                    updateScoreboardWithRankings();
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.showTitle(Title.title(getCountdownTitle(countdown), Component.empty(), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0))));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F + (0.1F * countdown));
                }
                countdown--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private Component getCountdownTitle(int countdown) {
        NamedTextColor color;
        if (countdown <= 1) {
            color = NamedTextColor.GREEN;
        } else if (countdown <= 3) {
            color = NamedTextColor.YELLOW;
        } else {
            color = NamedTextColor.RED;
        }

        return Component.text(countdown, color);
    }
}