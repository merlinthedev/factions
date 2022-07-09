package me.merlin.faction.warp.menu;

import com.google.common.collect.Maps;
import me.merlin.Factions;
import me.merlin.faction.Faction;
import me.merlin.faction.warp.menu.button.WarpButton;
import me.merlin.menu.Button;
import me.merlin.menu.Menu;
import me.merlin.menu.button.ExitButton;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class WarpMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "§6§lWarp Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = Maps.newHashMap();
        Faction faction = Factions.getInstance().getProfileHandler().getProfile(player).getFaction();



        for(int i = 0; i < faction.getWarps().size(); i++) {
            String name = faction.getWarps().keySet().toArray()[i].toString();
            buttonMap.put(i, new WarpButton(faction.getWarps().get(name), name));
        }

        buttonMap.put(49, new ExitButton());

        return buttonMap;
    }
}
