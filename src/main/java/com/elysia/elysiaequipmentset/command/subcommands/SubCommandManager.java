package com.elysia.elysiaequipmentset.command.subcommands;

import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;

public class SubCommandManager {
    private static final HashMap<String, ISubCommand> COMMAND_MAP = new HashMap<>();
    public static ISubCommand get(String nameOrAlias){
        return COMMAND_MAP.values()
                .stream()
                .filter(subCommand -> subCommand.getName().equalsIgnoreCase(nameOrAlias) ||
                        (subCommand.getAliases().length > 0 && ArrayUtils.contains(subCommand.getAliases(), nameOrAlias)))
                .findAny().orElse(null);
    }
    public static ISubCommand get(Class<? extends ISubCommand> clz){
        return COMMAND_MAP.values()
                .stream()
                .filter(subCommand -> subCommand.getClass() == clz)
                .findAny().orElse(null);
    }
    public static void register(ISubCommand... commands){
        for (ISubCommand command : commands){
            COMMAND_MAP.put(command.getName(), command);
        }
    }
}
