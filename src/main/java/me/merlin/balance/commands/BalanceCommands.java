package me.merlin.balance.commands;

import me.merlin.Factions;
import me.merlin.command.Command;
import me.merlin.command.CommandArgs;
import me.merlin.profile.Profile;
import me.merlin.profile.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BalanceCommands {


    @Command(name = "balance", aliases = {"bal"}, inGameOnly = true)
    public void balanceCommand(CommandArgs args) {
        Player player = args.getPlayer();
        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        if (args.getArgs().length < 1) {
            player.sendMessage("§7Balance: §e" + profileHandler.getProfile(player).getBalance());
            return;
        }
        if (args.getArgs().length == 1) {
            Player target = Bukkit.getPlayer(args.getArgs(0));
            if (target == null) {
                player.sendMessage("§cPlayer not found!");
                return;
            }

            player.sendMessage("§l" + target.getDisplayName() + " §7balance: §e" + profileHandler.getProfile(target).getBalance());
            return;

        }
    }

    @Command(name = "pay", inGameOnly = true)
    public void payCommand(CommandArgs args) {
        if (args.getArgs().length != 2) {
            args.getSender().sendMessage("§c/pay <player> <amount>");
            return;
        }

        Player player = args.getPlayer();
        Player target = Bukkit.getPlayer(args.getArgs(0));
        if (target == null) {
            player.sendMessage("§cPlayer not found!");
            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage("§cYou can't pay yourself!");
            return;
        }

        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        Profile profile = profileHandler.getProfile(player);
        Profile targetProfile = profileHandler.getProfile(target);

        if (profile.getBalance() < Integer.parseInt(args.getArgs(1))) {
            player.sendMessage("§cYou don't have enough money!");
            return;
        }

        profile.setBalance(profile.getBalance() - Integer.parseInt(args.getArgs(1)));
        targetProfile.setBalance(targetProfile.getBalance() + Integer.parseInt(args.getArgs(1)));

        player.sendMessage("§7You have paid §e" + target.getDisplayName() + " §7§l$" + args.getArgs(1));
        target.sendMessage("§7You have received §l$" + args.getArgs(1) + "§r §7from §e" + player.getDisplayName());
        return;

    }
}
