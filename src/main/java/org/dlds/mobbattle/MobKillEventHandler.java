package org.dlds.mobbattle;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;


public class MobKillEventHandler {

    private final ClockInventoryRepository clockInventoryRepository = new ClockInventoryRepository();
    private final CategoryService categoryService = new CategoryService();
    private final Random random = new Random();

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        if(event.getEntity().getKiller() != null){
            Player player = event.getEntity().getKiller();
            UUID playerId = player.getUniqueId();
            ClockInventory clockInventory = clockInventoryRepository.getInventory(playerId);

            EntityType entityType = event.getEntityType();

            int points = clockInventory.updateMobKill(entityType);

            if(points == 0){
                return;
            }

            clockInventory.updatePoints(points);

            Category category = categoryService.getCategoryForEntityType(entityType);

            for(ItemStack reward : category.getMainRewards()){
                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), reward);
            }

            if(random.nextDouble() < 0.10)
            for(ItemStack luckyReward : category.getLuckyRewards()){
                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), luckyReward);
            } else if(random.nextDouble() < 0.25){
                List<ItemStack> luckyRewards = category.getLuckyRewards();
                ItemStack randomLuckyReward = luckyRewards.get(random.nextInt(luckyRewards.size()));
                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), randomLuckyReward);
            }
        }
    }
}
