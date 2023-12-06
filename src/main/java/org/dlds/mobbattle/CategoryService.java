package org.dlds.mobbattle;

import com.google.common.primitives.Ints;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {
    private Map<Integer, List<MobCreature>> mobs = new HashMap<>();
    private Map<Integer, List<ItemStack>> mainRewards = new HashMap<>();
    private Map<Integer, List<ItemStack>> luckyRewards = new HashMap<>();
    private final int[] categoryPoints = new int[]{1, 3, 5, 10, 25, 50, 100, 150, 250};
    private List<Category> categories = new ArrayList<>();
    public List<Category> initializeCategories(){
        initializeMobLists();
        initializeMainRewardLists();
        initializeLuckyRewardLists();
        for (int points: categoryPoints) {
            int categoryNumber = Ints.indexOf(categoryPoints, points) + 1;
            List<MobCreature> mobList = mobs.get(categoryNumber);
            List<ItemStack> mainRewardList = mainRewards.get(categoryNumber);
            List<ItemStack> luckyRewardList = luckyRewards.get(categoryNumber);
            Category category = new Category(categoryNumber, points, mobList, mainRewardList, luckyRewardList);
            categories.add(category);
        }
        return categories;
    }

    private void initializeMainRewardLists(){
        List<ItemStack> categoryOneMain = new ArrayList<>();
        categoryOneMain.add(new ItemStack(Material.IRON_INGOT, 1));
        mainRewards.put(1, categoryOneMain);

        List<ItemStack> categoryTwoMain = new ArrayList<>();
        categoryTwoMain.add(new ItemStack(Material.IRON_INGOT, 1));
        mainRewards.put(2, categoryTwoMain);

        List<ItemStack> categoryThreeMain = new ArrayList<>();
        categoryThreeMain.add(new ItemStack(Material.DIAMOND, 1));
        mainRewards.put(3, categoryThreeMain);

        List<ItemStack> categoryFourMain = new ArrayList<>();
        categoryFourMain.add(new ItemStack(Material.DIAMOND, 1));
        mainRewards.put(4, categoryFourMain);

        List<ItemStack> categoryFiveMain = new ArrayList<>();
        categoryFiveMain.add(new ItemStack(Material.DIAMOND, 2));
        mainRewards.put(5, categoryFiveMain);

        List<ItemStack> categorySixMain = new ArrayList<>();
        categorySixMain.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        categorySixMain.add(new ItemStack(Material.DIAMOND, 2));
        mainRewards.put(6, categorySixMain);

        List<ItemStack> categorySevenMain = new ArrayList<>();
        categorySevenMain.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        categorySevenMain.add(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1));
        categorySevenMain.add(new ItemStack(Material.DIAMOND, 5));
        mainRewards.put(7, categorySevenMain);

        List<ItemStack> categoryEightMain = new ArrayList<>();
        categoryEightMain.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        mainRewards.put(8, categoryEightMain);

        List<ItemStack> categoryNineMain = new ArrayList<>();
        categoryNineMain.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        mainRewards.put(9, categoryNineMain);
    }

    private void initializeLuckyRewardLists(){
        List<ItemStack> categoryTwoLucky = new ArrayList<>();
        categoryTwoLucky.add(new ItemStack(Material.IRON_INGOT, 1));
        categoryTwoLucky.add(new ItemStack(Material.ARROW, 5));
        luckyRewards.put(2, categoryTwoLucky);

        List<ItemStack> categoryThreeLucky = new ArrayList<>();
        categoryThreeLucky.add(new ItemStack(Material.IRON_INGOT, 2));
        categoryThreeLucky.add(new ItemStack(Material.ARROW, 10));
        luckyRewards.put(3, categoryThreeLucky);

        List<ItemStack> categoryFourLucky = new ArrayList<>();
        categoryFourLucky.add(new ItemStack(Material.DIAMOND, 1));
        categoryFourLucky.add(new ItemStack(Material.GOLDEN_APPLE, 2));
        luckyRewards.put(4, categoryFourLucky);

        List<ItemStack> categoryFiveLucky = new ArrayList<>();
        categoryFiveLucky.add(new ItemStack(Material.DIAMOND, 2));
        categoryFiveLucky.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        categoryFiveLucky.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
        luckyRewards.put(5, categoryFiveLucky);
    }

    private void initializeMobLists(){
        List<MobCreature> categoryOne = new ArrayList<>();
        categoryOne.add(new MobCreature(EntityType.COW, ""));
        categoryOne.add(new MobCreature(EntityType.SHEEP, ""));
        categoryOne.add(new MobCreature(EntityType.PIG, ""));
        categoryOne.add(new MobCreature(EntityType.CHICKEN, ""));
        categoryOne.add(new MobCreature(EntityType.SQUID, ""));
        mobs.put(1, categoryOne);

        List<MobCreature> categoryTwo = new ArrayList<>();
        categoryTwo.add(new MobCreature(EntityType.ZOMBIE, ""));
        categoryTwo.add(new MobCreature(EntityType.HORSE, ""));
        categoryTwo.add(new MobCreature(EntityType.DONKEY, ""));
        categoryTwo.add(new MobCreature(EntityType.CREEPER, ""));
        categoryTwo.add(new MobCreature(EntityType.SKELETON, ""));
        categoryTwo.add(new MobCreature(EntityType.VILLAGER, ""));
        categoryTwo.add(new MobCreature(EntityType.WANDERING_TRADER, ""));
        categoryTwo.add(new MobCreature(EntityType.WOLF, ""));
        categoryTwo.add(new MobCreature(EntityType.CAT, ""));
        categoryTwo.add(new MobCreature(EntityType.SLIME, ""));
        categoryTwo.add(new MobCreature(EntityType.DROWNED, ""));
        categoryTwo.add(new MobCreature(EntityType.SPIDER, ""));
        categoryTwo.add(new MobCreature(EntityType.SILVERFISH, ""));
        categoryTwo.add(new MobCreature(EntityType.BAT, ""));
        mobs.put(2, categoryTwo);

        List<MobCreature> categoryThree = new ArrayList<>();
        categoryThree.add(new MobCreature(EntityType.AXOLOTL, ""));
        categoryThree.add(new MobCreature(EntityType.CAVE_SPIDER, ""));
        categoryThree.add(new MobCreature(EntityType.BEE, ""));
        categoryThree.add(new MobCreature(EntityType.GLOW_SQUID, ""));
        categoryThree.add(new MobCreature(EntityType.RABBIT, ""));
        categoryThree.add(new MobCreature(EntityType.GOAT, ""));
        categoryThree.add(new MobCreature(EntityType.FOX, ""));
        categoryThree.add(new MobCreature(EntityType.PARROT, ""));
        categoryThree.add(new MobCreature(EntityType.SNOWMAN, ""));
        categoryThree.add(new MobCreature(EntityType.TURTLE, ""));
        categoryThree.add(new MobCreature(EntityType.HUSK, ""));
        categoryThree.add(new MobCreature(EntityType.WITCH, ""));
        categoryThree.add(new MobCreature(EntityType.ZOMBIE_VILLAGER, ""));
        mobs.put(3, categoryThree);

        List<MobCreature> categoryFour = new ArrayList<>();
        categoryFour.add(new MobCreature(EntityType.MULE, ""));
        categoryFour.add(new MobCreature(EntityType.FROG, ""));
        categoryFour.add(new MobCreature(EntityType.STRIDER, ""));
        categoryFour.add(new MobCreature(EntityType.OCELOT, ""));
        categoryFour.add(new MobCreature(EntityType.CAMEL, ""));
        categoryFour.add(new MobCreature(EntityType.LLAMA, ""));
        categoryFour.add(new MobCreature(EntityType.ENDERMAN, ""));
        categoryFour.add(new MobCreature(EntityType.IRON_GOLEM, ""));
        categoryFour.add(new MobCreature(EntityType.PANDA, ""));
        categoryFour.add(new MobCreature(EntityType.GUARDIAN, ""));
        categoryFour.add(new MobCreature(EntityType.ENDERMITE, ""));
        categoryFour.add(new MobCreature(EntityType.GHAST, ""));
        categoryFour.add(new MobCreature(EntityType.HOGLIN, ""));
        categoryFour.add(new MobCreature(EntityType.PHANTOM, ""));
        categoryFour.add(new MobCreature(EntityType.PILLAGER, ""));
        categoryFour.add(new MobCreature(EntityType.STRAY, ""));
        categoryFour.add(new MobCreature(EntityType.VEX, ""));
        categoryFour.add(new MobCreature(EntityType.VINDICATOR, ""));
        categoryFour.add(new MobCreature(EntityType.WITHER_SKELETON, ""));
        categoryFour.add(new MobCreature(EntityType.ZOGLIN, ""));
        mobs.put(4, categoryFour);

        List<MobCreature> categoryFive = new ArrayList<>();
        categoryFive.add(new MobCreature(EntityType.ALLAY, ""));
        categoryFive.add(new MobCreature(EntityType.TADPOLE, ""));
        categoryFive.add(new MobCreature(EntityType.SKELETON_HORSE, ""));
        categoryFive.add(new MobCreature(EntityType.DOLPHIN, ""));
        categoryFive.add(new MobCreature(EntityType.POLAR_BEAR, ""));
        categoryFive.add(new MobCreature(EntityType.PIGLIN, ""));
        categoryFive.add(new MobCreature(EntityType.PIGLIN_BRUTE, ""));
        categoryFive.add(new MobCreature(EntityType.ZOMBIFIED_PIGLIN, ""));
        categoryFive.add(new MobCreature(EntityType.BLAZE, ""));
        categoryFive.add(new MobCreature(EntityType.MAGMA_CUBE, ""));
        mobs.put(5, categoryFive);

        List<MobCreature> categorySix = new ArrayList<>();
        categorySix.add(new MobCreature(EntityType.MUSHROOM_COW, ""));
        categorySix.add(new MobCreature(EntityType.ELDER_GUARDIAN, ""));
        categorySix.add(new MobCreature(EntityType.EVOKER, ""));
        categorySix.add(new MobCreature(EntityType.RAVAGER, ""));
        categorySix.add(new MobCreature(EntityType.SHULKER, ""));
        mobs.put(6, categorySix);

        List<MobCreature> categorySeven = new ArrayList<>();
        categorySeven.add(new MobCreature(EntityType.PLAYER, ""));
        categorySeven.add(new MobCreature(EntityType.WARDEN, ""));
        categorySeven.add(new MobCreature(EntityType.SNIFFER, ""));
        mobs.put(7, categorySeven);

        List<MobCreature> categoryEight = new ArrayList<>();
        categoryEight.add(new MobCreature(EntityType.WITHER, ""));
        mobs.put(8, categoryEight);

        List<MobCreature> categoryNine = new ArrayList<>();
        categoryNine.add(new MobCreature(EntityType.ENDER_DRAGON, ""));
        mobs.put(9, categoryNine);
    }
}
