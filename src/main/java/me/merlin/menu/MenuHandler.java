package me.merlin.menu;

import me.merlin.Factions;
import me.merlin.faction.Faction;
import me.merlin.faction.FactionHandler;
import me.merlin.profile.Profile;
import me.merlin.profile.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuHandler {

    public MenuHandler() {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), Factions.getInstance());
    }

    public static Inventory openTnt(Player player) {
        Inventory tntInv = Bukkit.createInventory(null, 9, "§6§lUpgrade TNT Bank");


        ItemStack firstLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);

        ItemMeta firstLevelMeta = firstLevel.getItemMeta();
        firstLevelMeta.setDisplayName("§6§lLevel 1");
        firstLevel.setItemMeta(firstLevelMeta);
        tntInv.setItem(4, firstLevel);


        ItemStack secondLevel = null;
        ItemStack thirdLevel = null;

        Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();

        switch (faction.getUpgrades().get("tnt")) {
            case 1 -> {
                secondLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 1);
                thirdLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
            }
            case 2 -> {
                secondLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                thirdLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 1);
            }
            case 3 -> {
                secondLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                thirdLevel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            }
        }

        ItemMeta secondLevelMeta = secondLevel.getItemMeta();
        secondLevelMeta.setDisplayName("§6§lLevel 2");
        secondLevel.setItemMeta(secondLevelMeta);
        tntInv.setItem(4, secondLevel);

        ItemMeta thirdLevelMeta = thirdLevel.getItemMeta();
        thirdLevelMeta.setDisplayName("§6§lLevel 3");
        thirdLevel.setItemMeta(thirdLevelMeta);
        tntInv.setItem(4, thirdLevel);




        return tntInv;
    }

    public static Inventory openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 45, "§6§lUpgrade Menu");
        ItemStack tntUpgrade = new ItemStack(Material.TNT);
        ItemStack cropUprade = new ItemStack(Material.WHEAT);
        ItemStack dropUpgrade = new ItemStack(Material.ROTTEN_FLESH);

        ItemMeta tntMeta = tntUpgrade.getItemMeta();
        ItemMeta cropMeta = cropUprade.getItemMeta();
        ItemMeta dropMeta = dropUpgrade.getItemMeta();


        tntMeta.setDisplayName("§6§lTNT Upgrade");
        cropMeta.setDisplayName("§6§lCrop Upgrade");
        dropMeta.setDisplayName("§6§lDrop Upgrade");

        tntUpgrade.setItemMeta(tntMeta);
        cropUprade.setItemMeta(cropMeta);
        dropUpgrade.setItemMeta(dropMeta);




        inv.setItem(20, tntUpgrade);
        inv.setItem(22, cropUprade);
        inv.setItem(24, dropUpgrade);

        return inv;
    }
}
