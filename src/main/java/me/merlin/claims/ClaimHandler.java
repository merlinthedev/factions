package me.merlin.claims;

import lombok.Getter;
import me.merlin.faction.Faction;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimHandler {

    @Getter private Map<Chunk, Faction> claims;

    public ClaimHandler() {
        claims = new HashMap<>();
    }


}
