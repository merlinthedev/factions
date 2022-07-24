package me.merlin.kits.command;

import me.merlin.Factions;
import me.merlin.command.Command;
import me.merlin.command.CommandArgs;
import me.merlin.config.ConfigHandler;
import me.merlin.kits.Kit;
import me.merlin.kits.KitHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand {

    @Command(name = "kit")
    public void kitCommand(CommandArgs args) {
        Player player = args.getPlayer();
        KitHandler kitHandler = Factions.getInstance().getKitHandler();
        if (args.getArgs().length < 1) {
            player.sendMessage("§cPlease specify a kit!");
            player.sendMessage("§cList of kits: ");

            kitHandler.getKitList().forEach(kit -> {
                player.sendMessage("§6" + kit.getDisplayName());
            });
        }

        if (args.getArgs().length == 1) {
            Kit kit = kitHandler.getKit(args.getArgs(0));
            if (kit == null) {
                player.sendMessage("§cKit not found");
                return;
            }

            for (ItemStack itemStack : kit.getInventory()) {
                if (itemStack != null) {
                    player.getInventory().addItem(itemStack);
                }
            }

            for (ItemStack itemStack : kit.getArmor()) {
                player.getInventory().addItem(itemStack);
            }

            player.sendMessage("§2§lYou have been given the kit §6" + kit.getDisplayName());
        }

    }

    @Command(name = "kit.help")
    public void kitHelpCommand(CommandArgs args) {
        Player player = args.getPlayer();
        player.sendMessage("§cUsage: /kit <kitname>");
        player.sendMessage("§cList of kits: /kit list");
    }

    @Command(name = "kit.list")
    public void kitListCommand(CommandArgs args) {
        Player player = args.getPlayer();
        KitHandler kitHandler = Factions.getInstance().getKitHandler();
        player.sendMessage("§2§lList of kits: ");
        kitHandler.getKitList().forEach(kit -> {
            player.sendMessage("§6" + kit.getDisplayName());
        });
    }


    @Command(name = "kit.create", permission = "faction.mod.kit.create")
    public void kitCreateCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length != 1) {
            player.sendMessage("§cUsage: /kit create <kitname>");
            return;
        }

        String kitName = args.getArgs(0);

        Kit kit = new Kit(kitName);
        KitHandler kitHandler = Factions.getInstance().getKitHandler();

        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        configHandler.getKitsFile().set("kits." + kitName + ".display-name", kit.getDisplayName());
        configHandler.saveKitsFile();

        kit.setDisplayName(kitName);
        kit.setInventory(player.getInventory().getContents());
        kit.setArmor(player.getInventory().getArmorContents());

        kitHandler.addKit(kit);

        player.sendMessage("§2§lKit §6" + kitName + " §2has been created");

        return;
    }

    @Command(name = "kit.delete", permission = "faction.mod.kit.delete")
    public void kitDeleteCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length != 1) {
            player.sendMessage("§cUsage: /kit delete <kitname>");
            return;
        }

        String kitName = args.getArgs(0);
        KitHandler kitHandler = Factions.getInstance().getKitHandler();
        Kit kit = kitHandler.getKit(kitName);
        if (kit == null) {
            player.sendMessage("§cKit not found");
            return;
        }
        kitHandler.removeKit(kit);

        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        configHandler.getKitsFile().set("kits." + kitName, null);
        configHandler.saveKitsFile();

        player.sendMessage("§2§lKit §6" + kit.getDisplayName() + " §2has been deleted");

        return;

    }

    @Command(name = "kit.setdisplayname", permission = "faction.mod.kit.setdisplayname")
    public void kitSetDisplayNameCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length != 2) {
            player.sendMessage("§cUsage: /kit setdisplayname <kitname> <displayname>");
            return;
        }

        String kitName = args.getArgs(0);
        String displayName = args.getArgs(1);
        KitHandler kitHandler = Factions.getInstance().getKitHandler();
        Kit kit = kitHandler.getKit(kitName);
        if (kit == null) {
            player.sendMessage("§cKit not found");
            return;
        }
        kit.setDisplayName(displayName);
        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        configHandler.getKitsFile().set("kits." + kitName + ".display-name", displayName);
        configHandler.saveKitsFile();

        player.sendMessage("§2§lYou have set the display name of the kit §6" + kit.getDisplayName() + " §2to §6" + displayName);

        return;
    }

    @Command(name = "kit.setdisplayitem", permission = "faction.mod.kit.setdisplayitem")
    public void kitSetDisplayItemCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length != 1) {
            player.sendMessage("§cUsage: /kit setdisplayitem <kitname>");
            return;
        }

        String kitName = args.getArgs(0);
        KitHandler kitHandler = Factions.getInstance().getKitHandler();
        Kit kit = kitHandler.getKit(kitName);
        if (kit == null) {
            player.sendMessage("§cKit not found");
            return;
        }
        if (player.getInventory().getItemInHand() == null) {
            player.sendMessage("§cYou must have an item in your hand to set as the display item");
            return;
        }

        kit.setDisplayItem(player.getInventory().getItemInHand());
        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        configHandler.getKitsFile().set("kits." + kitName + ".display-item-id", kit.getDisplayItem().getTypeId());
        configHandler.saveKitsFile();

        player.sendMessage("§2§lYou have set the display item for kit §6" + kit.getDisplayName());

        return;

    }

    @Command(name = "kit.setinventory", permission = "faction.mod.kit.setinventory")
    public void kitSetInventoryCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length != 1) {
            player.sendMessage("§cUsage: /kit setinventory <kitname>");
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.sendMessage("§cYou must be in survival mode to set the inventory");
            return;
        }

        String kitName = args.getArgs(0);
        KitHandler kitHandler = Factions.getInstance().getKitHandler();
        Kit kit = kitHandler.getKit(kitName);
        if (kit == null) {
            player.sendMessage("§cKit not found");
            return;
        }

        kit.setInventory(player.getInventory().getContents().clone());
        kit.setArmor(player.getInventory().getArmorContents().clone());

        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        configHandler.getKitsFile().set("kits." + kitName + ".inventory", kit.getInventory());
        configHandler.getKitsFile().set("kits." + kitName + ".armor", kit.getArmor());
        configHandler.saveKitsFile();

        player.sendMessage("§aInventory set for kit §e" + kitName + "§a.");
        return;
    }

}
