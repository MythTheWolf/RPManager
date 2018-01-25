package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.BotServ.lib.API.invoke.BotPlugin;

public class help implements CommandExecutor {

    private String messagebuild = "";
    private BotPlugin plugin;

    public help(BotPlugin botPlugin) {
        plugin = botPlugin;
    }

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        messagebuild = "";
        plugin.getCommands().forEach((name,instance) -> {
            messagebuild += name + "\n";
        });
    }
}
