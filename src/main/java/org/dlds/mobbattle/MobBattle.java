package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.dlds.mobbattle.eventHandlers.ClockInventoryEventHandler;
import org.dlds.mobbattle.eventHandlers.MobKillEventHandler;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;
import org.jetbrains.annotations.NotNull;

public final class MobBattle extends JavaPlugin {
    private LocationCalculator locationCalculator;
    private TimerHandler timerHandler;
    private ClockInventoryRepository clockInventoryRepository;


    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(Component.text("Welcome to Minecraft mobBattle!", NamedTextColor.AQUA));
        locationCalculator = new LocationCalculator();
        timerHandler = new TimerHandler();

        // Create an instance of ClockInventory and register it as an event listener
        ClockInventoryEventHandler clockInventoryEventHandler = new ClockInventoryEventHandler();
        MobKillEventHandler mobKillEventHandler = new MobKillEventHandler();
        getServer().getPluginManager().registerEvents(clockInventoryEventHandler, this);
        getServer().getPluginManager().registerEvents(mobKillEventHandler, this);

        clockInventoryRepository.loadAllInventories();
    }

    @Override
    public void onDisable() {
        clockInventoryRepository.saveAllInventories();
    }

    public void startBattle() {
        World world = Bukkit.getServer().getWorlds().get(0); // Get the default world

        locationCalculator.calculateSpawnLocations(world);
        locationCalculator.assignPlayerSpawns();
        timerHandler.setupScoreboardDisplay();
    }


    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        boolean retval;
        int distance;
        int timer;
        int spawncount;

        if (command.getName().equalsIgnoreCase("setdistance")) {
            if (!sender.hasPermission("mobbattle.setdistance")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return false;
            }
            try {
                distance = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid number format.", NamedTextColor.RED));
                return false;
            }
            retval = setStartDistance(distance, sender);
            sender.sendMessage(Component.text("Playing radius set to " + distance + " blocks.", NamedTextColor.GREEN));
            return retval;
        }

        if (command.getName().equalsIgnoreCase("settimer")) {
            if (!sender.hasPermission("mobbattle.settimer")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return false;
            }
            try {
                timer = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid number format.", NamedTextColor.RED));
                return false;
            }
            retval = editUpdateTime(timer, sender);
            sender.sendMessage(Component.text("Scoreboard will update every " + timer + " minutes.", NamedTextColor.GREEN));
            return retval;
        }

        if (command.getName().equalsIgnoreCase("setspawncount")) {
            if (!sender.hasPermission("mobbattle.setspawncount")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return false;
            }
            try {
                spawncount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid number format.", NamedTextColor.RED));
                return false;
            }
            sender.sendMessage(Component.text("Calculated number of spawns set to " + spawncount, NamedTextColor.GREEN));
            retval = setSpawnCount(spawncount, sender);
            return retval;
        }

        if (command.getName().equalsIgnoreCase("startBattle")) {
            if (!sender.hasPermission("mobbattle.startBattle")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return false;
            }
            sender.sendMessage(Component.text("GET READY, GAME WILL START NOW!!", NamedTextColor.GREEN));
            sender.sendMessage(Component.text("GET READY, GAME WILL START NOW!!", NamedTextColor.AQUA));
            sender.sendMessage(Component.text("GET READY, GAME WILL START NOW!!", NamedTextColor.DARK_AQUA));
            startBattle();
        }

        return false;
    }


    public boolean setStartDistance(int newDistance, CommandSender sender) {


        if (newDistance > 0) {
            locationCalculator.startDistance = newDistance;
            sender.sendMessage(Component.text("Distance set to " + locationCalculator.startDistance + " blocks.", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text("Please specify a distance.", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    public boolean setSpawnCount(int newCount, CommandSender sender) {
        if (newCount > 0) {
            locationCalculator.locationCount = newCount;
            sender.sendMessage(Component.text("Spawn counts set to " + locationCalculator.locationCount + " blocks.", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text("Please specify a spawn count.", NamedTextColor.RED));
            return false;
        }
        return true;
    }

    public boolean editUpdateTime(int newTime, CommandSender sender) {
        if (newTime > 0) {
            timerHandler.updateTimeMin = newTime;
            sender.sendMessage(Component.text("Update timer set to " + timerHandler.updateTimeMin + " blocks.", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text("Please specify a update timer.", NamedTextColor.RED));
            return false;
        }
        return true;
    }
}

