package com.myththewolf.RPManager.lib;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.User.DiscordUser;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public class DataCache {
    private static HashMap<String, DiscordUser> USERS;
    private static HashMap<Integer, RolePlayCharacter> CHARACTERS;
    private static HashMap<Integer, DiscordRoleplay> ROLEPLAYS;
    public static final String SYSTEM_DATE_FORMAT = "dd-MMM-yy hh.mm.ss aa";

    public static void makeMaps() {
        USERS = new HashMap<>();
        CHARACTERS = new HashMap<>();
        ROLEPLAYS = new HashMap<>();
    }

    public static DiscordUser getDiscordUserByID(String ID) {
        checkActualDatabase(ID);
        if (!USERS.containsKey(ID)) {
            USERS.put(ID, new DiscordUser(ID));
        }
        return USERS.get(ID);
    }

    private static void checkActualDatabase(String ID) {
        try {
            PreparedStatement ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Users` WHERE `discord_id` = ?");
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                PreparedStatement neu = RPManagerLoader.getSQLConnection().prepareStatement("INSERT INTO `Users` (`ID`, `discord_id`, `character_ids`, `status`, `max_concurrent_rps`, `reputation`) VALUES (NULL, ?, ?, ?, 4, ?)");
                neu.setString(1, ID);
                neu.setString(2, "");
                neu.setString(3, "OK");
                neu.setString(4, "0");
                neu.executeUpdate();
            }
        } catch (SQLException e) {
            RPManagerLoader.LogError(e);
        }
    }


    public static void updateUser(DiscordUser user) {
        USERS.put(user.getDiscordID(), user);
    }

    public static void updateCharacter(RolePlayCharacter character) {
        CHARACTERS.remove(character.getID());
        CHARACTERS.put(character.getID(), new RolePlayCharacter(character.getID()));
    }

    public static void updateRolePlay(DiscordRoleplay rp) {
        ROLEPLAYS.remove(rp.getId());
        ROLEPLAYS.put(rp.getId(), new DiscordRoleplay(rp.getId()));
    }

    public static HashMap<Integer, DiscordRoleplay> getRoleplayMap() {
        return new HashMap<>(ROLEPLAYS);
    }
}
