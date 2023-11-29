package org.dlds.mobbattle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.List;
public final class MobBattle extends JavaPlugin {



    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(Component.text("Welcome to Minecraft mobBattle!",
                NamedTextColor.AQUA));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void assignPlayerSpawns(){
        int playerCount = getCurrentPlayerCount();
        if (playerCount == 0) return; // No players online

        int locationCount = spawnLocations.size();
        float spawnOffset = (float)locationCount / playerCount;

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

    public int getCurrentPlayerCount() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }
    private int startDistance = 300; // Defines distance from spawn (0,0)
    private int locationCount = 24; // Defines number of calculated spawn locations
    private List<LocationSafetyPair> spawnLocations = new ArrayList<>();

    private class LocationSafetyPair{
        public Location location;
        public boolean isSafe;

        public LocationSafetyPair(Location location, boolean isSafe) {
            this.location = location;
            this.isSafe = isSafe;
        }
    }


    private LocationSafetyPair getSafeLocation(int startIndex) {
        // Maximum distance to check from the original index
        int maxDistance = 3;

        for (int distance = 0; distance <= maxDistance; distance++) {
            // Check at the current distance before and after the start index
            for (int direction = -1; direction <= 1; direction += 2) {
                int checkIndex = (startIndex + distance * direction + locationCount) % locationCount;
                LocationSafetyPair locationPair = spawnLocations.get(checkIndex);

                if (locationPair.isSafe) {
                    return locationPair;
                }
            }
        }
        return null; // No safe location found
    }

    public void startBattle() {
        World world = Bukkit.getServer().getWorlds().get(0); // Get the default world

        calculateSpawnLocations();
        assignPlayerSpawns();

    }

    private void calculateSpawnLocations() {
        World world = Bukkit.getServer().getWorlds().get(0);
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

    public boolean setStartDistance(int newDistance, CommandSender sender){


        if (newDistance > 0) {
            startDistance = newDistance;
            sender.sendMessage(Component.text("Distance set to " + startDistance + " blocks.",
            NamedTextColor.GREEN));
        }
        else {
            sender.sendMessage(Component.text("Please specify a distance.", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    public boolean setSpawnCount(int newCount, CommandSender sender){


        if (newCount > 0) {
            locationCount = newCount;
            sender.sendMessage(Component.text("Spawn counts set to " + locationCount + " blocks.",
                    NamedTextColor.GREEN));
        }
        else {
            sender.sendMessage(Component.text("Please specify a spawn count.", NamedTextColor.RED));
            return false;
        }
        return true;
    }




    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean retval;
        int distance;
        if (command.getName().equalsIgnoreCase("setdistance")) {
            if (!sender.hasPermission("mobbattle.setdistance")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.",
                        NamedTextColor.RED));
                return false;
            }
            try{
                distance = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                sender.sendMessage(Component.text("Invalid number format.", NamedTextColor.RED));
                return false;
            }
            retval = setStartDistance(distance, sender);
            return retval;
        }
        if(command.getName().equalsIgnoreCase("startBattle")){
            if (!sender.hasPermission("mobbattle.startBattle")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.",
                        NamedTextColor.RED));
                return false;
            }
            startBattle();
        }

        return false;
    }

}

