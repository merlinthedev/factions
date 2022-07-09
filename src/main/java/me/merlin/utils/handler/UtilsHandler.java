package me.merlin.utils.handler;

import lombok.Getter;
import me.merlin.utils.WorldMover;

public class UtilsHandler {

    @Getter private WorldMover worldMover;
    public UtilsHandler() {
        worldMover = new WorldMover();
    }
}
