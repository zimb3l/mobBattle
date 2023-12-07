package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import java.util.UUID;

import java.util.*;

public class ClockInventory implements Listener {
    private List<Category> categories;
    private CategoryService categoryService = new CategoryService();
    private List<MobCreature> killedMobs = new ArrayList<>();
    private Map<Integer, Inventory> pages = new HashMap<>();
    private int currentPoints = 0;
    private int currentPage = 0;
    private final int pageSize = 54;

    public ClockInventory() {
        this.categories = categoryService.initializeCategories();
    }

    public Category getCategoryForEntityType(EntityType entityType){
        for (Category category: categories) {
            for (MobCreature mobCreature: category.getMobs()) {
                if(mobCreature.getEntityType().equals(entityType)){
                    return category;
                }
            }
        }
        return null;
    }
    public int getCurrentPoints() {
        return currentPoints;
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
            if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
                inv.setItem(i, glassPane);
            }
        }
    }

    private void initializePages(Player player){

        String previousPageBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==";
        String nextPageBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19";

        Inventory pointsPage = Bukkit.createInventory(null, pageSize, Component.text("Player Points", NamedTextColor.BLACK));

        for (Category category : categories) {

            ItemStack categoryHead = getCustomSkull(category.getBase64HeadString());
            SkullMeta meta = (SkullMeta) categoryHead.getItemMeta();
            meta.displayName(Component.text("Category: " + category.getCategoryNumber(), NamedTextColor.GREEN));

            categoryHead.setItemMeta(meta);

            if(category.getCategoryNumber() < 5){
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
            fillEmptySlotsWithGlassPanes(categoryPage);
            pages.put(pageIndex, categoryPage);
            pageIndex++;
        }
    }

    private void addNavigationItem(Inventory inv, String base64, String name, int slot) {
        ItemStack item = getCustomSkull(base64);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name, NamedTextColor.GREEN));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    private String convertItemNameToString(ItemStack reward){
        String rewardName = "";

        switch (reward.getType()){
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

    private void initializeCategoryPage(Inventory page, Category category) {
        int slot = 10;
        int mobsInRow = 0;
        for (MobCreature mob : category.getMobs()) {
            ItemStack mobHead;

            if(mob.getMobHead() != null && !mob.getMobHead().isEmpty() && !mob.getName().equals("Ender Dragon")){
                mobHead = getCustomSkull(mob.getMobHead());
            } else if(mob.getName().equals("Ender Dragon")){
                mobHead = new ItemStack(Material.DRAGON_HEAD);
            } else {
                mobHead = new ItemStack(Material.PLAYER_HEAD);
            }

            SkullMeta meta = (SkullMeta) mobHead.getItemMeta();
            meta.displayName(Component.text(mob.getName(), NamedTextColor.GREEN));

            if (killedMobs.contains(mob)) {
                mobHead.addUnsafeEnchantment(Enchantment.LUCK, 1);
            }

            mobHead.setItemMeta(meta);
            page.setItem(slot, mobHead);

            mobsInRow++;

            if(mobsInRow == 7){
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
        page.setItem(pageSize - 6, mainRewardsItem);

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
        page.setItem(pageSize - 4, luckyRewardsItem);
    }

    public int updateMobKill(EntityType entityType){

        if(killedMobs.stream().anyMatch(x -> x.getEntityType() == entityType)){
            return 0;
        }

        for (Category category: categories) {
            for (MobCreature mobCreature: category.getMobs()) {
                if(mobCreature.getEntityType().equals(entityType)){
                    if(mobCreature.isDead()){
                        return 0;
                    }
                    mobCreature.killedMob();
                    killedMobs.add(mobCreature);
                    return category.getPoints();
                }
            }
        }
        return 0;
    }

    public void updatePoints(int points){
        this.currentPoints += points;
    }

    @EventHandler
public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();

    // Check if the player is right-clicking a clock
    if (item != null && item.getType() == Material.CLOCK && event.getAction().name().contains("RIGHT_CLICK")) {
        Player player = event.getPlayer();

        initializePages(player);

        player.openInventory(pages.getOrDefault(currentPage, pages.get(0)));
    }
}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if(pages.containsValue(event.getInventory())){
            event.setCancelled(true);
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().displayName() != null) {
            Component displayNameComponent = clickedItem.getItemMeta().displayName();
            assert displayNameComponent != null;
            String displayName = LegacyComponentSerializer.legacySection().serialize(displayNameComponent);

            if (displayName.contains("Next Site")) {
                currentPage = (currentPage + 1) % pages.size();
                player.openInventory(pages.get(currentPage));
            } else if (displayName.contains("Previous Site")) {
                currentPage = (currentPage - 1 + pages.size()) % pages.size();
                player.openInventory(pages.get(currentPage));
            }
        }
    }
}