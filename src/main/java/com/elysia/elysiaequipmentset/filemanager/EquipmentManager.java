package com.elysia.elysiaequipmentset.filemanager;

import com.elysia.elysiaequipmentset.ElysiaEquipmentSet;
import com.elysia.elysiaequipmentset.filemanager.data.EquipmentData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class EquipmentManager {
    private EquipmentManager(){}
    private static final EquipmentManager instance = new EquipmentManager();
    private final ElysiaEquipmentSet plugin = ElysiaEquipmentSet.getInstance();
    private final HashMap<String, EquipmentData> equipmentDataHashMap = new HashMap<>();
    public static EquipmentManager getInstance() {
        return instance;
    }
    public String findEquipmentDataByDisplayName(String displayName){
        for (String key : equipmentDataHashMap.keySet()) {
            EquipmentData equipmentData = equipmentDataHashMap.get(key);
            if (equipmentData.getName().contains(displayName)) {
                return key;
            }
        }
        return null;
    }
    public EquipmentData getEquipmentData(String id){
        return equipmentDataHashMap.get(id);
    }
    public void load(){
        equipmentDataHashMap.clear();
        findAllYmlFiles(new File(plugin.getDataFolder() + "/equipment"));
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
                    File formulaDataFolder = new File(folder + "/" + file.getName());
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(formulaDataFolder);
                    loadEquipmentData(config);
                }
            }
        }
    }
    private void loadEquipmentData(YamlConfiguration config){
        for (String id : config.getKeys(false)){
            List<String> name = config.getStringList(id + ".name");
            HashMap<Integer, EquipmentData.EffectData> effects = new HashMap<>();
            for (String i : config.getConfigurationSection(id + ".effect").getKeys(false)){
                List<String> enableCommand = config.getStringList(id + ".effect." + i + ".enable.command");
                List<String> enableAttribute = config.getStringList(id + ".effect." + i + ".enable.attribute");
                List<String> enableTips = config.getStringList(id + ".effect." + i + ".enable.tips");
                List<String> disableCommand = config.getStringList(id + ".effect." + i + ".disable.command");
                List<String> disableTips = config.getStringList(id + ".effect." + i + ".disable.tips");
                effects.put(Integer.parseInt(i), new EquipmentData.EffectData(enableCommand, enableAttribute, enableTips, disableCommand, disableTips));
            }
            EquipmentData equipmentData = new EquipmentData(id, name, effects);
            logEquipmentDataIfDebug(equipmentData);
            equipmentDataHashMap.put(id, equipmentData);
        }
    }
    private void logEquipmentDataIfDebug(EquipmentData equipmentData){
        if (ElysiaEquipmentSet.getConfigManager().getConfigData().isDebug()){
            plugin.getLogger().info("§eID: §a" + equipmentData.getId());
            plugin.getLogger().info("§eName: §a" + equipmentData.getName());
            for (Integer i : equipmentData.getEffects().keySet()){
                EquipmentData.EffectData effectData = equipmentData.getEffects().get(i);
                plugin.getLogger().info("§eEffect: §a" + i);
                plugin.getLogger().info("§eEnable Command: §a" + effectData.getEnable_command());
                plugin.getLogger().info("§eEnable Attribute: §a" + effectData.getEnable_attribute());
                plugin.getLogger().info("§eEnable Tips: §a" + effectData.getEnable_tips());
                plugin.getLogger().info("§eDisable Command: §a" + effectData.getDisable_command());
                plugin.getLogger().info("§eDisable Tips: §a" + effectData.getDisable_tips());
            }
        }
    }
}
