package com.elysia.elysiaequipmentset.listener;

import com.elysia.elysiaequipmentset.ElysiaEquipmentSet;
import com.elysia.elysiaequipmentset.filemanager.data.EquipmentData;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.serverct.ersha.api.AttributeAPI;
import org.serverct.ersha.attribute.data.AttributeData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DragonCoreEventListener implements Listener {
    @EventHandler
    public void onPlayerSlotUpdate(PlayerSlotUpdateEvent event){
        List<String> slot = ElysiaEquipmentSet.getConfigManager().getConfigData().getSlot();
        if (!slot.contains(event.getIdentifier())) return;
        updatePlayerEquipmentData(event.getPlayer().getUniqueId());
    }
    private void updatePlayerEquipmentData(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        List<String> slot = ElysiaEquipmentSet.getConfigManager().getConfigData().getSlot();
        HashMap<String, Integer> equipmentData = new HashMap<>();
        for (String s : slot) {
            ItemStack itemStack = SlotAPI.getCacheSlotItem(player, s);
            if (itemStack == null || !itemStack.hasItemMeta()) continue;
            String displayName = itemStack.getItemMeta().getDisplayName();
            String id = ElysiaEquipmentSet.getEquipmentManager().findEquipmentDataByDisplayName(displayName);
            if (id == null) continue;
            equipmentData.put(id, equipmentData.getOrDefault(id, 0) + 1);
        }
        compareToPlayerSaveData(uuid, equipmentData);
    }
    private void compareToPlayerSaveData(UUID uuid, HashMap<String, Integer> equipmentData){
        //获取玩家已有的套装数据
        HashMap<String, Integer> playerData = ElysiaEquipmentSet.getPlayerDataManager().getPlayerEquipmentData(uuid);
        System.out.println(playerData);
        System.out.println(equipmentData);
        //当玩家原本没有套装数据时，则生效当前的所有套装
        if (playerData == null || playerData.isEmpty()){
            for (String id : equipmentData.keySet())
                enableEquipment(uuid, id, equipmentData.get(id));
        }
        //当玩家原本有套装数据并且当前套装数据为空时，则失效当前所有套装
        else if (equipmentData == null || equipmentData.isEmpty()){
            for (String id : playerData.keySet())
                disableEquipment(uuid, id, playerData.get(id));
        } else {
            for (String id : playerData.keySet()) {
                if (!equipmentData.containsKey(id))
                    disableEquipment(uuid, id, playerData.get(id));
                else if (!Objects.equals(equipmentData.get(id), playerData.get(id))){
                    disableEquipment(uuid, id, playerData.get(id));
                    enableEquipment(uuid, id, equipmentData.get(id));
                }
            }
            for (String id : equipmentData.keySet()) {
                if (!playerData.containsKey(id))
                    enableEquipment(uuid, id, equipmentData.get(id));
            }
        }
        ElysiaEquipmentSet.getPlayerDataManager().putPlayerData(uuid, equipmentData);
    }
    private void enableEquipment(UUID uuid, String id, int level){
        EquipmentData.EffectData effectData = ElysiaEquipmentSet.getEquipmentManager().getEquipmentData(id).getEffects().get(level);
        Player player = Bukkit.getPlayer(uuid);
        AttributeData attributeData = AttributeAPI.getAttrData(player);
        if (effectData.getEnable_command() != null){
            for (String command : effectData.getEnable_command())
                dispatchCommand(uuid, command);
        }
        if (effectData.getEnable_tips() != null){
            for (String tips : effectData.getEnable_tips())
                sendTips(uuid, tips);
        }
        if (effectData.getEnable_attribute() != null){
            List<String> attributes = new ArrayList<>();
            for (String attribute : effectData.getEnable_attribute())
                attributes.add(parseString(attribute, player));
            AttributeAPI.addPersistentSourceAttribute(attributeData, "ElysiaEquipmentSet" + id + level, attributes, System.currentTimeMillis() ,true);
        }
    }
    private void disableEquipment(UUID uuid, String id, int level){
        EquipmentData.EffectData effectData = ElysiaEquipmentSet.getEquipmentManager().getEquipmentData(id).getEffects().get(level);
        Player player = Bukkit.getPlayer(uuid);
        AttributeData attributeData = AttributeAPI.getAttrData(player);
        if (effectData.getDisable_command() != null){
            for (String command : effectData.getDisable_command())
                dispatchCommand(uuid, command);
        }
        if (effectData.getDisable_tips() != null){
            for (String tips : effectData.getDisable_tips())
                sendTips(uuid, tips);
        }
        if (effectData.getEnable_attribute() != null)
            AttributeAPI.takePersistentSourceAttribute(attributeData, "ElysiaEquipmentSet" + id + level);
    }
    private void dispatchCommand(UUID uuid, String command){
        Player player = Bukkit.getPlayer(uuid);
        Pattern pattern = Pattern.compile("\\[(.*?)](.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String content = matcher.group(1);
            if (content.equals("console"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), matcher.group(2).replaceAll("%player_name%", player.getName()));
        } else {
            Bukkit.dispatchCommand(player, command.replaceAll("%player_name%", player.getName()));
        }
    }
    private void sendTips(UUID uuid, String tips){
        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage(
                ElysiaEquipmentSet.getConfigManager().getConfigData().getPrefix() + tips
        );
    }
    private String parseString(String string, Player player){
        if (string.contains("%")){
            String regex = "%(.*?)%";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find())
                return parseString(string.replaceAll(matcher.group(), PlaceholderAPI.setPlaceholders(player, matcher.group())), player);
        }
        return string;
    }
}
