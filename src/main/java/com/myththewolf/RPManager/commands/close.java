package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;

public class close implements CommandExecutor {
    private DiscordRoleplay target;
    private boolean found;

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DataCache.clearRPCache();
        RPManagerLoader.storeAllRPS();
        String chanID = discordCommand.e.getTextChannel().getId();
        found = false;
        target = null;
        DataCache.getRoleplayMap().forEach((id, rp) -> {
            if (rp.getHostChannel().getId().equals(chanID)) {
                found = true;
                target = rp;
            }
        });
        if (!found) {
            discordCommand.failed("This must be run in a RP channel!");
            return;
        }
        DiscordUser self = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        if (!target.getOwner().equals(self)) {
            discordCommand.failed("You must be the owner of this channel.");
            return;
        }
        target.archive("The owner has requested for the RP to be closed.");
    }
}
