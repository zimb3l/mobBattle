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
    private final Map<Integer, List<MobCreature>> mobs = new HashMap<>();
    private final Map<Integer, List<ItemStack>> mainRewards = new HashMap<>();
    private final Map<Integer, List<ItemStack>> luckyRewards = new HashMap<>();
    private final int[] categoryPoints = new int[]{1, 3, 5, 10, 25, 50, 100, 150, 250};
    private final Map<Integer, String> base64HeadStrings = new HashMap<>();
    private final List<Category> categories = new ArrayList<>();
    public List<Category> initializeCategories(){
        initializeMobLists();
        initializeMainRewardLists();
        initializeLuckyRewardLists();
        initializeBase64HeadStrings();
        for (int points: categoryPoints) {
            int categoryNumber = Ints.indexOf(categoryPoints, points) + 1;
            List<MobCreature> mobList = mobs.get(categoryNumber);
            List<ItemStack> mainRewardList = mainRewards.get(categoryNumber);
            List<ItemStack> luckyRewardList = luckyRewards.get(categoryNumber);
            String base64HeadString = base64HeadStrings.get(categoryNumber);
            Category category = new Category(categoryNumber, points, mobList, mainRewardList, luckyRewardList, base64HeadString);
            categories.add(category);
        }
        return categories;
    }

    private void initializeBase64HeadStrings(){
        base64HeadStrings.put(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0=");
        base64HeadStrings.put(2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNkOWVlZWU4ODM0Njg4ODFkODM4NDhhNDZiZjMwMTI0ODVjMjNmNzU3NTNiOGZiZTg0ODczNDE0MTk4NDcifX19");
        base64HeadStrings.put(3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ0ZWFlMTM5MzM4NjBhNmRmNWU4ZTk1NTY5M2I5NWE4YzNiMTVjMzZiOGI1ODc1MzJhYzA5OTZiYzM3ZTUifX19");
        base64HeadStrings.put(4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJlNzhmYjIyNDI0MjMyZGMyN2I4MWZiY2I0N2ZkMjRjMWFjZjc2MDk4NzUzZjJkOWMyODU5ODI4N2RiNSJ9fX0=");
        base64HeadStrings.put(5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1N2UzYmM4OGE2NTczMGUzMWExNGUzZjQxZTAzOGE1ZWNmMDg5MWE2YzI0MzY0M2I4ZTU0NzZhZTIifX19");
        base64HeadStrings.put(6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM0YjM2ZGU3ZDY3OWI4YmJjNzI1NDk5YWRhZWYyNGRjNTE4ZjVhZTIzZTcxNjk4MWUxZGNjNmIyNzIwYWIifX19");
        base64HeadStrings.put(7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRiNmViMjVkMWZhYWJlMzBjZjQ0NGRjNjMzYjU4MzI0NzVlMzgwOTZiN2UyNDAyYTNlYzQ3NmRkN2I5In19fQ==");
        base64HeadStrings.put(8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTkxOTQ5NzNhM2YxN2JkYTk5NzhlZDYyNzMzODM5OTcyMjI3NzRiNDU0Mzg2YzgzMTljMDRmMWY0Zjc0YzJiNSJ9fX0=");
        base64HeadStrings.put(9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3Y2FmNzU5MWIzOGUxMjVhODAxN2Q1OGNmYzY0MzNiZmFmODRjZDQ5OWQ3OTRmNDFkMTBiZmYyZTViODQwIn19fQ==");
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
        categoryEightMain.add(new ItemStack(Material.SHULKER_BOX, 1));
        categoryEightMain.add(new ItemStack(Material.NETHERITE_INGOT, 2));
        mainRewards.put(8, categoryEightMain);

        List<ItemStack> categoryNineMain = new ArrayList<>();
        categoryNineMain.add(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1));
        categoryNineMain.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
        categoryNineMain.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        mainRewards.put(9, categoryNineMain);
    }

    private void initializeLuckyRewardLists(){
        List<ItemStack> categoryOneLucky = new ArrayList<>();
        categoryOneLucky.add(new ItemStack(Material.OAK_LOG, 2));
        luckyRewards.put(1, categoryOneLucky);

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

        List<ItemStack> categorySixLucky = new ArrayList<>();
        categorySixLucky.add(new ItemStack(Material.GOLDEN_APPLE, 5));
        luckyRewards.put(6, categorySixLucky);

        List<ItemStack> categorySevenLucky = new ArrayList<>();
        categorySevenLucky.add(new ItemStack(Material.GOLDEN_APPLE, 5));
        luckyRewards.put(7, categorySevenLucky);

        List<ItemStack> categoryEightLucky = new ArrayList<>();
        categoryEightLucky.add(new ItemStack(Material.GOLDEN_APPLE, 5));
        luckyRewards.put(8, categoryEightLucky);

        List<ItemStack> categoryNineLucky = new ArrayList<>();
        categoryNineLucky.add(new ItemStack(Material.GOLDEN_APPLE, 5));
        luckyRewards.put(9, categoryNineLucky);
    }

    private void initializeMobLists(){
        List<MobCreature> categoryOne = new ArrayList<>();
        categoryOne.add(new MobCreature("Cow", EntityType.COW,     "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY1NTE4NDA5NTVmNTI0MzY3NTgwZjExYjM1MjI4OTM4YjY3ODYzOTdhOGYyZThjOGNjNmIwZWIwMWI1ZGIzZCJ9fX0="));
        categoryOne.add(new MobCreature("Sheep", EntityType.SHEEP,   "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBmNTAzOTRjNmQ3ZGJjMDNlYTU5ZmRmNTA0MDIwZGM1ZDY1NDhmOWQzYmM5ZGNhYzg5NmJiNWNhMDg1ODdhIn19fQ=="));
        categoryOne.add(new MobCreature("Pig", EntityType.PIG,     "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVlODUxNDg5MmYzZDc4YTMyZTg0NTZmY2JiOGM2MDgxZTIxYjI0NmQ4MmYzOThiZDk2OWZlYzE5ZDNjMjdiMyJ9fX0="));
        categoryOne.add(new MobCreature("Chicken", EntityType.CHICKEN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ=="));
        categoryOne.add(new MobCreature("Squid", EntityType.SQUID,   "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY0YmRjNmY2MDA2NTY1MTFiZWY1OTZjMWExNmFhYjFkM2Y1ZGJhYWU4YmVlMTlkNWMwNGRlMGRiMjFjZTkyYyJ9fX0="));
        mobs.put(1, categoryOne);

        List<MobCreature> categoryTwo = new ArrayList<>();
        categoryTwo.add(new MobCreature("Zombie", EntityType.ZOMBIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgzYWFhZWUyMjg2OGNhZmRhYTFmNmY0YTBlNTZiMGZkYjY0Y2QwYWVhYWJkNmU4MzgxOGMzMTJlYmU2NjQzNyJ9fX0="));
        categoryTwo.add(new MobCreature("Horse", EntityType.HORSE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JiNGIyODg5OTFlZmI4Y2EwNzQzYmVjY2VmMzEyNThiMzFkMzlmMjQ5NTFlZmIxYzljMThhNDE3YmE0OGY5In19fQ=="));
        categoryTwo.add(new MobCreature("Donkey", EntityType.DONKEY, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjNhOTc2YzA0N2Y0MTJlYmM1Y2IxOTcxMzFlYmVmMzBjMDA0YzBmYWY0OWQ4ZGQ0MTA1ZmNhMTIwN2VkYWZmMyJ9fX0="));
        categoryTwo.add(new MobCreature("Creeper", EntityType.CREEPER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZmOGY2ZDAwZDViMDczODc1ODRmMTE3YzY2ZDY5OGM5MGM2OWNlZGIwMWE2ZTY5ZGJiMDI3NzFjNzMwMmQxNiJ9fX0="));
        categoryTwo.add(new MobCreature("Skeleton", EntityType.SKELETON, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgyYjc4ZGE2ZWU3MTNkNWFjZmU1ZmNiMDc1NGVlNTY5MDA4MzFhNTA5ODMxMzA2NDEwOGRlNmU3ZTQwNjgzOSJ9fX0="));
        categoryTwo.add(new MobCreature("Villager", EntityType.VILLAGER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI2ZWMxY2ExODViNDdhYWQzOWY5MzFkYjhiMGE4NTAwZGVkODZhMTI3YTIwNDg4NmVkNGIzNzgzYWQxNzc1YyJ9fX0="));
        categoryTwo.add(new MobCreature("Wandering Trader", EntityType.WANDERING_TRADER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUwMTFhYWM4MTcyNTlmMmI0OGRhM2U1ZWYyNjYwOTQ3MDM4NjY2MDhiM2Q3ZDE3NTQ0MzJiZjI0OWNkMjIzNCJ9fX0="));
        categoryTwo.add(new MobCreature("Wolf", EntityType.WOLF, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkMWQzMTEzZWM0M2FjMjk2MWRkNTlmMjgxNzVmYjQ3MTg4NzNjNmM0NDhkZmNhODcyMjMxN2Q2NyJ9fX0="));
        categoryTwo.add(new MobCreature("Cat", EntityType.CAT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjNmYTU2MGRhNTNiMGZmYTkyYzczNWViZTA0OWE1NjIzNTUyM2I5Y2I1Y2QxMDI2OWRmOTAyY2MyMDUyYWUyMiJ9fX0="));
        categoryTwo.add(new MobCreature("Slime", EntityType.SLIME, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFhZmZkMzFlZmMzN2JhODRmNTAxODczOTRkODY4ODM0NGNjZDA2Y2RjOTI2ZGRmY2YyZGYxMTY5ODZkY2E5In19fQ=="));
        categoryTwo.add(new MobCreature("Drowned", EntityType.DROWNED, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0="));
        categoryTwo.add(new MobCreature("Spider", EntityType.SPIDER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlMjQ4ZGEyZTEwOGYwOTgxM2E2Yjg0OGEwZmNlZjExMTMwMDk3ODE4MGVkYTQxZDNkMWE3YThlNGRiYTNjMyJ9fX0="));
        categoryTwo.add(new MobCreature("Silverfish", EntityType.SILVERFISH, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5MWRhYjgzOTFhZjVmZGE1NGFjZDJjMGIxOGZiZDgxOWI4NjVlMWE4ZjFkNjIzODEzZmE3NjFlOTI0NTQwIn19fQ=="));
        categoryTwo.add(new MobCreature("Bat", EntityType.BAT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRlNzVhMmNjMWM5NTBlODJmNjJhYmUyMGQ0Mjc1NDM3OWRmYWQ2ZjVmZjU0NmU1OGYxYzA5MDYxODYyYmI5MiJ9fX0="));
        mobs.put(2, categoryTwo);

        List<MobCreature> categoryThree = new ArrayList<>();
        categoryThree.add(new MobCreature("Axolotl", EntityType.AXOLOTL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTIxNDllYjhhNDg5ZDhiMDM1OGQ5ODk1NjBjZDI3MDRiMjU2NGFjYjkxY2JmYjZkMDE0NmYzNWNjMDRhM2ZmIn19fQ=="));
        categoryThree.add(new MobCreature("Cave Spider", EntityType.CAVE_SPIDER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWNjYzRhMzJkNDVkNzRlOGIxNGVmMWZmZDU1Y2Q1ZjM4MWEwNmQ0OTk5MDgxZDUyZWFlYTEyZTEzMjkzZTIwOSJ9fX0="));
        categoryThree.add(new MobCreature("Bee", EntityType.BEE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWNhYWE3ZmMwMzk2ODUwMDg4YmJkZDBmODAwN2UyYzFhMjlhYmM5MDg4YzZkMjRiNzk0M2RiNjVlYmM4MTRmOCJ9fX0="));
        categoryThree.add(new MobCreature("Glow Squid", EntityType.GLOW_SQUID, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDMxYmJlY2RhNTgyMDEzMTQ0YWFiOGMwOWFiZTI5YTIxYTEyNDNiOTE4MzI3YTRjNWNkNDAyYzJhOTU0MTgwZiJ9fX0="));
        categoryThree.add(new MobCreature("Rabbit", EntityType.RABBIT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE2MzYxZmVhMjRiMTExZWQ3OGMxZmVmYzI5NTIxMmU4YTU5YjBjODhiNjU2MDYyNTI3YjE3YTJkNzQ4OWM4MSJ9fX0="));
        categoryThree.add(new MobCreature("Goat", EntityType.GOAT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY2MjMzNmQ4YWUwOTI0MDdlNThmN2NjODBkMjBmMjBlNzY1MDM1N2E0NTRjZTE2ZTMzMDc2MTlhMDExMDY0OCJ9fX0="));
        categoryThree.add(new MobCreature("Fox", EntityType.FOX, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc0NjRjYTUxOGIwMDJmYTk5Mjc1ZDNjOTgxZmIxOWI0MWI0NDVhZGIxOWU3NTg0MzBjYjI4ZDUxNTYwMDAzNyJ9fX0="));
        categoryThree.add(new MobCreature("Parrot", EntityType.PARROT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRmNGIzNDAxYTRkMDZhZDY2YWM4YjVjNGQxODk2MThhZTYxN2Y5YzE0MzA3MWM4YWMzOWE1NjNjZjRlNDIwOCJ9fX0="));
        categoryThree.add(new MobCreature("Snow Golem", EntityType.SNOWMAN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZkZmQxZjc1MzhjMDQwMjU4YmU3YTkxNDQ2ZGE4OWVkODQ1Y2M1ZWY3MjhlYjVlNjkwNTQzMzc4ZmNmNCJ9fX0="));
        categoryThree.add(new MobCreature("Turtle", EntityType.TURTLE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGE0MDUwZTdhYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ=="));
        categoryThree.add(new MobCreature("Husk", EntityType.HUSK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA5NjE2NGY4MTk1MGE1Y2MwZTMzZTg3OTk5Zjk4Y2RlNzkyNTE3ZjRkN2Y5OWE2NDdhOWFlZGFiMjNhZTU4In19fQ=="));
        categoryThree.add(new MobCreature("Witch", EntityType.WITCH, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U3MWE2ZWIzMDNhYjdlNmY3MGVkNTRkZjkxNDZhODBlYWRmMzk2NDE3Y2VlOTQ5NTc3M2ZmYmViZmFkODg3YyJ9fX0="));
        categoryThree.add(new MobCreature("Zombie Villager", EntityType.ZOMBIE_VILLAGER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM3NTA1ZjIyNGQ1MTY0YTExN2Q4YzY5ZjAxNWY5OWVmZjQzNDQ3MWM4YTJkZjkwNzA5NmM0MjQyYzM1MjRlOCJ9fX0="));
        mobs.put(3, categoryThree);

        List<MobCreature> categoryFour = new ArrayList<>();
        categoryFour.add(new MobCreature("Mule", EntityType.MULE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0ODZhNzQyZTdkZGEwYmFlNjFjZTJmNTVmYTEzNTI3ZjFjM2IzMzRjNTdjMDM0YmI0Y2YxMzJmYjVmNWYifX19"));
        categoryFour.add(new MobCreature("Frog", EntityType.FROG, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk1Njk0ZmM4ZjBkMDIwZGVhNmM2ZTVkZDdmZWM1ZTBlOTUzZTM1ZjdiMzU4Y2E3YTIwYjBiOWM3Y2MzZjBmNiJ9fX0="));
        categoryFour.add(new MobCreature("Strider", EntityType.STRIDER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTI0NWU0NzYwYWJmMTBmMjkwMDYyNjkxNGNmNDJmODA0NDBjZDUzMDk5YWU1NTI5NTM0ZjU5ODI0MDY3ZGFkNiJ9fX0="));
        categoryFour.add(new MobCreature("Ocelot", EntityType.OCELOT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAzYTJlMzc0MThlMGNmZmFhMmI1MTM5MTBjNTI4MmI5YmIwNmMzNWExZDQ3MDM5YTVjYzUxYjIzNGE1NDJmMyJ9fX0="));
        categoryFour.add(new MobCreature("Camel", EntityType.CAMEL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE0Yzk1YmZhMGI2MTcyMjI1NTM4OTE0MWI1MDVjZjFhMzhiYWQ5YjBlZjU0M2RlNjE5ZjBjYzkyMjFlZDk3NCJ9fX0="));
        categoryFour.add(new MobCreature("LLama", EntityType.LLAMA, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY3ZDkwYjMwNWFhNjQzMTNjOGQ0NDA0ZDhkNjUyYTk2ZWJhOGE3NTRiNjdmNDM0N2RjY2NkZDVhNmE2MzM5OCJ9fX0="));
        categoryFour.add(new MobCreature("Enderman", EntityType.ENDERMAN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA5ZjFkZTYxMzVmNGJlYTc4MWM1YThlMGQ2MTA5NWY4MzNlZTI2ODVkODE1NGVjZWE4MTRlZTZkMzI4YTVjNiJ9fX0="));
        categoryFour.add(new MobCreature("Iron Golem", EntityType.IRON_GOLEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTEzZjM0MjI3MjgzNzk2YmMwMTcyNDRjYjQ2NTU3ZDY0YmQ1NjJmYTlkYWIwZTEyYWY1ZDIzYWQ2OTljZjY5NyJ9fX0="));
        categoryFour.add(new MobCreature("Panda", EntityType.PANDA, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjIzYjIzN2EwZjI3ZDUwNWZjZDkzMWI2ZTljMjkzM2E3NDY3ODdjYzEwODQxNGY0ZjgyMzcwNzk1YmUwZmI5In19fQ=="));
        categoryFour.add(new MobCreature("Guardian", EntityType.GUARDIAN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk1MjkwZTA5MGMyMzg4MzJiZDc4NjBmYzAzMzk0OGM0ZDAzMTM1MzUzM2FjOGY2NzA5ODgyM2I3ZjY2N2YxYyJ9fX0="));
        categoryFour.add(new MobCreature("Endermite", EntityType.ENDERMITE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJjN2I5ZDM2ZmI5MmI2YmYyOTJiZTczZDMyYzZjNWIwZWNjMjViNDQzMjNhNTQxZmFlMWYxZTY3ZTM5M2EzZSJ9fX0="));
        categoryFour.add(new MobCreature("Ghast", EntityType.GHAST, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIyOGFhNTA0Yzg4MzAxY2I3NDJhNzI1YzU1OGRiYjExMmJjMGU0MjUyNjVmZjVkNGE4OTg2MTZmMDEyZWFhYSJ9fX0="));
        categoryFour.add(new MobCreature("Hoglin", EntityType.HOGLIN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJiOWJjMGYwMWRiZDc2MmEwOGQ5ZTc3YzA4MDY5ZWQ3Yzk1MzY0YWEzMGNhMTA3MjIwODU2MWI3MzBlOGQ3NSJ9fX0="));
        categoryFour.add(new MobCreature("Phantom", EntityType.PHANTOM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U5NTE1M2VjMjMyODRiMjgzZjAwZDE5ZDI5NzU2ZjI0NDMxM2EwNjFiNzBhYzAzYjk3ZDIzNmVlNTdiZDk4MiJ9fX0="));
        categoryFour.add(new MobCreature("Pillager", EntityType.PILLAGER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJmYjgwYTZiNjgzM2UzMWQ5Y2U4MzEzYTU0Nzc3NjQ1ZjljMWU1NWI4MTA5MThhNzA2ZTdiY2M4ZDM1YTVhMiJ9fX0="));
        categoryFour.add(new MobCreature("Stray", EntityType.STRAY, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjU3Mjc0N2E2MzlkMjI0MGZlZWFlNWM4MWM2ODc0ZTZlZTc1NDdiNTk5ZTc0NTQ2NDkwZGM3NWZhMjA4OTE4NiJ9fX0="));
        categoryFour.add(new MobCreature("Vex", EntityType.VEX, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY2MzEzNGQ3MzA2YmI2MDQxNzVkMjU3NWQ2ODY3MTRiMDQ0MTJmZTUwMTE0MzYxMWZjZjNjYzE5YmQ3MGFiZSJ9fX0="));
        categoryFour.add(new MobCreature("Vindicator", EntityType.VINDICATOR, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlZWQ5ZDhlZDE3NjllNzdlM2NmZTExZGMxNzk2NjhlZDBkYjFkZTZjZTI5ZjFjOGUwZDVmZTVlNjU3M2I2MCJ9fX0="));
        categoryFour.add(new MobCreature("Wither Skeleton", EntityType.WITHER_SKELETON, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg2ZGMwY2ZjYWVlY2ZlMWFiNjkxNDZlNGQ0ZjExOTA4MzcwNzZhNjdkZWMxMzVmYWJkYTYyNzFmMzc1ZDAxZiJ9fX0="));
        categoryFour.add(new MobCreature("Zoglin", EntityType.ZOGLIN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3ZTE4NjAyZTAzMDM1YWQ2ODk2N2NlMDkwMjM1ZDg5OTY2NjNmYjllYTQ3NTc4ZDNhN2ViYmM0MmE1Y2NmOSJ9fX0="));
        mobs.put(4, categoryFour);

        List<MobCreature> categoryFive = new ArrayList<>();
        categoryFive.add(new MobCreature("Allay", EntityType.ALLAY, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVlYTg0NWNjMGI1OGZmNzYzZGVjZmZlMTFjZDFjODQ1YzVkMDljM2IwNGZlODBiMDY2M2RhNWM3YzY5OWViMyJ9fX0="));
        categoryFive.add(new MobCreature("Tadpole", EntityType.TADPOLE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIzZWJmMjZiN2E0NDFlMTBhODZmYjVjMmE1ZjNiNTE5MjU4YTVjNWRkZGQ2YTFhNzU1NDlmNTE3MzMyODE1YiJ9fX0="));
        categoryFive.add(new MobCreature("Skeleton Horse", EntityType.SKELETON_HORSE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ=="));
        categoryFive.add(new MobCreature("Dolphin", EntityType.DOLPHIN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ=="));
        categoryFive.add(new MobCreature("Polar Bear", EntityType.POLAR_BEAR, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQyMTIzYWMxNWVmZmExYmE0NjQ2MjQ3Mjg3MWI4OGYxYjA5YzFkYjQ2NzYyMTM3NmUyZjcxNjU2ZDNmYmMifX19"));
        categoryFive.add(new MobCreature("Piglin", EntityType.PIGLIN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxYjNhZWUxODJiOWE5OWVkMjZjYmY1ZWNiNDdhZTkwYzJjM2FkYzA5MjdkZGUxMDJjN2IzMGZkZjdmNDU0NSJ9fX0="));
        categoryFive.add(new MobCreature("Piglin Brute", EntityType.PIGLIN_BRUTE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzMDBlOTAyNzM0OWM0OTA3NDk3NDM4YmFjMjllM2E0Yzg3YTg0OGM1MGIzNGMyMTI0MjcyN2I1N2Y0ZTFjZiJ9fX0="));
        categoryFive.add(new MobCreature("Zombified Piglin", EntityType.ZOMBIFIED_PIGLIN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2VhYmFlY2M1ZmFlNWE4YTQ5Yzg4NjNmZjQ4MzFhYWEyODQxOThmMWEyMzk4ODkwYzc2NWUwYThkZTE4ZGE4YyJ9fX0="));
        categoryFive.add(new MobCreature("Blaze", EntityType.BLAZE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIwNjU3ZTI0YjU2ZTFiMmY4ZmMyMTlkYTFkZTc4OGMwYzI0ZjM2Mzg4YjFhNDA5ZDBjZDJkOGRiYTQ0YWEzYiJ9fX0="));
        categoryFive.add(new MobCreature("Magma Cube", EntityType.MAGMA_CUBE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFjOTdhMDZlZmRlMDRkMDAyODdiZjIwNDE2NDA0YWIyMTAzZTEwZjA4NjIzMDg3ZTFiMGMxMjY0YTFjMGYwYyJ9fX0="));
        mobs.put(5, categoryFive);

        List<MobCreature> categorySix = new ArrayList<>();
        categorySix.add(new MobCreature("Mooshroom Cow", EntityType.MUSHROOM_COW, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI1Mjg0MWYyZmQ1ODllMGJjODRjYmFiZjllMWMyN2NiNzBjYWM5OGY4ZDZiM2RkMDY1ZTU1YTRkY2I3MGQ3NyJ9fX0="));
        categorySix.add(new MobCreature("Elder Guardian", EntityType.ELDER_GUARDIAN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTkyMDg5NjE4NDM1YTBlZjYzZTk1ZWU5NWE5MmI4MzA3M2Y4YzMzZmE3N2RjNTM2NTE5OWJhZDMzYjYyNTYifX19"));
        categorySix.add(new MobCreature("Evoker", EntityType.EVOKER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc5ZjEzM2E4NWZlMDBkM2NmMjUyYTA0ZDZmMmViMjUyMWZlMjk5YzA4ZTBkOGI3ZWRiZjk2Mjc0MGEyMzkwOSJ9fX0="));
        categorySix.add(new MobCreature("Ravager", EntityType.RAVAGER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM3M2UxNmZhMjkyNjg5OWNmMTg0MzQzNjBlMjE0NGY4NGVmMWViOTgxZjk5NjE0ODkxMjE0OGRkODdlMGIyYSJ9fX0="));
        categorySix.add(new MobCreature("Shulker", EntityType.SHULKER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUzM2ViZDEyYWU2ZGJmYTIzNDRkZjE2ZGE4ZmM2ZjM1OTdmZjQ4MDE3ZmJlMzgzYWJkMTY2OWNiZjU0NTYyZCJ9fX0="));
        mobs.put(6, categorySix);

        List<MobCreature> categorySeven = new ArrayList<>();
        categorySeven.add(new MobCreature("Enemy Player", EntityType.PLAYER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk2NGJmNDhjYTAxOTA4YTI5MjFkZTJhY2RmZjk0MjExNGU4ODFiNTNiMjI0Y2FiNGI1ZWJjZjBkODk3M2UxMSJ9fX0="));
        categorySeven.add(new MobCreature("Warden", EntityType.WARDEN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM5Yzg0MzQ5NzQyMTY0YTIyOTcxZWU1NDUxNmZmZjkxZDg2OGRhNzJjZGNjZTYyMDY5ZGIxMjhjNDIxNTRiMiJ9fX0="));
        categorySeven.add(new MobCreature("Sniffer", EntityType.SNIFFER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdhZDkyMGE2NmUzOGNjMzQyNmE1YmZmMDg0NjY3ZTg3NzIxMTY5MTVlMjk4MDk4NTY3YzEzOWYyMjJlMmM0MiJ9fX0="));
        mobs.put(7, categorySeven);

        List<MobCreature> categoryEight = new ArrayList<>();
        categoryEight.add(new MobCreature("Wither", EntityType.WITHER, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyODBjZWZlOTQ2OTExZWE5MGU4N2RlZDFiM2UxODMzMGM2M2EyM2FmNTEyOWRmY2ZlOWE4ZTE2NjU4ODA0MSJ9fX0="));
        mobs.put(8, categoryEight);

        List<MobCreature> categoryNine = new ArrayList<>();
        categoryNine.add(new MobCreature("Ender Dragon", EntityType.ENDER_DRAGON, ""));
        mobs.put(9, categoryNine);
    }
}
