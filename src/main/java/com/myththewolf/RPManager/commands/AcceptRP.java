package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.DiscordUtils;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import com.myththewolf.RPManager.lib.User.RolePlayRequest;

import java.util.stream.Collectors;

public class AcceptRP implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        if (discordCommand.getArgs().length < 2) {
            discordCommand.failed("Invalid number of args! Use `!man rpaccept` for more info");
            return;
        }
        if (!DiscordUtils.isInt(discordCommand.getArgs()[1])) {
            discordCommand.failed("Param `character id` must be a number! Use `!man rpaccept` for more info");
            return;
        }
        DiscordUser self = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        int cid = Integer.parseInt(discordCommand.getArgs()[1]);
        String rpid = discordCommand.getArgs()[0];
        if (self.getCharacters().stream().filter(character -> character.getID() == cid).collect(Collectors.toList()).size() < 1) {
            discordCommand.failed("You do not have a character with the ID '" + cid + "'");
            return;
        }
        if (self.getRPRequests().stream().filter(rolePlayRequest -> rolePlayRequest.getId().equals(rpid)).collect(Collectors.toList()).size() < 1) {
            discordCommand.failed("You do not have any RP requests with the ID of '" + rpid + "'");
            return;
        }
        RolePlayRequest rp = self.getRPRequestByID(rpid);
        rp.accept(self.getCharacterByID(cid));
        discordCommand.reply(":ok_hand: Accepted!");
    }
}
