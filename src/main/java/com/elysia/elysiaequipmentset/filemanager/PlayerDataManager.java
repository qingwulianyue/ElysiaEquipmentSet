package com.elysia.elysiaequipmentset.filemanager;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    private PlayerDataManager(){}
    private static final PlayerDataManager instance = new PlayerDataManager();
    public static PlayerDataManager getInstance() {
        return instance;
    }
    private final HashMap<UUID, HashMap<String, Integer>> playerEquipmentData = new HashMap<>();
    public void loadPlayerData(UUID uuid, HashMap<String, Integer> equipmentData){
        playerEquipmentData.put(uuid, equipmentData);
    }
    public HashMap<String, Integer> getPlayerEquipmentData(UUID uuid){
        return playerEquipmentData.get(uuid);
    }
}
