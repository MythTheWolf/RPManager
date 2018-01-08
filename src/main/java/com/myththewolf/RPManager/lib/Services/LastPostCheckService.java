package com.myththewolf.RPManager.lib.Services;

import com.myththewolf.RPManager.lib.DataCache;
import net.dv8tion.jda.core.EmbedBuilder;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.awt.*;

public class LastPostCheckService implements Runnable {
    @Override
    public void run() {
        DataCache.getRoleplayMap().forEach((id, rp) -> {
            DateTime lastPost = rp.getLastPostDate();
            DateTime lastPing = rp.getLastPing();
            DateTime now = new DateTime();
            int hoursSinceLastPost = Hours.hoursBetween(lastPost, now).getHours();
            int hoursSinceLastPing = (lastPing != null) ? Hours.hoursBetween(lastPing, now).getHours() : 17;
            //System.out.println(hoursSinceLastPost+"::"+hoursSinceLastPing);
            if (hoursSinceLastPing >= 1 && hoursSinceLastPost >= 1) {
                EmbedBuilder warning = new EmbedBuilder();
                warning.setColor(Color.YELLOW);
                warning.setTitle(":timer: Your turn to post!");
                warning.addField("RP name", "```" + rp.getRoleplayName() + "```", false);
                warning.addField("RP Character name:", "```" + rp.getStagedCharacter().getName() + "```", false);
                warning.setFooter("The RP thread is blocked until you reply! Run ^rpleave in the channel if you wish to leave.", null);
                rp.getStagedCharacter().getCharacterOwner().asPrivateChannel().sendMessage(warning.build()).queue();
                rp.setLastPing(now);
            }
        });
    }
}
