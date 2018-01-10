package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class whosturn implements CommandExecutor {
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
        EmbedBuilder rep = new EmbedBuilder();
        rep.setTitle("Roleplay Turn");
        rep.setColor(Color.BLACK);
        rep.setDescription(":information_source: It is "+target.getStagedCharacter().getCharacterOwner().asRawDiscordUser().getAsMention()+"'s turn to post.");
        target.getHostChannel().sendMessage(rep.build()).queue();
    }
}
