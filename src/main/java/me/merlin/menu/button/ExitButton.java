package me.merlin.menu.button;


import lombok.AllArgsConstructor;
import me.merlin.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ExitButton extends Button {

    private ItemStack itemStack;

    public ExitButton() {
        itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§c§lExit");
        itemStack.setItemMeta(meta);

    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return itemStack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
    }


}
