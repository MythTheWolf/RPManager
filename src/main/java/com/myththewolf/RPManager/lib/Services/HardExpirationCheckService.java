package com.myththewolf.RPManager.lib.Services;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import org.joda.time.DateTime;
import org.joda.time.Months;

public class HardExpirationCheckService implements Runnable {
    @Override
    public void run() {
        DataCache.clearRPCache();
        RPManagerLoader.storeAllRPS();
        DataCache.getRoleplayMap().forEach((id, rp) -> {
            DateTime create = rp.getCreationDate();
            DateTime now = new DateTime();
            if (Months.monthsBetween(create, now).getMonths() >= 1) {
                rp.archive("The RP has passed the maximum alive-time of 1 month");
                DataCache.clearRPCache();
                RPManagerLoader.storeAllRPS();
            }
        });
    }
}
