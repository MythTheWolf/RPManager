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
            int hours = Hours.hoursBetween(lastPost,now).getHours();
            System.out.println("h"+hours);
        });
    }
}
