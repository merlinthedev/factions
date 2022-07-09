package me.merlin.faction.commands;

import me.merlin.Factions;
import me.merlin.claims.ClaimHandler;
import me.merlin.command.Command;
import me.merlin.command.CommandArgs;
import me.merlin.faction.Faction;
import me.merlin.faction.FactionHandler;
import me.merlin.faction.warp.menu.WarpMenu;
import me.merlin.profile.Profile;
import me.merlin.profile.ProfileHandler;
import me.merlin.upgrades.menu.UpgradeMainMenu;
import me.merlin.utils.Comparison;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
            ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();

            if (!profileHandler.hasProfile(player)) {
                player.sendMessage("§cThe profile requested doesn't exist!");
                return;
            }

            Profile profile = profileHandler.getProfile(player);


            if (profile == null) {
                player.sendMessage("§cProfile is null!");
                return;
            }

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

    //Faction VALUE
    @Command(name = "faction.value", aliases = {"f.value"}, inGameOnly = true)
    public void factionValue(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        Faction faction = profile.getFaction();


        player.sendMessage("§7Your faction value: §e" + faction.getValue());
        return;
    }

    //Faction TOP
    @Command(name = "faction.top", aliases = {"f.top"}, inGameOnly = true)
    public void factionTop(CommandArgs args) {
        Player player = args.getPlayer();
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        player.sendMessage("§6_______________.[§2Factions Top§6]._______________");
        // Order factions by value
        List<Faction> factions = factionHandler.getFactionList();
        factions.sort(Comparator.comparing(Faction::getValue));
        Collections.reverse(factions);
        for (int i = 0; i < factions.size(); i++) {
            Faction faction = factions.get(i);
            player.sendMessage("§7§l" + (i + 1) + ". " + faction.getName() + ": §e$" + faction.getValue());
        }

    }

    //Faction SETWARP
    @Command(name = "faction.setwarp", aliases = {"f.setwarp"}, inGameOnly = true)
    public void factionSetwarp(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        if (args.getArgs().length < 1) {
            player.sendMessage("§cUsage: /f.setwarp <warp name>");
            return;
        }

        Faction faction = profile.getFaction();
        if (!faction.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader.");
            return;
        }

        String warpName = args.getArgs(0);

        if (!Comparison.chunkComparison(player.getLocation().getChunk(), faction)) {
            player.sendMessage("§cYour faction doesn't own this chunk.");
            return;
        }

        if (faction.getWarps().containsKey(warpName)) {
            player.sendMessage("§cWarp already exists.");
            return;
        }

        faction.getWarps().put(warpName, player.getLocation());
        player.sendMessage("§2§lYou have set a warp: §6" + warpName);

    }

    //Faction WARP
    @Command(name = "faction.warp", aliases = {"f.warp"}, inGameOnly = true)
    public void factionWarp(CommandArgs args) {
        Player player = args.getPlayer();

        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        Faction faction = profile.getFaction();


        WarpMenu menu = new WarpMenu();
        menu.openMenu(player);
        return;
    }


    //Faction SPAWNERS
    @Command(name = "faction.spawners", aliases = {"f.spawners"}, inGameOnly = true)
    public void factionSpawners(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction.");
            return;
        }

        Faction faction = profile.getFaction();
        if (!faction.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader.");
            return;
        }

        player.sendMessage("§7Your faction spawners");
        faction.getSpawners().forEach(spawner -> {
            player.sendMessage("§7 - §e" + spawner);
        });


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


        // TODO: open faction menu
        UpgradeMainMenu upgradeMainMenu = new UpgradeMainMenu();
        upgradeMainMenu.openMenu(player);
        return;


    }


    //Faction TNT BANK
    @Command(name = "faction.tntbank", aliases = {"f.tntbank", "f.tnt"}, inGameOnly = true)
    public void factionTntBankCommand(CommandArgs args) {
        Player player = args.getPlayer();

        // Check Balance
        // Withdraw
        // Deposit

        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        if (!profile.getFaction().getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cYou are not the faction leader!");
            return;
        }

        if (args.getArgs().length == 0) {
            player.sendMessage("§cUsage: /faction tntbank deposit <amout>");
            player.sendMessage("§cUsage: /faction tntbank withdraw <amout>");
            player.sendMessage("§cUsage: /faction tntbank balance");
            return;
        }

        if (args.getArgs().length == 1) {
            if (args.getArgs(0).equalsIgnoreCase("balance")) {
                player.sendMessage("§7Your faction tnt bank balance: §e" + profile.getFaction().getTntBalance());
                return;
            }

            if (args.getArgs(0).equalsIgnoreCase("deposit")) {

                AtomicInteger count = new AtomicInteger();

                //Remove all tnt from player inventory
                Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
                    if (itemStack != null && itemStack.getType() == Material.TNT) {
                        count.addAndGet(itemStack.getAmount());
                        player.getInventory().remove(itemStack);
                    }
                });
                //Add tnt to faction tnt bank
                profile.getFaction().setTntBalance(profile.getFaction().getTntBalance() + count.get());

                //Send message to player
                player.sendMessage("§2§lYou have deposited §6" + count.get() + "§2§l tnt to your faction tnt bank!");

                return;
            }

            if (args.getArgs(0).equalsIgnoreCase("withdraw")) {
                player.sendMessage("§cUsage: /faction tntbank withdraw <amout>");
                return;
            }
        }

        if (args.getArgs().length == 2) {
            if (args.getArgs(0).equalsIgnoreCase("deposit")) {
                try {
                    int amount = Integer.parseInt(args.getArgs(1));
                    if (amount < 1) {
                        player.sendMessage("§cYou can't deposit less than 1!");
                        return;
                    }
                    profile.getFaction().setTntBalance(profile.getFaction().getTntBalance() + amount);
                    player.sendMessage("§2§lYou have deposited §6" + amount + "§2§l to your faction tnt bank!");
                } catch (NumberFormatException e) {
                    player.sendMessage("§cYou can't deposit that!");
                }
                return;
            }
        }

    }


    // Faction CHAT
    @Command(name = "faction.chat", aliases = {"f.chat", "f.c"}, inGameOnly = true)
    public void factionChat(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            profile.setChatmode(Profile.CHATMODE.FACTION);
            return;
        }

        if (args.getArgs().length == 0) {
            if (profile.getChatmode() == Profile.CHATMODE.FACTION) {
                profile.setChatmode(Profile.CHATMODE.GLOBAL);
                player.sendMessage("§2§lYou are now in global chat!");
            } else {
                profile.setChatmode(Profile.CHATMODE.FACTION);
                player.sendMessage("§2§lYou are now in faction chat!");
            }
            return;
        }

        if (args.getArgs().length == 1) {
            if (args.getArgs(0).equalsIgnoreCase("global") || args.getArgs(0).equalsIgnoreCase("g")) {
                profile.setChatmode(Profile.CHATMODE.GLOBAL);
                player.sendMessage("§2§lYou are now in global chat!");
                return;
            }
            if (args.getArgs(0).equalsIgnoreCase("faction") || args.getArgs(0).equalsIgnoreCase("f")) {
                profile.setChatmode(Profile.CHATMODE.FACTION);
                player.sendMessage("§2§lYou are now in faction chat!");
                return;
            }
        }


    }


    // Faction HOME
    @Command(name = "faction.home", aliases = {"f.home"}, inGameOnly = true)
    public void factionHome(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        Location startLocation = player.getLocation();
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        Faction faction = profile.getFaction();
        if (faction.getFactionHome() == null) {
            player.sendMessage("§cYour faction does not have a home yet!");
            return;
        }

        player.sendMessage("§2§lCommencing teleport to your faction home...");

        // Teleport player after 5 seconds
        new BukkitRunnable() {
            int i = 5;

            public void run() {
                if (player.getVelocity().getX() > 0 || player.getVelocity().getZ() > 0 || player.getVelocity().getY() > 0) {
                    player.sendMessage("§cYour teleport has been canceled because you moved.");
                    this.cancel();
                    return;
                }

                if (i > 0) {
                    player.sendMessage(ChatColor.DARK_GREEN + "Teleporting to faction home in " + ChatColor.YELLOW + i + ChatColor.DARK_GREEN + " seconds!");
                }

                i--;
                if (i == -1) {
                    this.cancel();
                    Location loc = faction.getFactionHome();
                    loc.setWorld(Bukkit.getWorld("faction"));
                    player.teleport(loc);
                    player.sendMessage(ChatColor.DARK_GREEN + "Teleported to faction home!");
                    return;
                }

            }
        }.runTaskTimer(Factions.getInstance(), 20L, 20L);
    }

    // Faction SETHOME
    @Command(name = "faction.sethome", aliases = {"f.sethome"}, inGameOnly = true)
    public void setFactionHome(CommandArgs args) {
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

        if (faction.getFactionHome() != null) {
            player.sendMessage("§cYour faction already has a home!");
            return;
        }

        if (player.getLocation().getWorld() != Bukkit.getWorld("faction")) {
            player.sendMessage("§cYou can't set your faction home here!");
            return;
        }

        if (player.getLocation().getX() < 255 && player.getLocation().getX() > -255 && player.getLocation().getZ() < 255 && player.getLocation().getZ() > -255) {
            player.sendMessage("§cYou can't set your faction home in this area.");
            return;
        }

        if (!faction.getClaims().contains(player.getLocation().getChunk())) {
            player.sendMessage("§cYou can only set your faction home within your faction claims.");
            return;
        }

        faction.setFactionHome(player.getLocation());
        player.sendMessage("§2§lYou have set your faction home!");


    }

    // Faction DELHOME
    @Command(name = "faction.delhome", aliases = {"f.delhome"})
    public void deleteFactionHome(CommandArgs args) {
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

        if (faction.getFactionHome() == null) {
            player.sendMessage("§cYour faction does not have a home yet!");
            return;
        }

        faction.setFactionHome(null);
        player.sendMessage("§2§lYou have deleted your faction home!");
    }


    private void showFactionInformation(Player player, Profile targetProfile) {
        player.sendMessage("§6_______________.[§2" + targetProfile.getFaction().getName() + "§6]._______________");
        player.sendMessage("§7Description: §e" + targetProfile.getFaction().getDescription());
        player.sendMessage("§7Faction Value: §e" + targetProfile.getFaction().getValue());
        player.sendMessage("§7Leader: §e" + Bukkit.getOfflinePlayer(targetProfile.getFaction().getOwner()).getName());
        StringBuilder members = new StringBuilder();
        for (UUID member : targetProfile.getFaction().getMembers()) {
            members.append(Bukkit.getOfflinePlayer(member).getName()).append(", ");
        }

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

    //Faction SPAWNER LIST
    @Command(name = "faction.spawner.list", aliases = {"f.spawner.list", "f.sl"}, inGameOnly = true, permission = "faction.mod.spawner.list")
    public void factionSpawnerList(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }

        Faction faction = profile.getFaction();
        faction.getSpawners().forEach(spawner -> {
            player.sendMessage("§7§lSpawner: §e" + spawner);
        });

        return;
    }

    //Faction ADVANCE CHECK
    @Command(name = "faction.advance.check", aliases = {"f.ac", "f.a.c"}, inGameOnly = true, permission = "faction.mod.advance.check")
    public void factionAdvanceCheck(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        if (commandArgs.length() < 1) {
            if (Factions.getInstance().getProfileHandler().getProfile(player).getFaction() == null) {
                player.sendMessage("§cYou are not in a faction!");
                return;
            }

            Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();
            player.sendMessage("§7§lFaction name: §e" + faction.getName());
            player.sendMessage("§7§lFaction owner: §e" + Bukkit.getOfflinePlayer(faction.getOwner()).getName());
            // send the player all the members
            StringBuilder members = new StringBuilder();

            faction.getMembers().forEach(member -> {
                if (Bukkit.getOfflinePlayer(member).isOnline()) {
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

            player.sendMessage("§7§lFaction home: §e" + faction.getFactionHome());

            player.sendMessage("§7§lFaction spawners: §e" + faction.getSpawners().toString());
            player.sendMessage("§7§lFaction warps:");
            faction.getWarps().forEach((name, warp) -> {
                player.sendMessage("§7§lFaction warp: §e" + name + " §7- §e" + warp);
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
