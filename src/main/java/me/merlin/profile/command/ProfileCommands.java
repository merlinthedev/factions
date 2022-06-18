package me.merlin.profile.command;

import me.merlin.Factions;
import me.merlin.command.Command;
import me.merlin.command.CommandArgs;
import me.merlin.profile.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProfileCommands {

    @Command(name = "profiles", permission = "faction.mod.profiles")
    public void profilesCheck(CommandArgs args) {
        Player player = args.getPlayer();
        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        profileHandler.getProfileMap().forEach((uuid, profile) -> {
            player.sendMessage(Boolean.toString(profileHandler.hasProfile(Bukkit.getPlayer(uuid))) + ": " + Bukkit.getPlayer(uuid).getName());
        });

    }
}
