package com.myththewolf.RPManager.commands;


import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.RolePlay.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;

public class mychars implements CommandExecutor {

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DiscordUser self = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        String build = "ID - Name \n";
        for (RolePlayCharacter c : self.getCharacters()) {
            build += c.getID() + "-" + c.getName();
        }
        discordCommand.reply("```" + build + "```");
    }
}
