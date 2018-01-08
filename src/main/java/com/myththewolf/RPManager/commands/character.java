package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.DiscordUtils;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;

import javax.management.relation.Role;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class character implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        if (discordCommand.getArgs().length < 1) {
            discordCommand.failed("Command requires 1 argument. See `!man character` for more info.");
            return;
        }

        if (isInt(discordCommand.getArgs()[0])) {
            List<RolePlayCharacter> possible = new ArrayList<>();
            DataCache.getUserMap().forEach((key, val) -> {
                val.getCharacters().stream().filter(character -> character.getID() == Integer.parseInt(discordCommand.getArgs()[0])).map(possible::add);
            });
            if (possible.size() < 1) {
                discordCommand.reply(DiscordUtils.systemWarningMessage("No results for character ID '" + discordCommand.getArgs()[0]) + "'");
                return;
            } else {
                EmbedBuilder reply = new EmbedBuilder();
                RolePlayCharacter c = possible.get(0);
                reply.setColor(Color.GREEN);
                reply.setTitle("Character Info - " + c.getName());
                reply.addField("Name", "```" + c.getName() + "```", false);
                reply.addField("Species", "```" + c.getSpecies() + "```", false);
                reply.addField("Height", "```" + c.getHeight() + "```", false);
                reply.addField("Gender/Pronouns", "```" + c.getGender() + "```", false);
                reply.addField("Color patterns", "```" + c.getColors() + "```", false);
                reply.addField("Sexual Preference", "```" + c.getSexuality() + "```", false);
                if (c.getReferenceImages().size() < 1) {
                    reply.setThumbnail("https://vignette.wikia.nocookie.net/janethevirgin/images/4/42/Image-not-available_1.jpg/revision/latest?cb=20150721102313");
                } else {
                    reply.setThumbnail(c.getReferenceImages().get(0));
                }
                String bottom_build = "";
                bottom_build += c.getBio() + "\n";
                bottom_build += "**Additional Notes**+\n";
                bottom_build += "```" + c.getNotes() + "```\n";
                reply.setDescription(bottom_build);
                discordCommand.e.getTextChannel().sendMessage(reply.build()).queue();
            }
        }
    }

    boolean isInt(String test) {
        try {
            Integer.parseInt(test);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
