package me.merlin.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getName().equals("§6§lUpgrade Menu")) {
            if (event.getCurrentItem().getType() == Material.TNT) {
                MenuHandler.openTnt((Player) event.getWhoClicked());

            }
            event.setCancelled(true);
        }
    }

}

