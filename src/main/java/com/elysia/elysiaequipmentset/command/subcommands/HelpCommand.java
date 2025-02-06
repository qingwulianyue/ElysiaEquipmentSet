package com.elysia.elysiaequipmentset.command.subcommands;

import org.bukkit.command.CommandSender;

public class HelpCommand implements ISubCommand{
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"help"};
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage("/ElysiaForge help   -   获取帮助");
            sender.sendMessage("/ElysiaForge reload   -   重载插件");
        }
    }
}
