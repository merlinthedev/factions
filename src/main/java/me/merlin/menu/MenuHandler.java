package me.merlin.menu;

import me.merlin.Factions;
import org.bukkit.Bukkit;

public class MenuHandler {
    public MenuHandler() {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), Factions.getInstance());
    }
}
