package me.merlin.profile;

import me.merlin.Factions;
import me.merlin.config.ConfigHandler;
import me.merlin.faction.FactionHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        System.out.println(Factions.getInstance().getName());
        Player player = event.getPlayer();
        Factions.getInstance().getProfileHandler().addProfile(player);

        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();

        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        if (!configHandler.hasFile(player.getUniqueId())) {
            configHandler.createFile(player.getUniqueId());
            configHandler.loadFile(player.getUniqueId());
            return;
        }
        configHandler.loadFile(player.getUniqueId());
        FileConfiguration file = configHandler.getFile(player.getUniqueId());

        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        Profile profile = profileHandler.getProfile(player);

        profile.setFaction(factionHandler.getFaction(file.getString("faction")));
        profile.setBalance(file.getDouble("balance"));
        profile.setPower(file.getInt("power"));


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        FileConfiguration file = configHandler.getFile(player.getUniqueId());
        Profile profile = Factions.getInstance().getProfileHandler().getProfile(player);
        if (profile.getFaction() == null) file.set("faction", "None");
        else file.set("faction", profile.getFaction().getName());
        file.set("balance", profile.getBalance());
        file.set("power", profile.getPower());
        configHandler.saveFile(player.getUniqueId());


        Factions.getInstance().getProfileHandler().removeProfile(player);
    }
}
