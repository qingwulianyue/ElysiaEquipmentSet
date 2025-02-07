package com.elysia.elysiaequipmentset.filemanager;

import com.elysia.elysiaequipmentset.ElysiaEquipmentSet;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    private PlayerDataManager(){}
    private static final PlayerDataManager instance = new PlayerDataManager();
    public static PlayerDataManager getInstance() {
        return instance;
    }
    private final HashMap<UUID, HashMap<String, Integer>> playerEquipmentData = new HashMap<>();
    public void putPlayerData(UUID uuid, HashMap<String, Integer> equipmentData){
        playerEquipmentData.put(uuid, equipmentData);
    }
    public HashMap<String, Integer> getPlayerEquipmentData(UUID uuid){
        return playerEquipmentData.get(uuid);
    }
    public void savePlayerData(){
        for(UUID uuid : playerEquipmentData.keySet()){
            Path playerDataPath = ElysiaEquipmentSet.getInstance().getDataFolder().toPath().resolve("PlayerData").resolve(uuid.toString() + ".yml");
            try {
                Files.createDirectories(playerDataPath.getParent());
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                HashMap<String, Integer> equipmentData = playerEquipmentData.get(uuid);
                for(String key : equipmentData.keySet())
                    yamlConfiguration.set(key, equipmentData.get(key));
                try {
                    yamlConfiguration.save(playerDataPath.toFile());
                } catch (IOException e) {
                    throw new UncheckedIOException("Failed to save player data.", e);
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to save player data.", e);
            }
        }
    }
    public void load() {
        findAllYmlFiles(new File(ElysiaEquipmentSet.getInstance().getDataFolder() + "/PlayerData"));
    }
    private void findAllYmlFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹则递归查找
                    findAllYmlFiles(file);
                } else if (file.getName().endsWith(".yml")) {
                    // 如果是YML文件则加入结果列表
                    File playerDataFolder = new File(folder + "/" + file.getName());
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(playerDataFolder);
                    loadPlayerData(config, UUID.fromString(file.getName().replaceAll(".yml", "")));
                }
            }
        }
    }
    private void loadPlayerData(YamlConfiguration config, UUID uuid) {
        HashMap<String, Integer> equipmentData = new HashMap<>();
        for (String key : config.getKeys(false)) {
            equipmentData.put(key, config.getInt(key));
        }
        playerEquipmentData.put(uuid, equipmentData);
    }
}
