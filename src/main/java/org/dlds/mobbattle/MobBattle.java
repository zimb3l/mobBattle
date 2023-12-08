package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.dlds.mobbattle.eventHandlers.ClockInventoryEventHandler;
import org.dlds.mobbattle.eventHandlers.MobKillEventHandler;
import org.dlds.mobbattle.eventHandlers.PlayerDeathEventHandler;
import org.dlds.mobbattle.objects.ClockInventory;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;
import org.dlds.mobbattle.repositorys.MobBattleRepository;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;

public final class MobBattle extends JavaPlugin {
    private LocationCalculator locationCalculator;
    private TimerHandler timerHandler;
    private ClockInventoryRepository clockInventoryRepository;
    private MobBattleRepository mobBattleRepository;
    private boolean isBattleRunning = false;

    @Override
    public void onEnable() {
        Bukkit.getServer().broadcast(Component.text("Welcome to Minecraft mobBattle!", NamedTextColor.AQUA));
        locationCalculator = new LocationCalculator();
        timerHandler = new TimerHandler();

        ClockInventoryEventHandler clockInventoryEventHandler = new ClockInventoryEventHandler();
        MobKillEventHandler mobKillEventHandler = new MobKillEventHandler();
        PlayerDeathEventHandler playerDeathEventHandler = new PlayerDeathEventHandler();

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(clockInventoryEventHandler, this);
        pluginManager.registerEvents(mobKillEventHandler, this);
        pluginManager.registerEvents(playerDeathEventHandler, this);

        clockInventoryRepository = ClockInventoryRepository.getInstance();

        Bukkit.getLogger().info("Loading all clock inventories!");
        clockInventoryRepository.loadAllInventories();
        Bukkit.getLogger().info("Finished loading all clock inventories!");

        mobBattleRepository = new MobBattleRepository(this);
        isBattleRunning = mobBattleRepository.loadGameState();
    }

    @Override
    public void onDisable() {
        mobBattleRepository.saveGameState(isBattleRunning);
        Bukkit.getLogger().info("Saving all clock inventories!");
        clockInventoryRepository.saveAllInventories();
        Bukkit.getLogger().info("Finished saving all clock inventories!");
    }

    public void initiateStartSequence(World world) {
        new BukkitRunnable() {
            int countdown = 10;

            @Override
            public void run() {
                if (countdown <= 0) {

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.clearTitle();
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        player.removePotionEffect(PotionEffectType.SLOW);
                        player.removePotionEffect(PotionEffectType.SATURATION);
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                    }

                    isBattleRunning = true;
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Title title = Title.title(getCountdownTitle(countdown), Component.text("Get ready!"), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0)));
                    player.showTitle(title);

                    if (countdown == 10) {
                        world.setTime(1000);
                        world.setStorm(false);
                        world.setThundering(false);
                        player.setGameMode(GameMode.ADVENTURE);
                        player.setExp(0.0F);
                        player.setLevel(0);
                        player.getInventory().clear();
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 255, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 255, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 255, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 255, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, 255, false, false));
                    }

                    if (countdown == 3) {
                        player.setGameMode(GameMode.SURVIVAL);

                        ItemStack clock = new ItemStack(Material.CLOCK, 1);
                        player.getInventory().setItem(8, clock);
                    }
                }

                countdown--;
            }
        }.runTaskTimer(this, 0, 20);
    }


    private Component getCountdownTitle(int countdown) {
        NamedTextColor color;
        if (countdown <= 3) {
            color = NamedTextColor.GREEN;
        } else if (countdown <= 6) {
            color = NamedTextColor.YELLOW;
        } else {
            color = NamedTextColor.RED;
        }

        return Component.text(countdown, color);
    }

    public void startBattle() {
        if (isBattleRunning) {
            Bukkit.getServer().broadcast(Component.text("A battle is already ongoing!", NamedTextColor.RED));
            return;
        }

        World world = Bukkit.getServer().getWorlds().get(0);

        locationCalculator.calculateSpawnLocations(world);
        locationCalculator.assignPlayerSpawns();
        initiateStartSequence(world);
        timerHandler.setupScoreboardDisplay();
    }

    public void endBattle() {
        isBattleRunning = false;

        List<Map.Entry<UUID, Integer>> playerPoints = new ArrayList<>();

        for (Map.Entry<UUID, ClockInventory> entry : clockInventoryRepository.getInventoryMap().entrySet()) {
            UUID playerId = entry.getKey();
            ClockInventory inventory = entry.getValue();
            int points = inventory.getCurrentPoints();

            playerPoints.add(new AbstractMap.SimpleEntry<>(playerId, points));
        }

        playerPoints.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : playerPoints) {
            UUID playerId = entry.getKey();
            int points = entry.getValue();

            String playerName = Bukkit.getOfflinePlayer(playerId).getName();
            Bukkit.getServer().broadcast(Component.text(String.format("Platz %d: %s - %d Punkte", rank, playerName, points)));
            rank++;
        }

        clockInventoryRepository.resetPlayerData();
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
            startBattle();
            Bukkit.getServer().broadcast(Component.text("say GET READY, GAME WILL START NOW!!", NamedTextColor.GREEN));
            Bukkit.getServer().broadcast(Component.text("say GET READY, GAME WILL START NOW!!", NamedTextColor.AQUA));
            Bukkit.getServer().broadcast(Component.text("say GET READY, GAME WILL START NOW!!", NamedTextColor.DARK_AQUA));
        }

        if (command.getName().equalsIgnoreCase("endbattle")) {
            if (!sender.hasPermission("mobbattle.endbattle")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return false;
            }

            if (!isBattleRunning) {
                sender.sendMessage(Component.text("There is no ongoing battle to end.", NamedTextColor.RED));
                return true;
            }

            endBattle();
            Bukkit.getServer().broadcast(Component.text("The battle has been ended.", NamedTextColor.GREEN));
            return true;
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

