package me.merlin.faction;

import lombok.Getter;
import me.merlin.Factions;
import me.merlin.claims.ClaimHandler;
import me.merlin.faction.commands.FactionCommands;
import me.merlin.profile.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FactionHandler {




    @Getter private List<Faction> factionList;

    ClaimHandler claimHandler;
    private Factions plugin;

    public FactionHandler() {
        factionList = new ArrayList<>();

        plugin = Factions.getInstance();
        Factions.getCommandFramework().registerCommands(new FactionCommands());
        claimHandler = Factions.getInstance().getClaimHandler();

        plugin.getServer().getPluginManager().registerEvents(new FactionListener(), plugin);

        load();
        updateClaimHandler();
    }


    public void updateClaimHandler() {
        claimHandler.getClaims().clear();
        factionList.forEach(faction -> {
            for (Chunk claim : faction.getClaims()) {
                claimHandler.getClaims().put(claim, faction);
            }
        });
    }

    private void load() {

        if (plugin.getConfig().getConfigurationSection("factions") == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[FactionHandler] Factions section was empty!");
            return;
        }

        plugin.getConfig().getConfigurationSection("factions").getKeys(false).forEach(factionName -> {
            Faction faction = new Faction(factionName);
            faction.setDescription(plugin.getConfig().getString("factions." + factionName + ".description"));
            faction.setBalance(plugin.getConfig().getDouble("factions." + factionName + ".balance"));
            faction.setPower(plugin.getConfig().getInt("factions." + factionName + ".power"));
            faction.setMaxPower(plugin.getConfig().getInt("factions." + factionName + ".maxPower"));
            faction.setOwner(UUID.fromString(plugin.getConfig().getString("factions." + factionName + ".owner")));
            plugin.getConfig().getStringList("factions." + factionName + ".members").forEach(member -> faction.getMembers().add(UUID.fromString(member)));

            plugin.getConfig().getStringList("factions." + factionName + ".claims").forEach(claim -> {
                String[] split = claim.split(",");
                faction.getClaims().add(Bukkit.getWorld("faction").getChunkAt(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            });

            plugin.getConfig().getStringList("factions." + factionName + ".upgrades").forEach(upgrade -> {
                String[] split = upgrade.split(",");
                faction.getUpgrades().put(split[0], Integer.parseInt(split[1]));
            });

            factionList.add(faction);
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[FactionHandler] Faction " + factionName + " has been added to the faction list.");
        });

        plugin.getConfig().set("factions", null);
        plugin.saveConfig();
    }

    public void save() {
        // Save all the factions in the faction list to the config
        if (factionList.size() > 0) {
            factionList.forEach(faction -> {
                List<String> members = new ArrayList<>();
                List<String> chunks = new ArrayList<>();
                List<String> upgrades = new ArrayList<>();

                plugin.getConfig().set("factions." + faction.getName() + ".name", faction.getName());
                plugin.getConfig().set("factions." + faction.getName() + ".description", faction.getDescription());
                plugin.getConfig().set("factions." + faction.getName() + ".balance", faction.getBalance());
                plugin.getConfig().set("factions." + faction.getName() + ".power", faction.getPower());
                plugin.getConfig().set("factions." + faction.getName() + ".maxPower", faction.getMaxPower());
                plugin.getConfig().set("factions." + faction.getName() + ".owner", faction.getOwner().toString());
//                plugin.getConfig().set("factions." + faction.getName() + ".members", faction.getMembers().toArray());
                faction.getMembers().forEach(member -> {
                    members.add(member.toString());
                });
                plugin.getConfig().set("factions." + faction.getName() + ".members", members);

                faction.getClaims().forEach(claim -> {
                    chunks.add(claim.getX() + "," + claim.getZ());

                });

                faction.getUpgrades().forEach((upgrade, level) -> {
                    upgrades.add(upgrade + "," + level);

                });


                plugin.getConfig().set("factions." + faction.getName() + ".claims", chunks);
                plugin.getConfig().set("factions." + faction.getName() + ".upgrades", upgrades);

            });
        }
        plugin.saveConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[FactionHandler] All factions have been saved to the config.");
    }


    public boolean exists(String factionName) {
        return factionList.stream().anyMatch(faction -> faction.getName().equalsIgnoreCase(factionName));
    }


    public void createFaction(String name, Player owner) {
        Faction faction = new Faction(name);
        faction.setOwner(owner.getUniqueId());
        faction.setBalance(0);
        faction.setPower(0);
        faction.getMembers().add(owner.getUniqueId());
        factionList.add(faction);
        Factions.getInstance().getProfileHandler().getProfile(owner).setFaction(faction);
        owner.sendMessage(ChatColor.GREEN + "You have created " + ChatColor.GOLD + name + ChatColor.GREEN + ".");
    }

    public Faction getFaction(String name) {
        return factionList.stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void disbandFaction(Faction faction) {
        faction.getMembers().forEach(member -> {
            if (Bukkit.getOfflinePlayer(member) != null) {
                Bukkit.getPlayer(member).sendMessage(ChatColor.DARK_GREEN + "Your faction has been disbanded by " + ChatColor.GOLD + Bukkit.getPlayer(faction.getOwner()).getDisplayName() + ChatColor.DARK_GREEN + ".");
                Factions.getInstance().getProfileHandler().getProfile(Bukkit.getPlayer(member)).setFaction(null);
            } else {
                plugin.getConfig().set(member + ".faction", null);
                plugin.saveConfig();
            }
        });
        factionList.remove(faction);
    }
}
