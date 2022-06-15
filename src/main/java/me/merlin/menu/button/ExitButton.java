package me.merlin.menu.button;


import lombok.AllArgsConstructor;
import me.merlin.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ExitButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
    }


}
