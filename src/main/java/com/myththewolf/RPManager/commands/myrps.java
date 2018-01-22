package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class myrps implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DiscordUser user = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        String roleplays = "Roleplay name | # of participants | Your turn";
        List<DiscordRoleplay> userRPs = new ArrayList<>();
        user.getCharacters().stream().filter(character -> character.getActiveRoleplays().size() > 0).map(RolePlayCharacter::getActiveRoleplays).forEach(userRPs::addAll);
        for (DiscordRoleplay rp : userRPs) {
            roleplays += rp.getRoleplayName() + "  " + rp.getCharacterList().size() + "  " + rp.getStagedCharacter().getCharacterOwner().equals(user) + "\n";
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your roleplays");
        eb.setDescription("```" + roleplays + "```");
        discordCommand.reply(eb);
    }
}
