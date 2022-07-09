package me.merlin.utils;

import me.merlin.faction.Faction;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class Comparison {

    public static boolean chunkComparison(Chunk target, Faction faction) {
        for(Chunk chunk : faction.getClaims()) {
            if(chunk.getX() == target.getX() && chunk.getZ() == target.getZ()) {
                return true;
            }
        }
        return false;
    }

    public static boolean chunkComparison(Chunk target, Chunk chunk) {
        return chunk.getX() == target.getX() && chunk.getZ() == target.getZ();
    }

    public static boolean hasMoved(Location o, Location n) {
        return o.getX() != n.getX() || o.getY() != n.getY() || o.getZ() != n.getZ();
    }

}
