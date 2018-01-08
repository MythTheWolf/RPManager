package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.RolePlay.Character.CharacterBuilder;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;

public class CharacterWizard implements CommandExecutor{
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        discordCommand.reply(":ok_hand: Please check your DMs.");
        discordCommand.getSender().openPrivateChannel().complete().sendMessage("I will be asking you a series of questiions. You answer them by sending messages.").queue();
        discordCommand.getSender().openPrivateChannel().complete().sendMessage("First Question: What is the character name?").queue();
        DiscordUser DU = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        CharacterBuilder cb = new CharacterBuilder(DU);
        DU.setCharacterBuilder(cb);
        DataCache.updateUser(DU);
    }
}
