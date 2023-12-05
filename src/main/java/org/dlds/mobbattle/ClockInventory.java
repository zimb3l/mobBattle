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

    Allay
    Axolotl
    Bat
    Cat
    Chicken
    Cod
    Cow
    Donkey
    Fox
    Frog
    Glow Squid
    Horse
    Mooshroom
    Mule
    Ocelot
    Parrot
    Pig
    Pufferfish (defensive)
    Rabbit
    Salmon
    Sheep
    Skeleton Horse
    Snow Golem
    Squid
    Strider
    Tadpole
    Tropical Fish
    Turtle
    Villager/Wandering Trader
    Camel
    Sniffer (Upcoming)

Neutral Mobs (do not attack unless provoked):

    Bee
    Cave Spider
    Dolphin
    Enderman
    Goat
    Iron Golem (naturally spawned)
    Llama
    Panda
    Piglin
    Polar Bear
    Spider
    Trader Llama
    Wolf
    Zombified Piglin

Hostile Mobs (attack on sight):

    Blaze
    Chicken Jockey
    Creeper
    Drowned
    Elder Guardian
    Endermite
    Evoker
    Ghast
    Guardian
    Hoglin
    Husk
    Magma Cube
    Phantom
    Piglin Brute
    Pillager
    Ravager
    Shulker
    Silverfish
    Skeleton
    Skeleton Horseman
    Slime
    Spider Jockey
    Stray
    Vex
    Vindicator
    Warden
    Witch
    Wither Skeleton
    Zoglin
    Zombie
    Zombie Villager

Boss Mobs (powerful, require strategy to defeat):

    Ender Dragon
    Wither*/
