package me.merlin.chat;

import me.merlin.Cardinal.Cardinal;
import me.merlin.Cardinal.chat.ChatFormatListener;
import me.merlin.Cardinal.profile.PlayerProfile;
import me.merlin.Factions;
import me.merlin.faction.Faction;
import me.merlin.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LocalChatListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String factionName;
        PlayerProfile profile = Cardinal.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        Profile factionsProfile = Factions.getInstance().getProfileHandler().getProfile(player);
        Faction faction = factionsProfile.getFaction();

        if (faction == null) {
            factionName = "";
        } else {
            factionName = " §8[§2" + faction.getName() + "§8]";
        }




        switch (factionsProfile.getChatmode()) {
            case GLOBAL -> {
                event.setFormat(profile.getPrefix() + " " + player.getDisplayName() + factionName + ": §f" + event.getMessage());
            }
            case FACTION -> {
                event.setFormat("§l§7[§2FC§7] §r" + "§a[§2" + faction.getName() + "§a] " + profile.getPrefix() + " " + player.getDisplayName() + ": §a" + event.getMessage());
                event.getRecipients().clear();
                faction.getMembers().forEach(member -> {
                    if(Bukkit.getPlayer(member).isOnline()) {
                        event.getRecipients().add(Bukkit.getPlayer(member));
                    }
                });
            }
        }

//        player.sendMessage("§l§9DEBUG, LIST OF RECIPIENTS");
//        event.getRecipients().forEach(recipient -> {
//            player.sendMessage(recipient.getDisplayName());
//        });
//
//        player.sendMessage("§l§9DEBUG, CURRENT CHAT MODE: " + factionsProfile.getChatmode().toString());



    }


}
