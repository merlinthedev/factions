package me.merlin.faction.warp.menu.button;

import me.merlin.menu.Button;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpButton extends Button {

    private Location location;

    private ItemStack item;

    @Override
    public ItemStack getButtonItem(Player player) {
        return item;
    }

    public WarpButton(Location location, String warpName) {
        item = new ItemStack(Material.STAINED_CLAY, 1, (byte) 5);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6§l" + warpName);
        item.setItemMeta(meta);
        this.location = location;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.teleport(location);
        player.sendMessage("You have been warped.");
    }
}

