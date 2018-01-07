package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class minuwekk implements CommandExecutor{
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DateTime fakeCreate = new DateTime();
        DateTime fakePost = new DateTime();
        fakePost = fakePost.minusWeeks(4);
        discordCommand.reply("Fake creation date::"+ DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(fakeCreate));
        discordCommand.reply("Fake lastPostDate::"+DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(fakePost));
    }
}
