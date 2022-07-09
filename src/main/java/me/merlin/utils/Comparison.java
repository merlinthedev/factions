package me.merlin.utils;

import me.merlin.faction.Faction;
import org.bukkit.Chunk;

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

}
