package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;


public class renew implements CommandExecutor {

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DiscordUser theuser = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        if (!theuser.isAdmin()) {
            discordCommand.failed("None of your roles are an admin role! (As far as I know, that is)");
            return;
        }

        DataCache.getRoleplayMap().values().stream().filter(rp -> discordCommand.e.getMessage().getMentionedChannels().contains(rp.getHostChannel())).forEach(discordRoleplay -> {

        });
    }
}
