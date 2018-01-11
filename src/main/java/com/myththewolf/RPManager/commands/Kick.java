package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.RolePlay.RolePlayRequest;
import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.util.Optional;

public class Kick implements CommandExecutor {
    boolean found;
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
        DiscordUser self = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        if (!target.getOwner().equals(self)) {
            discordCommand.failed("You must be the owner of this channel.");
            return;
        }
        discordCommand.e.getMessage().getMentionedUsers().forEach(user -> target.getCharacterList().stream().filter(rolePlayCharacter -> rolePlayCharacter.getCharacterOwner().equals(self)).findFirst().ifPresent(rolePlayCharacter -> target.removeCharacter(rolePlayCharacter)));
        discordCommand.reply("Removed User(s)");
    }
}
