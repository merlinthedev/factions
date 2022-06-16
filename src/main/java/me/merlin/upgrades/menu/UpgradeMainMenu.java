package me.merlin.upgrades.menu;

import com.google.common.collect.Maps;
import me.merlin.menu.Button;
import me.merlin.menu.Menu;
import me.merlin.menu.button.ExitButton;
import me.merlin.upgrades.menu.button.UpgradeButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class UpgradeMainMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "§6§lUpgrade Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = Maps.newHashMap();

        buttonMap.put(20, new UpgradeButton(new ItemStack(Material.TNT), "§6§lUpgrade TNT"));
        buttonMap.put(22, new UpgradeButton(new ItemStack(Material.WHEAT), "§6§lUpgrade Crop Drop Rate"));
        buttonMap.put(24, new UpgradeButton(new ItemStack(Material.ROTTEN_FLESH), "§6§lUpgrade Mob Drop Rate"));

        buttonMap.put(40, new ExitButton());

        return buttonMap;
    }


}
