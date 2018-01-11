package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class RPRequests implements CommandExecutor {
    String build = "";

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        build = "";
        DiscordUser target = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        target.getRPRequests().forEach(request -> build += "\"" + request.getRoleplay().getRoleplayName() + "\" - " + request.getId() + "\n");
        EmbedBuilder rep = new EmbedBuilder();
        rep.setTitle("Your RP requests");
        rep.setColor(Color.CYAN);
        String fin = build.isEmpty() ? "(No RP requests)" : build;
        rep.addField("RP Name - Request ID", "```" + fin + "```", false);
        rep.setFooter("Use \"!man rpaccept\" for info on how to accept RP requests", null);
        discordCommand.reply(rep);
    }
}
