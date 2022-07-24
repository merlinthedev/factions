package me.merlin.kits;

import lombok.Getter;
import me.merlin.Factions;
import me.merlin.kits.command.KitCommand;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitHandler {

    @Getter private List<Kit> kitList;
    private final Factions instance;

    public KitHandler() {
        instance = Factions.getInstance();
        kitList = new ArrayList<>();

        Factions.getCommandFramework().registerCommands(new KitCommand());

        load();
    }

    private void load() {
        if (instance.getConfigHandler().getKitsFile().getConfigurationSection("kits") == null) return;

        instance.getConfigHandler().getKitsFile().getConfigurationSection("kits").getKeys(false).forEach(kitName -> {
            Kit kit = new Kit(kitName);
            kit.setDisplayName(instance.getConfigHandler().getKitsFile().getString("kits." + kitName + ".display-name"));
            ItemStack displayItem = new ItemStack(instance.getConfigHandler().getKitsFile().getInt("kits." + kitName + ".display-item-id"));
            kit.setDisplayItem(displayItem);

            if(instance.getConfigHandler().getKitsFile().getString("kits." + kitName + ".inventory") != null) {
                kit.setInventory(((List<ItemStack>)instance.getConfigHandler().getKitsFile().get("kits." + kitName + ".inventory")).toArray(new ItemStack[0]));
                kit.setArmor(((List<ItemStack>)instance.getConfigHandler().getKitsFile().get("kits." + kitName + ".armor")).toArray(new ItemStack[0]));
            }

            addKit(kit);

        });
    }



    public void addKit(Kit kit) {
        kitList.add(kit);
    }

    public void removeKit(Kit kit) {
        kitList.remove(kit);
    }

    public Kit getKit(String name) {
        return kitList.stream().filter(k -> k.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


    public List<Kit> getKits() {
        return kitList;
    }
}
