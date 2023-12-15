package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LocationCalculator {
    private final List<LocationSafetyPair> spawnLocations = new ArrayList<>();
    private final JavaPlugin plugin;
    int locationCount = 24;
    int startDistance = 500;

    public LocationCalculator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    private static BukkitRunnable getTitleUpdater() {
        AtomicReference<String> titleText = new AtomicReference<>("calculating spawn locations");

        return new BukkitRunnable() {
            private int dotCount = 1;

            @Override
            public void run() {
                StringBuilder titleBuilder = new StringBuilder("calculating spawn locations");
                for (int i = 0; i < dotCount; i++) {
                    titleBuilder.append(".");
                }
                titleText.set(titleBuilder.toString());
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    Title title = Title.title(Component.text(titleText.get(), NamedTextColor.GREEN), Component.empty(), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0)));
                    player.showTitle(title);
                }
                dotCount = (dotCount % 3) + 1;
            }
        };
    }

    public int getCurrentPlayerCount() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }

    void calculateSpawnLocations(World world, SpawnLocationsCalculatedCallback callback) {
        Location spawnLocation = world.getSpawnLocation();
        AtomicInteger pendingChunks = new AtomicInteger(locationCount);
        BukkitRunnable titleUpdater = getTitleUpdater();
        titleUpdater.runTaskTimerAsynchronously(plugin, 0L, 20L);

        for (int i = 0; i < locationCount; i++) {
            double angle = 2 * Math.PI * i / locationCount;
            double x = spawnLocation.getX() + startDistance * Math.cos(angle);
            double z = spawnLocation.getZ() + startDistance * Math.sin(angle);

            world.getChunkAtAsync(new Location(world, x, 0, z)).thenAccept(chunk -> {
                int y = world.getHighestBlockYAt((int) x, (int) z);

                Location location = new Location(world, x, y, z);
                boolean isSafe = isLocationSafe(location, world);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    spawnLocations.add(new LocationSafetyPair(location, isSafe));

                    if (pendingChunks.decrementAndGet() == 0) {
                        titleUpdater.cancel();
                        callback.onCompleted();
                    }
                });
            });
        }
    }

    private boolean isLocationSafe(Location location, World world) {
        Biome biome = world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        boolean isWaterBelow = world.getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).isLiquid();
        boolean isWaterAt = world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()).isLiquid();
        boolean isWaterAbove = world.getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).isLiquid();

        return biome != Biome.OCEAN && biome != Biome.DEEP_OCEAN && !isWaterBelow && !isWaterAt && !isWaterAbove;
    }

    public void assignPlayerSpawns(PlayerSpawnsAssignedCallback callback) {
        int playerCount = getCurrentPlayerCount();
        if (playerCount == 0) {
            callback.onCompleted();
            return;
        }

        int locationCount = spawnLocations.size();
        float spawnOffset = (float) locationCount / playerCount;

        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        new BukkitRunnable() {
            int i = 0;
            int countdown = 10;

            @Override
            public void run() {
                if (i < players.size()) {

                    for (Player player : players) {
                        Title title = Title.title(Component.text("Teleporting contestant " + (i + 1) + " / " + players.size(), NamedTextColor.GREEN), Component.text("Next in " + countdown, NamedTextColor.GREEN), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0)));
                        player.showTitle(title);
                    }

                    if (countdown == 0) {
                        Player playerToTeleport = players.get(i);
                        int spawnIndex = Math.round(i * spawnOffset) % locationCount;
                        LocationSafetyPair chosenLocation = getSafeLocation(spawnIndex);

                        if (chosenLocation != null && chosenLocation.isSafe) {
                            Location teleportLocation = chosenLocation.location.clone().add(0, 2, 0);
                            playerToTeleport.teleport(teleportLocation);
                        }

                        i++;
                        countdown = 10;
                    } else {
                        countdown--;
                    }
                } else {
                    callback.onCompleted();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20 * 3, 20);
    }

    private LocationSafetyPair getSafeLocation(int startIndex) {

        int maxDistance = 3;

        for (int distance = 0; distance <= maxDistance; distance++) {

            for (int direction = -1; direction <= 1; direction += 2) {
                int checkIndex = (startIndex + distance * direction + locationCount) % locationCount;
                LocationSafetyPair locationPair = spawnLocations.get(checkIndex);

                if (locationPair.isSafe) {
                    return locationPair;
                }
            }
        }
        return null;
    }

    public interface SpawnLocationsCalculatedCallback {
        void onCompleted();
    }

    public interface PlayerSpawnsAssignedCallback {
        void onCompleted();
    }

    private static class LocationSafetyPair {
        public Location location;
        public boolean isSafe;

        public LocationSafetyPair(Location location, boolean isSafe) {
            this.location = location;
            this.isSafe = isSafe;
        }
    }
}
