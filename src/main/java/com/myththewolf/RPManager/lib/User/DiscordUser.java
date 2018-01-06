package com.myththewolf.RPManager.lib.User;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.Character.CharacterBuilder;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DiscordUser {
    private String ID;
    private List<RolePlayCharacter> characters = new ArrayList<RolePlayCharacter>();
    private String status;
    private int max_rps;
    private int reputaion;
    private CharacterBuilder CB = null;

    public DiscordUser(String discordID) {
        PreparedStatement ps;
        try {
            ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Users` WHERE `discord_id` = ?");
            ps.setString(1, discordID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ID = discordID;
                for (String character_id : rs.getString("character_ids").split(",")) {
                    characters.add(new RolePlayCharacter(Integer.parseInt(character_id)));
                }
                status = rs.getString("status");
                max_rps = rs.getInt("max_concurrent_rps");
                reputaion = rs.getInt("reputation");
            }
        } catch (Exception e) {
            RPManagerLoader.LogError(e);
        }
    }

    public void setCharacterBuilder(CharacterBuilder b) {
        this.CB = b;
    }

    public String getName() {
        return RPManagerLoader.INSTANCE.getJDAInstance().getUserById(getDiscordID()).getName();
    }

    public CharacterBuilder getCharacterBuilder() {
        return CB;
    }

    public List<RolePlayCharacter> getCharacters() {
        return characters;
    }

    public String getStatus() {
        return status;
    }

    public int getMaxRPs() {
        return max_rps;
    }

    public String getDiscordID() {
        return ID;
    }

    public int getReputaion() {
        return reputaion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiscordUser) if (((DiscordUser) obj).getDiscordID().equals(this.getDiscordID())) return true;
        return false;
    }
}
