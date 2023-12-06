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
        initializePages();
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

    private void initializePages(){
        Inventory pointsPage = Bukkit.createInventory(null, pageSize, Component.text("Spieler Punkte", NamedTextColor.GOLD));
        pages.put(0, pointsPage);

        int pageIndex = 1;
        for (Category category : categories) {
            Inventory categoryPage = Bukkit.createInventory(null, pageSize, Component.text("Kategorie: " + category.getCategoryNumber(), NamedTextColor.GOLD));
            initializeCategoryPage(categoryPage, category);
            pages.put(pageIndex, categoryPage);
            pageIndex++;
        }
    }

    private void initializeCategoryPage(Inventory page, Category category) {
        for (MobCreature mob : category.getMobs()) {
            ItemStack mobHead;
            System.out.println("initializeCategoryPage mobName: " + mob.getName() +  ", mobHeadString: " + mob.getMobHead());
            if(mob.getMobHead() != null && !mob.getMobHead().isEmpty()){
                mobHead = getCustomSkull(mob.getMobHead());
            } else {
                mobHead = new ItemStack(Material.PLAYER_HEAD);
            }

            SkullMeta meta = (SkullMeta) mobHead.getItemMeta();
            meta.displayName(Component.text(mob.getName(), NamedTextColor.GREEN));

            if (killedMobs.contains(mob)) {
                mobHead.addUnsafeEnchantment(Enchantment.LUCK, 1);
            }

            mobHead.setItemMeta(meta);
            page.addItem(mobHead);
        }

        ItemStack mainRewardsItem = new ItemStack(Material.EMERALD);
        ItemMeta mainRewardsMeta = mainRewardsItem.getItemMeta();
        mainRewardsMeta.displayName(Component.text("Hauptbelohnungen", NamedTextColor.GREEN));
        List<Component> mainRewardsLore = new ArrayList<>();
        for (ItemStack reward : category.getMainRewards()) {
            mainRewardsLore.add(Component.text(reward.getType().name(), NamedTextColor.WHITE));
        }
        mainRewardsMeta.lore(mainRewardsLore);
        mainRewardsItem.setItemMeta(mainRewardsMeta);
        page.setItem(25, mainRewardsItem);

        ItemStack luckyRewardsItem = new ItemStack(Material.DIAMOND);
        ItemMeta luckyRewardsMeta = luckyRewardsItem.getItemMeta();
        luckyRewardsMeta.displayName(Component.text("Glücksbelohnungen", NamedTextColor.BLUE));
        List<Component> luckyRewardsLore = new ArrayList<>();
        for (ItemStack reward : category.getLuckyRewards()) {
            luckyRewardsLore.add(Component.text(reward.getType().name(), NamedTextColor.WHITE));
        }
        luckyRewardsMeta.lore(luckyRewardsLore);
        luckyRewardsItem.setItemMeta(luckyRewardsMeta);
        page.setItem(26, luckyRewardsItem);
    }

    private void initializeInventory() {
        Inventory pointsPage = pages.get(0);
        pointsPage.clear();

        for (Category category : categories) {
            int categoryPoints = category.getPoints();
            boolean allMobsKilled = category.getMobs().stream().allMatch(killedMobs::contains);

            ItemStack categoryHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) categoryHead.getItemMeta();
            meta.displayName(Component.text("Kategorie: " + category.getCategoryNumber() + ": " + categoryPoints + " Punkte", NamedTextColor.GREEN));

            if (allMobsKilled) {
                categoryHead.addUnsafeEnchantment(Enchantment.LUCK, 1);
            }

            pointsPage.addItem(categoryHead);
        }

        ItemStack totalPointsItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta totalPointsMeta = totalPointsItem.getItemMeta();
        totalPointsMeta.displayName(Component.text("Gesamtpunkte: " + currentPoints, NamedTextColor.GOLD));
        totalPointsItem.setItemMeta(totalPointsMeta);
        pointsPage.setItem(pageSize - 1, totalPointsItem);

        for (int i = 1; i <= categories.size(); i++) {
            Inventory categoryPage = pages.get(i);
            Category category = categories.get(i - 1);
            initializeCategoryPage(categoryPage, category);
        }
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

        initializeInventory();

        player.openInventory(pages.get(0));
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

            if (displayName.contains("Nächste Seite")) {
                currentPage = (currentPage + 1) % pages.size();
                player.openInventory(pages.get(currentPage));
            } else if (displayName.contains("Vorherige Seite")) {
                currentPage = (currentPage - 1 + pages.size()) % pages.size();
                player.openInventory(pages.get(currentPage));
            }
        }
    }
}