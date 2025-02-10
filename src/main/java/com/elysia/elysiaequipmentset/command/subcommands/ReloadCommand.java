package com.elysia.elysiaequipmentset.command.subcommands;

import com.elysia.elysiaequipmentset.ElysiaEquipmentSet;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements ISubCommand{
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"reload"};
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ElysiaEquipmentSet.getEquipmentManager().load();
        ElysiaEquipmentSet.getConfigManager().loadConfig();
        sender.sendMessage("重载成功");
    }
}
