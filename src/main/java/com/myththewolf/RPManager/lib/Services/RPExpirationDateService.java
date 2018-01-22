package com.myththewolf.RPManager.lib.Services;

import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import net.dv8tion.jda.core.EmbedBuilder;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.awt.*;

public class RPExpirationDateService implements Runnable {
    @Override
    public void run() {
        System.out.println("Running expire date check...");
        DataCache.getRoleplayMap().forEach((Integer id, DiscordRoleplay rp) -> {
            DateTime now = new DateTime();
            DateTime expireDate = rp.getExpireDate();
            int days = Days.daysBetween(now, expireDate).getDays();
            if (rp.expired()) {
                EmbedBuilder closed = new EmbedBuilder();
                closed.setColor(Color.RED);
                closed.setDescription("The RP you were active in: '" + rp.getRoleplayName() + "', was closed due to inactivity");
                closed.setTitle("RP Closed");
                rp.getCharacterList().forEach(character -> {
                    character.getCharacterOwner().asPrivateChannel().sendMessage(closed.build()).queue();
                });
                rp.archive("RP has been inactive for more than a week.");
            } else if (days <= 2) {
                EmbedBuilder scarywarning = new EmbedBuilder();
                scarywarning.setColor(Color.RED);
                scarywarning.setTitle(":warning: RP EXPIRING :warning:");
                scarywarning.addField("RP name", "```" + rp.getRoleplayName() + "```", false);
                scarywarning.addField("RP Character name:", "```" + rp.getStagedCharacter().getName() + "```", false);
                scarywarning.setDescription("This RP will expire in 24 hours, and it's your turn to post. Please leave the RP or make a post!");
                rp.getStagedCharacter().getCharacterOwner().asPrivateChannel().sendMessage(scarywarning.build()).queue();
                rp.sendBoardMessage(":warning: **This RP will expire in 24 hours due to no activity**");
            }
        });
    }
}
