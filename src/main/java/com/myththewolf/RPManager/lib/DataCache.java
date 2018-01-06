package com.myththewolf.RPManager.lib;

import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataCache {
    private static HashMap<String, DiscordUser> USERS;

    public static void makeMaps() {
        USERS = new HashMap<String, DiscordUser>();
    }

    public static DiscordUser getDiscordUserByID(String ID) {
        if (!USERS.containsKey(ID)) {
            USERS.put(ID, new DiscordUser(ID));
        }
        return USERS.get(ID);
    }

    public static void updateUser(DiscordUser user) {
        USERS.put(user.getDiscordID(), user);
    }
}
