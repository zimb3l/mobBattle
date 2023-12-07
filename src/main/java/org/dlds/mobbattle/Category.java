package org.dlds.mobbattle;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private final int categoryNumber;
    private final int points;
    private final List<MobCreature> mobs;
    private final List<ItemStack> mainRewards;
    private final List<ItemStack> luckyRewards;
    private final String base64HeadString;

    public Category(int categoryNumber, int points, List<MobCreature> mobs, List<ItemStack> mainRewards, List<ItemStack> luckyRewards, String base64HeadString){
        this.categoryNumber = categoryNumber;
        this.points = points;
        this.mobs = mobs;
        this.mainRewards = mainRewards;
        this.luckyRewards = luckyRewards;
        this.base64HeadString = base64HeadString;
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

    public String getBase64HeadString() {
        return base64HeadString;
    }
}
