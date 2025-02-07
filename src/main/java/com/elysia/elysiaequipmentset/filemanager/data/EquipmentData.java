package com.elysia.elysiaequipmentset.filemanager.data;

import java.util.HashMap;
import java.util.List;

public class EquipmentData {
    private final String id;
    private final List<String> name;
    private final HashMap<Integer, EffectData> effects;

    public EquipmentData(String id, List<String> name, HashMap<Integer, EffectData> effects) {
        this.id = id;
        this.name = name;
        this.effects = effects;
    }

    public static class EffectData {
        private final List<String> enable_command;
        private final List<String> enable_attribute;
        private final List<String> enable_tips;
        private final List<String> disable_command;
        private final List<String> disable_tips;

        public EffectData(List<String> enableCommand, List<String> enableAttribute, List<String> enableTips, List<String> disableCommand, List<String> disableTips) {
            enable_command = enableCommand;
            enable_attribute = enableAttribute;
            enable_tips = enableTips;
            disable_command = disableCommand;
            disable_tips = disableTips;
        }

        public List<String> getEnable_command() {
            return enable_command;
        }

        public List<String> getEnable_attribute() {
            return enable_attribute;
        }

        public List<String> getEnable_tips() {
            return enable_tips;
        }

        public List<String> getDisable_command() {
            return disable_command;
        }

        public List<String> getDisable_tips() {
            return disable_tips;
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getName() {
        return name;
    }

    public HashMap<Integer, EffectData> getEffects() {
        return effects;
    }
}
