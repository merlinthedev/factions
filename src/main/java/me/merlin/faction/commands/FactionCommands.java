package me.merlin.faction.commands;

import me.merlin.Factions;
import me.merlin.claims.ClaimHandler;
import me.merlin.command.Command;
import me.merlin.command.CommandArgs;
import me.merlin.faction.Faction;
import me.merlin.faction.FactionHandler;
import me.merlin.menu.MenuHandler;
import me.merlin.profile.Profile;
import me.merlin.profile.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class FactionCommands {


    //    Faction CREATE
    @Command(name = "faction.create", aliases = {"f.create"}, inGameOnly = true)
    public void factionCreate(CommandArgs args) {
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        if (args.getArgs().length != 1) {
            args.getPlayer().sendMessage("§c/faction create <faction name>");
            return;
        }
        if (factionHandler.exists(args.getArgs(0))) {
            args.getPlayer().sendMessage("§cFaction with this name already exists!");
            return;
        }

        factionHandler.createFaction(args.getArgs(0), args.getPlayer());
    }


    //    Faction DISBAND
    @Command(name = "faction.disband", aliases = {"f.disband"}, inGameOnly = true)
    public void factionDisband(CommandArgs args) {
        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        Profile profile = profileHandler.getProfile(args.getPlayer());
        if (profile.getFaction() == null) {
            args.getPlayer().sendMessage("§cYou are not in a faction!");
            return;
        }

        if (!profile.getFaction().getOwner().equals(args.getPlayer().getUniqueId())) {
            args.getPlayer().sendMessage("§cYou are not the leader of this faction!");
            return;
        }

        String factionName = profile.getFaction().getName();
        factionHandler.disbandFaction(profile.getFaction());
        profile.setFaction(null);
        args.getPlayer().sendMessage(ChatColor.DARK_GREEN + "You have disbanded " + ChatColor.GOLD + factionName + ChatColor.DARK_GREEN + "!");


    }


    //    Faction LEAVE
    @Command(name = "faction.leave", aliases = {"f.leave"}, inGameOnly = true)
    public void factionLeave(CommandArgs args) {
        Player player = args.getPlayer();
        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        Profile profile = profileHandler.getProfile(player);
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        if (profile.getFaction().getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are the leader of this faction!");
            return;
        }

        String factionName = profile.getFaction().getName();
        profile.getFaction().getMembers().remove(player.getUniqueId());
        profile.setFaction(null);
        player.sendMessage("§7You have left §e" + factionName + "!");
    }

    //    Faction INVITE
    @Command(name = "faction.invite", aliases = {"f.invite"}, inGameOnly = true)
    public void factionInvite(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length != 1) {
            player.sendMessage("§c/faction invite <player>");
            return;
        }

        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        Profile profile = profileHandler.getProfile(player);
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        if (!profile.getFaction().getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the leader of this faction!");
            return;
        }

        Player target = Bukkit.getPlayer(args.getArgs(0));
        if (target == null) {
            player.sendMessage("§cPlayer not found!");
            return;
        }

        Profile targetProfile = profileHandler.getProfile(target);
        if (targetProfile.getFaction() != null) {
            player.sendMessage("§cThis player is already in a faction!");
            return;
        }

        if (profile.getFaction().getMembers().contains(target.getUniqueId())) {
            player.sendMessage("§cThis player is already in your faction!");
            return;
        }

        profile.getFaction().getInvited().add(target.getUniqueId());
        player.sendMessage("§7You have invited §e" + target.getName() + " §7to your faction!");
        target.sendMessage("§7You have been invited to §6§l" + profile.getFaction().getName() + " §r§7by §e" + player.getName() + "§7!");
        target.sendMessage("§7Type §e/faction join §6§l" + profile.getFaction().getName() + " §r§7to join the faction!");
        return;

    }

    //    Faction JOIN
    @Command(name = "faction.join", aliases = {"f.join"}, inGameOnly = true)
    public void factionJoin(CommandArgs args) {
        Player player = args.getPlayer();

        if (args.getArgs().length != 1) {
            player.sendMessage("§c/faction join <faction>");
            return;
        }

        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        Profile profile = profileHandler.getProfile(player);
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        Faction targetFaction = factionHandler.getFaction(args.getArgs(0));
        if (targetFaction == null) {
            player.sendMessage("§cFaction with this name does not exist!");
            return;
        }

        if (targetFaction.getMembers().size() >= targetFaction.getMaxPlayers()) {
            player.sendMessage("§cThis faction is full!");
            return;
        }

        if (!targetFaction.getInvited().contains(player.getUniqueId())) {
            player.sendMessage("§cYou are not invited to this faction!");
            return;
        }

        if (targetFaction.getMembers().contains(player.getUniqueId())) {
            player.sendMessage("§cYou are already in this faction!");
            return;
        }

        targetFaction.getInvited().remove(player.getUniqueId());
        targetFaction.getMembers().add(player.getUniqueId());
        profile.setFaction(targetFaction);

        player.sendMessage("§7You have joined §e" + targetFaction.getName() + "§7!");
        targetFaction.getMembers().forEach(member -> {
            Player target = Bukkit.getPlayer(member);
            if (target == null) return;
            if (!target.isOnline()) return;
            target.sendMessage("§7§l" + player.getName() + "§r §7has joined §e" + targetFaction.getName() + "§7!");
        });

    }


    //    Faction SHOW
    @Command(name = "faction.show", aliases = {"f.show", "faction.who", "f.who"}, inGameOnly = true)
    public void factionShow(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length == 0) {
            Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
            if (profile.getFaction() == null) {
                player.sendMessage("§cYou are not in a faction!");
                return;
            }
            profile.getFaction().powerUpdate();
            showFactionInformation(player, profile);


        }

        if (args.getArgs().length == 1) {
            Player target = Bukkit.getPlayer(args.getArgs(0));
            if (target == null) {
                player.sendMessage("§cPlayer not found!");
                return;
            }
            Profile targetProfile = Factions.getInstance().getProfileHandler().getProfile(target);
            if (targetProfile.getFaction() == null) {
                player.sendMessage("§cThis player is not in a faction!");
                return;
            }
            targetProfile.getFaction().powerUpdate();
            showFactionInformation(player, targetProfile);


        }
    }


    //Faction MAP
    @Command(name = "faction.map", aliases = {"f.map"}, inGameOnly = true)
    public void factionMap(CommandArgs args) {
        Player player = args.getPlayer();
        ClaimHandler claimHandler = Factions.getInstance().getClaimHandler();
        String t;
        if (claimHandler.getClaims().containsKey(player.getLocation().getChunk())) {
            t = claimHandler.getClaims().get(player.getLocation().getChunk()).getName();
        } else {
            t = "Wilderness";
        }
        player.sendMessage("§6___________________.[§2" + t + "§6].___________________");
        String[][] map = new String[11][29];
        for (String[] rows : map) {
            Arrays.fill(rows, "§7-");
        }

        Chunk chunk = player.getLocation().getChunk();
        for (int i = 0; i < map.length; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < map[i].length; j++) {
                row.append(map[i][j]).append(" ");
            }
            claimHandler.getClaims().forEach((claim, faction) -> {
                if (claim.getX() >= chunk.getX() - 14 && claim.getX() <= chunk.getX() + 14 && claim.getZ() >= chunk.getZ() - 5 && claim.getZ() <= chunk.getZ() + 5) {
                    int x = claim.getX() - chunk.getX();
                    int z = claim.getZ() - chunk.getZ();

                    map[5 + z][14 + x] = ChatColor.GREEN + faction.getName().substring(0, 1);
                }
            });

            map[5][14] = "§6+§r";
            player.sendMessage(row.toString());
        }
    }


    //Faction CLAIM
    @Command(name = "faction.claim", aliases = {"f.claim"}, inGameOnly = true)
    public void factionClaim(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        Faction faction = profile.getFaction();


        if (player.getLocation().getWorld() != Factions.getInstance().getServer().getWorld("faction")) {
            player.sendMessage("§cYou can't claim in this world.");
            return;
        }

        // check if the player is within x 250 & x -250 & z 250 & z -250
        if (player.getLocation().getX() < 255 && player.getLocation().getX() > -255 && player.getLocation().getZ() < 255 && player.getLocation().getZ() > -255) {
            player.sendMessage("§cYou can't claim in this area.");
            return;
        }


        if (faction == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        if (!faction.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader.");
            return;
        }

        // TODO: Check if the faction has enough power to claim land
        if (faction.getPower() < faction.getClaims().size()) {
            player.sendMessage("§cYou don't have enough power to claim land.");
            return;
        }


        Chunk chunk = player.getLocation().getChunk();
        if (chunkComparison(chunk, faction)) {
            player.sendMessage("§cYour faction already owns this chunk.");
            return;
        }


        faction.getClaims().add(chunk);
        Factions.getInstance().getFactionHandler().updateClaimHandler();
        player.sendMessage("§2§lYou have claimed the current chunk for: §6" + faction.getName());
        return;
    }


    //Faction UNCLAIM
    @Command(name = "faction.unclaim", aliases = {"f.unclaim"}, inGameOnly = true)
    public void factionUnclaim(CommandArgs args) {
        Player player = args.getPlayer();
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        Profile profile = profileHandler.getProfile(player);
        Faction faction = profile.getFaction();

        if (faction == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        if (!faction.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader.");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        if (!chunkComparison(chunk, faction)) {
            player.sendMessage("§cYour faction doesn't own this chunk.");
            return;
        }

        faction.getClaims().remove(chunk);
        player.sendMessage("§2§lYou have unclaimed the current chunk for: §6" + faction.getName());
    }

    //Faction UNCLAIMALL
    @Command(name = "faction.unclaimall", aliases = {"f.unclaimall"}, inGameOnly = true)
    public void factionUnclaimAll(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        Faction faction = profile.getFaction();

        if (faction == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        if (!faction.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader.");
            return;
        }

        faction.getClaims().clear();
        player.sendMessage("§2§lYou have unclaimed all chunks for: §6" + faction.getName());
    }


    //Faction LIST
    @Command(name = "faction.list", aliases = {"f.list"}, inGameOnly = true)
    public void factionList(CommandArgs args) {
        Player player = args.getPlayer();
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        player.sendMessage("§6_______________.[§2Factions§6]._______________");
        factionHandler.getFactionList().forEach(faction -> {
            player.sendMessage("§7§l" + faction.getName() + " §e" + faction.getMembers().size() + "§7/§e" + faction.getMaxPlayers() + " §7members online");
        });
    }

    //Faction POWER
    @Command(name = "faction.power", aliases = {"f.power"}, inGameOnly = true)
    public void factionPower(CommandArgs args) {
        Player player = args.getPlayer();
        if (args.getArgs().length == 0) {
            Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
            player.sendMessage("§7Your power: §e" + profile.getPower());
        }
        if (args.getArgs().length == 1) {
            Player target = Bukkit.getPlayer(args.getArgs(0));
            if (target == null) {
                player.sendMessage("§cPlayer not found!");
                return;
            }
            Profile targetProfile = Factions.getInstance().getProfileHandler().getProfile(target);
            player.sendMessage("§l" + target.getDisplayName() + "§r§7 power: §e" + targetProfile.getPower());
        }
    }


    //Faction BALANCE
    @Command(name = "faction.balance", aliases = {"f.balance", "f.bal", "f.b"}, inGameOnly = true)
    public void factionBalance(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }
        player.sendMessage("§7Faction balance: §e" + profile.getFaction().getBalance());
    }

    //Faction DEPOSIT
    @Command(name = "faction.deposit", aliases = {"f.deposit", "f.d"}, inGameOnly = true)
    public void factionDeposit(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        if (args.getArgs().length == 0) {
            player.sendMessage("§cUsage: /faction deposit <amount>");
            return;
        }

        Faction faction = profile.getFaction();

        if (args.getArgs().length == 1) {
            try {
                int amount = Integer.parseInt(args.getArgs(0));
                if (amount < 1) {
                    player.sendMessage("§cYou can't deposit less than 1!");
                    return;
                }
                profile.getFaction().setBalance(profile.getFaction().getBalance() + amount);
                profile.setBalance(profile.getBalance() - amount);//
                player.sendMessage("§2§lYou have deposited §6" + amount + "§2§l to your faction!");
            } catch (NumberFormatException e) {
                player.sendMessage("§cYou can't deposit that!");//
            }
        }


    }


    //Faction WITHDRAW
    @Command(name = "faction.withdraw", aliases = {"f.withdraw", "f.w"}, inGameOnly = true)
    public void factionWithdraw(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }
        Faction faction = profile.getFaction();
        if (!faction.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader!");
            return;
        }

        if (args.getArgs().length == 0) {//
            player.sendMessage("§cUsage: /faction withdraw <amount>");
            return;
        }

        if (args.getArgs().length == 1) {
            try {
                int amount = Integer.parseInt(args.getArgs(0));
                if (amount < 0) {
                    player.sendMessage("§cAmount must be positive!"); // Check if the amount is positive
                    return;
                }
                if (amount > faction.getBalance()) { // Check if faction has enough money
                    player.sendMessage("§cYour faction does not have enough money!");
                    return;
                }
                faction.setBalance(faction.getBalance() - amount);
                profile.setBalance(profile.getBalance() + amount);
                player.sendMessage("§2§lYou have withdrawn §6" + amount + "§2 from your faction balance.");
            } catch (NumberFormatException exception) {
                player.sendMessage("§cYou can not withdraw that!");
            }
        }


    }


    //Faction UPGRADE
    @Command(name = "faction.upgrades", aliases = {"f.upgrades"}, inGameOnly = true)
    public void factionUpgrade(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        if (!profile.getFaction().getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader!");
            return;
        }


        player.openInventory(MenuHandler.openMainMenu(player));


    }


    private void showFactionInformation(Player player, Profile targetProfile) {
        player.sendMessage("§6_______________.[§2" + targetProfile.getFaction().getName() + "§6]._______________");
        player.sendMessage("§7Description: §e" + targetProfile.getFaction().getDescription());
        player.sendMessage("§7Leader: §e" + Bukkit.getOfflinePlayer(targetProfile.getFaction().getOwner()).getName());
        StringBuilder members = new StringBuilder();
        for (UUID member : targetProfile.getFaction().getMembers())
            members.append(Bukkit.getOfflinePlayer(member).getName()).append(", ");


        player.sendMessage("§7Members: §e" + members.toString().substring(0, members.toString().length() - 2));
        player.sendMessage("§7Balance: §e" + targetProfile.getFaction().getBalance());
        player.sendMessage("§7Power: §e" + targetProfile.getFaction().getPower() + "/" + targetProfile.getFaction().getMaxPower() + "/" + targetProfile.getFaction().getClaims().size());

    }


    private boolean chunkComparison(Chunk target, Faction faction) {
        for (Chunk chunk : faction.getClaims()) { //
            if (chunk.getX() == target.getX() && chunk.getZ() == target.getZ()) {
                return true;
            }
        }

        return false; // Check is not the same
    }


    // MOD COMMANDS

    //Faction DELETE
    @Command(name = "faction.delete", aliases = {"f.delete"}, inGameOnly = true, permission = "faction.mod.delete")
    public void factionModDelete(CommandArgs args) {
        Player player = args.getPlayer();
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        if (args.length() < 1) {
            player.sendMessage("§c/faction delete <faction name>");
            return;
        }
        if (!factionHandler.exists(args.getArgs(0))) {
            player.sendMessage("§cFaction with this name does not exist!");
            return;
        }


        factionHandler.disbandFaction(factionHandler.getFaction(args.getArgs(0)));
        player.sendMessage("§aFaction " + args.getArgs(0) + " deleted!");

    }


    //Faction WORLD CHECK
    @Command(name = "faction.worldcheck", aliases = {"f.wc", "f.w.c"}, inGameOnly = true, permission = "faction.mod.worldcheck")
    public void factionWorldCheck(CommandArgs commandArgs) {
        commandArgs.getPlayer().sendMessage("§7§lYou are in world §e" + commandArgs.getPlayer().getWorld().getName());

    }

    //Faction ADVANCE CHECK
    @Command(name = "faction.advance.check", aliases = {"f.ac", "f.a.c"}, inGameOnly = true, permission = "faction.mod.advance.check")
    public void factionAdvanceCheck(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        if(commandArgs.length() < 1) {
            if(Factions.getInstance().getProfileHandler().getProfile(player).getFaction() == null) {
                player.sendMessage("§cYou are not in a faction!");
                return;
            }

            Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();
            player.sendMessage("§7§lFaction name: §e" + faction.getName());
            player.sendMessage("§7§lFaction owner: §e" + Bukkit.getOfflinePlayer(faction.getOwner()).getName());
            // send the player all the members
            StringBuilder members = new StringBuilder();

            faction.getMembers().forEach(member -> {
                if(Bukkit.getOfflinePlayer(member).isOnline()) {
                    members.append("§2").append(Bukkit.getOfflinePlayer(member).getName()).append(", ");
                } else {
                    members.append("§4").append(Bukkit.getOfflinePlayer(member).getName()).append(", ");
                }

            });

            player.sendMessage("§7§lFaction members: " + members.toString().substring(0, members.toString().length() - 2));
            player.sendMessage("§7§lFaction balance: §e" + faction.getBalance());
            player.sendMessage("§7§lFaction power: §e" + faction.getPower() + "/" + faction.getMaxPower() + "/" + faction.getClaims().size());
            player.sendMessage("§7§lThis faction has claims at: §e" + faction.getClaims().toString());

            faction.getUpgrades().forEach((upgrade, level) -> {
                player.sendMessage("§7§lFaction upgrade: §e" + upgrade.toString() + " §7- §e" + level);
            });
            return;

        }
    }


    //Faction CLAIM CHECK
    @Command(name = "faction.claimcheck", aliases = {"f.cc", "f.c.c"}, inGameOnly = false, permission = "faction.mod.claimcheck")
    public void factionClaimCheck(CommandArgs commandArgs) {
        Factions.getInstance().getFactionHandler().getFactionList().forEach(f -> {
            f.getClaims().forEach(c -> {
                commandArgs.getSender().sendMessage("§l§7Faction: §e" + f.getName() + " §7Claim: §e" + c.getX() + "," + c.getZ());
            });
        });

    }

}
