package me.merlin.utils;

import me.merlin.Factions;
import me.merlin.command.Command;
import me.merlin.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldMover {

    public WorldMover() {
        Factions.getCommandFramework().registerCommands(this);
    }

    @Command(name = "move", permission = "faction.mod.worldmover")
    public void worldMover(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length == 0) {
            player.sendMessage("§cUsage: /move <worldname>");
            player.sendMessage("§l§4MAKE SURE YOU SELECT ONE OF THESE WORLDS IN THE CORRECT CASE: §l§2faction, content, world");
            return;
        }

        String world = args.getArgs(0);
        player.teleport(new Location(Bukkit.getWorld(world), 0, 100, 0));
        player.sendMessage("§2§lYou have been teleported to the world §6" + world);

    }

}
