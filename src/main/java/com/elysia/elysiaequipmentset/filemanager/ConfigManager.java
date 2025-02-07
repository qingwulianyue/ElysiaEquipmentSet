package com.elysia.elysiaequipmentset.filemanager;

import com.elysia.elysiaequipmentset.ElysiaEquipmentSet;
import com.elysia.elysiaequipmentset.filemanager.data.ConfigData;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private ConfigManager(){}
    private static final ConfigManager instance = new ConfigManager();
    private ConfigData configData;
    private final ElysiaEquipmentSet plugin = ElysiaEquipmentSet.getInstance();
    public static ConfigManager getInstance(){
        return instance;
    }
    public ConfigData getConfigData(){
        if (configData == null)
            loadConfig();
        return configData;
    }
    public void loadConfig(){
        configData = null;
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        configData = new ConfigData(
                config.getBoolean("debug"),
                config.getString("prefix"),
                config.getInt("save_timer"),
                config.getBoolean("save_tips"),
                config.getStringList("slot")
        );
        logConfigInfoIfDebug();
    }
    private void logConfigInfoIfDebug(){
        if (configData.isDebug()){
            plugin.getLogger().info("§eDebug: §a" + configData.isDebug());
            plugin.getLogger().info("§ePrefix: §a" + configData.getPrefix());
            plugin.getLogger().info("§eSave Timer: §a" + configData.getSave_timer());
            plugin.getLogger().info("§eSave Tips: §a" + configData.isSave_tips());
            plugin.getLogger().info("§eSlot: §a" + configData.getSlot());
        }
    }
}
