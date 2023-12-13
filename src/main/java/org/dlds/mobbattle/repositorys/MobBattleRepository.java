package org.dlds.mobbattle.repositorys;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MobBattleRepository {
    public static final String GAME_STATE_FILE = "gamestate.yml";
    private final Plugin plugin;

    public MobBattleRepository(Plugin plugin) {
        this.plugin = plugin;
    }

    public void saveGameState(boolean isBattleRunning, boolean isBattlePaused) {
        File file = new File(plugin.getDataFolder(), GAME_STATE_FILE);
        YamlConfiguration config = new YamlConfiguration();

        config.set("isBattleRunning", isBattleRunning);
        config.set("isBattlePaused", isBattlePaused);

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save the current game state: " + e.getMessage());
        }
    }

    public GameState loadGameState() {
        File file = new File(plugin.getDataFolder(), GAME_STATE_FILE);

        if (!file.exists()) {
            return new GameState(false, false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean isBattleRunning = config.getBoolean("isBattleRunning", false);
        boolean isPaused = config.getBoolean("isPaused", false);

        return new GameState(isBattleRunning, isPaused);
    }

    public static class GameState {
        public final boolean isBattleRunning;
        public final boolean isBattlePaused;

        public GameState(boolean isBattleRunning, boolean isBattlePaused) {
            this.isBattleRunning = isBattleRunning;
            this.isBattlePaused = isBattlePaused;
        }
    }
}
