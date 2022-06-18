package me.merlin.faction;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.merlin.Factions;
import me.merlin.config.ConfigHandler;
import me.merlin.profile.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


public class Faction {

    @Getter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private double balance;
    @Getter
    @Setter
    private UUID owner;
    @Getter
    private List<UUID> members;
    @Getter
    private List<UUID> invited;
    @Getter
    private int maxPlayers;
    @Getter
    @Setter
    private int maxPower;
    @Getter
    @Setter
    private int power;


    @Getter
    private List<Chunk> claims;

    @Getter
    private Map<String, Integer> upgrades;

    public Faction(String name) {
        this.name = name;
        this.claims = new ArrayList<>();
        this.description = "Default Description :(";
        this.members = new ArrayList<>();
        this.invited = new ArrayList<>();
        this.maxPlayers = 20;
        this.upgrades = Maps.newHashMap();
        powerUpdate();

    }


    public void powerUpdate() {
        this.maxPower = members.size() * 100;
        ProfileHandler profileHandler = Factions.getInstance().getProfileHandler();
        ConfigHandler configHandler = Factions.getInstance().getConfigHandler();
        AtomicInteger sum = new AtomicInteger();
        members.forEach(member -> {
            if(Bukkit.getOfflinePlayer(member).isOnline()) {
                sum.addAndGet(profileHandler.getProfile(Bukkit.getOfflinePlayer(member).getPlayer()).getPower());
            } else {
                configHandler.loadFile(member);
                sum.addAndGet(configHandler.getFile(member).getInt("power"));
            }


            System.out.println(member);

            // sum.addAndGet(profileHandler.getProfile(Bukkit.getPlayer(member)).getPower());
        });
        power = sum.intValue();
    }

}
