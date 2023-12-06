package org.dlds.mobbattle.repositorys;

import org.dlds.mobbattle.ClockInventory;

import java.util.HashMap;
import java.util.UUID;

public class ClockInventoryRepository {
    private HashMap<UUID, ClockInventory> inventories = new HashMap<>();

    public ClockInventory getInventory(UUID playerId){
        return inventories.computeIfAbsent(playerId, k -> new ClockInventory());
    }
}
