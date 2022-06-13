package me.merlin.profile;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.merlin.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ProfileHandler {

    @Getter
    private Map<UUID, Profile> profileMap;

    public ProfileHandler() {
        profileMap = Maps.newHashMap();


        // Register ProfileListener
        Factions.getInstance().getServer().getPluginManager().registerEvents(new ProfileListener(), Factions.getInstance());
    }

    public void addProfile(Player player) {
        profileMap.put(player.getUniqueId(), new Profile());
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Profile] Player " + player.getName() + " has been added to the profile map.");
    }

    public void removeProfile(Player player) {
        if (hasProfile(player)) {
            profileMap.remove(player.getUniqueId());
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Profile] Player " + player.getName() + " has been removed from the profile map.");
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Profile] Player " + player.getName() + " does not have a profile!");
        }
    }

    public boolean hasProfile(Player player) {
        return profileMap.containsKey(player.getUniqueId());
    }

    public Profile getProfile(Player player) {
        if (hasProfile(player)) {
            return profileMap.get(player.getUniqueId());
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Profile] Player " + player.getName() + " does not have a profile!");
            return null;
        }

    }
}
