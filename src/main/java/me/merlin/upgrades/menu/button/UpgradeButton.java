package me.merlin.upgrades.menu.button;

import lombok.AllArgsConstructor;
import me.merlin.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class UpgradeButton extends Button {

    private ItemStack itemStack;
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemStack(itemStack);
    }

    @Override
    public void clicked(Player player, ClickType clickType) {

    }
}
