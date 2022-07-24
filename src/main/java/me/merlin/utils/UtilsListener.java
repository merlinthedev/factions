package me.merlin.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class UtilsListener implements Listener {

    // Disable natural mob spawning
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        // Get entity spawn event

    }

}
