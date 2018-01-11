package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;


public class exclude implements CommandExecutor {
    boolean found;
    DiscordRoleplay target;
    String errors = "noerror";

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DataCache.clearRPCache();
        RPManagerLoader.storeAllRPS();
        String chanID = discordCommand.e.getTextChannel().getId();
        found = false;
        target = null;
        errors = "";
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
        discordCommand.e.getMessage().getMentionedUsers().forEach(user -> {
            DiscordUser dc = DataCache.getDiscordUserByID(user.getId());
            if (dc.getCharacters().stream().anyMatch(character -> target.getCharacterList().stream().anyMatch(ch -> ch.equals(character)))) {
                errors += " - " + user.getName() + "\n";
            }
        });
        if (errors.equals("noerror")) {
            EmbedBuilder warn = new EmbedBuilder();
            warn.setTitle("Friendly Error");
            warn.addField("The following users could not be exempted:", "```" + errors.substring("noerror".length(), errors.length()) + "```", false);
            warn.setDescription("These users have active characters in this roleplay");
            discordCommand.reply(warn);
        } else {
            discordCommand.reply(":ok_hand: Updated.");
        }
    }
}
