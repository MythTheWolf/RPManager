package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.DiscordUtils;
import com.myththewolf.RPManager.lib.RolePlay.RolePlayBuilder;
import com.myththewolf.RPManager.lib.RolePlay.RolePlayRequest;
import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.util.stream.Collectors;


public class newRP implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        if (discordCommand.getArgs().length < 2) {
            discordCommand.failed("Invalid number of args! Use `!man newrp` for more info");
            return;
        }
        if (!DiscordUtils.isInt(discordCommand.getArgs()[1])) {
            discordCommand.failed("Param `character id` must be a number! Use `!man newrp` for more info");
            return;
        }
        DiscordUser self = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        int cid = Integer.parseInt(discordCommand.getArgs()[1]);
        if (self.getCharacters().stream().filter(character -> character.getID() == cid).collect(Collectors.toList()).size() < 1) {
            discordCommand.failed("You do not have a character with the ID '" + cid + "'");
            return;
        }
        RolePlayBuilder rolePlayBuilder = new RolePlayBuilder(self);
        rolePlayBuilder.setName(discordCommand.getArgs()[0]);
        RolePlayCharacter sel = self.getCharacterByID(cid);
        rolePlayBuilder.initCharacter(sel);
        rolePlayBuilder.compile();
        discordCommand.reply(":ok_hand: Created!");
        DataCache.updateUser(new DiscordUser(discordCommand.getSender().getId()));
    }
}
