package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LocationCalculator {
    private final List<LocationSafetyPair> spawnLocations = new ArrayList<>();
    private final JavaPlugin plugin;
    int locationCount = 24;
    int startDistance = 500;

    public LocationCalculator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int getCurrentPlayerCount() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }

    void calculateSpawnLocations(World world) {
        Location spawnLocation = world.getSpawnLocation();

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
                });
            });
        }
    }

    private boolean isLocationSafe(Location location, World world) {
        Biome biome = world.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        boolean isWater = world.getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).isLiquid();

        return biome != Biome.OCEAN && biome != Biome.DEEP_OCEAN && !isWater;
    }

    public void assignPlayerSpawns() {
        int playerCount = getCurrentPlayerCount();
        if (playerCount == 0) return;

        int locationCount = spawnLocations.size();
        float spawnOffset = (float) locationCount / playerCount;

        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i < players.size()) {

                    for (Player player : players) {
                        Title title = Title.title(Component.text("Teleporting Contestant!"), Component.text(i + 1 + " / " + players.size()), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0)));
                        player.showTitle(title);
                    }

                    Player player = players.get(i);
                    int spawnIndex = Math.round(i * spawnOffset) % locationCount;
                    LocationSafetyPair chosenLocation = getSafeLocation(spawnIndex);

                    if (chosenLocation != null && chosenLocation.isSafe) {
                        player.teleport(chosenLocation.location);
                    }

                    i++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 3, 20 * 10);
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

    private static class LocationSafetyPair {
        public Location location;
        public boolean isSafe;

        public LocationSafetyPair(Location location, boolean isSafe) {
            this.location = location;
            this.isSafe = isSafe;
        }
    }
}
