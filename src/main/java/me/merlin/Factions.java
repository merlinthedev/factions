package me.merlin;

import lombok.Getter;
import me.merlin.balance.BalanceHandler;
import me.merlin.claims.ClaimHandler;
import me.merlin.command.CommandFramework;
import me.merlin.config.ConfigHandler;
import me.merlin.faction.FactionHandler;
import me.merlin.profile.ProfileHandler;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Factions extends JavaPlugin {

    private World factionsWorld;



    @Getter private static CommandFramework commandFramework;
    @Getter private static Factions instance;


    @Getter private BalanceHandler balanceHandler;
    @Getter private ClaimHandler claimHandler;
    @Getter private ConfigHandler configHandler;
    @Getter private FactionHandler factionHandler;
    @Getter private ProfileHandler profileHandler;

    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Factions] CommandFramework has been initialized.");
        // Config.yml logic
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Factions] Config.yml has been loaded.");


        // World logic
        createWorld();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Factions] World has been created.");

        registerHandlers();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Factions] Handlers registered successfully!");



        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[Factions] Factions has been enabled.");
    }

    public void onDisable() {
        factionHandler.save();
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_BLUE + "[Factions] Factions has been disabled.");
    }

    private void registerHandlers() {
        balanceHandler = new BalanceHandler();
        claimHandler = new ClaimHandler();
        configHandler = new ConfigHandler();
        factionHandler = new FactionHandler();
        profileHandler = new ProfileHandler();
    }

    

    private void createWorld() {
        WorldCreator worldCreator = new WorldCreator("faction");
        worldCreator.type(WorldType.FLAT);
        worldCreator.generateStructures(false);

        factionsWorld = worldCreator.createWorld();

    }
}

// TODO: When players get kicked when the server reloads, profile will not save correctly


// PERMISSIONS:
// MOD PERMS:
// FACTION DELETE: faction.mod.delete
// FACTION WORLDCHECK: faction.mod.worldcheck