package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.RolePlay.RolePlayRequest;
import com.myththewolf.RPManager.lib.User.DiscordUser;

public class invite implements CommandExecutor {
    boolean found = false;
    DiscordRoleplay target;

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
        discordCommand.e.getMessage().getMentionedUsers().forEach(user -> {
            RolePlayRequest rolePlayRequest = new RolePlayRequest(target);
            DiscordUser nu = DataCache.getDiscordUserByID(user.getId());
            nu.addRPReuqest(rolePlayRequest);
            DataCache.updateUser(nu);
        });
        discordCommand.reply(":ok_hand: Invited user(s)");
    }
}
