package com.elysia.elysiaequipmentset.filemanager.data;

import java.util.List;

public class ConfigData {
    private final boolean debug;
    private final String prefix;
    private final List<String> slot;

    public ConfigData(boolean debug, String prefix, List<String> slot) {
        this.debug = debug;
        this.prefix = prefix;
        this.slot = slot;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getSlot() {
        return slot;
    }
}
