package org.dlds.mobbattle.objects;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.dlds.mobbattle.services.CategoryService;

import java.util.*;

public class ClockInventory implements Listener {
    private final int pageSize = 54;
    private final Map<Integer, Inventory> pages;
    private List<Category> categories;
    private List<MobCreature> killedMobs;
    private int currentPoints;
    private int currentPage = 0;

    public ClockInventory() {
        CategoryService categoryService = new CategoryService();
        this.categories = categoryService.initializeCategories();
        this.killedMobs = new ArrayList<>();
        this.pages = new HashMap<>();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Map<Integer, Inventory> getPages() {
        return pages;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<MobCreature> getKilledMobs() {
        return killedMobs;
    }

    public void setKilledMobs(List<MobCreature> killedMobs) {
        this.killedMobs = killedMobs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Category getCategoryForEntityType(EntityType entityType) {
        for (Category category : categories) {
            for (MobCreature mobCreature : category.getMobs()) {
                if (mobCreature.getEntityType().equals(entityType)) {
                    return category;
                }
            }
        }
        return null;
    }

    public ItemStack getCustomSkull(String base64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", base64));
        skullMeta.setPlayerProfile(profile);

        skull.setItemMeta(skullMeta);
        return skull;
    }

    private void fillEmptySlotsWithGlassPanes(Inventory inv) {
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = glassPane.getItemMeta();
        meta.displayName(Component.text(""));
        glassPane.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null || Objects.requireNonNull(inv.getItem(i)).getType() == Material.AIR) {
                inv.setItem(i, glassPane);
            }
        }
    }

    private void addNavigationItem(Inventory inv, String base64, String name, int slot) {
        ItemStack item = getCustomSkull(base64);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name, NamedTextColor.GREEN));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    private void addPlayerHeadToPointsPage(Inventory pointsPage, Player player) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        playerMeta.setOwningPlayer(player);
        playerMeta.displayName(Component.text("back to main page", NamedTextColor.GOLD));
        playerHead.setItemMeta(playerMeta);
        pointsPage.setItem(pageSize - 5, playerHead);
    }

    private String convertItemNameToString(ItemStack reward) {
        String rewardName = "";

        switch (reward.getType()) {
            case IRON_INGOT:
                rewardName += "Iron Ingot";
                break;
            case DIAMOND:
                rewardName += "Diamond";
                break;
            case NETHERITE_INGOT:
                rewardName += "Netherite Ingot";
                break;
            case NETHERITE_UPGRADE_SMITHING_TEMPLATE:
                rewardName += "Netherite Upgrade Smithing Template";
                break;
            case SHULKER_BOX:
                rewardName += "Shulker Box";
                break;
            case ENCHANTED_GOLDEN_APPLE:
                rewardName += "Enchanted Golden Apple";
                break;
            case OAK_LOG:
                rewardName += "Oak Log";
                break;
            case ARROW:
                rewardName += "Arrow";
                break;
            case GOLDEN_APPLE:
                rewardName += "Golden Apple";
                break;
            default:
                break;
        }

        rewardName += " x ";

        rewardName += reward.getAmount();

        return rewardName;
    }

    public void initializePages(Player player) {

        String previousPageBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==";
        String nextPageBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19";

        Inventory pointsPage = Bukkit.createInventory(null, pageSize, Component.text("Player Points", NamedTextColor.BLACK));

        for (Category category : categories) {
            boolean allMobsKilled = new HashSet<>(killedMobs).containsAll(category.getMobs());

            ItemStack categoryHead = getCustomSkull(category.getBase64HeadString());
            SkullMeta meta = (SkullMeta) categoryHead.getItemMeta();

            NamedTextColor textColor = allMobsKilled ? NamedTextColor.RED : NamedTextColor.GREEN;
            meta.displayName(Component.text("Category: " + category.getCategoryNumber(), textColor));

            categoryHead.setItemMeta(meta);

            if (category.getCategoryNumber() < 5) {
                pointsPage.setItem((20 + category.getCategoryNumber()), categoryHead);
            } else {
                pointsPage.setItem((24 + category.getCategoryNumber()), categoryHead);
            }

        }

        ItemStack totalPointsHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) totalPointsHead.getItemMeta();
        meta.setOwningPlayer(player);
        totalPointsHead.setItemMeta(meta);
        ItemMeta totalPointsMeta = totalPointsHead.getItemMeta();
        totalPointsMeta.displayName(Component.text("Total Points: " + currentPoints, NamedTextColor.GOLD));
        totalPointsHead.setItemMeta(totalPointsMeta);
        pointsPage.setItem(20, totalPointsHead);

        addNavigationItem(pointsPage, nextPageBase64, "Next Site", pageSize - 1);
        fillEmptySlotsWithGlassPanes(pointsPage);
        pages.put(0, pointsPage);

        int pageIndex = 1;
        for (Category category : categories) {
            Inventory categoryPage = Bukkit.createInventory(null, pageSize, Component.text("Category: " + category.getCategoryNumber(), NamedTextColor.BLACK));
            initializeCategoryPage(categoryPage, category);
            addNavigationItem(categoryPage, previousPageBase64, "Previous Site", 45);
            if (pageIndex < categories.size()) {
                addNavigationItem(categoryPage, nextPageBase64, "Next Site", pageSize - 1);
            }
            addPlayerHeadToPointsPage(categoryPage, player);
            fillEmptySlotsWithGlassPanes(categoryPage);
            pages.put(pageIndex, categoryPage);
            pageIndex++;
        }
    }

    private void initializeCategoryPage(Inventory page, Category category) {
        int slot = 10;
        int mobsInRow = 0;
        for (MobCreature mob : category.getMobs()) {
            ItemStack mobHead;

            if (mob.getMobHead() != null && !mob.getMobHead().isEmpty() && !mob.getName().equals("Ender Dragon")) {
                mobHead = getCustomSkull(mob.getMobHead());
            } else if (mob.getName().equals("Ender Dragon")) {
                mobHead = new ItemStack(Material.DRAGON_HEAD);
            } else {
                mobHead = new ItemStack(Material.PLAYER_HEAD);
            }

            SkullMeta meta = (SkullMeta) mobHead.getItemMeta();

            if (mob.isDead()) {
                meta.displayName(Component.text(mob.getName(), NamedTextColor.RED));
            } else {
                meta.displayName(Component.text(mob.getName(), NamedTextColor.GREEN));
            }

            mobHead.setItemMeta(meta);
            page.setItem(slot, mobHead);

            mobsInRow++;

            if (mobsInRow == 7) {
                mobsInRow = 0;
                slot += 3;
            } else {
                slot++;
            }
        }

        ItemStack mainRewardsItem = new ItemStack(Material.EMERALD);
        ItemMeta mainRewardsMeta = mainRewardsItem.getItemMeta();
        mainRewardsMeta.displayName(Component.text("Main Rewards", NamedTextColor.GREEN));
        List<Component> mainRewardsLore = new ArrayList<>();
        for (ItemStack reward : category.getMainRewards()) {
            String rewardName = convertItemNameToString(reward);
            mainRewardsLore.add(Component.text(rewardName, NamedTextColor.WHITE));
        }
        mainRewardsMeta.lore(mainRewardsLore);
        mainRewardsItem.setItemMeta(mainRewardsMeta);
        page.setItem(pageSize - 7, mainRewardsItem);

        ItemStack luckyRewardsItem = new ItemStack(Material.DIAMOND);
        ItemMeta luckyRewardsMeta = luckyRewardsItem.getItemMeta();
        luckyRewardsMeta.displayName(Component.text("Luck Rewards", NamedTextColor.BLUE));
        List<Component> luckyRewardsLore = new ArrayList<>();
        for (ItemStack reward : category.getLuckyRewards()) {
            String rewardName = convertItemNameToString(reward);
            luckyRewardsLore.add(Component.text(rewardName, NamedTextColor.WHITE));
        }
        luckyRewardsMeta.lore(luckyRewardsLore);
        luckyRewardsItem.setItemMeta(luckyRewardsMeta);
        page.setItem(pageSize - 3, luckyRewardsItem);
    }

    public boolean updateMobKill(EntityType entityType, Player player) {

        if (killedMobs.stream().anyMatch(x -> x.getEntityType() == entityType)) {
            return false;
        }

        for (Category category : categories) {
            for (MobCreature mobCreature : category.getMobs()) {
                if (mobCreature.getEntityType().equals(entityType)) {
                    if (mobCreature.isDead()) {
                        return false;
                    }
                    int points = category.getPoints();

                    mobCreature.killedMob();
                    killedMobs.add(mobCreature);
                    updatePoints(points);

                    player.sendActionBar(Component.text("+" + points, NamedTextColor.GREEN));

                    return true;
                }
            }
        }
        return true;
    }

    public void updatePoints(int points) {
        this.currentPoints += points;
    }
}