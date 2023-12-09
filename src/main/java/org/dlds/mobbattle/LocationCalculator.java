package org.dlds.mobbattle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LocationCalculator {
    private final List<LocationSafetyPair> spawnLocations = new ArrayList<>();
    int locationCount = 24;
    int startDistance = 500;

    public int getCurrentPlayerCount() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }


    void calculateSpawnLocations(World world) {
        Location spawnLocation = world.getSpawnLocation();

        for (int i = 0; i < locationCount; i++) {
            double angle = 2 * Math.PI * i / locationCount;
            double x = spawnLocation.getX() + startDistance * Math.cos(angle);
            double z = spawnLocation.getZ() + startDistance * Math.sin(angle);
            int y = world.getHighestBlockYAt((int) x, (int) z);

            Location location = new Location(world, x, y, z);
            boolean isSafe = isLocationSafe(location, world);

            spawnLocations.add(new LocationSafetyPair(location, isSafe));
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

        int i = 0;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            int spawnIndex = Math.round(i * spawnOffset) % locationCount;
            LocationSafetyPair chosenLocation = getSafeLocation(spawnIndex);

            if (chosenLocation != null && chosenLocation.isSafe) {
                player.teleport(chosenLocation.location);
            }
            i++;
        }
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
