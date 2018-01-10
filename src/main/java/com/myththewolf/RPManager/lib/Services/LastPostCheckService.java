package com.myththewolf.RPManager.lib.Services;

import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import net.dv8tion.jda.core.EmbedBuilder;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.awt.*;

public class LastPostCheckService implements Runnable {
    @Override
    public void run() {
        DataCache.getRoleplayMap().forEach((Integer id, DiscordRoleplay rp) -> {
            DateTime lastPost = rp.getLastPostDate();
            DateTime lastPing = rp.getLastPing();
            DateTime now = new DateTime();
            int hoursSinceLastPost = Hours.hoursBetween(lastPost, now).getHours();
            int hoursSinceLastPing = (lastPing != null) ? Hours.hoursBetween(lastPing, now).getHours() : 17;
            if (hoursSinceLastPing >= 16 && hoursSinceLastPost >= 16) {
                EmbedBuilder warning = new EmbedBuilder();
                warning.setColor(Color.YELLOW);
                warning.setTitle(":timer: Your turn to post!");
                String image_url = rp.getStagedCharacter().getReferenceImages().size() > 0 ? rp.getStagedCharacter().getReferenceImages().get(0) : "https://vignette.wikia.nocookie.net/janethevirgin/images/4/42/Image-not-available_1.jpg/revision/latest?cb=20150721102313;";
                warning.setThumbnail(image_url);
                warning.addField("RP name", "```" + rp.getRoleplayName() + "```", false);
                warning.addField("RP Character name:", "```" + rp.getStagedCharacter().getName() + "```", false);
                warning.setFooter("The RP thread is blocked until you reply!", null);
                warning.setFooter("Wish to leave? Use ^leave in the the RP chat to leave.s", null);
                rp.getStagedCharacter().getCharacterOwner().asPrivateChannel().sendMessage(warning.build()).queue();
                rp.setLastPing(now);
            }
        });
    }
}
