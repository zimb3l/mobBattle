package org.dlds.mobbattle;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class Category {

    private UUID id;
    private int categoryNumber;
    private int points;
    private List<MobCreature> mobs;
    private List<ItemStack> mainRewards;
    private List<ItemStack> luckyRewards;

    public Category(int categoryNumber, int points, List<MobCreature> mobs, List<ItemStack> mainRewards, List<ItemStack> luckyRewards){
        this.id = UUID.randomUUID();
        this.categoryNumber = categoryNumber;
        this.points = points;
        this.mobs = mobs;
        this.mainRewards = mainRewards;
        this.luckyRewards = luckyRewards;
    }

    public UUID getId() {
        return id;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public int getPoints() {
        return points;
    }

    public List<MobCreature> getMobs() {
        return mobs;
    }

    public List<ItemStack> getMainRewards() {
        return mainRewards;
    }

    public List<ItemStack> getLuckyRewards() {
        return luckyRewards;
    }
}
