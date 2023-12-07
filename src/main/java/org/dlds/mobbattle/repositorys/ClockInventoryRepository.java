package org.dlds.mobbattle.repositorys;

import org.dlds.mobbattle.ClockInventory;

import java.util.HashMap;
import java.util.UUID;

public class ClockInventoryRepository {
    private static ClockInventoryRepository instance;
    private final HashMap<UUID, ClockInventory> inventoryMap = new HashMap<>();

    private ClockInventoryRepository() {}

    public static synchronized ClockInventoryRepository getInstance() {
        if (instance == null) {
            instance = new ClockInventoryRepository();
        }
        return instance;
    }

    public ClockInventory getInventory(UUID playerId) {
        return inventoryMap.computeIfAbsent(playerId, k -> new ClockInventory());
    }
}
