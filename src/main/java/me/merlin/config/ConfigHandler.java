package me.merlin.config;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.SneakyThrows;
import me.merlin.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class ConfigHandler {


    private Map<UUID, FileConfiguration> fileConfigMap;

    @Getter private YamlConfiguration spawnerFile;


    public ConfigHandler() {
        loadSpawnerFile();
        fileConfigMap = Maps.newHashMap();
    }


    @SneakyThrows
    private void loadSpawnerFile() {
        File sFile = new File(Factions.getInstance().getDataFolder(), "spawners.yml");
        if(!sFile.exists()) {
            sFile.createNewFile();
        }

        spawnerFile = YamlConfiguration.loadConfiguration(sFile);
    }

    @SneakyThrows
    public void saveSpawnerFile() {
        spawnerFile.save(new File(Factions.getInstance().getDataFolder(), "spawners.yml"));
    }

    public void createFile(UUID uuid) {
        File uFile = new File(Factions.getInstance().getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");
        try {
            if (!uFile.exists()) {
                uFile.createNewFile();
//                Logger.success("Created " + uuid.toString() + ".yml");
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Created " + uuid.toString() + ".yml");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
//             Logger.error("Could not make " + uuid.toString() + ".yml");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Could not make " + uuid.toString() + ".yml");
        }

    }

    public boolean hasFile(UUID uuid) {
        File uFile = new File(Factions.getInstance().getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");
        return uFile.exists();
    }

    public void loadFile(UUID uuid) {
        File uFile = new File(Factions.getInstance().getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");
        fileConfigMap.put(uuid, YamlConfiguration.loadConfiguration(uFile));

        getFile(uuid).set("username", Factions.getInstance().getServer().getOfflinePlayer(uuid).getName());
        saveFile(uuid);

//        Logger.success("Loaded " + uuid.toString() + ".yml");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Loaded " + uuid.toString() + ".yml");
    }

    public FileConfiguration getFile(UUID uuid) {
        return fileConfigMap.get(uuid);
    }

    public void saveFile(UUID uuid) {
        try {
            File uFile = new File(Factions.getInstance().getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");
            fileConfigMap.get(uuid).save(uFile);
//            Logger.success("Saved " + uuid.toString() + ".yml");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Saved " + uuid.toString() + ".yml");
        } catch (Exception ex) {
            ex.printStackTrace();
//            Logger.error("Error saving " + uuid.toString() + ".yml");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error saving " + uuid.toString() + ".yml");
        }
    }


}
