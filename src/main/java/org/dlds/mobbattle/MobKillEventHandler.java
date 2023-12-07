package org.dlds.mobbattle;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;


public class MobKillEventHandler implements Listener {

    private final ClockInventoryRepository clockInventoryRepository = ClockInventoryRepository.getInstance();
    private final Random random = new Random();

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        if(event.getEntity().getKiller() != null){
            Player player = event.getEntity().getKiller();
            UUID playerId = player.getUniqueId();
            ClockInventory clockInventory = clockInventoryRepository.getInventory(playerId);

            EntityType entityType = event.getEntityType();

            if(!clockInventory.updateMobKill(entityType)){
                return;
            }

            System.out.println("clockInventory onMobDeath: " + clockInventory.getCurrentPoints());

            Category category = clockInventory.getCategoryForEntityType(entityType);

            if(category == null){
                return;
            }

            for(ItemStack reward : category.getMainRewards()){
                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), reward);
            }

            if(random.nextDouble() < 0.05) {
                for (ItemStack luckyReward : category.getLuckyRewards()) {
                    player.getWorld().dropItemNaturally(event.getEntity().getLocation(), luckyReward);
                }
            } else if(random.nextDouble() < 0.15){
                List<ItemStack> luckyRewards = category.getLuckyRewards();
                ItemStack randomLuckyReward = luckyRewards.get(random.nextInt(luckyRewards.size()));
                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), randomLuckyReward);
            }
        }
    }
}