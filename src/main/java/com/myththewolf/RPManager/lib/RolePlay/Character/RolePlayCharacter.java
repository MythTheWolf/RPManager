package com.myththewolf.RPManager.lib.RolePlay.Character;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RolePlayCharacter {
    private int ID;
    private String name;
    private String bio;
    private String gender;
    private String Species;
    private String height;
    private String colors;
    private String notes;
    private String sexuality;
    private List<DiscordRoleplay> activeRoleplays = new ArrayList<DiscordRoleplay>();
    private List<String> refs = new ArrayList<>();
    private DiscordUser characterOwner;

    public RolePlayCharacter(int charID) {
        try {
            PreparedStatement ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Characters` WHERE `ID` = ?");
            ps.setInt(1, charID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ID = charID;
                bio = rs.getString("bio");
                name = rs.getString("name");
                gender = rs.getString("gender");
                Species = rs.getString("species");
                height = rs.getString("height");
                colors = rs.getString("colors");
                notes = rs.getString("notes");
                sexuality = rs.getString("sexuality");
                if (!rs.getString("image_urls").isEmpty()) {
                    Arrays.stream(rs.getString("image_urls").split(",")).forEach(con -> this.refs.add(con));
                }
                DataCache.getRoleplayMap().forEach((key, val) -> {
                    if (val.getCharacterList().contains(this)) {
                        this.activeRoleplays.add(val);
                    }
                });
                characterOwner = DataCache.getDiscordUserByID(rs.getString("owner_discord_id"));
            }
        } catch (SQLException e) {
            RPManagerLoader.LogError(e);
        }
    }

    public String getBio() {
        return bio;
    }

    public int getID() {
        return ID;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return Species;
    }

    public DiscordUser getCharacterOwner() {
        return characterOwner;
    }

    public List<DiscordRoleplay> getActiveRoleplays() {
        return activeRoleplays;
    }

    public List<String> getReferenceImages() {
        return refs;
    }

    public String getColors() {
        return colors;
    }

    public String getNotes() {
        return notes;
    }

    public String getSexuality() {
        return sexuality;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RolePlayCharacter) if (((RolePlayCharacter) obj).getID() == this.getID()) return true;
        return false;
    }
}
