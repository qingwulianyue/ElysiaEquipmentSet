package com.elysia.elysiaequipmentset.command;

import com.elysia.elysiaequipmentset.command.subcommands.HelpCommand;
import com.elysia.elysiaequipmentset.command.subcommands.ISubCommand;
import com.elysia.elysiaequipmentset.command.subcommands.SubCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0){
            SubCommandManager.get(HelpCommand.class).execute(commandSender, strings);
        }
        String subCommand = strings[0];
        ISubCommand iSubCommand = SubCommandManager.get(subCommand);
        if (iSubCommand == null)
            iSubCommand = SubCommandManager.get(HelpCommand.class);
        iSubCommand.execute(commandSender, strings);
        return true;
    }
}
