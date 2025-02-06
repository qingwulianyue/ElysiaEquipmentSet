package com.elysia.elysiaequipmentset.command.subcommands;

import org.bukkit.command.CommandSender;

public interface ISubCommand {
    String getName();
    default String[] getAliases() {
        return new String[0];
    }
    void execute(CommandSender sender, String[] args);
    default void register(){
        SubCommandManager.register(this);
    }
}
