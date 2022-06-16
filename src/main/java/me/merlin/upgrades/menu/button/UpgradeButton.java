package me.merlin.upgrades.menu.button;

import lombok.AllArgsConstructor;
import me.merlin.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


public class UpgradeButton extends Button {

    private ItemStack itemStack;
    private String name;

    public UpgradeButton(ItemStack itemStack, String name) {
        this.itemStack = itemStack;
        this.name = name;

        itemStack.getItemMeta().setDisplayName(name);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemStack(itemStack);
    }

    @Override
    public void clicked(Player player, ClickType clickType) {

    }
}
