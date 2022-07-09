package me.merlin;

import lombok.Getter;
import me.merlin.balance.BalanceHandler;
import me.merlin.chat.ChatHandler;
import me.merlin.claims.ClaimHandler;
import me.merlin.command.CommandFramework;
import me.merlin.config.ConfigHandler;
import me.merlin.faction.FactionHandler;
import me.merlin.kits.KitHandler;
import me.merlin.menu.MenuHandler;
import me.merlin.profile.ProfileHandler;
import me.merlin.upgrades.UpgradeHandler;
import me.merlin.utils.handler.UtilsHandler;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Factions extends JavaPlugin {

    private World factionsWorld;
    private World contentWorld;


    @Getter private static CommandFramework commandFramework;
    @Getter private static Factions instance;


    @Getter private BalanceHandler balanceHandler;
    @Getter private ChatHandler chatHandler;
    @Getter private ClaimHandler claimHandler;
    @Getter private ConfigHandler configHandler;
    @Getter private FactionHandler factionHandler;
    @Getter private KitHandler kitHandler;
    @Getter private MenuHandler menuHandler;
    @Getter private ProfileHandler profileHandler;
    @Getter private UpgradeHandler upgradeHandler;
    @Getter private UtilsHandler utilsHandler;

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
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Factions] Worlds have been created.");

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
        chatHandler = new ChatHandler();
        claimHandler = new ClaimHandler();
        configHandler = new ConfigHandler();
        factionHandler = new FactionHandler();
        kitHandler = new KitHandler();
        menuHandler = new MenuHandler();
        profileHandler = new ProfileHandler();
        upgradeHandler = new UpgradeHandler();
        utilsHandler = new UtilsHandler();
    }


    private void createWorld() {
        WorldCreator worldCreator = new WorldCreator("faction");
        worldCreator.type(WorldType.FLAT);
        worldCreator.generateStructures(false);

        factionsWorld = worldCreator.createWorld();

        factionsWorld.getWorldBorder().setSize(5024);
        factionsWorld.getWorldBorder().setCenter(0, 0);

        Bukkit.getConsoleSender().sendMessage("§8[§2Factions§8] §aFaction world has been created.");


        WorldCreator contentCreator = new WorldCreator("content");
        worldCreator.type(WorldType.VERSION_1_1);

        contentWorld = contentCreator.createWorld();

        Bukkit.getConsoleSender().sendMessage("§8[§2Factions§8] §aContent world has been created.");


    }
}

// TODO: When players get kicked when the server reloads, profile will not save correctly


// PERMISSIONS:
// MOD PERMS:
// FACTION DELETE: faction.mod.delete
// FACTION WORLDCHECK: faction.mod.worldcheck
// FACTION ADVANCE CHECK: faction.mod.advance.check
// FACTION WORLD MOVER: faction.mod.worldmover