package me.merlin.upgrades.menu.button;

import lombok.AllArgsConstructor;
import me.merlin.Factions;
import me.merlin.faction.Faction;
import me.merlin.menu.Button;
import me.merlin.menu.Menu;
import me.merlin.upgrades.UpgradeHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class UpgradeButton extends Button {

    private final ItemStack itemStack;
    private String type;

    private UpgradeHandler upgradeHandler;

    public UpgradeButton(ItemStack itemStack, String upgradeType) {
        this.itemStack = itemStack;
        type = upgradeType;
        upgradeHandler = Factions.getInstance().getUpgradeHandler();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return itemStack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();
        if(faction.getUpgrades().get(type) < 3) {
            faction.getUpgrades().replace(type, faction.getUpgrades().get(type) + 1);
            if(faction.getBalance() > upgradeHandler.getUpgradeMap().get(type + faction.getUpgrades().get(type))) {
                faction.setBalance(faction.getBalance() - upgradeHandler.getUpgradeMap().get(type + faction.getUpgrades().get(type)));
            } else {
                player.closeInventory();
                player.sendMessage("§c§lYour faction does not have enough money to upgrade.");
                return;
            }
            //faction.setBalance(faction.getBalance() - upgradeHandler.getUpgradeMap().get(type + faction.getUpgrades().get(type)));
            player.sendMessage("§aYou have upgraded your " + UpgradeHandler.convertType(type) + "!");

        } else {
            player.sendMessage("§cYou have already reached the maximum level for this upgrade.");
        }

    }
}
