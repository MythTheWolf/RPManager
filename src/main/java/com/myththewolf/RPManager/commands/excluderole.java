package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.Optional;

public class excluderole implements CommandExecutor {
    boolean found;
    DiscordRoleplay target;
    String rolebuild = "";

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
        DiscordUser self = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        if (!target.getOwner().equals(self)) {
            discordCommand.failed("You must be the owner of this channel.");
            return;
        }
        rolebuild = "";
        Arrays.stream(discordCommand.getArgs()).forEach(arg -> rolebuild += arg + " ");
        Optional<Role> roleOptional = discordCommand.e.getGuild().getRoles().stream().filter(role -> role.getName().startsWith(rolebuild)).findFirst();
        if (!roleOptional.isPresent()) {
            discordCommand.failed("No role found by that name");
            return;
        }
        if (target.getHostChannel().getPermissionOverride(roleOptional.get()) != null) {
            target.getHostChannel().getPermissionOverride(roleOptional.get()).delete().queue();
        }
        target.getHostChannel().createPermissionOverride(roleOptional.get()).setDeny(Permission.ALL_TEXT_PERMISSIONS).queue();
    }
}
