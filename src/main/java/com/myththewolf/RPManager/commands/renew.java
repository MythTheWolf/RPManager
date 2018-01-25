package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;


public class renew implements CommandExecutor {

    @Override
    public void onCommand(DiscordCommand discordCommand) {
        DiscordUser theuser = DataCache.getDiscordUserByID(discordCommand.getSender().getId());
        if (!theuser.isAdmin()) {
            discordCommand.failed("None of your roles are an admin role! (As far as I know, that is)");
            return;
        }
        if (discordCommand.getArgs().length < 2) {
            discordCommand.failed("The syntax of the command is incorrect. Use `!man renew` for more info.");
            return;
        }
        final PeriodFormatter format = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendWeeks()
                .appendSuffix("w").appendMonths().appendSuffix("mon").appendYears().appendSuffix("y")
                .appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").appendHours()
                .appendSuffix("h").toFormatter();

        DataCache.getRoleplayMap().values().stream().filter(rp -> discordCommand.e.getMessage().getMentionedChannels().contains(rp.getHostChannel())).forEach(discordRoleplay -> {
            try {
                Period p = format.parsePeriod(discordCommand.getArgs()[0]);
                DateTime oldExpire = discordRoleplay.getExpireDate();
                DateTime newDate = oldExpire.withPeriodAdded(p, 1);
                discordRoleplay.setExpireDate(newDate);
                discordRoleplay.recompile();
            } catch (Exception e) {
                discordCommand.failed(e.getMessage());
                return;
            }
        });
    }
}
