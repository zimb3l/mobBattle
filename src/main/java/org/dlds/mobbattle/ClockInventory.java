package org.dlds.mobbattle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ClockInventory implements Listener {
    private final Inventory inv;

    public ClockInventory() {
        // Create a new inventory with 64 slots
        inv = Bukkit.createInventory(null, 63, Component.text("Points Showcase", NamedTextColor.GOLD));

        // Add mob heads to the inventory
        // TODO: Replace with actual mob heads and their associated point values
        Material[] mobHeads = {
                Material.CREEPER_HEAD,
                Material.ZOMBIE_HEAD,
                Material.SKELETON_SKULL,
                Material.WITHER_SKELETON_SKULL,
                //TODO: Add more mob heads like:
                //   Material.PLAYER_HEAD
        };
    }

    @EventHandler
public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();

    // Check if the player is right-clicking a clock
    if (item != null && item.getType() == Material.CLOCK && event.getAction().name().contains("RIGHT_CLICK")) {
        Player player = event.getPlayer();

        // Get the player's head
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwningPlayer(player);

        // Get the player's current points
        // TODO: Replace with actual method to get player's points
        int points = 0;//getPlayerPoints(player);

        // Set the display name of the player's head to their current points
        Component displayName = Component.text("Points: " + points, NamedTextColor.GREEN);
        meta.displayName(displayName);
        playerHead.setItemMeta(meta);

        // Set the player's head in the center of the first line of the inventory
        inv.setItem(4, playerHead);

        player.openInventory(inv);
    }
}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is the showcase inventory
        if (event.getInventory().equals(inv)) {
            // Cancel the event to prevent items from being taken or put into the inventory
            event.setCancelled(true);
        }
    }
}

//TODO:Group and sort into different categories based on how hard they are to kill
/*
* Passive Mobs (harmless, can be bred or tamed):

    Category 1: 1 point
        Cow
        Sheep
        Pig
        Chicken
        Squid

    Category 2: 3 points
        Zombie
        Horse
        Donkey
        Creeper
        Skeleton
        Villager/Wandering Trader
        Wolf
        Cat
        Slime
        Drowned
        Spider
        Silverfish
        Bat

    Category 3: 5 points
        Axolotl
        Cave Spider
        Bee
        Glow Squid
        Rabbit
        Goat
        Fox
        Parrot
        Snow Golem
        Turtle
        Husk
        Witch
        Zombie Villager

    Category 4: 10 points
        Mule
        Frog
        Strider
        Ocelot
        Camel
        Llama
        Enderman
        Iron Golem
        Panda
        Guardian
        Endermite
        Ghast
        Hoglin
        Phantom
        Pillager
        Stray
        Vex
        Vindicator
        Wither Skeleton
        Zoglin

    Category 5: 25 points
        Allay
        Tadpole
        Skeleton Horse
        Dolphin
        Polar Bear
        Piglin
        Piglin Brute
        Zombified Piglin
        Blaze
        Magma Cube

    Category 6: 50 points
        Mooshroom
        Elder Guardian
        Evoker
        Ravager
        Shulker

    Category 7: 100 points
        Player
        Warden
        Sniffer

    Category 8: 150 points
        Wither

    Category 9: 250 points
        Ender Dragon
 */