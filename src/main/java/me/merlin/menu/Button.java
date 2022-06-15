package me.merlin.menu;

import me.merlin.Factions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class Button {
    public static Button placeholder(final Material material, final byte data, String... title) {
        return (new Button() {
            public ItemStack getButtonItem(Player player) {
                return new ItemStack(Material.IRON_FENCE, 1);
            }
        });
    }

    public abstract ItemStack getButtonItem(Player player);

    public void clicked(Player player, ClickType clickType) {}

    public void clicked(Player player, int slot, ClickType type, int hotbarSlot) {}

    public boolean shouldCancel(Player player, ClickType type) {
        return true;
    }

    public boolean shouldUpdate(Player player, ClickType type) {
        return false;
    }

   
}
