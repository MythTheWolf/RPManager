package com.myththewolf.RPManager.lib.Services;

import com.myththewolf.RPManager.lib.DataCache;
import org.joda.time.DateTime;
import org.joda.time.Hours;

public class LastPostCheckService implements Runnable {
    @Override
    public void run() {
        DataCache.getRoleplayMap().forEach((id, rp) -> {
            DateTime lastPost = rp.getLastPostDate();
            DateTime lastPing = rp.getLastPing();
            DateTime now = new DateTime();
            int hoursSinceLastPost = Hours.hoursBetween(lastPost, now).getHours();
            int hoursSinceLastPing = lastPing != null ? Hours.hoursBetween(lastPing, now).getHours() : 17;
            if (hoursSinceLastPing >= 16 && hoursSinceLastPost >= 16) {
                System.out.println("SHUD_POST");
                rp.getStagedCharacter().getCharacterOwner().asPrivateChannel().sendMessage("HU").queue();
            }
        });
    }
}
