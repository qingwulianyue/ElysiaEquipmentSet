package com.elysia.elysiaequipmentset;

import com.elysia.elysiaequipmentset.command.CommandManager;
import com.elysia.elysiaequipmentset.command.CommandTabComplete;
import com.elysia.elysiaequipmentset.command.subcommands.HelpCommand;
import com.elysia.elysiaequipmentset.command.subcommands.ReloadCommand;
import com.elysia.elysiaequipmentset.filemanager.ConfigManager;
import com.elysia.elysiaequipmentset.filemanager.EquipmentManager;
import com.elysia.elysiaequipmentset.filemanager.PlayerDataManager;
import com.elysia.elysiaequipmentset.listener.DragonCoreEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ElysiaEquipmentSet extends JavaPlugin {
    private static ElysiaEquipmentSet instance;
    private static ConfigManager configManager;
    private static EquipmentManager equipmentManager;
    private static PlayerDataManager playerDataManager;
    public static ElysiaEquipmentSet getInstance() {
        return instance;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static EquipmentManager getEquipmentManager() {
        return equipmentManager;
    }
    public static PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configManager = ConfigManager.getInstance();
        equipmentManager = EquipmentManager.getInstance();
        playerDataManager = PlayerDataManager.getInstance();
        createFile();
        configManager.loadConfig();
        equipmentManager.load();
        playerDataManager.load();
        new HelpCommand().register();
        new ReloadCommand().register();
        Bukkit.getPluginManager().registerEvents(new DragonCoreEventListener(), this);
        Bukkit.getPluginCommand("ElysiaEquipmentSet").setExecutor(new CommandManager());
        Bukkit.getPluginCommand("ElysiaEquipmentSet").setTabCompleter(new CommandTabComplete());
        startAutomaticSavePlayerDataTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        playerDataManager.savePlayerData();
    }
    private void createFile() {
        saveDefaultConfig();
        createDefaultFile();
    }
    private void createDefaultFile(){
        saveDefaultConfig();
        Path folderPath = getDataFolder().toPath().resolve("PlayerData");
        createDirectoryIfNotExists(folderPath);
        folderPath = getDataFolder().toPath().resolve("equipment");
        createDirectoryIfNotExists(folderPath);
        Path filePath = folderPath.resolve("测试套装.yml");
        if (!Files.exists(filePath)) {
            try (InputStream resourceStream = getResourceAsStream()) {
                Files.copy(resourceStream, filePath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create default file.", e);
            }
        }
    }
    private void createDirectoryIfNotExists(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create directory.", e);
            }
        }
    }
    private InputStream getResourceAsStream() {
        InputStream resourceStream = getClass().getResourceAsStream("/equipment/测试套装.yml");
        if (resourceStream == null) {
            throw new RuntimeException("Resource '/equipment/测试套装.yml' not found in classpath.");
        }
        return resourceStream;
    }
    private void startAutomaticSavePlayerDataTask() {
        long ticks = configManager.getConfigData().getSave_timer() * 20L;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (configManager.getConfigData().isSave_tips())
                    getLogger().info("§e开始保存玩家数据");
                playerDataManager.savePlayerData();
            }
        }.runTaskTimer(this, 0L, ticks);
    }
}
