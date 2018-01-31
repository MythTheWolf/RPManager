package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.DiscordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class isAdminRole implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        if (discordCommand.e.getMessage().getMentionedRoles().size() < 1) {
            discordCommand.failed("Nothing to do! Use `!man isadminrole` for more info");
            return;
        }
        if (!DataCache.getDiscordUserByID(discordCommand.getSender().getId()).isAdmin()) {
            discordCommand.failed("You must be the server admin or server owner to use this command!");
            return;
        }
        discordCommand.e.getMessage().getMentionedRoles().forEach(role -> {
            JSONArray roleList = discordCommand.getPlugin().getJSONConfig().getJSONArray("admin-roles");
            if (!DiscordUtils.inJSONArray(role.getId(), roleList)) {
                roleList.put(role.getId());
            }
            JSONObject oldconfig = discordCommand.getPlugin().getJSONConfig();
            oldconfig.remove("admin-roles");
            oldconfig.put("admin-roles", roleList);
            discordCommand.getPlugin().saveConfig(oldconfig);
        });
    }
}
