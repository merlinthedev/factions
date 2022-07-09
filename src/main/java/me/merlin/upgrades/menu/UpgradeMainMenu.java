package me.merlin.upgrades.menu;

import com.google.common.collect.Maps;
import me.merlin.Factions;
import me.merlin.faction.Faction;
import me.merlin.menu.Button;
import me.merlin.menu.Menu;
import me.merlin.menu.button.ExitButton;
import me.merlin.upgrades.UpgradeHandler;
import me.merlin.upgrades.menu.button.UpgradeButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

public class UpgradeMainMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "§6§lUpgrade Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();
        UpgradeHandler upgradeHandler = Factions.getInstance().getUpgradeHandler();

        Map<Integer, Button> buttonMap = Maps.newHashMap();



        ItemStack tnt = new ItemStack(Material.TNT);
        ItemMeta tntMeta = tnt.getItemMeta();

        ArrayList<String> tntLore = new ArrayList<>();
        tntLore.add("§eClick here to upgrade your faction's TNT bank.");
        tntLore.add("§eCurrent TNT bank level: §a" + faction.getUpgrades().get("tnt"));
        String tntCost = "tnt" + faction.getUpgrades().get("tnt");
        tntLore.add("§eCost: §a$" + upgradeHandler.getUpgradeMap().get(tntCost));
        tntMeta.setLore(tntLore);
        tntMeta.setDisplayName("§6§lUpgrade TNT Bank");
        tnt.setItemMeta(tntMeta);


        ItemStack crop = new ItemStack(Material.WHEAT);
        ItemMeta cropMeta = crop.getItemMeta();

        ArrayList<String> cropLore = new ArrayList<>();
        cropLore.add("§eClick here to upgrade your faction's crop harvest rate.");
        cropLore.add("§eCurrent crop drop rate level: §a" + faction.getUpgrades().get("crop"));
        String cropCost = "crop" + faction.getUpgrades().get("crop");
        cropLore.add("§eCost: §a$" + upgradeHandler.getUpgradeMap().get(cropCost));
        cropMeta.setLore(cropLore);
        cropMeta.setDisplayName("§6§lUpgrade Crop Harvest Rate");
        crop.setItemMeta(cropMeta);


        ItemStack mob = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta mobMeta = mob.getItemMeta();

        ArrayList<String> mobLore = new ArrayList<>();
        mobLore.add("§eClick here to upgrade your faction's mob item drop rate.");
        mobLore.add("§eCurrent mob drop rate level: §a" + faction.getUpgrades().get("mob"));
        String mobCost = "mob" + faction.getUpgrades().get("mob");
        mobLore.add("§eCost: §a$" + upgradeHandler.getUpgradeMap().get(mobCost));
        mobMeta.setLore(mobLore);
        mobMeta.setDisplayName("§6§lUpgrade Mob Item Drop Rate");
        mob.setItemMeta(mobMeta);


        buttonMap.put(20, new UpgradeButton(tnt, "tnt"));
        buttonMap.put(22, new UpgradeButton(crop, "crop"));
        buttonMap.put(24, new UpgradeButton(mob, "mob"));

        buttonMap.put(49, new ExitButton());

        return buttonMap;
    }


}
