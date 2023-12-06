package org.dlds.mobbattle;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int categoryNumber;
    private int points;
    private List<MobCreature> mobs;
    private List<ItemStack> mainRewards;
    private List<ItemStack> luckyRewards;

    public Category(int categoryNumber, int points, List<MobCreature> mobs, List<ItemStack> mainRewards, List<ItemStack> luckyRewards){
        this.categoryNumber = categoryNumber;
        this.points = points;
        this.mobs = mobs;
        this.mainRewards = mainRewards;
        this.luckyRewards = luckyRewards;
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
        if(mainRewards == null){
            return new ArrayList<>();
        }
        return mainRewards;
    }

    public List<ItemStack> getLuckyRewards() {
        if(luckyRewards == null){
            return new ArrayList<>();
        }
        return luckyRewards;
    }
}
