package me.merlin.upgrades;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

public class UpgradeHandler {

    @Getter private Map<String, Double> upgradeMap;

    public UpgradeHandler() {
        upgradeMap = Maps.newHashMap();

        load();
    }

    private void load() {
        upgradeMap.put("tnt1", 100.0);
        upgradeMap.put("tnt2", 200.0);
        upgradeMap.put("tnt3", 300.0);

        upgradeMap.put("crop1", 100.0);
        upgradeMap.put("crop2", 200.0);
        upgradeMap.put("crop3", 300.0);

        upgradeMap.put("mob1", 100.0);
        upgradeMap.put("mob2", 200.0);
        upgradeMap.put("mob3", 300.0);

    }

    public static String convertType(String type) {
        return switch (type) {
            case "tnt" -> "§6§lTNT Bank";
            case "crop" -> "§6§lCrop Drop Rate";
            case "mob" -> "§6§lMob Item Drop Rate";
            default -> "§cPlease report this error to the developer.";
        };
    }
}
