package org.dlds.mobbattle.repositorys;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MobBattleRepository {
    private final Plugin plugin;
    private static final String GAME_STATE_FILE = "gamestate.yml";

    public MobBattleRepository(Plugin plugin) {
        this.plugin = plugin;
    }

    public void saveGameState(boolean isBattleRunning) {
        File file = new File(plugin.getDataFolder(), GAME_STATE_FILE);
        YamlConfiguration config = new YamlConfiguration();

        config.set("isBattleRunning", isBattleRunning);

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save the current game state: " + e.getMessage());
        }
    }

    public boolean loadGameState() {
        File file = new File(plugin.getDataFolder(), GAME_STATE_FILE);

        if (!file.exists()) {
            return false;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getBoolean("isBattleRunning", false);
    }
}
