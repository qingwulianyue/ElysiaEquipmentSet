package com.elysia.elysiaequipmentset.filemanager.data;

import java.util.List;

public class ConfigData {
    private final boolean debug;
    private final String prefix;
    private final int save_timer;
    private final boolean save_tips;
    private final List<String> slot;

    public ConfigData(boolean debug, String prefix, int saveTimer, boolean saveTips, List<String> slot) {
        this.debug = debug;
        this.prefix = prefix;
        save_timer = saveTimer;
        save_tips = saveTips;
        this.slot = slot;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getSave_timer() {
        return save_timer;
    }

    public boolean isSave_tips() {
        return save_tips;
    }

    public List<String> getSlot() {
        return slot;
    }
}
