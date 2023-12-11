package org.dlds.mobbattle.repositorys;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.dlds.mobbattle.objects.Category;
import org.dlds.mobbattle.objects.ClockInventory;
import org.dlds.mobbattle.objects.MobCreature;
import org.dlds.mobbattle.services.CategoryService;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ClockInventoryRepository {
    private static ClockInventoryRepository instance;
    private final CategoryService categoryService;
    private final HashMap<UUID, ClockInventory> inventoryMap = new HashMap<>();
    private File dataFolder;

    private ClockInventoryRepository() {
        this.categoryService = new CategoryService();

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mobBattle");

        if (plugin != null) {
            dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                Bukkit.getLogger().warning("Could not create directory for mobBattles!");
            }
        } else {
            Bukkit.getLogger().warning("Plugin 'mobBattle' not found!");
        }
    }

    public static synchronized ClockInventoryRepository getInstance() {
        if (instance == null) {
            instance = new ClockInventoryRepository();
        }
        return instance;
    }

    public ClockInventory getInventory(UUID playerId) {
        return inventoryMap.computeIfAbsent(playerId, k -> new ClockInventory());
    }

    public HashMap<UUID, ClockInventory> getInventoryMap() {
        return inventoryMap;
    }

    public void initializeInventoriesForOnlinePlayers() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();

            inventoryMap.computeIfAbsent(playerId, k -> new ClockInventory());
        }
    }

    public void saveAllInventories() {
        for (Map.Entry<UUID, ClockInventory> entry : inventoryMap.entrySet()) {
            saveClockInventory(entry.getKey(), entry.getValue());
        }
    }

    private void saveClockInventory(UUID playerId, ClockInventory inventory) {
        File file = new File(dataFolder, playerId + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("currentPoints", inventory.getCurrentPoints());

        List<String> killedMobs = inventory.getKilledMobs().stream().filter(MobCreature::isDead).map(MobCreature::getEntityType).map(Enum::name).collect(Collectors.toList());
        config.set("killedMobs", killedMobs);

        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not save file for player id: " + playerId);
        }
    }

    public void loadAllInventories() {
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                UUID playerId = UUID.fromString(file.getName().replace(".yml", ""));
                ClockInventory loadedInventory = loadClockInventory(playerId);
                inventoryMap.put(playerId, loadedInventory);
            }
        }
    }

    private ClockInventory loadClockInventory(UUID playerId) {
        File file = new File(dataFolder, playerId + ".yml");
        if (!file.exists()) {
            return new ClockInventory();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ClockInventory inventory = new ClockInventory();

        inventory.setCurrentPoints(config.getInt("currentPoints", 0));

        List<String> killedMobsStrings = config.getStringList("killedMobs");
        List<MobCreature> killedMobsList = new ArrayList<>();

        List<Category> categories = categoryService.initializeCategories();
        for (Category category : categories) {
            for (MobCreature mob : category.getMobs()) {
                if (killedMobsStrings.contains(mob.getEntityType().name())) {
                    mob.killedMob();
                    killedMobsList.add(mob);
                }
            }
        }
        inventory.setKilledMobs(killedMobsList);
        inventory.setCategories(categories);

        return inventory;
    }

    public void resetPlayerData() {
        for (Map.Entry<UUID, ClockInventory> entry : inventoryMap.entrySet()) {
            UUID playerId = entry.getKey();
            ClockInventory inventory = entry.getValue();

            inventory.setCurrentPoints(0);

            inventory.getKilledMobs().clear();

            saveClockInventory(playerId, inventory);
        }
    }
}
