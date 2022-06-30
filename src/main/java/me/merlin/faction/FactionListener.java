package me.merlin.faction;

import me.merlin.Factions;
import me.merlin.utils.Comparison;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class FactionListener implements Listener {

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.MOB_SPAWNER) {
            Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();

            if(!Comparison.chunkComparison(event.getBlock().getChunk(), faction)) {
                player.sendMessage("§cYou can only place spawners in your faction's chunks.");
                event.setCancelled(true);
            }


            CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();

            faction.getSpawners().add(spawner);
            faction.valueUpdate();
            player.sendMessage("§a" + spawner.getCreatureTypeName() + " spawner" + " §7has been placed.");


        }
    }

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.MOB_SPAWNER) {
            Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();

            if (!Comparison.chunkComparison(event.getBlock().getChunk(), faction)) {
                player.sendMessage("§cYou can only break spawners in your faction's claims.");
                event.setCancelled(true);
                return;
            }

            CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
            faction.getSpawners().remove(spawner);
            faction.valueUpdate();
            player.sendMessage("§a" + spawner.getCreatureTypeName() + " spawner" + " §7has been broken.");
        }
    }

    @EventHandler
    public void onMobKilledEvent(EntityDeathEvent event) {
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        if (event.getEntity() instanceof Player) return;
        factionHandler.getFactionList().forEach(faction -> {
            if (faction.getClaims().contains(event.getEntity().getLocation().getChunk())) {
                event.getDrops().forEach(item -> {
                    item.setAmount(item.getAmount() * faction.getUpgrades().get("mob"));
                    //event.getEntity().getKiller().sendMessage("§aYou have received " + item.getAmount() + "x " + item.getType().name() + " from a mob.");
                });
            }
        });


    }


    @EventHandler
    public void onCropDestroy(BlockBreakEvent event) {
        FactionHandler factionHandler = Factions.getInstance().getFactionHandler();
        if (event.getBlock().getType() == Material.SUGAR_CANE_BLOCK || event.getBlock().getType() == Material.CACTUS) {
            factionHandler.getFactionList().forEach(faction -> {
                if (faction.getClaims().contains(event.getBlock().getChunk())) {
                    // multply the drop rate by the upgrade level
                    event.getBlock().getDrops().forEach(item -> {
                        item.setAmount(item.getAmount() * faction.getUpgrades().get("crop"));
                    });
                }
            });
        }

    }
}
