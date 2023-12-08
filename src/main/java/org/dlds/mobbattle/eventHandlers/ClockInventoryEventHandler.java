package org.dlds.mobbattle.eventHandlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dlds.mobbattle.objects.Category;
import org.dlds.mobbattle.objects.ClockInventory;
import org.dlds.mobbattle.repositorys.ClockInventoryRepository;

import java.util.UUID;

public class ClockInventoryEventHandler implements Listener {

    private final ClockInventoryRepository clockInventoryRepository = ClockInventoryRepository.getInstance();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.CLOCK && event.getAction().name().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            UUID playerId = player.getUniqueId();
            ClockInventory clockInventory = clockInventoryRepository.getInventory(playerId);

            clockInventory.initializePages(player);

            player.openInventory(clockInventory.getPages().getOrDefault(clockInventory.getCurrentPage(), clockInventory.getPages().get(0)));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        UUID playerId = player.getUniqueId();
        ClockInventory clockInventory = clockInventoryRepository.getInventory(playerId);

        if (clockInventory.getPages().containsValue(clickedInventory)) {
            event.setCancelled(true);
        }

        if (clickedItem != null && clickedItem.hasItemMeta()) {
            ItemMeta meta = clickedItem.getItemMeta();
            Component displayNameComponent = meta.displayName();

            if (displayNameComponent != null) {
                String displayName = LegacyComponentSerializer.legacySection().serialize(displayNameComponent);

                for (int i = 0; i < clockInventory.getCategories().size(); i++) {
                    Category category = clockInventory.getCategories().get(i);
                    if (displayName.contains("Category: " + category.getCategoryNumber())) {
                        clockInventory.setCurrentPage((i + 1));
                        player.openInventory(clockInventory.getPages().getOrDefault(clockInventory.getCurrentPage(), clockInventory.getPages().get(0)));
                        return;
                    }
                }

                if (displayName.contains("Next Site")) {
                    clockInventory.setCurrentPage(((clockInventory.getCurrentPage() + 1) % clockInventory.getPages().size()));
                    player.openInventory(clockInventory.getPages().getOrDefault(clockInventory.getCurrentPage(), clockInventory.getPages().get(0)));
                } else if (displayName.contains("Previous Site")) {
                    clockInventory.setCurrentPage(((clockInventory.getCurrentPage() - 1) % clockInventory.getPages().size()));
                    player.openInventory(clockInventory.getPages().getOrDefault(clockInventory.getCurrentPage(), clockInventory.getPages().get(0)));
                }

                if (displayName.contains("back to main page")) {
                    clockInventory.setCurrentPage(0);
                    player.openInventory(clockInventory.getPages().get(clockInventory.getCurrentPage()));
                }
            }
        }
    }
}
