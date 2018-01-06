package com.myththewolf.RPManager.lib;

import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataCache {
    private static List<DiscordUser> USERS;

    public static void makeMaps() {
        USERS = new ArrayList<DiscordUser>();
    }

    public static DiscordUser getDiscordUserByID(String ID) {
        if (USERS.stream().noneMatch(d -> d.getDiscordID().equals(ID))) {
            USERS.add(new DiscordUser(ID));
        }
        return USERS.stream().filter(it -> it.getDiscordID().equals(ID)).findFirst().get();
    }
    public static void updateUser(DiscordUser user){
        USERS.remove(user);
        USERS.add(user);
    }
}
