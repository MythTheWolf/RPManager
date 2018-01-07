package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import net.dv8tion.jda.core.EmbedBuilder;

public class charsof implements CommandExecutor {
    String build = "";

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        if (discordCommand.e.getMessage().getMentionedUsers().size() == 0) {
            discordCommand.failed("Paramater 'user' is not optional! Use `!man charsof` for more information.");
            return;
        }
        String to_find_id = discordCommand.e.getMessage().getMentionedUsers().get(0).getId();
        build = "**ID - Name**\n";
        DataCache.getDiscordUserByID(to_find_id).getCharacters().forEach(character -> {
            build += character.getID() + " - " + character.getName() + "\n";
        });
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Character Listing");
        eb.setThumbnail(discordCommand.e.getJDA().getUserById(to_find_id).getAvatarUrl());
        eb.setDescription("```" + build + "```");
        discordCommand.reply(eb);
    }
}
